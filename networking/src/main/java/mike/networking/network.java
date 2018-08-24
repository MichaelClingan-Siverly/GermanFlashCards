package mike.networking;

/**
 * Created by mike on 5/27/2018.
 */
public class network implements NetworkingI {
    private final String path = "http://www.germanwords.epizy.com/wordPairs.txt";
    /**
     * Requests the new list of cards from the server.
     * This method will not not process or parse the string for you - you must do that yourself.
     *
     * @return the String encoding the english and german words that was returned from the server,
     * or null if this was unable to retrieve cards from the server.
     */
    @Override
    public String getCards() {
        return null;
    }
}
