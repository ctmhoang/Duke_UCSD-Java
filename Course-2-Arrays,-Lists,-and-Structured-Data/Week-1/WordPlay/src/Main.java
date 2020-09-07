import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
//        System.out.println(isVowel('A'));
//        System.out.println(replaceVowels("Hello World", '*'));
//        System.out.println(emphasize("dna ctgaaactga", 'a') );
        System.out.println(emphasize("Mary Bella Abracadabra", 'a'));
    }
    public static boolean isVowel(char ch) {
        return Pattern.matches("[aeiouAEIOU]", ch + "");
    }
    public static String replaceVowels(String phrase, char ch){
        StringBuilder newPhrase = new StringBuilder();
        for(char c : phrase.toCharArray()){
            if(isVowel(c)){
                newPhrase.append(ch);
            }else {
                newPhrase.append(c);
            }
        }
        return newPhrase.toString();
    }

    public static String emphasize (String phrase, char ch){
        StringBuilder newPhrase = new StringBuilder(phrase);
        for(int i = 0; i < phrase.length(); i++){
            if(Character.toString(newPhrase.charAt(i)).equalsIgnoreCase(ch+"")){
                if(i % 2 == 0){
                    newPhrase.setCharAt(i, '*');
                }else {
                    newPhrase.setCharAt(i, '+');
                }
            }
        }
        return newPhrase.toString();
    }
}
