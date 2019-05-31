package pl.sii;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class PopularWordsTest {
    private static final PopularWords testee = new PopularWords();

    @Test
    public void shouldReturnOneThousandMostPopularWords() {
        //given
        Map<String, Long> wordsFrequencyListCreatedByAdamKilgarriff = getWordsFrequencyListCreatedByAdamKilgarriff();

        //when
        Map<String, Long> result = testee.findOneThousandMostPopularWords();

        //then
        assertFalse(result.isEmpty());
        assertEquals(1000, result.size());
        compareWordListsFrequency(wordsFrequencyListCreatedByAdamKilgarriff, result);
    }

    private void compareWordListsFrequency(Map<String, Long> wordsFrequencyListCreatedByAdamKilgarriff, Map<String, Long> result) {
        long totalFrequencyByKilgarriff = wordsFrequencyListCreatedByAdamKilgarriff.values().stream().reduce(0L, Long::sum);
        long totalFrequencyInAResult = result.values().stream().reduce(0L, Long::sum);
        System.out.println("totalFrequencyByKilgarriff = " + totalFrequencyByKilgarriff);
        System.out.println("totalFrequencyInAResult = " + totalFrequencyInAResult);

        result.forEach((key, value) -> {
            BigDecimal valueUsagePercentage = calculatePercentage(value, totalFrequencyInAResult);
            Long kilgarriffCount = wordsFrequencyListCreatedByAdamKilgarriff.get(key);
            BigDecimal kilgarriffUsagePercentage = calculatePercentage(kilgarriffCount != null ? kilgarriffCount : 0, totalFrequencyByKilgarriff);
            BigDecimal diff = kilgarriffUsagePercentage.subtract(valueUsagePercentage);
            System.out.println(key + "," + valueUsagePercentage + "%," + kilgarriffUsagePercentage + "%," + (new BigDecimal(0.5).compareTo(diff.abs()) > 0) + " " + diff);
        });
    }

    private BigDecimal calculatePercentage(double obtained, double total) {
        return BigDecimal.valueOf(obtained * 100 / total).setScale(4, RoundingMode.HALF_UP);
    }

    private Map<String, Long> getWordsFrequencyListCreatedByAdamKilgarriff() {
        Map<String, Long> result = new HashMap<>(1000);

        InputStream is = getClass().getResourceAsStream("/all.num");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            for (int i = 0; i < 1000 && (line = reader.readLine()) != null; i++) {
                String[] data = line.split(" ");
                result.put(data[1], Long.valueOf(data[0]));
            }
        } catch (IOException ex) {
            System.err.println("Exception during reading file: " + ex.getMessage());
        }

        return result;
    }
}
