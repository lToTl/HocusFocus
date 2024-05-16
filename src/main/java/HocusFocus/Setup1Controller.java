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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class Setup1Controller implements Initializable {
    @FXML
    private GridPane buttonGridPane;

    private Stage stage;
    private List<String> programmid;
    private int veerud = 5;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            this.programmid = new KaardistaProgrammid().getProgrammid();
            Main.uuedProgrammid = new ArrayList<>();
            generateButtons(programmid);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void generateButtons(List<String> programmid) throws FileNotFoundException {
//        int read = (int) Math.ceil(programmid.size()/(double) veerud);
        int veeruIndeks = 0;
        int reaIndeks = 0;
        int i = 0;
        double aknaLaius = Window.getWindows().getFirst().getWidth()*0.93;
        double suurus = aknaLaius/veerud;
        SimpleDoubleProperty pildiSuurus = new SimpleDoubleProperty(Window.getWindows().getFirst().getWidth()/veerud*0.8);
        Button edasi = new Button();
        Tooltip silt = new Tooltip("Edasi");
        silt.setShowDelay(Duration.millis(100));
        edasi.setTooltip(silt);
        edasi.setPrefSize(suurus, suurus);
        edasi.setOnAction(event -> {
            try {
                switchToSetup2(event);
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
            String[] jupid = programm.split("\\.exe",2);
            String nimi = jupid[0].substring(jupid[0].lastIndexOf("\\")+1);
            if (jupid.length > 1){
                nimi = nimi + jupid[1];
            }
            String aadress = jupid[0] + ".exe";

            ToggleButton button = new ToggleButton();

//            System.out.println(aadress);

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

    private void switchToSetup2(ActionEvent event) throws IOException {
        List<Integer> valitud = new ArrayList<>();
        List<Node> buttons = buttonGridPane.getChildren();
        for (Node button : buttons) {
            if (button instanceof ToggleButton && ((ToggleButton)button).isSelected()){
                int veerg = GridPane.getColumnIndex(button);
                int rida = GridPane.getRowIndex(button);
                int indeks = rida * veerud + veerg;
                valitud.add(indeks-1);
//                System.out.println(button.getTypeSelector() + " "+ indeks +" " + veerg + " " + rida);
            }
        }
        int i = 0;
        for (String programm : programmid) {
            if (valitud.contains(i)){
                Main.addProgramm(programm);
                System.out.println(i+" " + programm);
            }

            i++;
        }

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("setup2.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
        stage.show();
        showSetup2Instruction();
    }

    private void showSetup2Instruction() {
        Platform.runLater(new Runnable() {
            public void run() {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Algseadistus");
                alert.setHeaderText("Järgnevalt on kuvatud kõik valitud programmid");
                alert.setContentText("Vali programmide seast produktiivsed. Jätkamiseks vajuta esimese rea esimest nuppu");
                alert.showAndWait();
            }
        });
    }


}
