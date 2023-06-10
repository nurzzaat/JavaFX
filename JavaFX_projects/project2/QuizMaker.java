package CSS.project2;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class QuizMaker extends Application {

    private Stage stage;
    private String name = "";
    private TimeShower time = new TimeShower();
    private static ArrayList<Question> questions = new ArrayList<>();
    private ArrayList<Pane> second = new ArrayList<Pane>();
    static ArrayList<Image> images = new ArrayList<>();
    private String[] wer;
    private int x = 25;
    private int finalI = 0;
    private int score = 0;
    private Slider slTime = new Slider();
    private String totalTime = "";

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        VBox mainpane = new VBox();
        mainpane.setStyle("-fx-background-color: #46178f");

        //Add kahoot photo
        Image kahoot = new Image(new FileInputStream("src/kahoot.jpg"));
        ImageView view = new ImageView(kahoot);
        view.setFitWidth(500);
        view.setFitHeight(300);

        //Add welcome text
        Text text = new Text("\nWelcome to KAHOOT!");
        text.setStyle("-fx-font-family:Montserrat , Noto Sans Arabic, Helvetica Neue, Helvetica, Arial, sans-serif;"+
                "-fx-font-weight: bold; -fx-fill: white;-fx-font-size: 40px; " +
                "-fx-start-margin: 200");

        //Add FileNames
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
        start.setOnAction(e-> {
            String a = names.getValue();
            setName(a);
            try {
                start2(primaryStage);
            } catch (FileNotFoundException ex) {
            }
        });
        mainpane.getChildren().addAll(text , view , label , start);
        mainpane.setAlignment(Pos.CENTER);
        mainpane.setSpacing(30);

        Scene scene = new Scene(mainpane , 800 , 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Project_2");
        primaryStage.show();
    }
    public void start2(Stage primaryStage) throws FileNotFoundException {
        stage = primaryStage;
        Pane mainpane = new Pane();
        mainpane.setStyle("-fx-background-color:#46178f");

        //Add ViewImages
        if(getName().equals("Java.txt")){
            Java(images);
        }else if(getName().equals("English.txt")){
            English(images);
        }else if(getName().equals("World.txt")){
            World(images);
        }else{
            QuestionBox(images);
        }
        _(name);
        Shuffle(true);
        helper(images);
        wer = new String[questions.size()];

        //Add time
        time.setTranslateY(630);
        time.setTranslateX(690);
        time.play();

        //Add Sound emblem
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
        on.setTranslateX(655);
        on.setOnMousePressed(e->{
            if(on.getImage().equals(onn)) {
                on.setImage(off);
                player.stop();
            }else{
                on.setImage(onn);
                player.play();
            }
        });

        //Add Finish Button
        Button finish = new Button("Finish");
        finish.setPadding(new Insets(5 , 15 ,5 , 15 ));
        finish.setTranslateX(700);
        finish.setTranslateY(660);
        finish.setOnMousePressed(e->{
            try {
                finish();
                time.pause();
                player.stop();
            } catch (FileNotFoundException ex) {
            }
        });
        //Add Buttons
        Button right = new Button(">");
        Button left = new Button("<");
        right.setTranslateY(250);
        right.setTranslateX(756);
        right.setPadding(new Insets(18 , 18 , 18 , 18));
        left.setTranslateY(250);
        left.setPadding(new Insets(18 , 18 , 18 , 18));
        if (finalI==0)
            left.setVisible(false);
        right.setOnMousePressed(e->{
            try {
                finalI++;
                if(finalI == questions.size() - 1)
                    right.setVisible(false);
                mainpane.getChildren().set(0 , second.get(finalI));
                left.setVisible(true);
            } catch (Exception ee) {
            }
        });
        left.setOnMousePressed(e->{
            try {
                finalI--;
                right.setVisible(true);
                mainpane.getChildren().set(0 , second.get(finalI));
                if (finalI==0)
                    left.setVisible(false);
            } catch (Exception ee) {
            }
        });
        mainpane.getChildren().addAll(second.get(0) , right , left , on , finish , time);
        Scene scene = new Scene(mainpane, 800, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Project_2");
        primaryStage.show();
    }
    private void finish() throws FileNotFoundException {
        Pane finishpane = new Pane();
        finishpane.setStyle("-fx-background-color:#46178f");
        DecimalFormat df = new DecimalFormat("0.00");
        double aq = (double)(score * 100) / questions.size();
        Text one = new Text("Your score is: " + df.format(aq) +  "%");
        one.setStyle("-fx-font-size: 30px; -fx-font-family: Arial ;-fx-font-weight: bold ;-fx-fill: white;-fx-translate-y: 70 ; -fx-translate-x: 280");
        Text two = new Text("You spend " + time.get() + " times for quiz");
        two.setStyle("-fx-font-size: 20px; -fx-font-family: Arial ;-fx-font-weight: bold ;-fx-fill: white;-fx-translate-y: 120 ; -fx-translate-x: 260");
        Text three = new Text("Answers: " + score + "/" + questions.size());
        three.setStyle("-fx-font-size: 20px; -fx-font-family: Arial ;-fx-font-weight: bold ;-fx-fill: white;-fx-translate-y: 170 ; -fx-translate-x: 340");

        Rectangle show = new Rectangle( 340  , 80);
        show.setStyle("-fx-fill: silver ; -fx-text-alignment: center ; -fx-arc-width: 10 ; -fx-arc-height: 10 ; -fx-text-fill: white ; -fx-font-family: Arial");
        Text a = new Text("Show Answers");
        a.setStyle("-fx-font-size: 20px; -fx-font-family: Arial ;-fx-font-weight: bold ;");
        StackPane bir = new StackPane(show , a);
        bir.setTranslateX(230);
        bir.setTranslateY(200);
        Rectangle end = new Rectangle( 340  , 80);
        end.setStyle("-fx-fill: silver ; -fx-text-alignment: center ; -fx-arc-width: 10 ; -fx-arc-height: 10 ; -fx-text-fill: white ; -fx-font-family: Arial");
        Text b = new Text("Close quiz");
        b.setStyle("-fx-font-size: 20px; -fx-font-family: Arial ;-fx-font-weight: bold ;");
        StackPane eki = new StackPane(end , b);
        eki.setTranslateX(230);
        eki.setTranslateY(290);
        Image em = new Image(new FileInputStream("src/kahoot1.jpg"));
        ImageView eq = new ImageView(em);
        eq.setFitWidth(460);
        eq.setFitHeight(250);
        eq.setStyle("-fx-translate-x: 170 ; -fx-translate-y:425;");

        bir.setOnMouseClicked(e->{
            Pane pane = new Pane();
            pane.setStyle("-fx-background-color: #46178f");
            Text on = new Text("Your score  " + df.format(aq) +   "%");
            on.setStyle("-fx-font-size: 18px; -fx-font-family: Arial ;-fx-font-weight: bold ;-fx-fill: white;-fx-translate-y: 20 ; -fx-translate-x: 635");
            Text tw = new Text("Your time  " + time.get());
            tw.setStyle("-fx-font-size: 16px; -fx-font-family: Arial ;-fx-font-weight: bold ;-fx-fill: white;-fx-translate-y: 40 ; -fx-translate-x: 635");
            Text thre = new Text("Answers: " + score + "/" + questions.size());
            thre.setStyle("-fx-font-size: 16px; -fx-font-family: Arial ;-fx-font-weight: bold ;-fx-fill: white;-fx-translate-y: 60 ; -fx-translate-x: 635");
            pane.getChildren().addAll(on , tw , thre);
            for (int i = 0; i < questions.size(); i++) {
                Text text = new Text((i + 1) + ". " + questions.get(i).getDescription());
                text.setStyle("-fx-font-family: Arial;-fx-font-size: 20;-fx-fill:white;-fx-font-weight: bold");
                text.setTranslateY(x);
                pane.getChildren().add(text);
                if(questions.get(i) instanceof Test){
                    String[] qw = questions.get(i).getAnswer().split("_");
                    Test.setOptions(qw);
                    for (int j = 0; j < 4; j++) {
                        x += 23;
                        Text q = new Text(Test.getOptionAt(j));
                        q.setTranslateY(x);
                        q.setTranslateX(20);
                        if(qw[0].equals(wer[i])) {
                            if (q.getText().equals(wer[i])) {
                                q.setStyle("-fx-fill: green;-fx-font-size: 17;-fx-font-family: Arial;-fx-font-weight: bold");
                            }
                            else{
                                q.setStyle("-fx-font-family: Arial;-fx-font-size: 17;-fx-fill:white;-fx-font-weight: bold");
                            }
                        }else{
                            if(qw[0].equals(q.getText())){
                                if(wer[i] != null)
                                    q.setStyle("-fx-fill: green;-fx-font-size: 17;-fx-font-family: Arial;-fx-font-weight: bold");
                                else
                                    q.setStyle("-fx-fill: rgba(255,221,0,0.8);-fx-font-size: 17;-fx-font-family: Arial;-fx-font-weight: bold");
                            }
                            else if(q.getText().equals(wer[i])){
                                q.setStyle("-fx-fill: red;-fx-font-size: 17;-fx-font-family: Arial;-fx-font-weight: bold");
                            }else{
                                q.setStyle("-fx-font-family: Arial;-fx-font-size: 17;-fx-fill:white;-fx-font-weight: bold");
                            }
                        }
                        pane.getChildren().add(q);
                    }
                }else{
                    x += 23;
                    Text qwe = new Text(questions.get(i).getAnswer());
                    qwe.setTranslateY(x);
                    qwe.setTranslateX(20);
                    if(qwe.getText().equalsIgnoreCase(wer[i])){
                        qwe.setStyle("-fx-font-family: Arial;-fx-font-size: 17;-fx-fill:green;-fx-font-weight: bold");
                    }else{
                        qwe.setText(wer[i]);
                        qwe.setStyle("-fx-font-family: Arial;-fx-font-size: 17;-fx-fill:red;-fx-font-weight: bold");
                        Text ans = new Text("Correct answer is: " + questions.get(i).getAnswer());
                        if(wer[i] != null)
                            x += 23;
                        ans.setTranslateY(x);
                        ans.setTranslateX(20);
                        if(wer[i] != null)
                            ans.setStyle("-fx-font-family: Arial;-fx-font-size: 17;-fx-fill: green;-fx-font-weight: bold");
                        else
                            ans.setStyle("-fx-font-family: Arial;-fx-font-size: 17;-fx-fill: rgba(255,221,0,0.8);-fx-font-weight: bold");
                        pane.getChildren().add(ans);
                    }
                    pane.getChildren().add(qwe);
                }
                x += 50;
            }
            Button close = new Button("Close");
            close.setPadding(new Insets(5 , 10 , 5 , 10));
            close.setTranslateX(660);
            close.setTranslateY(75);
            close.setOnMouseClicked(mouseEvent->{
                stage.close();
            });
            pane.getChildren().add(close);
            Scene scene = new Scene(pane , 800 , x);
            stage.setScene(scene);
            stage.show();
        });

        eki.setOnMouseClicked(e->{
            stage.close();
        });
        finishpane.getChildren().addAll(one , two , three , bir , eki , eq);
        Scene scenee = new Scene(finishpane , 800 , 700);
        stage.setScene(scenee);
        stage.show();
    }
    public void helper(ArrayList<Image> images) {
        for (int i = 0; i < questions.size(); i++) {
            if (questions.get(i).getDescription().contains("'blank'")) {
                Pane fillin = new Pane();
                Text text = new Text(i + 1 + ". " + ((Fillin) questions.get(i)).toString());
                text.setStyle("-fx-font-size: 25px; -fx-font-family: Arial ;-fx-font-weight: bold ;-fx-fill: white;");
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
                fill.setStyle("-fx-min-width: 200");
                int y = i;
                fill.setOnKeyTyped(e-> {
                    if (wer[y] == null) {
                        wer[y] = fill.getText();
                        if (wer[y].equalsIgnoreCase(questions.get(y).getAnswer()))
                            score++;
                    } else {
                        if (wer[y].equalsIgnoreCase(fill.getText())) {
                        } else if (wer[y].equalsIgnoreCase(questions.get(y).getAnswer()))
                            score--;
                        else if (fill.getText().equalsIgnoreCase(questions.get(y).getAnswer()))
                            score++;
                        wer[y] = fill.getText();
                    }
                });
                fillin.getChildren().addAll(text, view, label);
                second.add(fillin);
            }else if(questions.get(i).getDescription().contains(".mp4")){
                videofile(i);
            }else if(questions.get(i).getDescription().contains(".mp3")){
                audiofile(i);
            }else {
                Pane test = new Pane();
                Text text = new Text(i + 1 + ". " +((Test) questions.get(i)).toString());
                text.setStyle("-fx-font-size: 25px; -fx-font-family: Arial ;-fx-font-weight: bold ;-fx-fill: white;");
                text.setWrappingWidth(800);
                text.setY(50);
                text.setTextAlignment(TextAlignment.CENTER);
                ImageView view = new ImageView(questions.get(i).getImage());
                view.setFitWidth(500);
                view.setFitHeight(270);
                view.setX(150);
                view.setY(120);
                ArrayList<RadioButton> buttons = new ArrayList<>();
                ToggleGroup to = new ToggleGroup();

                String[] a = questions.get(i).getAnswer().split("_");
                Test.setOptions(a);
                for (int j = 0; j < 4; j++) {
                    buttons.add(new RadioButton());
                    buttons.get(j).setText(Test.getOptionAt(j));
                    buttons.get(j).setToggleGroup(to);
                    buttons.get(j).setTextFill(Color.WHITE);
                }
                buttons.get(0).setStyle("-fx-background-color: red ;-fx-min-width: 370px ; -fx-min-height: 90 ; " +
                        "-fx-max-width: 370 ; -fx-max-height: 90; -fx-font-size: 20px;-fx-translate-x: 210 ; -fx-translate-y: 410");
                buttons.get(1).setStyle("-fx-background-color: orange ;-fx-min-width: 370px ;-fx-max-width: 370 ;" +
                        "-fx-max-height: 90; -fx-min-height: 90 ; -fx-font-size: 20px;-fx-translate-x: 20 ; -fx-translate-y: 510");
                buttons.get(2).setStyle("-fx-background-color: blue ;-fx-min-width: 370px ;-fx-max-width: 370 ;" +
                        " -fx-max-height: 90; -fx-min-height: 90 ; -fx-font-size: 20px;-fx-translate-x: 410 ; -fx-translate-y: 510");
                buttons.get(3).setStyle("-fx-background-color: green ;-fx-min-width: 370px ;-fx-max-width: 370 ;" +
                        "-fx-max-height: 90; -fx-min-height: 90 ; -fx-font-size: 20px;-fx-translate-x: 210 ; -fx-translate-y: 610");
                test.getChildren().addAll(text, view, buttons.get(0), buttons.get(1), buttons.get(2), buttons.get(3));
                int y = i;
                buttons.get(0).setOnMouseClicked(e-> {
                    if (wer[y] == null) {
                        wer[y] = buttons.get(0).getText();
                        if(wer[y].equals(a[0]))
                            score++;
                    }else {
                        if(wer[y].equals(buttons.get(0).getText())){}
                        else if(wer[y].equals(a[0]))
                            score--;
                        else if (buttons.get(0).getText().equals(a[0]))
                            score++;

                        wer[y] = buttons.get(0).getText();
                    }
                });buttons.get(1).setOnMouseClicked(e->{
                    if (wer[y] == null) {
                        wer[y] = buttons.get(1).getText();
                        if(wer[y].equals(a[0]))
                            score++;
                    }else {
                        if(wer[y].equals(buttons.get(1).getText())){}
                        else if(wer[y].equals(a[0]))
                            score--;
                        else if (buttons.get(1).getText().equals(a[0]))
                            score++;

                        wer[y] = buttons.get(1).getText();
                    }

                });buttons.get(2).setOnMouseClicked(e->{
                    if (wer[y] == null) {
                        wer[y] = buttons.get(2).getText();
                        if(wer[y].equals(a[0]))
                            score++;
                    }else {
                        if(wer[y].equals(buttons.get(2).getText())){}
                        else if(wer[y].equals(a[0]))
                            score--;
                        else if (buttons.get(2).getText().equals(a[0]))
                            score++;

                        wer[y] = buttons.get(2).getText();
                    }
                });buttons.get(3).setOnMouseClicked(e->{
                    if (wer[y] == null) {
                        wer[y] = buttons.get(3).getText();
                        if(wer[y].equals(a[0]))
                            score++;
                    }else {
                        if(wer[y].equals(buttons.get(3).getText())){}
                        else if(wer[y].equals(a[0]))
                            score--;
                        else if (buttons.get(3).getText().equals(a[0]))
                            score++;

                        wer[y] = buttons.get(3).getText();
                    }
                });
                second.add(test);
            }
        }
    }
    private void videofile(int i) {
        Pane video = new Pane();
        String[] vi = questions.get(i).getDescription().split("<>");
        questions.get(i).setDescription(vi[1]);
        Text desc = new Text((i + 1) + ". " + vi[1]);
        desc.setStyle("-fx-font-size: 25px; -fx-font-family: Arial ;-fx-font-weight: bold ;-fx-fill: white;");
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
        ArrayList<RadioButton> buttons = new ArrayList<>();
        ToggleGroup to = new ToggleGroup();

        String[] a = questions.get(i).getAnswer().split("_");

        Test.setOptions(a);
        for (int j = 0; j < 4; j++) {
            buttons.add(new RadioButton());
            buttons.get(j).setText(Test.getOptionAt(j));
            buttons.get(j).setToggleGroup(to);
            buttons.get(j).setTextFill(Color.WHITE);
        }
        buttons.get(0).setStyle("-fx-background-color: red ;-fx-min-width: 370px ; -fx-min-height: 90 ; " +
                "-fx-max-width: 370 ; -fx-max-height: 90; -fx-font-size: 20px;-fx-translate-x: 210 ; -fx-translate-y: 410");
        buttons.get(1).setStyle("-fx-background-color: orange ;-fx-min-width: 370px ;-fx-max-width: 370 ;" +
                "-fx-max-height: 90; -fx-min-height: 90 ; -fx-font-size: 20px;-fx-translate-x: 20 ; -fx-translate-y: 510");
        buttons.get(2).setStyle("-fx-background-color: blue ;-fx-min-width: 370px ;-fx-max-width: 370 ;" +
                " -fx-max-height: 90; -fx-min-height: 90 ; -fx-font-size: 20px;-fx-translate-x: 410 ; -fx-translate-y: 510");
        buttons.get(3).setStyle("-fx-background-color: green ;-fx-min-width: 370px ;-fx-max-width: 370 ;" +
                "-fx-max-height: 90; -fx-min-height: 90 ; -fx-font-size: 20px;-fx-translate-x: 210 ; -fx-translate-y: 610");
        int y = i;
        buttons.get(0).setOnMouseClicked(e-> {
            if (wer[y] == null) {
                wer[y] = buttons.get(0).getText();
                if(wer[y].equals(a[0]))
                    score++;
            }else {
                if(wer[y].equals(buttons.get(0).getText())){}
                else if(wer[y].equals(a[0]))
                    score--;
                else if (buttons.get(0).getText().equals(a[0]))
                    score++;

                wer[y] = buttons.get(0).getText();
            }
        });buttons.get(1).setOnMouseClicked(e->{
            if (wer[y] == null) {
                wer[y] = buttons.get(1).getText();
                if(wer[y].equals(a[0]))
                    score++;
            }else {
                if(wer[y].equals(buttons.get(1).getText())){}
                else if(wer[y].equals(a[0]))
                    score--;
                else if (buttons.get(1).getText().equals(a[0]))
                    score++;

                wer[y] = buttons.get(1).getText();
            }
        });buttons.get(2).setOnMouseClicked(e->{
            if (wer[y] == null) {
                wer[y] = buttons.get(2).getText();
                if(wer[y].equals(a[0]))
                    score++;
            }else {
                if(wer[y].equals(buttons.get(2).getText())){}
                else if(wer[y].equals(a[0]))
                    score--;
                else if (buttons.get(2).getText().equals(a[0]))
                    score++;

                wer[y] = buttons.get(2).getText();
            }
        });buttons.get(3).setOnMouseClicked(e->{
            if (wer[y] == null) {
                wer[y] = buttons.get(3).getText();
                if(wer[y].equals(a[0]))
                    score++;
            }else {
                if(wer[y].equals(buttons.get(3).getText())){}
                else if(wer[y].equals(a[0]))
                    score--;
                else if (buttons.get(3).getText().equals(a[0]))
                    score++;

                wer[y] = buttons.get(3).getText();
            }
        });
        video.getChildren().addAll(desc, mediaView , hBox, buttons.get(0), buttons.get(1), buttons.get(2), buttons.get(3));
        second.add(video);
    }
    void audiofile(int i){
        Pane video = new Pane();
        String[] vi = questions.get(i).getDescription().split("<>");
        questions.get(i).setDescription(vi[1]);
        Text desc = new Text((i + 1) + ". " + vi[1]);
        desc.setStyle("-fx-font-size: 25px; -fx-font-family: Arial ;-fx-font-weight: bold ;-fx-fill: white;");
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
        ArrayList<RadioButton> buttons = new ArrayList<>();
        ToggleGroup to = new ToggleGroup();

        String[] a = questions.get(i).getAnswer().split("_");

        Test.setOptions(a);
        for (int j = 0; j < 4; j++) {
            buttons.add(new RadioButton());
            buttons.get(j).setText(Test.getOptionAt(j));
            buttons.get(j).setToggleGroup(to);
            buttons.get(j).setTextFill(Color.WHITE);
        }
        buttons.get(0).setStyle("-fx-background-color: red ;-fx-min-width: 370px ; -fx-min-height: 90 ; " +
                "-fx-max-width: 370 ; -fx-max-height: 90; -fx-font-size: 20px;-fx-translate-x: 210 ; -fx-translate-y: 410");
        buttons.get(1).setStyle("-fx-background-color: orange ;-fx-min-width: 370px ;-fx-max-width: 370 ;" +
                "-fx-max-height: 90; -fx-min-height: 90 ; -fx-font-size: 20px;-fx-translate-x: 20 ; -fx-translate-y: 510");
        buttons.get(2).setStyle("-fx-background-color: blue ;-fx-min-width: 370px ;-fx-max-width: 370 ;" +
                " -fx-max-height: 90; -fx-min-height: 90 ; -fx-font-size: 20px;-fx-translate-x: 410 ; -fx-translate-y: 510");
        buttons.get(3).setStyle("-fx-background-color: green ;-fx-min-width: 370px ;-fx-max-width: 370 ;" +
                "-fx-max-height: 90; -fx-min-height: 90 ; -fx-font-size: 20px;-fx-translate-x: 210 ; -fx-translate-y: 610");
        int y = i;
        buttons.get(0).setOnMouseClicked(e-> {
            if (wer[y] == null) {
                wer[y] = buttons.get(0).getText();
                if(wer[y].equals(a[0]))
                    score++;
            }else {
                if(wer[y].equals(buttons.get(0).getText())){}
                else if(wer[y].equals(a[0]))
                    score--;
                else if (buttons.get(0).getText().equals(a[0]))
                    score++;

                wer[y] = buttons.get(0).getText();
            }
        });buttons.get(1).setOnMouseClicked(e->{
            if (wer[y] == null) {
                wer[y] = buttons.get(1).getText();
                if(wer[y].equals(a[0]))
                    score++;
            }else {
                if(wer[y].equals(buttons.get(1).getText())){}
                else if(wer[y].equals(a[0]))
                    score--;
                else if (buttons.get(1).getText().equals(a[0]))
                    score++;

                wer[y] = buttons.get(1).getText();
            }
        });buttons.get(2).setOnMouseClicked(e->{
            if (wer[y] == null) {
                wer[y] = buttons.get(2).getText();
                if(wer[y].equals(a[0]))
                    score++;
            }else {
                if(wer[y].equals(buttons.get(2).getText())){}
                else if(wer[y].equals(a[0]))
                    score--;
                else if (buttons.get(2).getText().equals(a[0]))
                    score++;

                wer[y] = buttons.get(2).getText();
            }
        });buttons.get(3).setOnMouseClicked(e->{
            if (wer[y] == null) {
                wer[y] = buttons.get(3).getText();
                if(wer[y].equals(a[0]))
                    score++;
            }else {
                if(wer[y].equals(buttons.get(3).getText())){}
                else if(wer[y].equals(a[0]))
                    score--;
                else if (buttons.get(3).getText().equals(a[0]))
                    score++;

                wer[y] = buttons.get(3).getText();
            }
        });
        video.getChildren().addAll(desc, mediaView , view, hBox, buttons.get(0), buttons.get(1), buttons.get(2), buttons.get(3));
        second.add(video);
    }
    public static void _(String FileName) throws FileNotFoundException{
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
}