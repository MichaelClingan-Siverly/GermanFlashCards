package clingan_siverly.germanflashcards;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import mike.logic.Logic;
import mike.logic.LogicI;
import mike.networking.DownloadCallback;
import mike.networking.NetworkFragment;

public class MainActivity extends AppCompatActivity implements DownloadCallback{
    //I need to keep a reference to the network fragment which owns the AsyncTask that actually does the work
    private NetworkFragment mNetworkFragment;
    //tells if a downlaod is in progress, so I won't let user try again while still downloading
    private boolean mDownloading = false;


    //The two methods here are used as listeners for the two buttons for activity_main layout
    public void downloadCards(View v){
        startDownload();
    }

    public void goToFlashCards(View v){
        //This may look complicated, but all it does is display the cards
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        CardFragment fragment = new CardFragment();
        fragmentTransaction.add(R.id.mainLayout, fragment, "cardFrag");
        fragmentTransaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        getNewWords();
    }

    private void getNewWords(){
        final String address = "http://germanwords.000webhostapp.com/wordPairs.php";

        mNetworkFragment = new NetworkFragment();
        Bundle args = new Bundle();
        args.putString(NetworkFragment.BASE_ADDRESS, address);

        mNetworkFragment.setArguments(args);
        getFragmentManager().beginTransaction().add(mNetworkFragment, NetworkFragment.TAG).commit();
    }

    @Override
    public void onBackPressed(){
        FragmentManager fragmentManager = getSupportFragmentManager();

        if(fragmentManager.findFragmentByTag("cardFrag") != null){
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(fragmentManager.findFragmentByTag("cardFrag"));
            fragmentTransaction.commit();
        }
        else
            super.onBackPressed();
    }




    private void startDownload() {
        if (!mDownloading && mNetworkFragment != null) {
            // Execute the async download.
            if(mNetworkFragment.startDownload())
                mDownloading = true;
            else
                Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Get the device's active network status in the form of a NetworkInfo object.
     */
    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if(connectivityManager != null)
            return connectivityManager.getActiveNetworkInfo();
        else
            return null;
    }

    @Override
    public void postProgress(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Indicates that the download operation has finished. This method is called even if the
     * download hasn't completed successfully.
     *
     * @param success true if the result represents the actual string, false if its an error message
     */
    @Override
    public void finishDownloading(String resultMessage, boolean success) {
        mDownloading = false;
        if (mNetworkFragment != null) {
            mNetworkFragment.cancelDownload();
        }
        String text;
        if(success)
            text = resultMessage + " new words added to the library.";
        else
            text = resultMessage;
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}
