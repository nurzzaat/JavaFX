package CSS.project2;

import javafx.scene.image.Image;

public class Question {
    private String description;
    private String answer;
    private Image image;
    public void setImage(Image image){
        this.image = image;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public void setAnswer(String answer){
        this.answer = answer;
    }
    public String getDescription(){
        return description;
    }
    public String getAnswer(){
        return answer;
    }
    public Image getImage(){
        return image;
    }
}
