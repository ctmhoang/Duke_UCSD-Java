import edu.duke.FileResource;

//TestCaesarCipher Class
public class Main{
    public static void main(String[] args) {
        simpleTest();
    }

    public static int[] countLetters(String message){
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        int[] counts = new int[26];
        for(int i = 0 ; i < message.length(); i++){
            char ch = Character.toLowerCase(message.charAt(i));
            int idx = alphabet.indexOf(ch);
            if(idx != -1){
                counts[idx] ++;
            }
        }
        return counts;
    }
    public static int maxIndex(int[] values){
        int max = 0, idx = 0 ;
        for(int i = 0 ; i < values.length ; i++){
            if(max < values[i]){
                max = values[i];
                idx = i;
            }
        }
        return idx;
    }

    public static void simpleTest(){
        FileResource resource = new FileResource();
        CaesarCipher cc = new CaesarCipher(18);
        String encrypted = cc.encrypt(resource.asString());
        String decrypted = cc.decrypt(encrypted);
        System.out.println(encrypted + "\n");
        System.out.println(decrypted + "\n");
        System.out.println("Break: " + breakCaesarCipher(encrypted));
    }

    public static String breakCaesarCipher(String input){
        int[] freq = countLetters(input);
        int maxIdx = maxIndex(freq);
        int gapKey = maxIdx - 4;
        if(maxIdx < 4){
            gapKey = 26 + maxIdx - 4;
        }
        return new CaesarCipher(26-gapKey).encrypt(input);
    }
}
