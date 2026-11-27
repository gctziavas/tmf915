package org.etsi.osl.controllers.tmf915.artifacts;

import java.io.IOException;

/**
 * A download ran past {@code aimodel.artifacts.download-timeout}. The zip is cut off, so the
 * client sees a truncated download, not a valid smaller one.
 */
public class DownloadTimeoutException extends IOException {

    public DownloadTimeoutException(String message) {
        super(message);
    }
}
