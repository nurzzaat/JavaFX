package CSS.project;

import java.io.IOException;
import java.util.Scanner;

public class  Quizmaker{

    public static void main(String[] args) throws IOException  {
        try{
            Quiz quiz = new Quiz();
//            if(!args[0].equals(quiz.getName())) throw new InvalidQuizFormatException();
//            Scanner scan = new Scanner(System.in);
//            Test test = new Test();
//            Fillin fillin = new Fillin();
//
//            quiz.loadFromFile(quiz.getName());
//
//            System.out.print("1.Test\n2.Fillin\nChoose type of question:");
//            int qwe = scan.nextInt();
//            if(qwe == 1){
//                Quiz.AddQuestion(test);
//            }
//            else{
//                Quiz.AddQuestion(fillin);
//            }
            quiz.start();
        }catch (Exception exception){
            System.out.println("Such file doesn't exist.");
        }
    }
}