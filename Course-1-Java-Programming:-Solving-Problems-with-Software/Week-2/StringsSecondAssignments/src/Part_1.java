public class Part_1 {
    public static int findStopCodon(String dna, int strIndex, String stpCodon) {
        //This method returns the index of the first occurrence of stopCodon
        //that appears past startIndex and is a multiple of 3 away from startIndex.
        //If there is no such stopCodon, this method returns the length of the dna strand.
        int stpIndex = dna.indexOf(stpCodon, strIndex + 3);
        while (stpIndex != -1) {
            if ((stpIndex - strIndex) % 3 == 0) {
                return stpIndex;
            }
            stpIndex = dna.indexOf(stpCodon, stpIndex + 1);
        }
        return dna.length();
    }

    public static String findGene(String dna) {
        //representing a string
        //Find the index of the first occurrence of the start codon “ATG”. If there is no “ATG”, return the empty string.
        int strIndex = dna.indexOf("ATG");
        if (strIndex == -1) {
            return "";
        }
        int taaIndex = (findStopCodon(dna, strIndex, "TAA"));
        int tagIndex = (findStopCodon(dna, strIndex, "TAG"));
        int tgaIndex = (findStopCodon(dna, strIndex, "TGA"));
        int minIndex = Math.min(taaIndex, Math.min(tagIndex, tgaIndex));
        if (minIndex == dna.length()) {
            return "";
        }
        return dna.substring(strIndex, minIndex + 3);
    }

    public static void testFindStopCodon() {
        String dna = "ATTATGCGTCTGTAGTGGGGC"; // strIndex = 3
        int strIndex = 3;
        String stpCodon = "TAG";
        int stpIndex = findStopCodon(dna, strIndex, stpCodon);
        System.out.println(stpIndex);

        dna = "ATTATGCGTCTGTGTGGGGC"; // strIndex = 3 without stpCodon len =20
        strIndex = 3;
        stpCodon = "TAG";
        stpIndex = findStopCodon(dna, strIndex, stpCodon);
        System.out.println(stpIndex);

        dna = "ATTATGCGTAGGTAGTGGGGC"; // strIndex = 3 with 1 invalid TAG
        strIndex = 3;
        stpCodon = "TAG";
        stpIndex = findStopCodon(dna, strIndex, stpCodon);
        System.out.println(stpIndex);
    }

    public static void testFindGene() {
        String dna = "ATTTTAGATTCCGG";//dna with no start codon"ATG"
        String gene = findGene(dna);
        System.out.println(dna);
        System.out.println(gene);

        dna = "ATTATGTCACCACCGGGCTAGCCA";//dna with ATG and one valid stop codon TAG
        gene = findGene(dna);
        System.out.println(dna);
        System.out.println(gene);

        dna = "ATTATGTCACCACCGTGAGGCTAGTAACCA";//dna with ATG and multi valid stop codon TAG
        gene = findGene(dna);
        System.out.println(dna);
        System.out.println(gene);

        dna = "ATTATGTCACCACCGGGTAGCCA";//dna with ATG and no valid stop codon TAG
        gene = findGene(dna);
        System.out.println(dna);
        System.out.println(gene);
    }

    public static void printAllGenes(String dna) {
        int strIndex = 0;
        while (true) {
            strIndex = dna.indexOf("ATG", strIndex);
            if (strIndex == -1) {
                break;
            }
            int taaIndex = findStopCodon(dna, strIndex, "TAA"),
                    tagIndex = findStopCodon(dna, strIndex, "TAG"),
                    tgaIndex = findStopCodon(dna, strIndex, "TGA"),
                    stpIndex = Math.min(Math.min(taaIndex, tagIndex), tgaIndex);
            if (stpIndex == dna.length()) {
                break;
            }
            String gene = dna.substring(strIndex, stpIndex + 3);
            System.out.println(gene);
            strIndex = stpIndex + 3;
        }
    }

    public static void main(String[] args) {
        testFindStopCodon();
        testFindGene();
        printAllGenes("ATGTAG" +
                "TTAATGGTATTATAA" +
                "ATCATGTTAGGGTAA");
    }
}
