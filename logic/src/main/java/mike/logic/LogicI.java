package mike.logic;

import android.content.Context;

/**
 * Created by mike on 5/27/2018.
 * Define methods which Sam will have to implement, and give a bit of guidance on what she needs to do
 */

public interface LogicI {
    /**
     * readies the next word pair in the set. If all words have been gone through, this should go back
     * to the first word pair
     */
    void nextWord();
    /**
     * The words have been downloaded and are now passed in as a parameter for you to process
     * You will need to check if there were any errors (check if the string is null).
     * If the string exists, it will need to be parsed for each word/pair.
     * Finally, the pairs will need to be saved to a file of your choosing in a format of your choosing;
     * It is up to you if you want the saved words to overwrite any currently saved words or if
     * they should be appended to the end of the list.
     * You should not set any global variables for this method.
     *
     * @param context the context that this is called from
     * @param words string representing the words that was received from the server
     *
     * @return an integer value representing the number of word pairs downloaded.
     * If downloading failed (words is null or an empty string), return 0.
     */
    int processDownloadedWords(Context context, String words);
    /**
     * Open the file you use to save cards to (see above)
     * read the file and store the appropriate pairs in an appropriate structure.
     * I recommend storing pairs in a private class you create, and an ArrayList to store those -
     * This should make it easy for you to deal with each pair or words as a single object.
     *
     * See this for how to save files:
     * https://developer.android.com/training/data-storage/files#java
     * @param context the context that this is called from
     *
     * @return a boolean (true/false) value indicating whether there were any cards to be loaded or not
     */
    boolean loadCards(Context context);

    /**
     * Look at the readied word pair (see nextWord) for the english part in the pair.
     * I go into a bit more detail about why I say 'pair' in loadCards.
     * @return the english translation of the word
     */
    String getEnglishWord();

    /**
     * Look at the readied word pair (see nextWord) for the german part in the pair.
     * I go into a bit more detail about why I say 'pair' in loadCards.
     * @return the german translation of the word
     */
    String getGermanTranslation();

}
