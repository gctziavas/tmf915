package org.etsi.osl.controllers.tmf915.integrations.hub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Decides which webhook callback URLs the server may POST to (handoff 5.7).
 *
 * <p>Without a check, an authenticated user could register a callback such as
 * {@code http://169.254.169.254/} (cloud metadata) or {@code http://mysql:3306}, and the server
 * would send requests to addresses only it can reach.
 *
 * <ul>
 *   <li>Only absolute {@code http} and {@code https} URLs with a host are accepted.</li>
 *   <li>If {@code hub.webhook.allowed-callback-hosts} is set, the host must be on it. Listed
 *       hosts are trusted, including internal ones: the operator named them.</li>
 *   <li>Otherwise, with {@code hub.webhook.block-private-addresses} (default {@code true}), the
 *       host is resolved and rejected if any address is loopback, link-local, private
 *       (RFC 1918, IPv6 unique-local), carrier-grade NAT, unspecified or multicast.</li>
 * </ul>
 *
 * <p>Checked when a subscription is registered and again before each delivery, because DNS
 * answers can change. A DNS answer that changes between the check and the connection is not
 * covered; use the allow-list where that matters.
 */
@Component
public class CallbackUrlPolicy {

    /** Resolves a host name to its addresses. Replaceable in tests. */
    @FunctionalInterface
    interface Resolver {
        InetAddress[] resolve(String host) throws UnknownHostException;
    }

    private final Set<String> allowedHosts;
    private final boolean blockPrivateAddresses;
    private final Resolver resolver;

    @Autowired
    public CallbackUrlPolicy(@Value("${hub.webhook.allowed-callback-hosts:}") String allowedHosts,
                             @Value("${hub.webhook.block-private-addresses:true}") boolean blockPrivateAddresses) {
        this(allowedHosts, blockPrivateAddresses, InetAddress::getAllByName);
    }

    CallbackUrlPolicy(String allowedHosts, boolean blockPrivateAddresses, Resolver resolver) {
        this.allowedHosts = allowedHosts == null ? Set.of() : Arrays.stream(allowedHosts.split(","))
                .map(h -> h.strip().toLowerCase(Locale.ROOT))
                .filter(h -> !h.isEmpty())
                .collect(Collectors.toUnmodifiableSet());
        this.blockPrivateAddresses = blockPrivateAddresses;
        this.resolver = resolver;
    }

    /**
     * @throws IllegalArgumentException if the callback is not allowed; the message is safe to
     *                                  return to the client
     */
    public void check(String callback) {
        if (callback == null || callback.isBlank()) {
            throw new IllegalArgumentException("callback is required");
        }
        URI uri;
        try {
            uri = new URI(callback.strip());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("callback is not a valid URL");
        }
        String scheme = uri.getScheme() == null ? "" : uri.getScheme().toLowerCase(Locale.ROOT);
        if (!uri.isAbsolute() || !(scheme.equals("http") || scheme.equals("https")) || uri.getHost() == null) {
            throw new IllegalArgumentException("callback must be an absolute http or https URL with a host");
        }

        String host = uri.getHost().toLowerCase(Locale.ROOT);
        if (host.startsWith("[") && host.endsWith("]")) {
            host = host.substring(1, host.length() - 1);
        }

        if (!allowedHosts.isEmpty()) {
            if (!allowedHosts.contains(host)) {
                throw new IllegalArgumentException("callback host '" + host + "' is not in hub.webhook.allowed-callback-hosts");
            }
            return;
        }
        if (!blockPrivateAddresses) {
            return;
        }

        InetAddress[] addresses;
        try {
            addresses = resolver.resolve(host);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("callback host '" + host + "' cannot be resolved");
        }
        for (InetAddress address : addresses) {
            if (isInternal(address)) {
                throw new IllegalArgumentException("callback host '" + host + "' resolves to an internal address; "
                        + "ask the operator to add it to hub.webhook.allowed-callback-hosts if it is intended");
            }
        }
    }

    static boolean isInternal(InetAddress address) {
        if (address.isLoopbackAddress() || address.isLinkLocalAddress() || address.isSiteLocalAddress()
                || address.isAnyLocalAddress() || address.isMulticastAddress()) {
            return true;
        }
        byte[] b = address.getAddress();
        if (address instanceof Inet6Address) {
            return (b[0] & 0xFE) == 0xFC;                               // fc00::/7 unique local
        }
        int first = b[0] & 0xFF;
        int second = b[1] & 0xFF;
        return first == 0                                               // 0.0.0.0/8
                || (first == 100 && second >= 64 && second <= 127);     // 100.64.0.0/10 carrier-grade NAT
    }
}
