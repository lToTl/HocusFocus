package HocusFocus;

import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class Setup2Controller implements Initializable {
    @FXML
    private GridPane buttonGridPane;

    private Stage stage;
    private int veerud = 5;
    private List<String[]> programmid;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            generateButtons(Main.uuedProgrammid);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void generateButtons(List<String> programmid) throws FileNotFoundException {
//        int read = (int) Math.ceil(programmid.size()/(double) veerud);
        int veeruIndeks = 0;
        int reaIndeks = 0;
        int i = 0;
        double aknaLaius = Window.getWindows().getFirst().getWidth()*0.93;
        double suurus = aknaLaius/veerud;
        SimpleDoubleProperty pildiSuurus = new SimpleDoubleProperty(Window.getWindows().getFirst().getWidth()/veerud*0.8);
        Button edasi = new Button();
        Tooltip silt = new Tooltip("Lõpeta");
        silt.setShowDelay(Duration.millis(100));
        edasi.setTooltip(silt);
        edasi.setPrefSize(suurus, suurus);
        edasi.setOnAction(event -> {
            try {
                finishSetup(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        ImageView pilt = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("tagasi.png"))));
        pilt.fitHeightProperty().bind(pildiSuurus);
        pilt.fitWidthProperty().bind(pildiSuurus);
        edasi.setGraphic(pilt);
        edasi.setRotate(180);
        buttonGridPane.add(edasi,0,0);
        for (String programm : programmid) {
            String[] jupid = programm.split(".exe",2);
            String nimi = jupid[0].substring(jupid[0].lastIndexOf("\\")+1);
            if (jupid.length > 1){
                nimi = nimi + jupid[1];
            }
            String aadress = jupid[0] + ".exe";

            ToggleButton button = new ToggleButton();
            button.setId("setupButton");


            ImageView imageView = Abi.exeToImage(aadress);
            imageView.fitHeightProperty().bind(pildiSuurus);
            imageView.fitWidthProperty().bind(pildiSuurus);
            button.setGraphic(imageView);

            Tooltip tooltip = new Tooltip(nimi);
            tooltip.setShowDelay(Duration.millis(100));
            button.setTooltip(tooltip);
            button.setPrefSize(suurus, suurus);


            i++;
            veeruIndeks = i % veerud;
            reaIndeks = i / veerud;
            buttonGridPane.add(button, veeruIndeks, reaIndeks);

        }
    }
    public void switchToMain(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainMenu.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
        stage.getScene().getStylesheets().add(getClass().getResource("application.css").toExternalForm());

        ProgressBar progressBar = (ProgressBar)stage.getScene().lookup("#progress_bar");
        javafx.scene.control.Label skoor = ((Label)stage.getScene().lookup("#skoor"));

        stage.show();
        Main.taskmonitor = new TaskMonitor();
        Main.updater(progressBar, skoor);
    }
    public void finishSetup(ActionEvent event) throws IOException {
        List<String[]> programmid = new ArrayList<>();
        String[] programm;
        List<Node> buttons = buttonGridPane.getChildren();
        int i = 0;
        for (Node button : buttons) {
            programm = new String[3];
            if (button instanceof ToggleButton){
                String rida = Main.uuedProgrammid.get(i);
                String[] jupid = rida.split(".exe");
                String nimi = jupid[0].substring(jupid[0].lastIndexOf("\\")+1);
                programm[0] = nimi;
                programm[1] = rida;
                if (((ToggleButton)button).isSelected()){
                    programm[2] = "1";
                    System.out.println("1 " + ((ToggleButton)button).getTooltip().getText());
                }else{
                    programm[2] = "0";
                    System.out.println("0 " + ((ToggleButton)button).getTooltip().getText());

                }
                i++;
                programmid.add(programm);
            }
        }

        Main.programmid = programmid;


        FileWriter fileWriter = new FileWriter("rakendused.txt");
        for (String[] prog : programmid) {
            fileWriter.write(prog[0] + ".exe;" + prog[1] + ";" + prog[2] + "\n");
        }
        fileWriter.close();
        switchToMain(event);
//        Platform.runLater(new Runnable() {
//            public void run() {
//                Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                alert.setTitle("Algseadistus");
//                alert.setHeaderText("Seadistamise lõpetamiseks tuleb äpp taaskäivita");
//                alert.setContentText("Vajutads OK programm sulgub. Palun ava uuesti.");
//                alert.showAndWait();
//                Main.exit();
//            }
//        });
////
    }

}
