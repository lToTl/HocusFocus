//https://www.youtube.com/watch?v=hcM-R-YOKkQ
//https://stackoverflow.com/questions/38973133/javafx-resizing-to-maximized-each-time-scene-changes
//https://stackoverflow.com/questions/14413040/converting-integer-to-observablevalueinteger-in-javafx
//https://stackoverflow.com/questions/11662857/javafx-2-1-messagebox/36137669#36137669
package HocusFocus;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class SceneController {
    private Stage stage;


    public void switchToSetup(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("setup.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
        stage.show();
    }

    public void switchToMain(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainMenu.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
        stage.getScene().getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        if (Main.taskmonitor != null && Main.taskmonitor.isRunning()){
            ((ToggleButton)stage.getScene().lookup("#bt_taskmonitor")).setSelected(true);
            stage.getScene().lookup("#bt_setup").setDisable(true);
        }
        ProgressBar progressBar = (ProgressBar)stage.getScene().lookup("#progress_bar");
        Label skoor = ((Label)stage.getScene().lookup("#skoor"));
        File f = new File("rakendused.txt");
        if (!f.isFile()){
            stage.getScene().lookup("#bt_taskmonitor").setDisable(true);
            stage.getScene().lookup("#bt_rakendused").setDisable(true);
            stage.getScene().lookup("#bt_lucky").setDisable(true);
        }


//        ((ProgressBar)stage.getScene().lookup("#progress_bar")).progressProperty().bind(Main.taskmonitor.skoorP);
//        ((Label)stage.getScene().lookup("#skoor")).textProperty().bind(Main.taskmonitor.skoorS);

        stage.show();
        Main.updater(progressBar, skoor);
    }

    public void switchToRakendused(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("rakendused.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
        stage.show();
    }
    public void toggleTaskMonitor(ActionEvent event){
        ToggleButton nupp = (ToggleButton) event.getSource();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        if (nupp.isSelected()){
            Thread tm = new Thread(Main.taskmonitor);
            tm.start();
            stage.getScene().lookup("#bt_setup").setDisable(true);
//            ((ProgressBar)stage.getScene().lookup("#progress_bar")).progressProperty().bind(Main.taskmonitor.skoorP);
        }
        if (!nupp.isSelected()) {
            Main.taskmonitor.stop();
            stage.getScene().lookup("#bt_setup").setDisable(false);
        }
    }

    public void openRandom(ActionEvent event){
        if (Main.taskmonitor.getSkoor() < 1) {
            showAlert();
        } else {
            Abi.käivitaSuvaline(Main.programmid);
        }
    }

    private void showAlert() {
        Platform.runLater(new Runnable() {
            public void run() {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Teade");
                alert.setHeaderText("Selle toimingu jaoks on peab positiivne skoor olema.");
                alert.setContentText("Skoor hetkel: " + Main.taskmonitor.getSkoor());
                alert.showAndWait();
            }
        });
    }



    //phind.com

    public void exit(){
        if (Main.taskmonitor != null && Main.taskmonitor.isRunning()) Main.taskmonitor.stop();
        Main.exit();
    }
}
