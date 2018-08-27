package mike.networking;

import android.net.NetworkInfo;

/**
 * Copied from https://developer.android.com/training/basics/network-ops/connecting
 */
public interface DownloadCallback {

    /**
     * Get the device's active network status in the form of a NetworkInfo object.
     */
    NetworkInfo getActiveNetworkInfo();

    /**
     * Indicates that the download operation has finished. This method is called even if the
     * download hasn't completed successfully.
     */
    void finishDownloading(String resultMessage, boolean success);
}
