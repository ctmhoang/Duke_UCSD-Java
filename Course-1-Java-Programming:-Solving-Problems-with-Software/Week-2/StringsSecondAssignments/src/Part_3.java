public class Part_3 {
    //    public static int findStopCodon(String dna, int strIndex, String stpCodon) {
//        //This method returns the index of the first occurrence of stopCodon
//        //that appears past startIndex and is a multiple of 3 away from startIndex.
//        //If there is no such stopCodon, this method returns the length of the dna strand.
//        int stpIndex = dna.indexOf(stpCodon, strIndex + 3);
//        while (stpIndex != -1) {
//            if ((stpIndex - strIndex) % 3 == 0) {
//                return stpIndex;
//            }
//            stpIndex = dna.indexOf(stpCodon, stpIndex + 1);
//        }
//        return dna.length();
//    }
//
//    public static String findGene(String dna) {
//        //representing a string
//        //Find the index of the first occurrence of the start codon “ATG”. If there is no “ATG”, return the empty string.
//        int strIndex = dna.indexOf("ATG");
//        if (strIndex == -1) {
//            return "";
//        }
//        int taaIndex = (findStopCodon(dna, strIndex, "TAA"));
//        int tagIndex = (findStopCodon(dna, strIndex, "TAG"));
//        int tgaIndex = (findStopCodon(dna, strIndex, "TGA"));
//        int minIndex = Math.min(taaIndex, Math.min(tagIndex, tgaIndex));
//        if (minIndex == dna.length()) {
//            return "";
//        }
//        return dna.substring(strIndex, minIndex + 3);
//    }
//
//    public static void printAllGenes(String dna) {
//        int strIndex = 0;
//        while (true) {
//            strIndex = dna.indexOf("ATG", strIndex);
//            if (strIndex == -1) {
//                break;
//            }
//            int taaIndex = findStopCodon(dna, strIndex, "TAA"),
//                    tagIndex = findStopCodon(dna, strIndex, "TAG"),
//                    tgaIndex = findStopCodon(dna, strIndex, "TGA"),
//                    stpIndex = Math.min(Math.min(taaIndex, tagIndex), tgaIndex);
//            if(stpIndex == dna.length()){
//                break;
//            }
//            String gene = dna.substring(strIndex, stpIndex + 3);
//            System.out.println(gene);
//            strIndex = stpIndex + 3;
//        }
//    }
//    code copied in part 1
    public static int countGenes(String dna) {
        int strIndex = 0, counter = 0;
        while (true) {
            strIndex = dna.indexOf("ATG", strIndex);
            if (strIndex == -1) {
                break;
            }
            int taaIndex = Part_1.findStopCodon(dna, strIndex, "TAA"),
                    tagIndex = Part_1.findStopCodon(dna, strIndex, "TAG"),
                    tgaIndex = Part_1.findStopCodon(dna, strIndex, "TGA"),
                    stpIndex = Math.min(Math.min(taaIndex, tagIndex), tgaIndex);
            if (stpIndex == dna.length()) {
                break;
            }
            strIndex = stpIndex + 3;
            counter++;
        }
        return counter;
    }

    public static void testCountGenes() {
        int numgenes = countGenes("ATGTAAGATGCCCTAGT");
        System.out.println(numgenes);

    }

    public static void main(String[] args) {
        testCountGenes();
    }
}
