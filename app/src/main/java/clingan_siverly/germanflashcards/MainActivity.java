package clingan_siverly.germanflashcards;

import android.arch.lifecycle.ViewModelProviders;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import mike.networking.DownloadCallback;
import mike.networking.NetworkFragment;

public class MainActivity extends AppCompatActivity implements DownloadCallback, MyFrags.ShowsMyFrags{
    //I need to keep a reference to the network fragment which owns the AsyncTask that actually does the work
    private NetworkFragment mNetworkFragment;
    private FirebaseAuth mAuth;
    WordModel mWordModel = null;

    //The two methods here are used as listeners for the two buttons for activity_main layout
    public void goToFlashCards(View v){
        startCardFrag();
    }

    public void showAllCards(View v){
        startListFrag();
    }


    private void removeOldFrag(String tag){
        Fragment prev = getSupportFragmentManager().findFragmentByTag(tag);
        if (prev != null) {
            getSupportFragmentManager().beginTransaction().remove(prev).commitNow();
        }
    }

    private void startListFrag(){
        if(mWordModel.loadCards(getFilesDir().getAbsolutePath())) {
            removeOldFrag(CardListFragment.tag);
            CardListFragment fragment = new CardListFragment();

            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, fragment, CardListFragment.tag).commit();
        }
    }

    private void startCardFrag(Bundle args){
        if(mWordModel.loadCards(getFilesDir().getAbsolutePath())) {
            removeOldFrag(CardFragment.tag);

            CardFragment fragment = new CardFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, fragment, CardFragment.tag).commit();
        }
    }

    private void startCardFrag(){
        Bundle args = new Bundle();
        args.putBoolean(CardFragment.shuffle, true);
        startCardFrag(args);
    }

    public void startCardFrag(int position, boolean english){
        Bundle args = new Bundle();
        args.putInt(CardFragment.showWordIndex, position);
        args.putBoolean(CardFragment.displayEnglish, english);
        startCardFrag(args);
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
    public void showFrag(CardListFragment frag){
        startListFrag();
    }
    @Override
    public void dismissFrag(Fragment frag){
        getSupportFragmentManager().beginTransaction().remove(frag).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mWordModel = ViewModelProviders.of(this).get(WordModel.class);

        //I just want to download words when the user starts the app
        if(savedInstanceState == null || !savedInstanceState.getBoolean("downloaded", false))
            getNewWords();
        else if(mWordModel.getNumWordPairs() > 0){
            refreshDisplayedFrags();
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        // This is all rather basic. For a more in-depth use, I'll have to add sign-in screens, etc.
        // But since I'm only authenticating for practice (no actual reason), I'll live with this for now
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        String toastText;
                        if(task.isSuccessful()){
                            toastText = "Signed in anonymously.";
                        }
                        else{
                            Log.w("meh", "signInAnonymously:failure", task.getException());

                            toastText = "Authentication failed.";
                        }
                        Toast.makeText(MainActivity.this, toastText,
                                Toast.LENGTH_SHORT).show();
                    }
                });
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
//        getSupportFragmentManager().beginTransaction()
//                .add(mNetworkFragment, NetworkFragment.TAG).commitNow();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("germanEnglishWordPairs")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("meh", document.getId() + " => " + document.getData());
                    }
                } else {
                    Log.w("meh", "Error getting documents.", task.getException());
                }

            }
        });
    }

    @Override
    public void onBackPressed(){
        List<Fragment> frags = getSupportFragmentManager().getFragments();

        if(frags != null && frags.size() > 0) {
            Fragment frag = frags.get(frags.size() - 1);
            getSupportFragmentManager().beginTransaction().remove(frag).commitNow();
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
            String modifiedResultMessage;

            //if user decided to already view cards, I want to refresh that Fragment with the new cards
            if(!resultMessage.equals("0")){
                modifiedResultMessage = resultMessage;
                mWordModel.indicateNewWordsDownloaded();
                refreshDisplayedFrags();
            }
            else
                modifiedResultMessage = "No";
            text = modifiedResultMessage + " new words added to the library.";
        }
        else
            text = resultMessage;
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}
