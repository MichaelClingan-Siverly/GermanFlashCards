package mike.networking;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

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
        /* This alone is enough for surviving configuration changes.
         * If the Activity that created this needs a reference to it (ex. if I want a user to
         * be able to cancel the download), I'll need to have a getInstance method as well
         */
        setRetainInstance(true);
        startDownload();
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
     */
    private void startDownload() {
        Bundle args = getArguments();
        if(args == null)
            return;

        String baseAddress = args.getString(BASE_ADDRESS);

        if(baseAddress != null && mDownloadTask == null && getActivity() != null){
            String parentDirPath = getActivity().getFilesDir().getAbsolutePath();
            mDownloadTask = new DownloadTask(parentDirPath, mCallback);
            mDownloadTask.execute(baseAddress);

        }
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