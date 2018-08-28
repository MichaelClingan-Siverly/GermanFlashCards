package mike.networking;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Implementation of headless Fragment that runs an AsyncTask to fetch data from the network.
 * Copied from https://developer.android.com/training/basics/network-ops/connecting
 * I would have done roughly the same thing myself (AsyncTask, HttpsURLConnection),
 * but there's no need doing that when this is available and is more robust than what I
 * would have done (I probably wouldn't have tried using a fragment for this).
 *
 * results are formatted here instead of having the server do it. I'm much better with java than PHP
 */
public class NetworkFragment extends Fragment {
    public static final String TAG = "NetworkFragment";
    public static final String BASE_ADDRESS = "base";

    private DownloadCallback mCallback;
    private DownloadTask mDownloadTask;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO I'm sure I'll come back to do something here once I begin dealing with Android lifecycles
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Host Activity will handle callbacks from task.
        mCallback = (DownloadCallback) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Clear reference to host Activity to avoid memory leak.
        mCallback = null;
    }

    @Override
    public void onDestroy() {
        // Cancel task when Fragment is destroyed.
        cancelDownload();
        super.onDestroy();
    }

    /**
     * Start non-blocking execution of DownloadTask.
     * @return false if no base address was provided, true otherwise
     */
    public boolean startDownload() {
        String baseAddress = getArguments().getString(BASE_ADDRESS);

        if(baseAddress == null)
            return false;
        else if(mDownloadTask == null) {
            String parentDirPath = getActivity().getFilesDir().getAbsolutePath();
            mDownloadTask = new DownloadTask(parentDirPath, mCallback);
            mDownloadTask.execute(baseAddress);
        }
        return true;
    }

    /**
     * Cancel (and interrupt if necessary) any ongoing DownloadTask execution.
     */
    public void cancelDownload() {
        if (mDownloadTask != null) {
            mDownloadTask.cancel(true);
        }
    }
}