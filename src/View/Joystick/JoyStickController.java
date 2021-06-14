package View.Joystick;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;

public class JoyStickController extends BorderPane {

    @FXML
    public Circle innerCircle;
    @FXML
    public Circle outerCircle;
    @FXML
    public Slider throttle;
    @FXML
    public Slider rudder;

    public DoubleProperty aileron, elevator;

    // Initialize each part of axis x and axis y
    private double jx, jy;
    private double Ax, Ay;

    // Constructor to the joystick
    public JoyStickController() {
        Ax = 0;
        Ay = 0;

    }

}