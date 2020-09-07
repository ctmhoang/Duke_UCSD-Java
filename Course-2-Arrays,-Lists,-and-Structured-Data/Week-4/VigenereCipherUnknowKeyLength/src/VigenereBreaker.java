import edu.duke.FileResource;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;


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
        FileResource dictRes = new FileResource();
        HashSet<String> dict = readDictionary(dictRes);
        String decryptedMessage = breakForLanguage(message, dict);
        System.out.println(decryptedMessage);
    }

    public HashSet<String> readDictionary(FileResource fr) {
        HashSet<String> dic = new HashSet<>();
        for (String word : fr.lines()) {
            dic.add(word.toLowerCase());
        }
        return dic;
    }

    public int countWords(String message, HashSet<String> dictionary) {
        int tot = 0;
        //split message to words
        String[] wordsInMessage = message.split("\\W+");
        //Iterate see how many of them are real
        for (String word : wordsInMessage) {
            if (dictionary.contains(word)) {
                tot += 1;
            }
        }
        return tot;
    }

    public String breakForLanguage(String encrypted, HashSet<String> dictionary) {
        int maxScore = 0;
        String bestScript = null;
        //try all key lengths from 1 to 100
        for (int i = 1; i <= 100; i++) {
            //decrypt the message in english
            int[] keys = tryKeyLength(encrypted.toLowerCase(), i, 'e');
            VigenereCipher cipher = new VigenereCipher(keys);
            String decrypted = cipher.decrypt(encrypted);
            //count how many of the “words” in it are real
            int score = countWords(decrypted.toLowerCase(), dictionary);
            //figure out which decryption gives the largest count of real words
            if (score > maxScore) {
                maxScore = score;
                bestScript = decrypted;
                System.out.println("Best key so far :" + Arrays.toString(keys) + "\n Size:" + keys.length);
                System.out.println("Which contains: " + maxScore + " words");
            }
        }
        System.out.println("Contain: " + maxScore + " valid words out of " + encrypted.split("\\W+").length + " words");
        return bestScript;
    }

}
