package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

import java.awt.*;
import java.awt.event.InputEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class Controller implements Initializable {
    private double mouseX;
    private double mouseY;
    private ArrayList<Coordinates> coordinates = new ArrayList<Coordinates>();

    @FXML
    private Button stop_btn;
    @FXML
    private Button start_btn;
    @FXML
    private Button clear_list_btn;
    @FXML
    private TextField move_time_inp;
    @FXML
    private TextField loop_time_inp;
    @FXML
    private CheckBox hold_click;
    Timeline timeline, tl;
    Iterator<Coordinates> IT;

    public void initialize(URL location, ResourceBundle resources) {
        stop_btn.setDisable(true);
        clear_list_btn.setDisable(true);
        Parent root = start_btn.getParent();

        root.setOnKeyPressed(event -> {
            if (event.isControlDown() && event.isShiftDown() && event.getCode() == KeyCode.X) {
                mouseX = MouseInfo.getPointerInfo().getLocation().getX();
                mouseY = MouseInfo.getPointerInfo().getLocation().getY();
                coordinates.add(new Coordinates(mouseX, mouseY));
            }
        });
        root.setOnKeyReleased(event -> {
            if (event.isControlDown() && event.isShiftDown() && event.getCode() == KeyCode.X) {
                start_btn.setDisable(false);
                clear_list_btn.setDisable(false);
            }
        });


    }


    public void startAction() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);

        if (!move_time_inp.getText().isEmpty() && !loop_time_inp.getText().isEmpty() && !coordinates.isEmpty()) {
            stop_btn.setDisable(false);
            start_btn.setDisable(true);
            clear_list_btn.setDisable(true);
            move_time_inp.setDisable(true);
            loop_time_inp.setDisable(true);
            int move_tm = Integer.parseInt(move_time_inp.getText());
            int loop_tm = Integer.parseInt(loop_time_inp.getText());


            //cursor animation
            IT = coordinates.iterator();
            tl = new Timeline(new KeyFrame(
                    Duration.ZERO,
                    ae -> {
                        Robot robot = null;
                        if (IT.hasNext()) {
                            try {
                                robot = new Robot();
                                Coordinates coor = IT.next();
                                robot.mouseMove((int) coor.getMouseX(), (int) coor.getMouseY());
                                if (hold_click.isSelected()) {  robot.mousePress(InputEvent.BUTTON1_MASK); }

                            } catch (AWTException e) {
                                e.printStackTrace();
                            }
                        }
                    }), new KeyFrame(Duration.millis(move_tm)));
            tl.setCycleCount(coordinates.size());


            //begin animation
            timeline = new Timeline(new KeyFrame(
                    Duration.millis(loop_tm),
                    ae_c ->
                    {
                        IT = coordinates.iterator();
                        tl.play();
                        if (timeline == null) {
                            tl.stop();
                        }
                    }));
            timeline.setCycleCount(1);
            timeline.play();
            tl.setOnFinished(event -> {
                timeline.play();
            });



        } else if (move_time_inp.getText().isEmpty() && loop_time_inp.getText().isEmpty()) {

            alert.setContentText("Fill inputs!!!");
            alert.showAndWait();

        } else if (coordinates.isEmpty()) {
            alert.setContentText("Coordinates is not available!!!");
            alert.showAndWait();
        }

    }

    public void stopAction() {
        stop_btn.setDisable(true);
        start_btn.setDisable(false);
        clear_list_btn.setDisable(false);
        move_time_inp.setDisable(false);
        loop_time_inp.setDisable(false);
        timeline.stop();
        tl = null;
    }

    public void clearList() {
        clear_list_btn.setDisable(true);
        coordinates.clear();
    }


}



