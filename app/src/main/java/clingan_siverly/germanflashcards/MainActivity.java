package clingan_siverly.germanflashcards;

import android.arch.lifecycle.ViewModelProviders;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import mike.networking.DownloadCallback;
import mike.networking.NetworkFragment;

public class MainActivity extends AppCompatActivity implements DownloadCallback, MyFrags.ShowsMyFrags{
    //I need to keep a reference to the network fragment which owns the AsyncTask that actually does the work
    private NetworkFragment mNetworkFragment;
    WordModel mWordModel = null;

    //The two methods here are used as listeners for the two buttons for activity_main layout
    public void goToFlashCards(View v){
        startCardFrag();
    }

    public void showAllCards(View v){
        startListFrag();
    }


    private void startListFrag(){
        Toast.makeText(this, "This has not been implemented yet.", Toast.LENGTH_SHORT).show();
        //TODO
    }

    private void startCardFrag(){
        if(mWordModel.loadCards(getFilesDir().getAbsolutePath())) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            Fragment prev = getSupportFragmentManager().findFragmentByTag(CardFragment.tag);
            if (prev != null) {
                fragmentTransaction.remove(prev).commitNow();
            }
            CardFragment fragment = new CardFragment();
            fragmentTransaction.replace(android.R.id.content, fragment, CardFragment.tag).commit();
        }
    }

    private void refreshDisplayedFrags(){
        List<Fragment> fragList = getSupportFragmentManager().getFragments();

        for(Fragment frag : fragList){
            if(frag != null && frag.isVisible()) {
                MyFrags thisFrag = ((MyFrags) frag);
                thisFrag.showFrag(this);
            }
        }
    }


    @Override
    public void showFrag(CardFragment frag){
        startCardFrag();
    }
    @Override
    public void dismissFrag(Fragment frag){
        getSupportFragmentManager().beginTransaction().remove(frag).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWordModel = ViewModelProviders.of(this).get(WordModel.class);

        //I just want to download words when the user starts the app
        if(savedInstanceState == null || !savedInstanceState.getBoolean("downloaded", false))
            getNewWords();
        else if(mWordModel.getNumWordPairs() > 0){
            refreshDisplayedFrags();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        outState.putBoolean("downloaded", true);

        super.onSaveInstanceState(outState);
    }

    private void getNewWords(){
        final String address = "http://germanwords.000webhostapp.com/wordPairs.php";

        mNetworkFragment = new NetworkFragment();
        Bundle args = new Bundle();
        args.putString(NetworkFragment.BASE_ADDRESS, address);
        mNetworkFragment.setArguments(args);

        //I need this to be commitNow. Starting download before commit is finished will cause crashes
        getSupportFragmentManager().beginTransaction()
                .add(mNetworkFragment, NetworkFragment.TAG).commitNow();
    }

    @Override
    public void onBackPressed(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment frag = fragmentManager.findFragmentByTag("cardFrag");

        if(frag != null) {
            fragmentManager.beginTransaction().remove(frag).commitNow();
        }
        else {
            super.onBackPressed();
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
        if (mNetworkFragment != null) {
            mNetworkFragment.cancelDownload();
            //shouldn't use commitNow here. Other transactions involving it may still be running
            getSupportFragmentManager().beginTransaction().remove(mNetworkFragment).commit();
            mNetworkFragment = null;

        }
        String text;
        if(success) {
            text = resultMessage + " new words added to the library.";
            //if user decided to already view cards, I want to refresh that Fragment with the new cards
            if(!text.equals("no")){
                refreshDisplayedFrags();
            }
        }
        else
            text = resultMessage;
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}
