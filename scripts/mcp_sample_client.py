#!/usr/bin/env python3
"""Minimal MCP client for the TMF915 Spring AI MCP server.

This client:
1. Opens the SSE stream and waits for the dynamic message endpoint event.
2. Sends initialize + initialized notification.
3. Lists available tools.
4. Calls two sample tools:
   - listAvailableAiModelSpecifications
   - listAvailableAiModels
"""

from __future__ import annotations

import argparse
import json
import queue
import threading
import time
import urllib.error
import urllib.request
from dataclasses import dataclass


@dataclass
class SseEvent:
    event: str
    data: str
    id: str | None = None


class SseReader(threading.Thread):
    def __init__(self, sse_url: str, out_queue: queue.Queue[SseEvent]):
        super().__init__(daemon=True)
        self.sse_url = sse_url
        self.out_queue = out_queue

    def run(self) -> None:
        request = urllib.request.Request(self.sse_url, headers={"Accept": "text/event-stream"})
        with urllib.request.urlopen(request, timeout=60) as response:
            event_type = "message"
            event_id = None
            data_lines: list[str] = []

            for raw in response:
                line = raw.decode("utf-8", errors="replace").rstrip("\r\n")

                if line == "":
                    if data_lines:
                        self.out_queue.put(
                            SseEvent(
                                event=event_type,
                                data="\n".join(data_lines),
                                id=event_id,
                            )
                        )
                    event_type = "message"
                    event_id = None
                    data_lines = []
                    continue

                if line.startswith(":"):
                    continue

                if line.startswith("event:"):
                    event_type = line[6:].strip()
                elif line.startswith("id:"):
                    event_id = line[3:].strip()
                elif line.startswith("data:"):
                    data_lines.append(line[5:].lstrip())


def post_json(url: str, payload: dict) -> None:
    body = json.dumps(payload).encode("utf-8")
    req = urllib.request.Request(
        url,
        data=body,
        method="POST",
        headers={"Content-Type": "application/json"},
    )
    with urllib.request.urlopen(req, timeout=30) as response:
        if response.status != 200:
            raise RuntimeError(f"POST {url} failed with status {response.status}")


def wait_for_event(q: queue.Queue[SseEvent], timeout_sec: int, predicate):
    deadline = time.time() + timeout_sec
    while time.time() < deadline:
        remaining = max(0.1, deadline - time.time())
        try:
            event = q.get(timeout=remaining)
        except queue.Empty:
            continue
        if predicate(event):
            return event
    raise TimeoutError("Timed out waiting for expected SSE event")


def wait_for_rpc_result(q: queue.Queue[SseEvent], rpc_id: str, timeout_sec: int = 20) -> dict:
    def match(ev: SseEvent) -> bool:
        if ev.event != "message":
            return False
        try:
            payload = json.loads(ev.data)
        except json.JSONDecodeError:
            return False
        return payload.get("id") == rpc_id

    event = wait_for_event(q, timeout_sec, match)
    payload = json.loads(event.data)
    return payload


def main() -> int:
    parser = argparse.ArgumentParser(description="Sample MCP client for TMF915")
    parser.add_argument(
        "--base-url",
        default="http://localhost:13082/tmf-api",
        help="TMF915 base URL (including context path)",
    )
    parser.add_argument(
        "--spec-offset",
        type=int,
        default=0,
        help="Offset for listAvailableAiModelSpecifications",
    )
    parser.add_argument(
        "--spec-limit",
        type=int,
        default=10,
        help="Limit for listAvailableAiModelSpecifications",
    )
    args = parser.parse_args()

    base_url = args.base_url.rstrip("/")
    sse_url = f"{base_url}/sse"

    events: queue.Queue[SseEvent] = queue.Queue()
    reader = SseReader(sse_url, events)
    reader.start()

    print(f"Connecting to SSE: {sse_url}")

    endpoint_event = wait_for_event(events, 20, lambda ev: ev.event == "endpoint")
    message_path = endpoint_event.data
    message_url = f"{base_url}{message_path}"

    print(f"Session id: {endpoint_event.id}")
    print(f"Message endpoint: {message_url}")

    initialize_id = "init-1"
    post_json(
        message_url,
        {
            "jsonrpc": "2.0",
            "id": initialize_id,
            "method": "initialize",
            "params": {
                "protocolVersion": "2025-03-26",
                "capabilities": {},
                "clientInfo": {"name": "tmf915-sample-client", "version": "1.0.0"},
            },
        },
    )
    init_result = wait_for_rpc_result(events, initialize_id)
    print("\nInitialize result:")
    print(json.dumps(init_result, indent=2))

    post_json(
        message_url,
        {
            "jsonrpc": "2.0",
            "method": "notifications/initialized",
            "params": {},
        },
    )

    tools_id = "tools-1"
    post_json(
        message_url,
        {
            "jsonrpc": "2.0",
            "id": tools_id,
            "method": "tools/list",
            "params": {},
        },
    )
    tools_result = wait_for_rpc_result(events, tools_id)
    print("\nAvailable tools:")
    print(json.dumps(tools_result, indent=2))

    specs_id = "call-specs-1"
    post_json(
        message_url,
        {
            "jsonrpc": "2.0",
            "id": specs_id,
            "method": "tools/call",
            "params": {
                "name": "listAvailableAiModelSpecifications",
                "arguments": {"offset": args.spec_offset, "limit": args.spec_limit},
            },
        },
    )
    specs_result = wait_for_rpc_result(events, specs_id)
    print("\nlistAvailableAiModelSpecifications result:")
    print(json.dumps(specs_result, indent=2))

    models_id = "call-models-1"
    post_json(
        message_url,
        {
            "jsonrpc": "2.0",
            "id": models_id,
            "method": "tools/call",
            "params": {
                "name": "listAvailableAiModels",
                "arguments": {},
            },
        },
    )
    models_result = wait_for_rpc_result(events, models_id)
    print("\nlistAvailableAiModels result:")
    print(json.dumps(models_result, indent=2))

    print("\nMCP sample run completed.")
    return 0


if __name__ == "__main__":
    try:
        raise SystemExit(main())
    except urllib.error.URLError as exc:
        print(f"Connection error: {exc}")
        raise SystemExit(2)
    except TimeoutError as exc:
        print(f"Timeout: {exc}")
        raise SystemExit(3)
