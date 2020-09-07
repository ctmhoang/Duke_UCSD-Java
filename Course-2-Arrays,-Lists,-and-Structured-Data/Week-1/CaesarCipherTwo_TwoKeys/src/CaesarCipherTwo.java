public class CaesarCipherTwo {
    private String alphabet;
    private String shiftedAlphabet1;
    private String shiftedAlphabet2;
    private int key1, key2;

    public CaesarCipherTwo(int key1, int key2) {
        alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        shiftedAlphabet1 = alphabet.substring(key1) + alphabet.substring(0, key1);
        shiftedAlphabet2 = alphabet.substring(key2) + alphabet.substring(0, key2);
        this.key1 = key1;
        this.key2 = key2;
    }

    public String encrypt(String input) {
        StringBuilder output = new StringBuilder(input);
        for (int i = 0; i < output.length(); i++) {
            char c = output.charAt(i);
            int idx = alphabet.indexOf(Character.toUpperCase(c));
            if (idx != -1) {
                if (Character.isUpperCase(c)) {
                    if (i % 2 == 0) {
                        output.setCharAt(i, shiftedAlphabet1.charAt(idx));
                    } else {
                        output.setCharAt(i, shiftedAlphabet2.charAt(idx));
                    }
                } else {
                    if (i % 2 == 0) {
                        output.setCharAt(i, Character.toLowerCase(shiftedAlphabet1.charAt(idx)));
                    } else {
                        output.setCharAt(i, Character.toLowerCase(shiftedAlphabet2.charAt(idx)));
                    }
                }
            }
        }
        return output.toString();
    }

    public String decrypt(String input) {
        CaesarCipherTwo temp = new CaesarCipherTwo(26 - key1, 26 - key2);
        return temp.encrypt(input);
    }
}
