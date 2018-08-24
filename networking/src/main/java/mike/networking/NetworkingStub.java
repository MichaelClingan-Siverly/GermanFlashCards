package mike.networking;

/**
 * Created by mike on 5/27/2018.
 *
 * A mock is more appropriate - since the method is supposed to return different things.
 * However, she is extremely new to programming, and it may be best to leave the whole test runner
 * thing for another time.
 */

public class NetworkingStub implements NetworkingI {
    @Override
    public String getCards() {
        String s = "seufzen,sigh.Pfähle,piles.erhob,to lift.bloß,just;merely.Erfahrungen,experience.";
//        String s = null;
        return s;
    }
}
