package mike.utils;

import org.apache.commons.io.input.ReversedLinesFileReader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class FileUtils {
    private final static String childPath = "wordPairs";

    public static void saveWordsToFile(String fileDirectory, List<WordPair> pairs) throws IOException {
        File cardFile = new File(fileDirectory, childPath);
        //not sure if I prefer try-with-resources or try-finally to ensure resources are closed
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(cardFile, true), StandardCharsets.UTF_8)){
            for (int i = pairs.size()-1; i >= 0; i--) {
                WordPair pair = pairs.get(i);
                writer.write(pair.getEnglishWord() + '\n' + pair.getGermanWord());
            }
            writer.flush();
        }
    }

    public static void readWordsFromFile(){
        //TODO move the file reading from Logic to this. I'm positive I'll have this throw an exception as well
    }

    public static String readLastWordFromFile(String fileDirectory) throws IOException {
        File cardFile = new File(fileDirectory, childPath);
        if(!cardFile.exists()) {
            return null;
        }

        ReversedLinesFileReader reader = new ReversedLinesFileReader(cardFile, StandardCharsets.UTF_8);
        String line = reader.readLine();
        reader.close();

        return line;
    }
}
