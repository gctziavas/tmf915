package org.etsi.osl.controllers.tmf915.integrations.hub;

import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Handoff 5.7. Host names are resolved by a stub, so no DNS is needed. */
class CallbackUrlPolicyTest {

    private static InetAddress ip(String literal) {
        try {
            return InetAddress.getByName(literal);   // IP literals are parsed, not looked up
        } catch (UnknownHostException e) {
            throw new IllegalStateException(e);
        }
    }

    /** Resolves a few fixed names; everything else is unknown. */
    private static InetAddress[] stubResolve(String host) throws UnknownHostException {
        return switch (host) {
            case "oss.example.com" -> new InetAddress[]{ip("93.184.216.34")};
            case "sneaky.example.com" -> new InetAddress[]{ip("93.184.216.34"), ip("10.0.0.7")};
            case "metadata.internal" -> new InetAddress[]{ip("169.254.169.254")};
            case "oss.corp.internal" -> new InetAddress[]{ip("10.1.2.3")};
            default -> {
                if (host.matches("[0-9.]+") || host.contains(":")) yield new InetAddress[]{ip(host)};
                throw new UnknownHostException(host);
            }
        };
    }

    private final CallbackUrlPolicy policy = new CallbackUrlPolicy("", true, CallbackUrlPolicyTest::stubResolve);

    private void rejected(CallbackUrlPolicy p, String url, String reasonFragment) {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> p.check(url), url);
        assertTrue(ex.getMessage().contains(reasonFragment), url + " -> " + ex.getMessage());
    }

    @Test
    void publicHttpAndHttpsCallbacksAreAccepted() {
        assertDoesNotThrow(() -> policy.check("https://oss.example.com/tmf915/events"));
        assertDoesNotThrow(() -> policy.check("http://93.184.216.34:8080/hook?token=abc"));
    }

    @Test
    void internalAddressesAreRejected() {
        for (String url : new String[]{
                "http://169.254.169.254/latest/meta-data/",   // cloud metadata (link-local)
                "http://metadata.internal/",
                "http://127.0.0.1:3306/",
                "http://localhost-alias.invalid/".replace("localhost-alias.invalid", "127.0.0.2"),
                "http://10.0.0.5/", "http://172.16.100.94:8080/", "http://192.168.1.10/",
                "http://100.64.0.1/",                         // carrier-grade NAT
                "http://0.0.0.0:13082/",
                "http://[::1]/", "http://[fd00::1]/", "http://[fe80::1]/",
                "http://sneaky.example.com/"}) {              // one public, one private address
            rejected(policy, url, "internal address");
        }
    }

    @Test
    void malformedOrNonHttpCallbacksAreRejected() {
        rejected(policy, "ftp://oss.example.com/x", "http or https");
        rejected(policy, "file:///etc/passwd", "http or https");
        rejected(policy, "/relative/path", "http or https");
        rejected(policy, "http://exa mple.com/", "not a valid URL");
        rejected(policy, " ", "required");
    }

    @Test
    void unresolvableHostIsRejected() {
        rejected(policy, "http://no-such-host.invalid/", "cannot be resolved");
    }

    @Test
    void allowListAdmitsOnlyListedHosts_includingInternalOnes() {
        CallbackUrlPolicy listed = new CallbackUrlPolicy("OSS.corp.internal, oss.example.com", true,
                CallbackUrlPolicyTest::stubResolve);
        assertDoesNotThrow(() -> listed.check("http://oss.corp.internal/events"));
        assertDoesNotThrow(() -> listed.check("https://oss.example.com/events"));
        rejected(listed, "https://other.example.com/events", "allowed-callback-hosts");
    }

    @Test
    void privateAddressesCanBeAllowedExplicitly() {
        CallbackUrlPolicy open = new CallbackUrlPolicy("", false, CallbackUrlPolicyTest::stubResolve);
        assertDoesNotThrow(() -> open.check("http://127.0.0.1:3000/hook"));
        rejected(open, "ftp://127.0.0.1/", "http or https");
    }
}
