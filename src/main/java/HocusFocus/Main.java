package HocusFocus;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class Main extends Application {
    public static TaskMonitor taskmonitor;
    public static List<String[]> programmid;

    public static void updater(ProgressBar progressBar, Label skoor){
        Thread updater_thread = new Thread(() -> {
            while (true) {
                if (Main.taskmonitor != null) {
                    try {
                        Thread.sleep(100);
                        Platform.runLater(() -> {
                            progressBar.setProgress(taskmonitor.getProgress());
                            skoor.textProperty().setValue(taskmonitor.getSkoorString());
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        updater_thread.setDaemon(true);
        updater_thread.start();

    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        File f = new File("rakendused.txt"); //kontrolli rakendused.txt olemasolu ja vajadusel loo see
//        if (!f.isFile()) { // kui rakendused.txt on puudu, siis tee setup ja loo rakendused.txt
//            Setup_GUI.LooRakendusedTXT();
//        }
        if(f.isFile()) {
            programmid = Abi.loeFailListi("rakendused.txt");
            taskmonitor = new TaskMonitor();
        }


        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainMenu.fxml")));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.getScene().getStylesheets().add(getClass().getResource("application.css").toExternalForm());
//            ((ProgressBar)stage.getScene().lookup("#progress_bar")).progressProperty().bind(new SimpleDoubleProperty(Main.taskmonitor.getProgress()).asObject());
//            ((Label)stage.getScene().lookup("#skoor")).textProperty().bind(new SimpleStringProperty(Main.taskmonitor.getSkoorString()));
//            ((ProgressBar)stage.getScene().lookup("#progress_bar")).progressProperty();
            ProgressBar progressBar = ((ProgressBar)stage.getScene().lookup("#progress_bar"));
            Label skoor = ((Label)stage.getScene().lookup("#skoor"));
//            ((Label)stage.getScene().lookup("#skoor")).textProperty().bind(Main.taskmonitor.skoorS);
            stage.show();
            updater(progressBar,skoor);
            if (!f.isFile()){
                stage.getScene().lookup("#bt_taskmonitor").setDisable(true);
                stage.getScene().lookup("#bt_rakendused").setDisable(true);
                stage.getScene().lookup("#bt_lucky").setDisable(true);

            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }
    public void startTaskMonitor(TaskMonitor taskMonitor){

    }
    public void stopTaskMonitor(TaskMonitor taskMonitor){

    }
    public static void exit(){

        Platform.exit();
    }
}
