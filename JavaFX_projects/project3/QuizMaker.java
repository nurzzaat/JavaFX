package CSS.project3;

import CSS.project2.Fillin;
import CSS.project2.Question;
import CSS.project2.Test;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class QuizMaker extends Application {
    static int PIN;
    private Stage stage;
    private String name = "";
    private static ArrayList<Question> questions = new ArrayList<>();
    private ArrayList<Pane> second = new ArrayList<Pane>();
    static ArrayList<Image> images = new ArrayList<>();
    private Slider slTime = new Slider();
    private String totalTime = "";
    int users = 0;
    private int secondd = 60;
    boolean to = false;
    ArrayList<ArrayList> list =new ArrayList<>();
    private Text timeshow = new Text();
    private Timeline animation;
    HBox clients = new HBox();
    Pane mainpane;
    int page = 0 ;
    Map<String ,Integer> show = new TreeMap<>();
    @Override
    public void start(Stage primaryStage) throws IOException {
        QuizMaker q = new QuizMaker();
        setPIN(q.createPIN());

        stage = primaryStage;
        stage.setTitle("Server");
        clients.setMaxWidth(700);
        timeshow.setStyle("-fx-fill: white; -fx-font-size: 30px;-fx-font-family:'Comic Sans MS'");
        animation = new Timeline(new KeyFrame(Duration.millis(1000), e -> {
            try {
                run();
            } catch (IOException | InterruptedException ex) {}
        }));
        animation.setCycleCount(60);
        play();
        new Thread(()->{
            try {
                ServerSocket newsocket = new ServerSocket(1136);
                while (true){
                    Socket ns = newsocket.accept();
                    new Thread(() -> {
                        try {
                            DataOutputStream toClient = new DataOutputStream(ns.getOutputStream());
                            DataInputStream fromClient = new DataInputStream(ns.getInputStream());
                            toClient.writeInt(PIN);
                            if(fromClient.readBoolean()){
                                users++;
                                String[] usnm={""};
                                Platform.runLater(() -> {
                                    try {
                                        Text text = new Text(fromClient.readUTF());
                                        usnm[0] = text.getText();
                                        text.setStyle("-fx-font-family:Montserrat , Noto Sans Arabic, Helvetica Neue, Helvetica, Arial, sans-serif;" +
                                                "-fx-font-weight: bold; -fx-fill: white;-fx-font-size: 30px;-fx-translate-x: 100 ");
                                        clients.getChildren().add(text);
                                    } catch (IOException e) { }
                                });
                                while(!to){
                                    toClient.writeBoolean(false);
                                }
                                System.out.println("True");
                                toClient.writeBoolean(true);
                                toClient.writeInt(questions.size());
                                for (int i = 0; i < questions.size(); i++) {
                                    if(questions.get(i) instanceof Test)
                                        toClient.writeChar('T');
                                    else
                                        toClient.writeChar('F');
                                }
                                int score = 0;
                                while (to){
                                    String answer = fromClient.readUTF();
                                    String[] as = questions.get(page).getAnswer().split("_");

                                    if(answer.equals("R")){
                                        int l = as[0].length();
                                        String o = list.get(page).get(0).toString();
                                        if(o.substring(o.length()-l - 1 , o.length() - 1).equals(as[0]))
                                            score++;

                                    }if(answer.equals("O")){
                                        int l = as[0].length();
                                        String o = list.get(page).get(1).toString();
                                        if(o.substring(o.length()-l - 1 , o.length() - 1).equals(as[0]))
                                            score++;

                                    }if(answer.equals("B")){
                                        int l = as[0].length();
                                        String o = list.get(page).get(2).toString();
                                        if(o.substring(o.length()-l - 1 , o.length() - 1).equals(as[0]))
                                            score++;

                                    }if(answer.equals("G")){
                                        int l = as[0].length();
                                        String o = list.get(page).get(3).toString();
                                        if(o.substring(o.length()-l - 1 , o.length() - 1).equals(as[0]))
                                            score++;
                                    }
                                    System.out.println(answer);
                                    System.out.println(as[0]);
                                    if(answer.equalsIgnoreCase(as[0])){
                                        score++;
                                        System.out.println("fillin");
                                    }
                                    if(page + 1 == questions.size())
                                        to =false;
                                }
                                show.put(usnm[0],score);
                            }
                        } catch (Exception e) {}
                    }).start();
                }
            }catch (Exception e){}
        }).start();
        stage.setScene(new Scene(page() , 800  , 700));
        stage.show();
    }
    public VBox page(){
        VBox intro = new VBox();
        intro.setStyle("-fx-background-color: #46178f ;-fx-max-width: 600 ;-fx-start-margin: 100");

        //Add PIN
        Text pin = new Text("PIN : " + getPIN());
        pin.setStyle("-fx-font-family: 'Lucida Sans';"+
                "-fx-font-weight: bold; -fx-fill: white;-fx-font-size: 40px; " +
                "-fx-start-margin: 200");

        //add choicebox
        ChoiceBox<String> names = new ChoiceBox<>();
        names.getItems().addAll("Java" , "QuestionBox" , "English" , "World");
        Label label = new Label("Choose one" , names);
        label.setContentDisplay(ContentDisplay.TOP);
        label.setTextFill(Color.WHITE);

        //Add starting Button
        Button start = new Button("Start");
        start.setScaleX(2);
        start.setTextFill(Color.ROYALBLUE);
        start.setPadding(new Insets( 10));
        start.setOnMousePressed(e-> {
            System.out.println("Started");
            animation.setCycleCount(Timeline.INDEFINITE);
            secondd = 15;

            setName(names.getValue() );
            try {
                start2(stage);
            } catch (IOException ex) {}to = true;
        });
        clients.setSpacing(20);
        intro.getChildren().addAll(timeshow, pin, names ,clients , start );
        intro.setAlignment(Pos.CENTER);
        intro.setSpacing(20);

        return intro;
    }
    public void start2(Stage primaryStage) throws IOException {
        stage = primaryStage;
        mainpane = new Pane();
        mainpane.setStyle("-fx-background-color:#45178f");

        if (getName().equals("Java.txt"))
            Java(images);
         else if (getName().equals("English.txt"))
            English(images);
         else if (getName().equals("World.txt"))
            World(images);
         else
            QuestionBox(images);

        _(name);
        Shuffle(true);

        helper(images);
        timeshow.setStyle("-fx-fill: #4a0e8c; -fx-font-size: 50px;-fx-font-family: 'Comic Sans MS'");
        Circle circle = new Circle(50);
        circle.setFill(Color.WHITE);
        StackPane time = new StackPane();
        time.getChildren().add(circle);
        time.getChildren().add(timeshow);
        time.setTranslateX(700);
        time.setTranslateY(200);

        Media music = new Media(new File("src/sound.mp3").toURI().toString());
        MediaPlayer player = new MediaPlayer(music);
        player.setCycleCount(MediaPlayer.INDEFINITE);
        player.play();
        Image onn = new Image(new FileInputStream("src/on.jpg"));
        Image off = new Image(new FileInputStream("src/off.jpg"));
        ImageView on = new ImageView(onn);
        on.setFitWidth(30);
        on.setFitHeight(30);
        on.setTranslateY(660);
        on.setTranslateX(760);
        on.setOnMousePressed(e -> {
            if (on.getImage().equals(onn)) {
                on.setImage(off);
                player.stop();
            } else {
                on.setImage(onn);
                player.play();
            }
        });
        mainpane.getChildren().addAll(second.get(page), on, time);
        Scene scene = new Scene(mainpane, 800, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Server");
        primaryStage.show();
    }
    public void helper(ArrayList<Image> images) {
        for (int i = 0; i < questions.size(); i++) {
            if (questions.get(i).getDescription().contains("'blank'")) {
                Pane fillin = new Pane();
                Text text = new Text(i + 1 + ". " + ((Fillin) questions.get(i)).toString());
                text.setStyle("-fx-font-size: 25px; -fx-font-family: 'Comic Sans MS' ;-fx-font-weight: bold ;-fx-fill: white;");
                text.setWrappingWidth(800);
                text.setY(50);
                text.setTextAlignment(TextAlignment.CENTER);
                ImageView view = new ImageView(questions.get(i).getImage());
                view.setFitWidth(500);
                view.setFitHeight(270);
                view.setX(150);
                view.setY(120);

                TextField fill = new TextField();
                Label label = new Label("Enter your answer here" , fill);
                label.setTextFill(Color.WHITE);
                label.setContentDisplay(ContentDisplay.BOTTOM);
                label.setTranslateX(300);
                label.setTranslateY(430);
                fill.setStyle("-fx-min-width: 200;-fx-focus-traversable: false");
                list.add(new ArrayList<>());
                fillin.getChildren().addAll(text, view, label);
                second.add(fillin);
            }else if(questions.get(i).getDescription().contains(".mp4")){
                videofile(i);
            }else if(questions.get(i).getDescription().contains(".mp3")){
                audiofile(i);
            }else {
                Pane test = new Pane();
                Text text = new Text(i + 1 + ". " +((Test) questions.get(i)).toString());
                text.setStyle("-fx-font-size: 25px; -fx-font-family: 'Comic Sans MS' ;-fx-font-weight: bold ;-fx-fill: white;");
                text.setWrappingWidth(800);
                text.setY(50);
                text.setTextAlignment(TextAlignment.CENTER);
                ImageView view = new ImageView(questions.get(i).getImage());
                view.setFitWidth(500);
                view.setFitHeight(270);
                view.setX(150);
                view.setY(120);
                ArrayList<Button> buttons = new ArrayList<>();
                list.add(buttons);
                String[] a = questions.get(i).getAnswer().split("_");
                Test.setOptions(a);
                for (int j = 0; j < 4; j++) {
                    buttons.add(new Button());
                    buttons.get(j).setText(Test.getOptionAt(j));
                    buttons.get(j).setTextFill(Color.WHITE);
                }

                buttons.get(0).setMouseTransparent(true);
                buttons.get(1).setMouseTransparent(true);
                buttons.get(2).setMouseTransparent(true);
                buttons.get(3).setMouseTransparent(true);
                buttons.get(0).setStyle("-fx-background-color: red ;-fx-min-width: 370px ; -fx-min-height: 90 ; " +
                        "-fx-max-width: 370 ; -fx-max-height: 90; -fx-font-size: 20px;-fx-translate-x: 210 ; -fx-translate-y: 410");
                buttons.get(1).setStyle("-fx-background-color: orange ;-fx-min-width: 370px ;-fx-max-width: 370 ;" +
                        "-fx-max-height: 90; -fx-min-height: 90 ; -fx-font-size: 20px;-fx-translate-x: 20 ; -fx-translate-y: 510");
                buttons.get(2).setStyle("-fx-background-color: blue ;-fx-min-width: 370px ;-fx-max-width: 370 ;" +
                        " -fx-max-height: 90; -fx-min-height: 90 ; -fx-font-size: 20px;-fx-translate-x: 410 ; -fx-translate-y: 510");
                buttons.get(3).setStyle("-fx-background-color: green ;-fx-min-width: 370px ;-fx-max-width: 370 ;" +
                        "-fx-max-height: 90; -fx-min-height: 90 ; -fx-font-size: 20px;-fx-translate-x: 210 ; -fx-translate-y: 610");
                test.getChildren().addAll(text, view, buttons.get(0), buttons.get(1), buttons.get(2), buttons.get(3));
                second.add(test);
            }
        }
    }
    public void videofile(int i) {
        Pane video = new Pane();
        String[] vi = questions.get(i).getDescription().split("<>");
        questions.get(i).setDescription(vi[1]);
        Text desc = new Text((i + 1) + ". " + vi[1]);
        desc.setStyle("-fx-font-size: 25px; -fx-font-family: 'Comic Sans MS' ;-fx-font-weight: bold ;-fx-fill: white;");
        desc.setWrappingWidth(800);
        desc.setY(50);
        desc.setTextAlignment(TextAlignment.CENTER);
        Media media = new Media(new File("src/" + vi[0]).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setFitWidth(500);
        mediaView.setFitHeight(270);
        mediaView.setX(150);
        mediaView.setY(90);
        Button playButton = new Button("Play");
        playButton.setOnAction(e -> {
            if (playButton.getText().equals("Play") || playButton.getText().equals("Paused")) {
                mediaPlayer.play();
                totalTime = getTimeFormat(mediaPlayer.getStopTime().toMillis());
                slTime.setMax(mediaPlayer.getStopTime().toMillis());
                playButton.setText("Playing");
            }else {
                mediaPlayer.pause();
                playButton.setText("Paused");
            }
        });
        Button rewindButton = new Button("Replay");
        rewindButton.setOnAction(e -> mediaPlayer.seek(Duration.ZERO));
        Slider slVolume = new Slider();
        slVolume.setPrefWidth(100);
        slVolume.setMaxWidth(Region.USE_PREF_SIZE);
        slVolume.setMinWidth(30);
        slVolume.setValue(50);
        mediaPlayer.volumeProperty().bind(slVolume.valueProperty().divide(100));


        slTime.setPrefWidth(150);

        HBox hBox = new HBox(4);
        hBox.setTranslateX(150);
        hBox.setTranslateY(370);
        hBox.setAlignment(Pos.CENTER);
        Label lblVideoTime = new Label(getTimeFormat(0), slTime);
        lblVideoTime.setTextFill(Color.WHITE);
        hBox.getChildren().addAll(playButton, rewindButton, lblVideoTime, slVolume);

        mediaPlayer.currentTimeProperty().addListener(e -> {
            if (!slTime.isValueChanging()) {
                slTime.setValue(mediaPlayer.getCurrentTime().toMillis());
            }

            lblVideoTime.setText(
                    getTimeFormat(mediaPlayer.getCurrentTime().toMillis()) + "/" + totalTime);
        });

        slTime.valueProperty().addListener(ov -> {
            if (slTime.isValueChanging()) {
                mediaPlayer.seek(new Duration(slTime.getValue()));
            }
        });
        ArrayList<Button> buttons = new ArrayList<>();
        String[] a = questions.get(i).getAnswer().split("_");
        Test.setOptions(a);
        list.add(buttons);
        for (int j = 0; j < 4; j++) {
            buttons.add(new Button());
            buttons.get(j).setText(Test.getOptionAt(j));
            buttons.get(j).setTextFill(Color.WHITE);
        }
        buttons.get(0).setMouseTransparent(true);
        buttons.get(1).setMouseTransparent(true);
        buttons.get(2).setMouseTransparent(true);
        buttons.get(3).setMouseTransparent(true);
        buttons.get(0).setStyle("-fx-background-color: red ;-fx-min-width: 370px ; -fx-min-height: 90 ; " +
                "-fx-max-width: 370 ; -fx-max-height: 90; -fx-font-size: 20px;-fx-translate-x: 210 ; -fx-translate-y: 410");
        buttons.get(1).setStyle("-fx-background-color: orange ;-fx-min-width: 370px ;-fx-max-width: 370 ;" +
                "-fx-max-height: 90; -fx-min-height: 90 ; -fx-font-size: 20px;-fx-translate-x: 20 ; -fx-translate-y: 510");
        buttons.get(2).setStyle("-fx-background-color: blue ;-fx-min-width: 370px ;-fx-max-width: 370 ;" +
                " -fx-max-height: 90; -fx-min-height: 90 ; -fx-font-size: 20px;-fx-translate-x: 410 ; -fx-translate-y: 510");
        buttons.get(3).setStyle("-fx-background-color: green ;-fx-min-width: 370px ;-fx-max-width: 370 ;" +
                "-fx-max-height: 90; -fx-min-height: 90 ; -fx-font-size: 20px;-fx-translate-x: 210 ; -fx-translate-y: 610");
        video.getChildren().addAll(desc, mediaView , hBox, buttons.get(0), buttons.get(1), buttons.get(2), buttons.get(3));
        second.add(video);
    }
    public void audiofile(int i){
        Pane video = new Pane();
        String[] vi = questions.get(i).getDescription().split("<>");
        questions.get(i).setDescription(vi[1]);
        Text desc = new Text((i + 1) + ". " + vi[1]);
        desc.setStyle("-fx-font-size: 25px; -fx-font-family:'Comic Sans MS';-fx-font-weight: bold ;-fx-fill: white;");
        desc.setWrappingWidth(800);
        desc.setY(50);
        desc.setTextAlignment(TextAlignment.CENTER);
        Media media = new Media(new File("src/" + vi[0]).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);
        ImageView view = new ImageView(questions.get(i).getImage());
        view.setFitWidth(500);
        view.setFitHeight(270);
        view.setX(150);
        view.setY(90);
        Button playButton = new Button("Play");
        playButton.setOnAction(e -> {
            if (playButton.getText().equals("Play") || playButton.getText().equals("Paused")) {
                mediaPlayer.play();
                totalTime = getTimeFormat(mediaPlayer.getStopTime().toMillis());
                slTime.setMax(mediaPlayer.getStopTime().toMillis());
                playButton.setText("Playing");
            }else {
                mediaPlayer.pause();
                playButton.setText("Paused");
            }
        });
        Button rewindButton = new Button("Replay");
        rewindButton.setOnAction(e -> mediaPlayer.seek(Duration.ZERO));
        Slider slVolume = new Slider();
        slVolume.setPrefWidth(100);
        slVolume.setMaxWidth(Region.USE_PREF_SIZE);
        slVolume.setMinWidth(30);
        slVolume.setValue(50);
        mediaPlayer.volumeProperty().bind(slVolume.valueProperty().divide(100));


        slTime.setPrefWidth(150);

        HBox hBox = new HBox(4);
        hBox.setTranslateX(160);
        hBox.setTranslateY(370);
        hBox.setAlignment(Pos.CENTER);
        Label lblVideoTime = new Label(getTimeFormat(0), slTime);
        lblVideoTime.setTextFill(Color.WHITE);
        hBox.getChildren().addAll(playButton, rewindButton, lblVideoTime, slVolume);

        mediaPlayer.currentTimeProperty().addListener(e -> {
            if (!slTime.isValueChanging()) {
                slTime.setValue(mediaPlayer.getCurrentTime().toMillis());
            }

            lblVideoTime.setText(
                    getTimeFormat(mediaPlayer.getCurrentTime().toMillis()) + "/" + totalTime);
        });

        slTime.valueProperty().addListener(ov -> {
            if (slTime.isValueChanging()) {
                mediaPlayer.seek(new Duration(slTime.getValue()));
            }
        });
        ArrayList<Button> buttons = new ArrayList<>();
        list.add(buttons);
        String[] a = questions.get(i).getAnswer().split("_");

        Test.setOptions(a);
        for (int j = 0; j < 4; j++) {
            buttons.add(new Button());
            buttons.get(j).setText(Test.getOptionAt(j));
            buttons.get(j).setTextFill(Color.WHITE);
        }
        buttons.get(0).setMouseTransparent(true);
        buttons.get(1).setMouseTransparent(true);
        buttons.get(2).setMouseTransparent(true);
        buttons.get(3).setMouseTransparent(true);
        buttons.get(0).setStyle("-fx-background-color: red ;-fx-min-width: 370px ; -fx-min-height: 90 ; " +
                "-fx-max-width: 370 ; -fx-max-height: 90; -fx-font-size: 20px;-fx-translate-x: 210 ; -fx-translate-y: 410");
        buttons.get(1).setStyle("-fx-background-color: orange ;-fx-min-width: 370px ;-fx-max-width: 370 ;" +
                "-fx-max-height: 90; -fx-min-height: 90 ; -fx-font-size: 20px;-fx-translate-x: 20 ; -fx-translate-y: 510");
        buttons.get(2).setStyle("-fx-background-color: blue ;-fx-min-width: 370px ;-fx-max-width: 370 ;" +
                " -fx-max-height: 90; -fx-min-height: 90 ; -fx-font-size: 20px;-fx-translate-x: 410 ; -fx-translate-y: 510");
        buttons.get(3).setStyle("-fx-background-color: green ;-fx-min-width: 370px ;-fx-max-width: 370 ;" +
                "-fx-max-height: 90; -fx-min-height: 90 ; -fx-font-size: 20px;-fx-translate-x: 210 ; -fx-translate-y: 610");
        video.getChildren().addAll(desc, mediaView , view, hBox, buttons.get(0), buttons.get(1), buttons.get(2), buttons.get(3));
        second.add(video);
    }
    public void _(String FileName) throws FileNotFoundException{
        File file = new File(FileName);
        Scanner reader  = new Scanner(file);
        while (reader.hasNextLine()) {
            String qatar = reader.nextLine();
            if(qatar.contains("'blank'")){
                Fillin fillin = new Fillin();
                fillin.setDescription(qatar);
                qatar = reader.nextLine();
                fillin.setAnswer(qatar);
                questions.add(fillin);
            }else{
                Test test = new Test();
                test.setDescription(qatar);
                String answers = "";
                for (int i = 0; i < 4; i++)
                    answers += reader.nextLine() + "_";
                test.setAnswer(answers);
                answers = "";
                questions.add(test);
            }
            if(reader.hasNextLine()) {
                reader.nextLine();
            }
        }
        for (int i = 0; i < images.size(); i++) {
            questions.get(i).setImage(images.get(i));
        }
    }
    public void Java(ArrayList<Image> java) throws FileNotFoundException {
        java.add(new Image(new FileInputStream("src/java1.jpg")));
        java.add(new Image(new FileInputStream("src/java2.jpg")));
        java.add(new Image(new FileInputStream("src/java3.jpg")));
        java.add(new Image(new FileInputStream("src/java4.jpg")));
        java.add(new Image(new FileInputStream("src/java5.jpg")));
    }
    public void English(ArrayList<Image> english) throws FileNotFoundException{
        english.add(new Image(new FileInputStream("src/eng5.jpg")));
        english.add(new Image(new FileInputStream("src/eng1.jpg")));
        english.add(new Image(new FileInputStream("src/eng3.jpg")));
        english.add(new Image(new FileInputStream("src/eng1.jpg")));
        english.add(new Image(new FileInputStream("src/eng5.jpg")));
    }
    public void World(ArrayList<Image> world) throws FileNotFoundException{
        world.add(new Image(new FileInputStream("src/w1.jpg")));
        world.add(new Image(new FileInputStream("src/canada.jpg")));
        world.add(new Image(new FileInputStream("src/cars.jpg")));
        world.add(new Image(new FileInputStream("src/w3.jpg")));
        world.add(new Image(new FileInputStream("src/w4.jpg")));
        world.add(new Image(new FileInputStream("src/SeeYou.jpg")));
    }
    public void QuestionBox(ArrayList<Image> que) throws FileNotFoundException{
        que.add(new Image(new FileInputStream("src/q3.jpg")));
        que.add(new Image(new FileInputStream("src/qw.jpg")));
        que.add(new Image(new FileInputStream("src/q1.jpg")));
        que.add(new Image(new FileInputStream("src/q4.jpg")));
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name + ".txt";
    }
    public void Shuffle(boolean value) {
        if (value == true)
            Collections.shuffle(questions);
    }
    private String getTimeFormat(double milliseconds) {
        milliseconds /= 1000;
        String seconds =  formatTwoDigits(milliseconds % 60);
        milliseconds /= 60;
        String minutes = formatTwoDigits(milliseconds % 60);
        milliseconds /= 60;
        String hours =  formatTwoDigits(milliseconds % 24);
        return hours + ":" + minutes + ":" + seconds;
    }
    private String formatTwoDigits(double time) {
        int intTime = (int) time;
        return (intTime > 9) ? intTime + "" : "0" + intTime;
    }
    public int createPIN(){
        PIN = (int)(Math.random()* 1000000);
        return PIN;
    }
    public static int getPIN() {
        return QuizMaker.PIN;
    }
    public void setPIN(int PIN) {
        this.PIN = PIN;
    }
    public void play() {
        animation.play();
    }
    public void run() throws IOException, InterruptedException {
        secondd = secondd < 70 ? secondd - 1 : 0;
        if (animation.getCycleCount() == 60) {
            timeshow.setText("You have " + get() + " seconds to join!");
        } else {
            if (secondd < 0) {
                animation.stop();
                page++;
                secondd = 15;
                Thread.sleep(200);
                if(page == questions.size()){
                    to = false;
                    stage.setScene(new Scene(finishpane() , 800,700));
                    animation.stop();
                }else {
                    mainpane.getChildren().set(0, second.get(page));play();
                }

            }
            timeshow.setText(get());
        }
    }

    private BorderPane finishpane() throws FileNotFoundException {
        BorderPane finish = new BorderPane();
        finish.setStyle("-fx-background-color: #46178f;");
        ImageView bn = new ImageView(new Image(new FileInputStream("src/kahoot1.jpg")));
        bn.setFitWidth(400);
        bn.setFitHeight(250);
        bn.setTranslateX(200);
        Text fin = new Text("Congratulations!");
        fin.setStyle("-fx-font-weight: bold;-fx-font-size: 30;-fx-font-family: 'Comic Sans MS';-fx-fill: white;-fx-translate-x: 250");
        VBox left = new VBox();
        VBox right = new VBox();
        ArrayList<String> ae = new ArrayList<>();
        for(Map.Entry<String ,Integer> entry : show.entrySet()){
            ae.add(entry.getKey() + " ------------------------ " + entry.getValue());
        }
        int s = ae.size();
        for (int i = s-1; i >=0 ; i--) {
            Text nme = new Text(ae.get(i));
            nme.setStyle("-fx-font-family: 'Lucida Sans Typewriter';-fx-fill: white;-fx-font-size: 25 ;-fx-font-weight: bold;-fx-translate-x: 150");
            right.getChildren().add(nme);
        }
        finish.setTop(fin);
        finish.setBottom(bn);
        finish.setCenter(right);
        return finish;
    }

    public String get() {
        return String.valueOf(secondd);
    }
}