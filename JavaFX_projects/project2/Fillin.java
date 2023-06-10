package CSS.project2;

public class Fillin extends Question {
    @Override
    public String toString(){
        if(getDescription().contains("<>")) {
            String[] a = getDescription().replaceAll("'blank'", "_______").split("<>");
            setDescription(a[1]);
            return a[0] + "\n" + a[1];
        }
        else {
            setDescription(getDescription().replaceAll("'blank'" , "_______"));
            return getDescription().replaceAll("'blank'" , "_______");
        }
    }
}
