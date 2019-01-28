package clingan_siverly.germanflashcards;

/**
 * represents a german word and it's english translation
 * (or vice versa, however one wants to look at it)
 */
public class WordPair {
    private String gWord;
    private String eWord;
    public WordPair(String germanWord, String englishWord){
        gWord = germanWord;
        eWord = englishWord;
    }
    public String getGermanWord(){
        return gWord;
    }
    public String getEnglishWord(){
        return eWord;
    }
}
