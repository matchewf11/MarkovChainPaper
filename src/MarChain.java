import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class MarChain {

    private Map<String, Map<String, Double>> chain;

    public MarChain(String filePath) {
        chain = new HashMap<>();
        initChain(new File(filePath));
        toPercent();
    }

    public void print(int length, String fileName) {

        String word = chain.keySet().iterator().next();
        try(PrintWriter printer = new PrintWriter(fileName)) {
            for (int i = 0; i < length; i++) {
                String nextWord = ranWord(word);

                //handle null case
                if (nextWord == null) {
                    nextWord = chain.keySet().iterator().next();
                }

                printer.print(nextWord + " ");
                if (i != 0 && i % 10 == 0) printer.println();
                word = nextWord;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private String ranWord(String t) {
        double rand = new Random().nextDouble();
        double total = 0.0;

        if (!chain.containsKey(t)) return null;

        Map<String, Double> map = chain.get(t);
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            total += entry.getValue();
            if (total >= rand) {
                return entry.getKey();
            }
        }
        return null;
    }

    private void initChain(File file) {
        try(Scanner scanner = new Scanner(file)) {
            String prev = scanner.next();
            while (scanner.hasNext()) {
                String nextWord = scanner.next().replaceAll("[^\\sa-zA-Z0-9]", "").toLowerCase();
                if (chain.containsKey(prev)) {
                    Map<String, Double> map = chain.get(prev);
                    map.put(nextWord, map.getOrDefault(nextWord, 0.0) + 1);
                } else {
                    Map<String, Double> map = new HashMap<>();
                    map.put(nextWord, 1.0);
                    chain.put(prev, map);
                }
                prev = nextWord;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void toPercent() {
        // alter each hashmap to hold a percentage rather than total amount
        for (String t : chain.keySet()) {
            Map<String, Double> map = chain.get(t);
            double count = 0;
            for (String s : map.keySet()) {
                count += map.get(s);
            }
            //now we have the count for all values in this map
            for (String s : map.keySet()) {
                map.put(s, map.get(s) / count);
            }
        }
    }

}
