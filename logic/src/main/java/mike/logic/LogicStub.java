package mike.logic;

import android.content.Context;

/**
 * Created by mike on 5/27/2018.
 * Something just to check if my fragment layouts look alright and stuff
 */

public class LogicStub implements LogicI {
    @Override
    public void nextWord() {
        //it's a stub. so I'll call this good already
    }

    @Override
    public int processDownloadedWords(Context context, String words) {
        return 1;
    }

    @Override
    public boolean loadCards(Context context) {
        return true;
    }

    @Override
    public String getEnglishWord() {
        return "hello";
    }

    @Override
    public String getGermanTranslation() {
        return "hallo";
    }
}
