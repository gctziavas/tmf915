#!/usr/bin/env python3
"""Interactive MCP lookup client for TMF915.

This client:
1. Connects to SSE and initializes an MCP session.
2. Prompts for a specification id and calls getAiModelSpecification.
3. Prompts for a model id and calls getAiModel.
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
                        self.out_queue.put(SseEvent(event=event_type, data="\n".join(data_lines), id=event_id))
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
    return json.loads(event.data)


def call_tool(events: queue.Queue[SseEvent], message_url: str, request_id: str, tool_name: str, arguments: dict) -> dict:
    post_json(
        message_url,
        {
            "jsonrpc": "2.0",
            "id": request_id,
            "method": "tools/call",
            "params": {
                "name": tool_name,
                "arguments": arguments,
            },
        },
    )
    return wait_for_rpc_result(events, request_id)


def ask(prompt: str, fallback: str | None = None) -> str:
    while True:
        suffix = f" [{fallback}]" if fallback else ""
        value = input(f"{prompt}{suffix}: ").strip()
        if value:
            return value
        if fallback:
            return fallback
        print("Value is required.")


def main() -> int:
    parser = argparse.ArgumentParser(description="Interactive MCP lookup client for TMF915")
    parser.add_argument(
        "--base-url",
        default="http://localhost:13082/tmf-api",
        help="TMF915 base URL (including context path)",
    )
    parser.add_argument("--specification-id", help="Optional prefilled AI model specification id")
    parser.add_argument("--model-id", help="Optional prefilled AI model id")
    args = parser.parse_args()

    base_url = args.base_url.rstrip("/")
    sse_url = f"{base_url}/sse"

    events: queue.Queue[SseEvent] = queue.Queue()
    reader = SseReader(sse_url, events)
    reader.start()

    print(f"Connecting to SSE: {sse_url}")
    endpoint_event = wait_for_event(events, 20, lambda ev: ev.event == "endpoint")
    message_url = f"{base_url}{endpoint_event.data}"
    print(f"Session id: {endpoint_event.id}")

    init_id = "init-interactive-1"
    post_json(
        message_url,
        {
            "jsonrpc": "2.0",
            "id": init_id,
            "method": "initialize",
            "params": {
                "protocolVersion": "2025-03-26",
                "capabilities": {},
                "clientInfo": {"name": "tmf915-interactive-client", "version": "1.0.0"},
            },
        },
    )
    initialize_result = wait_for_rpc_result(events, init_id)
    print("Initialize result:")
    print(json.dumps(initialize_result, indent=2))

    post_json(
        message_url,
        {
            "jsonrpc": "2.0",
            "method": "notifications/initialized",
            "params": {},
        },
    )

    specification_id = args.specification_id or ask("Enter AI Model Specification ID")
    spec_result = call_tool(
        events,
        message_url,
        "call-get-spec-1",
        "getAiModelSpecification",
        {"specificationId": specification_id},
    )
    print("\ngetAiModelSpecification result:")
    print(json.dumps(spec_result, indent=2))

    model_id = args.model_id or ask("Enter AI Model ID")
    model_result = call_tool(
        events,
        message_url,
        "call-get-model-1",
        "getAiModel",
        {"modelId": model_id},
    )
    print("\ngetAiModel result:")
    print(json.dumps(model_result, indent=2))

    print("\nInteractive MCP lookup completed.")
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
