
package View;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class FxmlLoader {

    private Pane view;

    public Pane getPage(String fileName){
        try{
            URL fileUrl = Main.class.getResource("/View/" + fileName + ".fxml");
            if (fileUrl == null){
                throw  new java.io.FileNotFoundException("Fxml can't be found");

            }

            view = new FXMLLoader().load(fileUrl);
        }
        catch (Exception e)
        {
            System.out.println("No page " + fileName + " please check FxmlLoader.");
        }
        return view;

    }
////

}

