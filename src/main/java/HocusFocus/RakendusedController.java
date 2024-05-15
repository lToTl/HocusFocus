//https://github.com/xdsswar/fxml-tutorial/tree/master

package HocusFocus;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class RakendusedController implements Initializable {
    @FXML
    private GridPane buttonGridPane;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generateButtons(Main.programmid);
    }

    public void generateButtons(List<String[]> programmid){
        int veerud = 5;
        int read = (int) Math.ceil(programmid.size()/(double) veerud);
        int veeruIndeks = 0;
        int reaIndeks = 0;
        int i = 0;

        for (String[] programm : programmid) {
            int suurus = 100;
            Button button = new Button(programm[0]);
//            System.out.println(programm[1] +" "+ programm[1].length() + " " + programm[1].indexOf(".exe") );
            File file = new File(programm[1]);
            if (programm[1].length() - programm[1].indexOf(".exe") == 4 && file.exists()){
            button.setGraphic(exeToImage(programm[1]));
            button.setPrefSize(suurus, suurus);
            buttonGridPane.add(button, veeruIndeks, reaIndeks);
            i++;
            veeruIndeks = i % veerud;
            reaIndeks = i / veerud;
            }
        }
    }

    public static ImageView exeToImage (String aadress){
        ImageIcon icon = (ImageIcon) FileSystemView.getFileSystemView()
                .getSystemIcon(new File(aadress));
        BufferedImage bufferedImage = new BufferedImage(
                icon.getIconWidth(),
                icon.getIconHeight(),
                BufferedImage.TYPE_INT_RGB);

        Graphics g = bufferedImage.createGraphics();
        icon.paintIcon(null, g, 0,0);
        g.dispose();

        return new ImageView(SwingFXUtils.toFXImage(bufferedImage, null));
    }
}
