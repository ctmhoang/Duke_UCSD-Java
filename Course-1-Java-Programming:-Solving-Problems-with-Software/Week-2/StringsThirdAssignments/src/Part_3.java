import edu.duke.StorageResource;
import edu.duke.FileResource;

public class Part_3 {
    public static void processGenes(StorageResource sr) {
        // This method processes all the strings in sr to find out information about them.
        //Specifically, it should:
        // print all the Strings in sr that are longer than 9 characters
        // print the number of Strings in sr that are longer than 9 characters
        // print the Strings in sr whose C-G-ratio is higher than 0.35
        // print the number of strings in sr whose C-G-ratio is higher than 0.35
        // print the length of the longest gene in sr
        int counterG9 = 0, counterH35 = 0, maxLen = 0;
//        System.out.println("DNA strands that are longer than 9 charactersz: ");
        System.out.println("DNA strands that are longer than 60 charactersz: ");
        for (String gene : sr.data()) {
            //Modify your processGenes method so that it prints all the Strings that
            // are longer than 60 characters and prints the number of Strings that
            // are longer than 60 characters.
//            if (gene.length() > 9) {
//                System.out.println(gene);
//                counterG9 += 1;
//            }
            if (gene.length() > 60) {
                System.out.println(gene);
                counterG9 += 1;
            }

            if (gene.length() > maxLen) {
                maxLen = gene.length();
            }
        }

        System.out.println("DNA strands that have C-G ratio higher than 0.35 :");
        for (String gene : sr.data()) {
            if (Part_2.cgRatio(gene) > 0.35) {
                System.out.println(gene);
                counterH35 += 1;
            }
        }

//        System.out.println("the number of Strings in sr that are longer than 9 characters is " + counterG9);
        System.out.println("the number of Strings in sr that are longer than 60 characters is " + counterG9);
        System.out.println("the number of strings in sr whose C-G-ratio is higher than 0.35 is " + counterH35);
        System.out.println("the length of the longest gene in sr " + maxLen);
    }

    public static void testProcessGenes() {
        FileResource fr = new FileResource();
        String dna = (fr.asString()).toUpperCase();
        StorageResource genes = Part_1.getAllGenes(dna);
        processGenes(genes);
        System.out.println("The number of genes is " + Part_1.countGenes(dna));
        System.out.println("the codon CTG appear in "+ Part_2.countCTG(dna) + " times");
        // String dna ="";// have some geenes longer than 9
        // dna = "";// have no genes longer than 9
        // dna = ""; // have some genes whose CG ratio is higher than 0.35
        // dna=""; // have genes whose CG ratio is lower than 0.35
    }

    public static void main(String[] args) {
        testProcessGenes();
    }
}
