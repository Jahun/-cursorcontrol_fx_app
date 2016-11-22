package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private boolean stopped = false;
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
            Iterator<Coordinates> IT = coordinates.iterator();
            int move_tm = Integer.parseInt(move_time_inp.getText());
            int loop_tm = Integer.parseInt(loop_time_inp.getText());
            System.out.println();
            Robot robot = null;
            try {
                robot = new Robot();

                while (stopped == false) {
                    while (IT.hasNext()) {
                        Coordinates coor = IT.next();
                        robot.mouseMove((int) coor.getMouseX(), (int) coor.getMouseY());
//                    robot.mousePress(InputEvent.BUTTON1_MASK);
                        Thread.sleep(move_tm);
                    }
                    Thread.sleep(loop_tm);
                }


            } catch (AWTException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else if(move_time_inp.getText().isEmpty() && loop_time_inp.getText().isEmpty()){

            alert.setContentText("Fill inputs!!!");
            alert.showAndWait();

        }else if(coordinates.isEmpty()){
            alert.setContentText("Coordinates is not available!!!");
            alert.showAndWait();
        }
    }

    public void stopAction() {
        stop_btn.setDisable(true);
        start_btn.setDisable(false);
        clear_list_btn.setDisable(false);
        stopped = true;
    }

    public void clearList(){
        clear_list_btn.setDisable(true);
        coordinates.clear();
    }


}



