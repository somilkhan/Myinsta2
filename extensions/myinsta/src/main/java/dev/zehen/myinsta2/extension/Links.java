package dev.zehen.myinsta2.extension;

import java.io.IOException;
import java.net.URI;

/** Runtime URI filters used by MyInsta2's network-level privacy hooks. */
public final class Links {
    private Links() {}

    /**
     * Blocks Instagram's live heartbeat/viewer-count request. The request is
     * cancelled by throwing through Instagram's existing network error path;
     * unrelated requests are left untouched.
     */
    public static void interceptUri(URI uri) throws IOException {
        if (uri == null) return;
        String path = uri.getPath();
        if (path != null && path.contains("/heartbeat_and_get_viewer_count/")) {
            throw new IOException("MyInsta2: block live viewer heartbeat");
        }
    }
}
