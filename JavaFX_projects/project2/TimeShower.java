package CSS.project2;

import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.scene.text.Text;
import javafx.geometry.Insets;
public class TimeShower extends StackPane {
        private int minute;
        private int second;
        private Text text = new Text();
        private Timeline animation;

        public TimeShower() {
            setPadding(new Insets(5, 15, 5, 15));
            clear();
            text.setStyle("-fx-fill: white; -fx-font-size: 15px;-fx-font-family: Arial");
            getChildren().add(text);
            animation = new Timeline(new KeyFrame(Duration.millis(1000), e -> run()));
            animation.setCycleCount(Timeline.INDEFINITE);
        }
        public void play() {
            animation.play();
        }
        public void pause() {
            animation.pause();
        }
        protected void run() {
            if (second == 59)
                minute = minute + 1;

            second = second < 59 ? second + 1 : 0;
            text.setText(get());
        }
        public void clear() {
            minute = 0;
            second = 0;
            text.setText(get());
        }

        public String get() {
            return change(minute) + ":" + change(second);
        }
        private String change(int n) {
            return n < 10 ? "0" + n : n + "";
        }

}
