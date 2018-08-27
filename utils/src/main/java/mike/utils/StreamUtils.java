package mike.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class StreamUtils {

    /**
     * reads an InputStream and adds their contents to an ArrayList
     */
    public static List<WordPair> readStream(InputStream stream) throws IOException {
        ArrayList<WordPair> pairs = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String line = reader.readLine();
        String german = null;

        while(line != null){
            if(german == null){
                german = line;
            }
            else{
                pairs.add(new WordPair(german, line));
                german = null;
            }
            line = reader.readLine();
        }
        return pairs;
    }
}
