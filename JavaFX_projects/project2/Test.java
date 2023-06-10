package CSS.project2;

import java.util.ArrayList;
import java.util.Collections;

public class Test extends Question {
    private static int numberOfOptions = 4;
    private static String[] options = new String[numberOfOptions];
    private static ArrayList<String> labels = new ArrayList<>();

    public Test(){

    }

    public static void setOptions(String[] option){
        for (int i = 0; i < 4; i++) {
            labels.add(option[i]);
        }
        Collections.shuffle(labels);
        for (int i = 0; i < 4; i++) {
            options[i] = labels.get(i);
        }
        labels.clear();
    }

    public static String getOptionAt(int n){
        return options[n];
    }

    @Override
    public String toString(){
        return getDescription();
    }
}