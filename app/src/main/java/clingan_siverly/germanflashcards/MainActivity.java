package clingan_siverly.germanflashcards;

import android.arch.lifecycle.ViewModelProviders;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyFrags.ShowsMyFrags{
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
        if(mWordModel.getNumWordPairs() > 0) {
            removeOldFrag(CardListFragment.tag);
            CardListFragment fragment = new CardListFragment();

            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, fragment, CardListFragment.tag).commit();
        }
    }

    private void startCardFrag(Bundle args){
        if(mWordModel.getNumWordPairs() > 0) {
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
        //this creates/retrieves my model and retains it as long as "this" Activity is alive
        mWordModel = ViewModelProviders.of(this).get(WordModel.class);
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
        getNewWords();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        outState.putBoolean("downloaded", true);

        super.onSaveInstanceState(outState);
    }

    private void getNewWords(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("germanEnglishWordPairs")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    ArrayList<WordPair> pairs = new ArrayList<>(task.getResult().size());

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        pairs.add(new WordPair(document.getString("german"),
                                document.getString("english")));
                    }
                    mWordModel.loadCards(pairs);
                    // This isn't necessary, but it seems courteous to update a
                    // user's view in case the list of cards has changed
                    refreshDisplayedFrags();
                }
                else {
                    Log.w("meh", "Error loading word pairs.", task.getException());
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
}
