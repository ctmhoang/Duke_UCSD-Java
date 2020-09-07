import edu.duke.FileResource;

public class CaesarCipher{

    private String alphabet;
    private String shiftedAlphabet;
    private int mainKey;
    public CaesarCipher(int key){
        alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        shiftedAlphabet = alphabet.substring(key) + alphabet.substring(0,key);
        mainKey = key;
    }
    public String encrypt(String input){
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

    public String decrypt(String input){
        CaesarCipher temp = new CaesarCipher(26-mainKey);
        return temp.encrypt(input);
    }
}
