package CSS.project;

public abstract class Question {
    private String description;
    private String answer;

    void setDescription(String description){
        this.description = description;
    }
    void setAnswer(String answer){
        this.answer = answer;
    }
    String getDescription(){
        return description;
    }
    String getAnswer(){
        return answer;
    }
}
