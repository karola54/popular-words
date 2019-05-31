package pl.sii;

import org.apache.commons.lang3.NotImplementedException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;

public class PopularWords {
    public static void main(String[] args) {
        PopularWords popularWords = new PopularWords();
        Map<String, Long> result = popularWords.findOneThousandMostPopularWords();
        //result.entrySet().forEach(System.out::println);

        int i = 1;
        for (Map.Entry<String, Long> entry : result.entrySet()) {
            System.out.println(i + ": " + entry.getKey() + " - " + entry.getValue());
            i++;
        }
    }

    public Map<String, Long> findOneThousandMostPopularWords() {
        Map<String, Long> result = findAllWords();

        result = result.entrySet().stream()
                .sorted(comparingByReverseValueAndKey())
                .limit(1000)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v1, LinkedHashMap::new));
        return result;
    }

    private Map<String, Long> findAllWords() {
        Map<String, Long> result = new HashMap<>();

        InputStream is = getClass().getResourceAsStream("/3esl.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split("\\s+");
                for (String word : words) {
                    result.merge(word.toLowerCase(), 1L, Long::sum);
                }

            }
        } catch (IOException ex) {
            System.err.println("Exception during reading file: " + ex.getMessage());
        } finally {
            try {
                reader.close();
            } catch (IOException e) {

            }
        }

        return result;
    }

    private Comparator<Map.Entry<String, Long>> comparingByReverseValueAndKey() {
        return (e1, e2) -> {
            int cmp = e2.getValue().compareTo(e1.getValue());
            if (cmp == 0) {
                cmp = e1.getKey().compareTo(e2.getKey());
            }
            return cmp;
        };
    }
}
