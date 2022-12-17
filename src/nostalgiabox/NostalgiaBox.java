//////////////////////////////////////
//      Ashley Butela presents      //
//       A CIT-244 Production       //
//        December 16th, 2022       //
//////////////////////////////////////

package nostalgiabox;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class NostalgiaBox extends Application {    

    @Override 
    public void start(Stage stage) throws InterruptedException, IOException{
            Image splash = new Image("images/splash.png",1280,720,false,false);
            ImageView splashView = new ImageView(splash);
            Pane test = new Pane(splashView);
            Scene scene = new Scene(test, 1280,720);
            
            stage.setTitle("NostalgiaBox - Cozy TV Time at Grandma's");
            stage.setScene(scene);
            stage.show();


            FadeTransition ft = new FadeTransition(Duration.millis(5000), splashView);
            ft.setFromValue(0.0);
            ft.setToValue(1.0);
            ft.setOnFinished(e -> {
                try {
                    stage.setScene(createScene(getPane("startMenu.fxml")));
                } catch (IOException ex) {
                    Logger.getLogger(NostalgiaBox.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            ft.play();
    }
    
    public Pane getPane(String fxmlFile) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        
        Pane mainPane = loader.load(getClass().getResource(fxmlFile));
        
        return mainPane;
    }
    
    private Scene createScene(Pane mainPane) {
        Scene scene = new Scene(mainPane);
        
        return scene;
    }
}

