import edu.duke.FileResource;

public class Main {

    public static void main(String[] args) {
        FileResource resource = new FileResource();
        System.out.println(decryptTwoKeys(resource.asString()));
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
    public static String decrypt(String encrypted){
        int[] freq = countLetters(encrypted);
        int maxIdx = maxIndex(freq);
        int gapKey = maxIdx - 4;
        if(maxIdx < 4){
            gapKey = 26 + maxIdx - 4;
        }
        return CaesarCipher.encrypt(encrypted, 26-gapKey);
    }
    public static void testDecrypt(){
        System.out.println(decrypt("Grpq x qbpq pqofkd tfqe ilqp lc bbbbbbbbbbbbbbbbbp"));
    }

    public static String halfOfString(String message, int start){
        StringBuilder halfMess = new StringBuilder();
        for(int i = 0 ; i < message.length(); i ++){
            if(start == 0 && i % 2 == 0){
                halfMess.append(message.charAt(i));
            }else if(start == 1 && i % 2 != 0){
                halfMess.append(message.charAt(i));
            }
        }
        return halfMess.toString();
    }

    public static int getKey(String s){
        int[] freq = countLetters(s);
        int maxIdx = maxIndex(freq);
        int gapKey = maxIdx - 4;
        if(maxIdx < 4){
            gapKey = 26 + maxIdx - 4;
        }

        return gapKey;
    }
    public static String decryptTwoKeys(String encrypted){
        String firstHalf = halfOfString(encrypted,0);
        String remainHalf = halfOfString(encrypted, 1);
        int key1 = getKey(firstHalf);
        int key2 = getKey(remainHalf);
        System.out.println("Key1 : " + key1 + "\nKey2: " + key2);
        return CaesarCipher.encryptTwoKeys(encrypted, 26- key1, 26 - key2);
    }
}
