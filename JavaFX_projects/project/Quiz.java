package CSS.project;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Quiz {
    private String name = "JavaQuiz.txt";
    private static ArrayList<Question> questions = new ArrayList<>();

    Quiz() {

    }
    static Quiz loadFromFile(String filename) throws FileNotFoundException {

        File file = new File("JavaQuiz.txt");
        Scanner reader  = new Scanner(file);
        while (reader.hasNextLine()) {
            String qatar = reader.nextLine();
            if(qatar.contains("'blank'")){
                Fillin fillin =new Fillin();
                fillin.setDescription(qatar);
                qatar = reader.nextLine();
                fillin.setAnswer(qatar);
                questions.add(fillin);
            }else{
                Test test = new Test();
                test.setDescription(qatar);
                String answers = "";
                for (int i = 0; i < 4; i++) {
                    answers += reader.nextLine() + "_";
                }
                test.setAnswer(answers);
                answers = "";
                questions.add(test);
            }
            if(reader.hasNextLine()) {
                reader.nextLine();
            }
        }
        return new Quiz();
    }

    void start() {
        Scanner userIn = new Scanner(System.in);
        int count = 0;
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" +
                "\n\nWelcome to my \"JavaQuiz\" quiz!");
        System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        for (int i = 0; i < questions.size(); i++) {
            System.out.print((i+1)+ ". ");

            if(questions.get(i).getDescription().contains("'blank'")){
                System.out.println(questions.get(i).toString());
                System.out.print("\nWrite your answer: ");
                String userAns = userIn.nextLine();
                if (userAns.equalsIgnoreCase(questions.get(i).getAnswer())) {
                    System.out.println("\nCorrect!\n-----------------------------------------");
                    count++;
                } else {
                    System.out.println("\nIncorrect!\n-----------------------------------------");
                }
            }
            else{
                String[] options = questions.get(i).getAnswer().split("_");
                questions.get(i).setAnswer(options[0]);
                Test.setOptions(options);
                System.out.println(questions.get(i).toString());
                System.out.print("Choose one: ");
                boolean value = false;
                while (!value) {
                    try{
                        char userAns = userIn.nextLine().charAt(0);
                        int ind = (userAns - 'A');
                        if (ind > 3 || ind < 0) throw new Exception();
                        if (Test.getOptionAt(ind).equals(questions.get(i).getAnswer())) {
                            System.out.println("\nCorrect!\n-----------------------------------------");
                            count++;
                        } else {
                            System.out.println("\nIncorrect!\n-----------------------------------------");
                        }
                        value = true;
                    } catch (Exception exception) {
                        System.out.print("Invalid choice! Try again (Ex: A, B, ...): ");
                    }
                }
            }
        }
        System.out.println("Correct Answers: " + count + "/" + questions.size() + " (" + (count * 100) / questions.size() + "%)");
    }

    static void AddQuestion(Question question) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Write question: ");
        String add = scan.nextLine();
        question.setDescription(add);
        System.out.print("Write answer for a queation: ");
        add = scan.nextLine();
        question.setAnswer(add);
        questions.add(question);
    }

    public String toString(){
        return "";
    }

    void setName(String name){
        this.name = name;
    }

    String getName(){
        return name;
    }

}
