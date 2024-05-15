//https://github.com/xdsswar/fxml-tutorial/tree/master
//https://stackoverflow.com/questions/32776970/calling-setonaction-from-another-class
//https://github.com/MrMarnic/JIconExtractReloaded
//https://www.iconpacks.net/free-icon/arrow-back-3783.html

package HocusFocus;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import me.marnic.jiconextract2.*;


public class RakendusedController implements Initializable {
    @FXML
    private GridPane buttonGridPane;

    private Stage stage;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            generateButtons(Main.programmid);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void generateButtons(List<String[]> programmid) throws FileNotFoundException {
        int veerud = 5;
//        int read = (int) Math.ceil(programmid.size()/(double) veerud);
        int veeruIndeks = 1;
        int reaIndeks = 0;
        int i = 0;
        double aknaLaius = Window.getWindows().getFirst().getWidth()*0.93;
        double suurus = aknaLaius/veerud;
        SimpleDoubleProperty pildiSuurus = new SimpleDoubleProperty(Window.getWindows().getFirst().getWidth()/veerud*0.8);
        Button tagasi = new Button();
        Tooltip silt = new Tooltip("Tagasi");
        silt.setShowDelay(Duration.millis(100));
        tagasi.setTooltip(silt);
        tagasi.setPrefSize(suurus, suurus);
        tagasi.setOnAction(event -> {
            try {
                switchToMain(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        ImageView pilt = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("tagasi.png"))));
        pilt.fitHeightProperty().bind(pildiSuurus);
        pilt.fitWidthProperty().bind(pildiSuurus);
        tagasi.setGraphic(pilt);
        buttonGridPane.add(tagasi,0,0);
        for (String[] programm : programmid) {
            Button button = new Button();
            File file = new File(programm[1]);
            if (programm[1].length() - programm[1].indexOf(".exe") == 4 && file.exists()){
                ImageView imageView = exeToImage(programm[1]);
                imageView.fitHeightProperty().bind(pildiSuurus);
                imageView.fitWidthProperty().bind(pildiSuurus);
                button.setGraphic(imageView);
                Tooltip tooltip = new Tooltip(programm[0]);
                tooltip.setShowDelay(Duration.millis(100));
                button.setTooltip(tooltip);
                button.setPrefSize(suurus, suurus);
                button.setOnAction(event -> Abi.käivita(programm[1]));

                buttonGridPane.add(button, veeruIndeks, reaIndeks);
                i++;
                veeruIndeks = i % veerud;
                reaIndeks = i / veerud;
            }
        }
    }

//    public static ImageView exeToImage (String aadress){
//        ImageIcon icon = (ImageIcon) FileSystemView.getFileSystemView()
//                .getSystemIcon(new File(aadress));
//        BufferedImage bufferedImage = new BufferedImage(
//                icon.getIconWidth(),
//                icon.getIconHeight(),
//                BufferedImage.TYPE_INT_RGB);
//
//        Graphics g = bufferedImage.createGraphics();
//        icon.paintIcon(null, g, 0,0);
//        g.dispose();
//
//        return new ImageView(SwingFXUtils.toFXImage(bufferedImage, null));
//    }

    public static ImageView exeToImage (String aadress){
        BufferedImage bufferedImage = JIconExtract.getIconForFile(128,128,aadress);
        return new ImageView(SwingFXUtils.toFXImage(bufferedImage, null));
    }

    public void switchToMain(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainMenu.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
        stage.getScene().getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        if (Main.taskmonitor.isRunning()){
            ((ToggleButton)stage.getScene().lookup("#bt_taskmonitor")).setSelected(true);
            stage.getScene().lookup("#bt_setup").setDisable(true);
        }
        ProgressBar progressBar = (ProgressBar)stage.getScene().lookup("#progress_bar");
        javafx.scene.control.Label skoor = ((Label)stage.getScene().lookup("#skoor"));


//        ((ProgressBar)stage.getScene().lookup("#progress_bar")).progressProperty().bind(Main.taskmonitor.skoorP);
//        ((Label)stage.getScene().lookup("#skoor")).textProperty().bind(Main.taskmonitor.skoorS);

        stage.show();
        Main.updater(progressBar, skoor);
    }

}
