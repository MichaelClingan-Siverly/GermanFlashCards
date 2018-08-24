package clingan_siverly.germanflashcards;

import android.support.v4.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.Random;

import mike.logic.LogicI;
import mike.logic.LogicStub;

/**
 * Created by mike on 5/27/2018.
 * I'm being a bit lazy about handling lifecycle stuff.
 * We can get to that later if I really feel like it (I doubt it)
 */

public final class CardFragment extends DialogFragment {
    View v = null;
    LogicI logic;
    int bgColor = Color.TRANSPARENT;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logic = new LogicStub();
        if(!logic.loadCards(getContext())){
            getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
            Toast.makeText(getContext(), "no words saved. try downloading.", Toast.LENGTH_LONG).show();
        }
    }

    private View.OnClickListener nextButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            logic.nextWord();
            Random rand = new Random();
            boolean getEnglishWord = rand.nextBoolean();
            Button b = v.findViewById(R.id.cardButton);
            setWord(b, getEnglishWord); //Fix this
        }
    };

    private View.OnClickListener cardButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setWord((Button)v, bgColor == Color.BLACK);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.card_layout, container, false);
        //and then set my background and listeners
        Button cardButton = v.findViewById(R.id.cardButton);
        cardButton.setOnClickListener(cardButtonListener);
        v.findViewById(R.id.nextButton).setOnClickListener(nextButtonListener);
        //and finally, set the text on the button.
        nextButtonListener.onClick(cardButton);
        return v;
    }

    private void setWord(Button b,boolean english){
        String word;
        if(english) {
            setColors(b, Color.WHITE, Color.BLACK);
            word = logic.getEnglishWord();
        }
        else {
            setColors(b, Color.BLACK, Color.WHITE);
            word = logic.getGermanTranslation();
        }
        b.setText(word);
    }

    private void setColors(Button b, int bg, int font){
        bgColor = bg;
        b.setBackgroundColor(bg);
        b.setTextColor(font);
    }
}