import edu.duke.FileResource;


public class VigenereBreaker {
    public String sliceString(String message, int whichSlice, int totalSlices) {
        //REPLACE WITH YOUR CODE
        //return "WRITE ME!";
        StringBuilder result = new StringBuilder();
        for (int i = whichSlice; i < message.length(); i += totalSlices) {
            result.append(message.charAt(i));
        }
        return result.toString();
    }

    public int[] tryKeyLength(String encrypted, int klength, char mostCommon) {
        int[] key = new int[klength];
        //WRITE YOUR CODE HERE
        CaesarCracker cracker = new CaesarCracker(mostCommon);
        for (int i = 0; i < klength; i++) {
            String encryptedPart = sliceString(encrypted, i, klength);
            key[i] = cracker.getKey(encryptedPart);
        }
        return key;
    }

    public void breakVigenere() {
        //WRITE YOUR CODE HERE
        FileResource resource = new FileResource();
        String message = resource.asString();
        int[] keys = tryKeyLength(message,5,'e');
        VigenereCipher cipher = new VigenereCipher(keys);
        System.out.println(cipher.decrypt(message));
    }

}
