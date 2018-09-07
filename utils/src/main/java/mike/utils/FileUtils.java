package mike.utils;

import org.apache.commons.io.input.ReversedLinesFileReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    private final static String childPath = "wordPairs";

    public static void saveWordsToFile(String fileDirectory, List<WordPair> pairs) throws IOException {
        File cardFile = new File(fileDirectory, childPath);
        //not sure if I prefer try-with-resources or try-finally to ensure resources are closed
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(cardFile, true), StandardCharsets.UTF_8)){
            for (int i = pairs.size()-1; i >= 0; i--) {
                WordPair pair = pairs.get(i);
                writer.write(pair.getEnglishWord() + '\n' + pair.getGermanWord() + '\n');
            }
            writer.flush();
        }
    }

    public static List<WordPair> readWordsFromFile(String fileDirectory){
        ArrayList<WordPair> list = new ArrayList<>();
        File cardFile = new File(fileDirectory, childPath);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(cardFile), StandardCharsets.UTF_8));

            String englishWord = null;
            String word;
            while ((word = reader.readLine()) != null) {
                if (englishWord == null) {
                    englishWord = word;
                } else {
                    list.add(new WordPair(word, englishWord));
                    englishWord = null;
                }
            }
            return list;
        }
        catch(IOException e){
            //if there's a problem opening or reading the file, I guess it's finished reading all it can
            return list;
        }
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
