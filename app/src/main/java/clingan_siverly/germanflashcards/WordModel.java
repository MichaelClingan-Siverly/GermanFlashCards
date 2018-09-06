package clingan_siverly.germanflashcards;

import android.arch.lifecycle.ViewModel;

import java.util.Iterator;
import java.util.List;

import mike.utils.FileUtils;
import mike.utils.WordPair;

//removed the Interface. To make the ViewModel, something would need to know this class anyway
public class WordModel extends ViewModel {
    private List<WordPair> wordPairs = null;
    private int currentWordIndex = 0;
    private boolean nextwordIsEnglish = false;


    public int getNumWordPairs(){
        if(wordPairs == null)
            return 0;
        return wordPairs.size();
    }

    /**
     * readies the next word pair in the set. If all words have been gone through, this should go back
     * to the first word pair
     */
    public void readyNextPair() {
        if(wordPairs != null && wordPairs.size() > 0){
            if(currentWordIndex + 1 == wordPairs.size())
                currentWordIndex = 0;
            else
                currentWordIndex++;
        }
    }

    /**
     * Reads the saved cards and loads them so they can be displayed later
     *
     * @param mainDir the base directory for this app
     * @return true if cards were loaded, false if not
     */
    public boolean loadCards(String mainDir) {
        wordPairs = FileUtils.readWordsFromFile(mainDir);
        return wordPairs != null && wordPairs.size() > 0;
    }

    /**
     * Look at the readied word pair (see nextWord) for the other part in the pair
     *
     * @return the other word in the English-German word pair
     */
    public String flipCard() {
        return  getWord(nextwordIsEnglish);
    }

    /**
     * Look at the readied word pair (see nextWord) for the english part in the pair.
     *
     * @return the english translation of the word
     */
    public String getEnglishWord() {
        return getWord(true);
    }

    /**
     * Look at the readied word pair (see nextWord) for the german part in the pair.
     *
     * @return the german translation of the word
     */
    public String getGermanTranslation() {
        return getWord(false);
    }

    private String getWord(boolean english){
        if(wordPairs == null || currentWordIndex >= wordPairs.size())
            return null;
        else {
            nextwordIsEnglish = !english;
            if (english)
                return wordPairs.get(currentWordIndex).getEnglishWord();
            else
                return wordPairs.get(currentWordIndex).getEnglishWord();
        }
    }

    /**
     * like getEnglishWord. But for all English words.
     * Added this in case I wanted to allow the user to pick a specific word out to work on
     *
     * @return array containing all the English words saved, in no particular order
     */
    public String[] getAllEnglishWords() {
        return getAllWords(true);
    }

    /**
     * like getGermanWord. But for all German words.
     * Added this in case I wanted to allow the user to pick a specific word out to work on
     *
     * @return array containing all the German words saved, in no particular order
     */
    public String[] getAllGermanWords() {
        return getAllWords(false);
    }

    private String[] getAllWords(boolean english){
        if(wordPairs == null || currentWordIndex >= wordPairs.size())
            return null;

        String[] words = new String[wordPairs.size()];
        Iterator iter = wordPairs.iterator();
        for(int i = 0; i < words.length; i++){
            if(english)
                words[i] = ((WordPair)iter.next()).getEnglishWord();
            else
                words[i] = ((WordPair)iter.next()).getGermanWord();
        }
        return words;
    }
}
