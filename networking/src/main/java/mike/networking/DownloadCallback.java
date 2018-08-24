package mike.networking;

import android.net.NetworkInfo;

/**
 * Copied from https://developer.android.com/training/basics/network-ops/connecting
 * @param <T> type of the network operation result this is expected to handle
 */
public interface DownloadCallback<T> {

    /**
     * Get the device's active network status in the form of a NetworkInfo object.
     */
    NetworkInfo getActiveNetworkInfo();

    /**
     * Indicates that the download operation has finished. This method is called even if the
     * download hasn't completed successfully.
     */
    void finishDownloading(T result, boolean success);
}
