import edu.duke.FileResource;

import javax.naming.Name;
import java.util.ArrayList;
import java.util.Arrays;

public class CharactersInPlay {
    private ArrayList<String> names;
    private ArrayList<Integer>counts;
    public CharactersInPlay(){
        names = new ArrayList<>();
        counts = new ArrayList<>();
    }
    private void update(String person){
        if(!names.contains(person.toLowerCase())){
            names.add(person.toLowerCase());
            counts.add(1);
        }else {
            int idx = names.indexOf(person.toLowerCase());
            counts.set(idx,counts.get(idx)+1);
        }
    }
    public void findAllCharacters(){
        names.clear();
        counts.clear();
        FileResource resource  = new FileResource();
        for (String line : resource.lines()){
            if(line.contains(".")){
                String name = line.substring(0,line.indexOf("."));
                update(name);
            }
        }
    }
    public int mainCharacters(){
        Integer[] values = counts.toArray(new Integer[0]);
        Arrays.sort(values);
        return values[values.length - 1];
    }
    public void tester(){
        findAllCharacters();
        int mainIdx = mainCharacters();
        for(int i = 0; i < names.size(); i ++){
            if(counts.get(i) == mainIdx){
                System.out.println(names.get(i) + "\t" + counts.get(i));
            }
        }
        System.out.println("In range (2,3)");
        charactersWithNumParts(10,15);
    }
    public void charactersWithNumParts(int num1, int num2){
        for(int i = 0 ; i < names.size(); i++){
            int value = counts.get(i);
            if (value >= num1 && value <= num2){
                System.out.println(names.get(i) + "\t" + value);
            }
        }
    }
}
