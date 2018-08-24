package mike.logic;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import mike.networking.NetworkingI;
import mike.networking.NetworkingStub;

public class Logic implements LogicI {
    /**
     * readies the next word pair in the set. If all words have been gone through, this should go back
     * to the first word pair
     */
    @Override
    public void nextWord() {

    }

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
    @Override
    public int processDownloadedWords(Context context, String words) {
       if(words == null || words.equals("")){
           return 0;
       }
       else {
           //int englishsymbol         declaration
           //englishsymbol = 0         assignment
           //int englishsymbol = 0     declaration + initialize (assignment)
           int englishsymbol = -1;
           int germansymbol = 0;
           int paircount = 0;
           ArrayList<String> list = new ArrayList<>();
           //note from Mike. do-while loop is more appropriate here, but while is easier to teach.
           while (true) {
               germansymbol = words.indexOf(',', englishsymbol);
               if(germansymbol == -1)
                   break;
               String germanword = words.substring(englishsymbol +1, germansymbol);
               englishsymbol = words.indexOf('.', germansymbol);
               String englishword = words.substring(germansymbol + 1, englishsymbol);
               list.add(germanword);
               list.add(englishword);
               paircount = paircount +1; //paircount++
           }
            int size = list.size();
           //file stuff
           File cardfile = new File(context.getFilesDir(), "wordpairs");
           try {
               FileWriter cardfilewriter = new FileWriter(cardfile);
               for(int i = 0; i < size ;i++){ //i is the index of list im working on
                   cardfilewriter.write(list.get(i) + '\n');
               }
               cardfilewriter.flush();
           } catch (IOException e) {
               e.printStackTrace();
               //TODO ...maybe...
           }

           return paircount;
       }
    }

    /**
     * Open the file you use to save cards to (see above)
     * read the file and store the appropriate pairs in an appropriate structure.
     * I recommend storing pairs in a private class you create, and an ArrayList to store those -
     * This should make it easy for you to deal with each pair or words as a single object.
     * <p>
     * See this for how to save files:
     * https://developer.android.com/training/data-storage/files#java
     *
     * @return a boolean (true/false) value indicating whether there were any cards to be loaded or not
     */
    @Override
    public boolean loadCards(Context context) {
        File cardfile = new File(context.getFilesDir(), "wordpairs");
        try {
 //            BufferedReader reader = new BufferedReader(new FileReader(cardfile));
            FileReader cardfilereader = new FileReader(cardfile);
            BufferedReader reader = new BufferedReader(cardfilereader);
            String gline = reader.readLine();
            if(gline == null)
                return false;
            String eline = reader.readLine();
            if(eline == null)
                return false;
            //TODO ASSIGN THEM TO CONTAINER
            for (int i = 2; gline != null; i++){
                if (i % 2 == 0) {//even line
                    gline = reader.readLine();
                }
                else if(i % 2 == 1){ //odd line
                    eline = reader.readLine();
                    //TODO stuff
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;

    }

    /**
     * Look at the readied word pair (see nextWord) for the english part in the pair.
     * I go into a bit more detail about why I say 'pair' in loadCards.
     *
     * @return the english translation of the word
     */
    @Override
    public String getEnglishWord() {
        return null;
    }

    /**
     * Look at the readied word pair (see nextWord) for the german part in the pair.
     * I go into a bit more detail about why I say 'pair' in loadCards.
     *
     * @return the german translation of the word
     */
    @Override
    public String getGermanTranslation() {
        return null;
    }
}
