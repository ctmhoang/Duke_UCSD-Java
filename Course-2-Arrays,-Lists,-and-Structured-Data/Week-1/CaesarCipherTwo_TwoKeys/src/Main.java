import edu.duke.FileResource;

public class Main {
    //TestCaesarCipherTwo class
    public static void main(String[] args) {
        simpleTests();
    }

    private static int[] countLetters(String message) {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        int[] counts = new int[26];
        for (int i = 0; i < message.length(); i++) {
            char ch = Character.toLowerCase(message.charAt(i));
            int idx = alphabet.indexOf(ch);
            if (idx != -1) {
                counts[idx]++;
            }
        }
        return counts;
    }

    private static int maxIndex(int[] values) {
        int max = 0, idx = 0;
        for (int i = 0; i < values.length; i++) {
            if (max < values[i]) {
                max = values[i];
                idx = i;
            }
        }
        return idx;
    }

    private static String halfOfString(String message, int start) {
        StringBuilder halfMess = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            if (start == 0 && i % 2 == 0) {
                halfMess.append(message.charAt(i));
            } else if (start == 1 && i % 2 != 0) {
                halfMess.append(message.charAt(i));
            }
        }
        return halfMess.toString();
    }

    public static void simpleTests(){
        FileResource resource = new FileResource();
        CaesarCipherTwo cc2 = new CaesarCipherTwo(17,3);
        String encrypted = cc2.encrypt(resource.asString());
        String decrypted = cc2.decrypt(encrypted);
        System.out.println(encrypted + "\n");
        System.out.println(decrypted + "\n");
        System.out.println("Break: " + breakCaesarCipher(encrypted));
    }

    private static int getKey(String s){
        int[] freq = countLetters(s);
        int maxIdx = maxIndex(freq);
        int gapKey = maxIdx - 4;
        if(maxIdx < 4){
            gapKey = 26 + maxIdx - 4;
        }

        return gapKey;
    }

    public static String breakCaesarCipher(String encrypted){
        String firstHalf = halfOfString(encrypted,0);
        String remainHalf = halfOfString(encrypted, 1);
        int key1 = getKey(firstHalf);
        int key2 = getKey(remainHalf);
        System.out.println("Key1 : " + key1 + "\nKey2: " + key2);
        return new CaesarCipherTwo(26- key1, 26 - key2).encrypt(encrypted);
    }
}
