import edu.duke.*;
public class Part_4 {
    private static URLResource Duke = new URLResource ("http://www.dukelearntoprogram.com/course2/data/manylinks.html");
    private static void findingWebLinks(){
        int strIndex = 0, lastIndex = 0;
        for(String word : Duke.words()){
            String clone = word.toLowerCase();
            if (clone.contains("youtube.com")){
                int pos = clone.indexOf("youtube.com");
                strIndex = word.lastIndexOf("\"", pos)+ 1;
                lastIndex = word.indexOf("\"", pos+ 1);
                System.out.println(word.substring(strIndex,lastIndex));
            }
        }
    }

    public static void main(String[] args) {
        findingWebLinks();
    }
}
