package clingan_siverly.germanflashcards;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by mike on 5/27/2018.
 * I'm being a bit lazy about handling lifecycle stuff.
 * We can get to that later if I really feel like it (I doubt it)
 */

public final class CardFragment extends Fragment implements MyFrags{
    public final static String tag = "cardFrag";
    public final static String shuffle = "shuffle";
    public final static String showWordIndex = "show";
    public final static String displayEnglish = "eng";
    private WordModel mWordModel = null;
    private View mLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();
        if(activity != null) {
            mWordModel = ViewModelProviders.of(activity).get(WordModel.class);
            if(mWordModel.getNumWordPairs() == 0){
                Toast.makeText(activity, "No words saved. Try again later.",Toast.LENGTH_SHORT).show();
                activity.dismissFrag(this);
            }
        }
    }

    private View.OnClickListener nextButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mWordModel != null) {
                mWordModel.readyNextPair();
                //this may be called before onCreateView is set, so this works while getView() does not
                Button b = mLayout.findViewById(R.id.cardButton);
                setWord(b);
            }
        }
    };

    private View.OnClickListener cardButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setWord((Button)v);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mLayout = inflater.inflate(R.layout.card_layout, container, false);
        //and then set my background and listeners
        Button button = mLayout.findViewById(R.id.cardButton);
        button.setOnClickListener(cardButtonListener);

        button = mLayout.findViewById(R.id.nextButton);
        button.setOnClickListener(nextButtonListener);

        //and finally, set the text on the button.
        if(initWord())
            nextButtonListener.onClick(button);

        return mLayout;
    }


    //return true if pairs were shuffled, false otherwise
    private boolean initWord(){
        if(getArguments() != null) {
            if (getArguments().getBoolean(shuffle, false)) {
                mWordModel.shuffleWords();
                return true;
            }
            if (getArguments().getInt(showWordIndex, -1) >= 0) {
                setWord(new Button(getContext()));
            }
        }
        return false;
    }

    private void setWord(Button b){
        String word;
        //if a German word is currently displayed, I want to display the English translation (and vice versa)
        if(mWordModel.checkIfGermanDisplayed()) {
            setColors(b, Color.GRAY, Color.BLACK);
            word = mWordModel.getEnglishWord();
        }
        else {
            setColors(b, Color.BLACK, Color.WHITE);
            word = mWordModel.getGermanTranslation();

        }
        word = word.replaceAll(";", "\n");
        b.setText(word);
    }

    private void setColors(Button b, int bg, int font){
        b.setBackgroundColor(bg);
        b.setTextColor(font);
    }

    @Override
    public void showFrag(ShowsMyFrags caller){
        caller.showFrag(this);
    }
}