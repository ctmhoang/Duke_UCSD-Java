
import edu.duke.FileResource;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class CodonCount {
    private HashMap<String, Integer> DNACodons;

    public CodonCount() {
        DNACodons = new HashMap<>();
    }

    public void buildCodonMap(int start, String dna) {
        DNACodons.clear();
        int strandLen = (dna.length() - start) / 3 * 3 + start;
        for (int i = start; i < strandLen; i += 3) {
            String codon = dna.substring(i, i + 3);
            if (!DNACodons.containsKey(codon)) {
                DNACodons.put(codon, 1);
            } else {
                DNACodons.put(codon, DNACodons.get(codon) + 1);
            }
        }
    }

    public String getMostCommonCodon() {
        return Collections.max(DNACodons.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
    }

    public void printCodonCounts(int start, int end) {
        for (Map.Entry codon : DNACodons.entrySet()) {
            int value = (int) codon.getValue();
            if (value >= start && value <= end) {
                System.out.println(codon.getKey() + "\t" + codon.getValue());
            }
        }
    }

    public void tester() {
        FileResource resource = new FileResource();
        String DNA = resource.asString().trim();
        CodonCount Tester = new CodonCount();
        for (int i = 0; i < 3; i++) {
            buildCodonMap(i, DNA);
            System.out.println("\nReading frame starting with " + i + " results in " + DNACodons.size() + " unique codons\n" +
                    "and most common codon is " + getMostCommonCodon() + " with count " + DNACodons.get(getMostCommonCodon()) + "\n" +
                    "Counts of codons between 1 and 5 inclusive are:");
            printCodonCounts(1z, 5);
        }
    }
}
