package CSS.project;

public class Fillin  extends Question{

    @Override
    public String toString(){
        return getDescription().replaceAll("'blank'" , "_______");
    }
}
