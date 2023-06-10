package CSS.project3;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class copy1 extends Application {
    Socket client;
    Stage  stage;
    String user = "User";
    String pc;
    Pane pane;
    DataOutputStream toServer;
    DataInputStream fromServer;
    private int secondd = 14;
    private Timeline animation;
    int page = 0;int size;
    ArrayList<Pane> panes = new ArrayList<>();
    public static void main(String[] args) {
        Application.launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        client = new Socket("LocalHost", 1136);
        toServer = new DataOutputStream(client.getOutputStream());
        fromServer = new DataInputStream(client.getInputStream());
        stage.setScene(new Scene(as() , 420 , 320));
        stage.setTitle(user);
        stage.show();
    }
    Pane as(){
        pane = new Pane();
        pane.setStyle("-fx-background-color: #46178f");

        //Add username
        TextField username = new TextField();
        username.setPromptText("User Name");
        username.setTranslateX(160);
        username.setTranslateY(100);
        username.setMaxWidth(100);
        username.setFocusTraversable(false);
        username.setOnKeyTyped(e->{
            user = username.getText();
        });

        //Add pin
        TextField pin = new TextField();
        pin.setPromptText("Game PIN");
        pin.setTranslateX(160);
        pin.setTranslateY(140);
        pin.setMaxWidth(100);
        pin.requestFocus();
        pin.setFocusTraversable(false);
        pin.setOnKeyTyped(e->{
            pc = pin.getText();
        });

        //Add Enter
        Button start = new Button("Start");
        start.setMaxWidth(100);
        start.setTextFill(Color.ROYALBLUE);
        start.setPadding(new Insets( 5 , 10 , 5 , 10));
        start.setTranslateX(182);
        start.setTranslateY(190);
        start.setOnMouseClicked(event->{
            try {
                int pn = fromServer.readInt();
                if(Integer.parseInt(pc)==pn){
                    toServer.writeBoolean(true);
                    System.out.println("ADDED");
                    toServer.writeUTF(user);
                    stage.setTitle(user);
                    boolean from = fromServer.readBoolean();
                    from = check(from, fromServer);
                    if (from) {
                        size = fromServer.readInt();
                        animation = new Timeline(new KeyFrame(Duration.millis(1000), e -> {
                            try {
                                run();
                            } catch (InterruptedException | IOException ex) {
                                ex.printStackTrace();
                            }
                        }));
                        animation.setCycleCount(Timeline.INDEFINITE);
                        play();
                        for (int i = 0; i < size; i++) {
                            char f = fromServer.readChar();
                            System.out.println(f);
                            if (f == 'T') {
                                Pane testpane = new Pane();
                                ArrayList<Button> buttons = new ArrayList<>();

                                for (int j = 0; j < 4; j++) {
                                    buttons.add(new Button());
                                    buttons.get(j).setTextFill(Color.WHITE);
                                }
                                buttons.get(0).setStyle("-fx-background-color: red ;-fx-min-width: 200px ; -fx-min-height: 90 ; " +
                                        "-fx-max-width: 200 ; -fx-max-height: 90; -fx-font-size: 20px;-fx-translate-x: 110 ; -fx-translate-y: 15");
                                buttons.get(1).setStyle("-fx-background-color: orange ;-fx-min-width: 200px ;-fx-max-width: 200 ;" +
                                        "-fx-max-height: 90; -fx-min-height: 90 ; -fx-font-size: 20px;-fx-translate-x: 5 ; -fx-translate-y: 115");
                                buttons.get(2).setStyle("-fx-background-color: blue ;-fx-min-width: 200px ;-fx-max-width: 200 ;" +
                                        " -fx-max-height: 90; -fx-min-height: 90 ; -fx-font-size: 20px;-fx-translate-x: 215 ; -fx-translate-y: 115");
                                buttons.get(3).setStyle("-fx-background-color: green ;-fx-min-width: 200px ;-fx-max-width: 200 ;" +
                                        "-fx-max-height: 90; -fx-min-height: 90 ; -fx-font-size: 20px;-fx-translate-x: 110 ; -fx-translate-y: 215" +
                                        "");
                                buttons.get(0).setOnMousePressed(ev -> {
                                    try {
                                        toServer.writeUTF("R");
                                        pane.getChildren().clear();
                                        ImageView gif = new ImageView(new Image(new FileInputStream("src/CSS/project3/loading.gif")));
                                        gif.setFitWidth(420);
                                        gif.setFitHeight(320);
                                        pane.getChildren().add(gif);
                                    } catch (IOException ex) {
                                    }
                                });
                                buttons.get(1).setOnMousePressed(ev -> {
                                    try {
                                        toServer.writeUTF("O");
                                        pane.getChildren().clear();
                                        ImageView gif = new ImageView(new Image(new FileInputStream("src/CSS/project3/loading.gif")));
                                        gif.setFitWidth(420);
                                        gif.setFitHeight(320);
                                        pane.getChildren().add(gif);
                                    } catch (IOException ex) {
                                    }
                                });
                                buttons.get(2).setOnMousePressed(ev -> {
                                    try {
                                        toServer.writeUTF("B");
                                        pane.getChildren().clear();
                                        ImageView gif = new ImageView(new Image(new FileInputStream("src/CSS/project3/loading.gif")));
                                        gif.setFitWidth(420);
                                        gif.setFitHeight(320);
                                        pane.getChildren().add(gif);
                                    } catch (IOException ex) {
                                    }
                                });
                                buttons.get(3).setOnMousePressed(ev -> {
                                    try {
                                        toServer.writeUTF("G");
                                        pane.getChildren().clear();
                                        ImageView gif = new ImageView(new Image(new FileInputStream("src/CSS/project3/loading.gif")));
                                        gif.setFitWidth(420);
                                        gif.setFitHeight(320);
                                        pane.getChildren().add(gif);
                                    } catch (IOException ex) {
                                    }
                                });
                                testpane.getChildren().addAll(buttons.get(0), buttons.get(1), buttons.get(2), buttons.get(3));

                                panes.add(testpane);
                            } else {
                                Pane fieldpane = new Pane();
                                TextField field = new TextField();
                                final String[] fe = {""};
                                field.setOnKeyTyped(e->{
                                    fe[0] = field.getText();
                                    try {
                                        toServer.writeUTF(fe[0]);
                                    } catch (IOException ex) {}
                                });
                                field.setTranslateX(130);
                                field.setTranslateY(150);
                                fieldpane.getChildren().addAll(field);
                                panes.add(fieldpane);

                            }
                        }

                        pane.getChildren().clear();
                        pane.getChildren().add(panes.get(0));
                        stage.setScene(new Scene(pane, 420, 320));
                    }
                }else {
                    Text text = new Text("Your PIN was wrong!\nWrite again ;)");
                    text.setStyle("-fx-font-family:Montserrat , Noto Sans Arabic, Helvetica Neue, Helvetica, Arial, sans-serif;" +
                            "-fx-font-weight: bold; -fx-fill: red;-fx-font-size: 16px;-fx-translate-x: 150;-fx-translate-y: 250 ");
                    Platform.runLater(()->{
                        FadeTransition transition = new FadeTransition(Duration.millis(1000) , text);
                        transition.setFromValue(1.0);
                        transition.setToValue(0.0);
                        transition.setCycleCount(5);
                        transition.setAutoReverse(true);
                        transition.play();
                        pane.getChildren().add(text);
                    });
                }
            } catch (Exception ex) {
            }
        });
        pane.getChildren().addAll(username , pin , start);
        return pane;
    }
    private boolean check(boolean from, DataInputStream fromServer) throws IOException {
        while (!from){
            from = fromServer.readBoolean();
        }
        return from;
    }
    public void play() {
        animation.play();
    }
    protected void run() throws InterruptedException, IOException {
        --secondd;
        if (secondd < 0) {
            animation.stop();
            if(page > 2 && page < 3)
                secondd = 14;
            else
                secondd = 15;
            pane.getChildren().clear();
            page++;
            if(page == size){
                pane.getChildren().clear();
                ImageView view = new ImageView(new Image(new FileInputStream("src/kahoot1.jpg")));
                view.setFitWidth(300);
                view.setFitHeight(200);
                view.setX(60);
                view.setY(60);
                pane.getChildren().add(view);
                animation.stop();
            }else {
                pane.getChildren().add(panes.get(page));
                play();
            }
        }
    }
    public String get() {
        return String.valueOf(secondd);
    }
}

