import edu.duke.FileResource;

public class Main {

    public static void main(String[] args) {
	// write your code here
//        System.out.println(encrypt("FIRST LEGION ATTACK EAST FLANK!", 23));
//        testCaesar();
//        System.out.println(encrypt("First Legion", 17));
        System.out.println(encryptTwoKeys("At noon be in the conference room with your hat on for a surprise party. YELL LOUD!\n" +
                "\n", 8, 21));
    }
    public static String encrypt(String input, int key){
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String shiftedAlphabet = alphabet.substring(key) + alphabet.substring(0,key);
        StringBuilder output = new StringBuilder(input);
        for(int i = 0; i < output.length(); i++){
            char c = output.charAt(i);
            int idx = alphabet.indexOf(Character.toUpperCase(c));
            if(idx != -1){
                if(Character.isUpperCase(c)) {
                    output.setCharAt(i, shiftedAlphabet.charAt(idx));
                }
                else {
                    output.setCharAt(i,Character.toLowerCase(shiftedAlphabet.charAt(idx)));
                }
            }
        }
        return output.toString();
    }

    public static void testCaesar(){
        FileResource fr = new FileResource();
        String message = fr.asString();
        int key = 23;
        String encrypted = encrypt(message, key);
        System.out.println("key is " + key + "\n" + encrypted);
    }

    public static String encryptTwoKeys(String input, int key1 , int key2){
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String k1Alphabet = alphabet.substring(key1) + alphabet.substring(0,key1);
        String k2Alphabet = alphabet.substring(key2) + alphabet.substring(0,key2);
        StringBuilder output = new StringBuilder(input);
        for(int i = 0; i < output.length(); i++){
            char c = output.charAt(i);
            int idx = alphabet.indexOf(Character.toUpperCase(c));
            if(idx != -1){
                if(Character.isUpperCase(c)) {
                    if (i %2 == 0){
                        output.setCharAt(i, k1Alphabet.charAt(idx));
                    }else{
                        output.setCharAt(i, k2Alphabet.charAt(idx));
                    }
                }
                else {
                    if(i%2 == 0){
                        output.setCharAt(i,Character.toLowerCase(k1Alphabet.charAt(idx)));
                    }
                    else{
                        output.setCharAt(i,Character.toLowerCase(k2Alphabet.charAt(idx)));
                    }
                }
            }
        }
        return output.toString();
    }
}
