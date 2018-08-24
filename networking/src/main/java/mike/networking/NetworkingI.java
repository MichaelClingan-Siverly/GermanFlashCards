package mike.networking;

/**
 * Created by mike on 5/27/2018.
 *
 * Interface for the network functions your app will use.
 * When creating the object in your app, declare it as this kind of object
 * (not as one of the children), but create it as the intended child.
 * This will allow your code to use the stub I create until I'm finished making the actual class
 * and the corresponding website it will communicate with.
 * An example of what I meant above is this:
 * NetworkingI net = new NetworkingStub();
 *
 * You will not have to implement this interface yourself, nor will you have to modify the classes
 * which do.
 * It would be more appropriate for me to stick this module in a .jar so that you couldn't do it,
 * but at least this way you can look at my code to see how it was done.
 */

public interface NetworkingI {
    /**
     * Requests the new list of cards from the server.
     * This method will not not process or parse the string for you - you must do that yourself.
     * @return the String encoding the english and german words that was returned from the server,
     * or null if this was unable to retrieve cards from the server.
     */
    String getCards();
}
