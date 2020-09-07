import edu.duke.DirectoryResource;
import edu.duke.FileResource;

import java.io.File;
import java.util.*;


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
        //Read encrypted message
        FileResource resource = new FileResource();
        String message = resource.asString();
        //map each language name to the set of words in its dictionary
        DirectoryResource dictDir = new DirectoryResource();
        HashMap<String, HashSet<String>> dictRes = new HashMap<>();
        for (File f : dictDir.selectedFiles()) {
            FileResource dict = new FileResource(f);
            dictRes.put(f.getName(), readDictionary(dict));
            System.out.println("Reading: " + f.getName() + " DONE!!! ");
        }
        breakForAllLangs(message, dictRes);
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
            int[] keys = tryKeyLength(encrypted.toLowerCase(), i, mostCommonCharIn(dictionary));
            VigenereCipher cipher = new VigenereCipher(keys);
            String decrypted = cipher.decrypt(encrypted);
            //count how many of the “words” in it are real
            int score = countWords(decrypted.toLowerCase(), dictionary);
            //figure out which decryption gives the largest count of real words
            if (score > maxScore) {
                maxScore = score;
                bestScript = decrypted;
//                System.out.println("Best key so far :" + Arrays.toString(keys) + "\n Size:" + keys.length);
//                System.out.println("Which contains: " + maxScore + " words");
            }
        }
//        System.out.println("Contain: " + maxScore + " valid words out of " + encrypted.split("\\W+").length + " words");
        return bestScript;
    }

    public char mostCommonCharIn(HashSet<String> dictionary) {
        HashMap<Character, Integer> timesAppear = new HashMap<>();
        for (String word : dictionary) {
            char[] letters = word.toCharArray();
            for (char letter : letters) {
                if (Character.isLetter(letter)) {
                    if (!timesAppear.containsKey(letter)) {
                        timesAppear.put(letter, 1);
                    } else {
                        timesAppear.put(letter, timesAppear.get(letter) + 1);
                    }
                }
            }
        }
        return Collections.max(timesAppear.entrySet(),Comparator.comparingInt(Map.Entry::getValue)).getKey();
    }

    public void breakForAllLangs(String encrypted, HashMap<String, HashSet<String>> languages) {
        int maxScore = 0;
        String bestScriptSoFar = null, lang = null;
        //Try breaking the encryption for each language
        for (String language : languages.keySet()) {
            System.out.println("Decrypting... " + language);
            HashSet<String> dict = languages.get(language);
            String decrypted = breakForLanguage(encrypted, dict);
            int score = countWords(decrypted, dict);
            if (maxScore < score) {
                maxScore = score;
                bestScriptSoFar = decrypted;
                lang = language;
            }
        }
        System.out.println(bestScriptSoFar + "\n lang = " + lang);
    }

}
