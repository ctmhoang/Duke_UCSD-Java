public class Part_3 {
    public static boolean twoOccurrences(String stringa, String stringb) {
        //  This method returns true if stringa appears at least twice
        //in stringb, otherwise it returns false.
        int currIndex = stringb.indexOf(stringa);
        int nextIndex = stringb.indexOf(stringa, currIndex + stringa.length());
        return currIndex != -1 && nextIndex != -1;
    }

    public static String lastPart(String stringa, String stringb) {
        //This method finds the first occurrence of stringa in stringb
        //, and returns the part of stringb that follows stringa.
        //If stringa does not occur in stringb, then return stringb.
        int firstOcc = stringb.indexOf(stringa);
        if (firstOcc == -1) {
            return stringb;
        } else {
            return stringb.substring(firstOcc + stringa.length());
        }
    }

    public static void testing() {
        String testa = "by";
        String testb = "A story by Abby Long";
        System.out.println(twoOccurrences(testa, testb));
        System.out.println(lastPart(testa, testb));

        testa = "a";
        testb = "banana";
        System.out.println(twoOccurrences(testa, testb));
        testa = "an";
        System.out.println(lastPart(testa, testb));

        testa = "atg";
        testb = "ctgatgta";
        System.out.println(twoOccurrences(testa, testb));
        System.out.println(lastPart(testa, testb));

        testa = "zoo";
        testb = "forest";
        System.out.println(lastPart(testa, testb));

    }

    public static void main(String[] args) {
        testing();
    }
}
