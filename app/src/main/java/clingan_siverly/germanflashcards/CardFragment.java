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

import java.util.Random;

/**
 * Created by mike on 5/27/2018.
 * I'm being a bit lazy about handling lifecycle stuff.
 * We can get to that later if I really feel like it (I doubt it)
 */

public final class CardFragment extends Fragment implements MyFrags{
    View v = null;
    public static String tag = "cardFrag";
    int bgColor = Color.TRANSPARENT;
    WordModel mWordModel = null;

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
            else{
                mWordModel.shuffleWords();
            }
        }
    }



    private View.OnClickListener nextButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(mWordModel != null) {
                mWordModel.readyNextPair();
                Random rand = new Random();
                boolean getEnglishWord = rand.nextBoolean();
                Button b = v.findViewById(R.id.cardButton);
                setWord(b, getEnglishWord); //Fix this
            }
        }
    };

    private View.OnClickListener cardButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setWord((Button)v , mWordModel.checkIfGermanDisplayed());
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.card_layout, container, false);
        //and then set my background and listeners
        Button button = v.findViewById(R.id.cardButton);
        button.setOnClickListener(cardButtonListener);

        button = v.findViewById(R.id.nextButton);
        button.setOnClickListener(nextButtonListener);
        //and finally, set the text on the button.
        nextButtonListener.onClick(button);
        return v;
    }

    private void setWord(Button b,boolean english){
        String word;
        if(english) {
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
        bgColor = bg;
        b.setBackgroundColor(bg);
        b.setTextColor(font);
    }

    @Override
    public void showFrag(ShowsMyFrags caller){
        caller.showFrag(this);
    }
}