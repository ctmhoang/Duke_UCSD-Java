public class Part_2 {
    public static int howMany(String stringa, String stringb) {
        //returns an integer indicating how many times stringa appears in stringb,
        //where each occurrence of stringa must not overlap with another occurrence of it.
        int counter = 0, currIndex = stringb.indexOf(stringa, 0);
        while (currIndex != -1) {
            counter++;
            currIndex = stringb.indexOf(stringa, currIndex + stringa.length());
        }
        return counter;
    }

    public static void testHowMany() {
        String testa = "GAA";
        String testb = "ATGAACGAATTGAATC";
        int testval = howMany(testa, testb);
        System.out.println(testval);

        testa = "AA";
        testb = "ATAAAA";
        testval = howMany(testa, testb);
        System.out.println(testval);
    }

    public static void main(String[] args) {
        testHowMany();
    }
}
