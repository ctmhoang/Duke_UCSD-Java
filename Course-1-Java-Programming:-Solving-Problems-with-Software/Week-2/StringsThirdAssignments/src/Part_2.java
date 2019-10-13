import edu.duke.FileResource;
import edu.duke.StorageResource;

public class Part_2 {
    public static double cgRatio(String dna) {
        //returns the ratio of C’s and G’s in dna
        //as a fraction of the entire strand of DNA
        double counter = 0;
        int tot = dna.length(), indexC =  dna.indexOf('C', 0), indexG = dna.indexOf('G', 0);;
        while (indexG != -1 || indexC != -1) {
            if (indexC != -1) {
                counter += 1;
                indexC = dna.indexOf('C', indexC + 1);
            }
            if (indexG != -1) {
                counter += 1;
                indexG = dna.indexOf('G', indexG + 1);
            }
        };
        return counter / tot;
    }

    public static int countCTG(String dna) {
        int counter = 0, index = dna.indexOf("CTG", 0);
        while (index != -1) {
            index = dna.indexOf("CTG", index + 1);
            counter ++;
        }
        return counter;
    }

    public static void main(String[] args) {
        System.out.println(cgRatio("ATGCCATAG"));
        System.out.println(countCTG("CTGCCTCTGTATTCTGGA"));
    }
}
