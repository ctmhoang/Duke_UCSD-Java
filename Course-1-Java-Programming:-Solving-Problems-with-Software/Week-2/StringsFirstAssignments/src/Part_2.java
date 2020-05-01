public class Part_2 {

    public static String findSimpleGene(String dna, String strCodon, String stpCodon) {
        int strIndex = 0, endIndex = 0;
        String clone = dna.toUpperCase();
        strIndex = clone.indexOf(strCodon.toUpperCase());
        endIndex = clone.indexOf(stpCodon.toUpperCase(), strIndex + 3);
        if (strIndex == -1 || endIndex == -1) return null;
        else if ((endIndex + 3 - strIndex) % 3 != 0) return null;
        else return dna.substring(strIndex, endIndex + 3);
    }

    public static void testSimpleGene() {
        String dna = "ATTTTACTATTAXCG"; //no ATG
        System.out.println("DNA stand is " + dna);
        String stpCodon = "TAA";
        String strCodon = "ATG";
        String gene = findSimpleGene(dna, strCodon, stpCodon);
        System.out.println("Gene is " + gene);

        dna = "CGATACATGCTGATTACTA";//no TAA
        System.out.println("DNA stand is " + dna);
        gene = findSimpleGene(dna, strCodon, stpCodon);
        System.out.println("Gene is " + gene);

        dna = "ATCTTAAGGACT";//no ATG & TAA
        System.out.println("DNA stand is " + dna);
        gene = findSimpleGene(dna, strCodon, stpCodon);
        System.out.println("Gene is " + gene);

        dna = "CTACCAATGTTGATGAGTAAATTGCT";//with ATG & TAA not valid
        System.out.println("DNA stand is " + dna);
        gene = findSimpleGene(dna, strCodon, stpCodon);
        System.out.println("Gene is " + gene);

        dna = "CTACCAATGTTGATGACGTAAATTGCT";//with ATG & TAA valid
        System.out.println("DNA stand is " + dna);
        gene = findSimpleGene(dna, strCodon, stpCodon);
        System.out.println("Gene is " + gene);

        dna = "ctaccaatgttgatgacgtaaattgct";//with ATG & TAA in lowercase form-valid
        System.out.println("DNA stand is " + dna);
        gene = findSimpleGene(dna, strCodon, stpCodon);
        System.out.println("Gene is " + gene);


    }

    public static void main(String[] args) {
        testSimpleGene();
    }
}
