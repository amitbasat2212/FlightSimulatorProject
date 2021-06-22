package View;

import Model.XmlWrite;
import Model.property;
import ModelView.ViewModel;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Timer;

import javafx.scene.chart.XYChart;
import test.Circle;
import test.Point;
import test.TimeSeries;


public class MainWindowController  {

    public ViewModel viewModel;

    @FXML
    Charlist ChartList;
    @FXML
    View.Clocks Clocks;
    @FXML
    JoyStick Joystick;
    @FXML
    Player player;

    popupcontroller popup;

    public StringProperty path;

    public IntegerProperty timestep;

    public Timer ts;
    public int index2;
    public int index;


    //constructor that create and intalize all the four part the includes int the main window controller
    public MainWindowController() {
        path = new SimpleStringProperty();
        index2=0;
        index=0;
    }

    //fubction for choosing the file itself
    public String  chooseFile(){
        FileChooser fileccsv = new FileChooser();
        File file = fileccsv.showOpenDialog(null);
        String pathtest = file.toURI().toString();
        Path p3 = Paths.get(URI.create(pathtest));
        pathtest = p3.getFileName().toString();
        return pathtest;
    }

    //function to choose specificly the csv file and intalize the player
    public void ChooseFileCsv() {

        String pathtocompare = chooseFile();
        path.setValue(pathtocompare);
        if (pathtocompare.contains("csv")) {
            viewModel.CreateTimeSeries(path.getValue().toString());
            player.playerController.ScrollFlight.setMin(1);
            player.playerController.ScrollFlight.setMax(this.viewModel.ts.getNumLine()-1);
            loadData();


        } else {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("the csv is not correct or not been upload");
            a.show();
        }
        players();





    }

    //function to choose specificly the xml file for the properties
    public void ChooseFilexml() {
        String pathtocompare = chooseFile();
        path.setValue(pathtocompare);
        if (pathtocompare.contains("xml")) {
            viewModel.CreateProperty(path.getValue().toString());
        }
        else {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("the xml is not correct or not been upload");
            a.show();
        }



    }

    //initialize the view model
    public void init(ViewModel vm) {
        this.viewModel = vm;


        XmlWrite XML =new XmlWrite();
        XML.deserializeFromXML("Properties.xml");

        TimerClockBind();
        clockbind();
        PlayerBind();
        ChartList.charListController.linechart.getData().add(viewModel.series);
        ChartList.charListController.linechart2.getData().add(viewModel.seriesseconed);
        ChartList.charListController.linechart3.getData().addAll(viewModel.seriesforth, viewModel.seriesthird);

        ChartList.charListController.linechart3.setLegendVisible(false);

        ChartList.charListController.linechart.getXAxis().setStyle("-fx-border-color: Pink transparent transparent; -fx-border-width:2");
        ChartList.charListController.linechart2.getXAxis().setStyle("-fx-border-color: Pink transparent transparent; -fx-border-width:2");
        ChartList.charListController.linechart3.getXAxis().setStyle("-fx-border-color: Pink transparent transparent; -fx-border-width:2");


        Joystick.joyStickController.paint();

        viewModel.service2.addListener(new ListChangeListener<XYChart.Data<String, Number>>() {
            @Override
            public void onChanged(Change<? extends XYChart.Data<String, Number>> change) {
                XYChart.Series<String, Number> seriesnew =new XYChart.Series<>();
                seriesnew.getData().add(new XYChart.Data<String, Number>(String.valueOf(index2),viewModel.seriesfifth.getData().get(index2).getYValue()));
                index2++;

                ChartList.charListController.linechart3.getData().add(seriesnew);

                ChartList.charListController.linechart3.setLegendVisible(false);

            }
        });

        viewModel.radios.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                Point pointCircle = new Point(viewModel.circle.x,viewModel.circle.y);
                Circle circleWlzl = new Circle(pointCircle, viewModel.radios.getValue());

                ChartList.charListController.paint(circleWlzl);

                //ChartList.charListController.paint();
            }
        });







        Joystick.joyStickController.aileron.addListener((o, ov, nv) -> this.Joystick.joyStickController.paint()); // x axis
        Joystick.joyStickController.elevator.addListener((o, ov, nv) -> this.Joystick.joyStickController.paint()); // y axis

        Joystick.joyStickController.rudder.valueProperty().addListener((o, ov, nv) ->
                Joystick.joyStickController.rudder.setValue(nv.doubleValue()));
        Joystick.joyStickController.throttle.valueProperty().addListener((o, ov, nv) ->
                Joystick.joyStickController.throttle.setValue(nv.doubleValue()));

        Joystickbind();


        ChartList.charListController.listview.getSelectionModel().selectedItemProperty().addListener(((observableValue, s, t1) -> {


            if(index!=0){
                viewModel.time.stop();
            }

            index++;
            index2 = 0;
            viewModel.timeSeriesRow=0;

            viewModel.series.getData().clear();
            viewModel.seriesseconed.getData().clear();
            viewModel.seriesthird.getData().clear();
            viewModel.seriesforth.getData().clear();
            viewModel.seriesfifth.getData().clear();

            ChartList.charListController.series.getData().clear();
            ChartList.charListController.series=new XYChart.Series<>();
            // viewModel.series=new XYChart.Series<>();
            viewModel.index=0;


            String selectedItem = ChartList.charListController.listview.getSelectionModel().getSelectedItem();

            viewModel.getfeture(selectedItem);

            ///------------new coloring part------------///


            Node line = viewModel.seriesseconed.getNode().lookup(".chart-series-line");

            //   Node point = viewModel.seriesseconed.getNode().lookup(".chart-series-line");

            Color color = Color.BLACK; // or any other color

            String rgb = String.format("%d, %d, %d",
                    (int) (color.getRed() * 255),
                    (int) (color.getGreen() * 255),
                    (int) (color.getBlue() * 255));

            line.setStyle("-fx-stroke: rgba(" + rgb + ", 1.0);");

            ///------------new coloring part------------///


        }));

    }




    //bind the slider from the player itself
    private void PlayerBind() {
        player.playerController.ScrollFlight.valueProperty().bindBidirectional(this.viewModel.TimeLine);
        player.playerController.PlaySpeed.textProperty().addListener(((old, oldValue, newValue)-> {
            this.viewModel.setPlaySpeed(Double.parseDouble(newValue.toString()));
        }));

    }

    //bind the timer itself with a stringproperty from the view model to the model
    public void TimerClockBind() {
        player.playerController.SecondsTimer.textProperty().bind(viewModel.ClockTimerValues.get("Seconds").asString());
        player.playerController.MinutesTimer.textProperty().bind(viewModel.ClockTimerValues.get("Minutes").asString());
        player.playerController.HoursTimer.textProperty().bind(viewModel.ClockTimerValues.get("Hours").asString());

    }


    //load the pop up to choose the class of the anomaly detector from him

    public void loadData() {
        ChartList.charListController.getFetures().setAll(viewModel.fetures);
        ChartList.charListController.listview.setItems( ChartList.charListController.getFetures());
    }



    //interline the function from the view model to the view itself and run them
    public void players(){
        viewModel.Players();
        player.playerController.onPlay =viewModel.Play;
        player.playerController.onPause = viewModel.Pause;
        player.playerController.onStop = viewModel.Stop;
        player.playerController.onRewind =viewModel.Rewind;
        player.playerController.onFastRewind = viewModel.FastRewind;
        player.playerController.onForward = viewModel.Forward;
        player.playerController.onFastForward = viewModel.FastForward;

    }

    //load the anomaly detector from the popup that we got and put in the viewmodel
    public void ClassLoadPop(String path){
        if(path.equals("hibride") || path.equals("Zscore") || path.equals("SimpleAnomalyDetector")) {
            popup.mc.viewModel.CreateTimeSeriesAnomalyDetector(path);
        }else {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("the class is not correct or not been upload");
            a.show();

        }
    }

    //load the pop up to choose the class of the anomaly detector from him
    public void Classload(javafx.event.ActionEvent event) {
        try {
            Button btn = (Button) event.getSource();
            FXMLLoader loader1 = new FXMLLoader(getClass().getResource("popupClass.fxml"));
            Parent r = loader1.load();
            popup = (popupcontroller) loader1.getController();
            Scene secene = new Scene(r);
            Stage stage = new Stage();
            popup.mc=this;
            stage.setTitle("Algorithm choosing ");
            stage.setScene(secene);
            stage.show();
        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    // Setting the time series by binding from the view to model view
    public void Joystickbind()
    {
        // connect the view model by using view model object and joystick controller object using binding
        Joystick.joyStickController.aileron.bind(viewModel.DisplaVaribales.get("aileron"));
        Joystick.joyStickController.elevator.bind(viewModel.DisplaVaribales.get("elevator"));
        Joystick.joyStickController.rudder.valueProperty().bind(viewModel.DisplaVaribales.get("rudder"));
        Joystick.joyStickController.throttle.valueProperty().bind(viewModel.DisplaVaribales.get("throttle"));
    }


    //bind the clocks with the informetion that will fome from the view model to the view from the timeseries
    public void clockbind()
    {
        Clocks.clocksController.altimeter.textProperty().bind(viewModel.DisplaVaribales.get("altimeter_pressure-alt-ft").asString());
        Clocks.clocksController.roll.textProperty().bind(viewModel.DisplaVaribales.get("roll-deg").asString());
        Clocks.clocksController.pitch.textProperty().bind(viewModel.DisplaVaribales.get("pitch-deg").asString());
        Clocks.clocksController.yaw.textProperty().bind(viewModel.DisplaVaribales.get("heading-deg").asString());
        Clocks.clocksController.airspeed.textProperty().bind(viewModel.DisplaVaribales.get("airspeed-kt").asString());
        Clocks.clocksController.direction.textProperty().bind(viewModel.DisplaVaribales.get("gps_indicated-vertical-speed").asString());
    }






}