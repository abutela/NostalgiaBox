//////////////////////////////////////
//      Ashley Butela presents      //
//       A CIT-244 Production       //
//        December 16th, 2022       //
//////////////////////////////////////

package nostalgiabox;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

/** The NostalgiaBoxController class controls all happenings in NostalgiaBox,
 * after the opening animation.
 * 
 */
public class NostalgiaBoxController{
    
    @FXML private Button btnStart;
    
    @FXML private RadioButton rbSeventies;
    @FXML private RadioButton rbEighties;
    @FXML private RadioButton rbNineties;
    
    @FXML private RadioButton rbNoXmas;
    @FXML private RadioButton rbYesXmas;
    @FXML private RadioButton rbOnlyXmas;
    
    @FXML private RadioButton rbMorning;
    @FXML private RadioButton rbNoon;
    @FXML private RadioButton rbNight;
    
    @FXML private RadioButton rbCozy;
    @FXML private RadioButton rbBigTV;
    
    @FXML static public ImageView splashView;
    
    static public Rectangle blankScreen;
    static public Button channelButton; 
    static public Button pwrButton;
    static private Browser browser;
    static public String[] playArray;
    static public int rotation = 0;
    static public StackPane tvPane;
    static public Stage stage;
    static public int channel;
    static public int change = 1;
    static public Rotate chRotate = new Rotate();
    static public Rotate pwrRotate = new Rotate();
    static private Boolean isOn = false;
    
    // Event listener for btnStart component
    public void btnStartListener()throws IOException{
        Window window = btnStart.getScene().getWindow();//Major props to SO user James_D
        String[] roomChoices = new String[3];
        if (window instanceof Stage) {
            stage = (Stage) window;
            if (rbSeventies.isSelected()){
                roomChoices[0] = "seventies";
            }
            else if (rbEighties.isSelected()){
                roomChoices[0] = "eighties";
            }
            else if (rbNineties.isSelected()){
                roomChoices[0] = "nineties";
            }
            
            if (rbNoXmas.isSelected()){
                roomChoices[1] = "noXmas";
            }
            else if (rbYesXmas.isSelected()){
                roomChoices[1] = "yesXmas";
            }
            else if (rbOnlyXmas.isSelected()){
                roomChoices[1] = "onlyXmas";
            }
            
            if (rbMorning.isSelected()){
                roomChoices[2] = "morning";
            }
            else if (rbNoon.isSelected()){
                roomChoices[2] = "noon";
            }
            else if (rbNight.isSelected()){
                roomChoices[2] = "night";
            }
            stage.setScene(createScene(getRoomPane(roomChoices)));
            
            System.out.println(roomChoices[0]+", " +roomChoices[1]);
            stage.show();    
        }
    }
    
    /** The getRoomPane method constructs a scene based on the parameters given
     * in the preceeding questionnaire.  It is also massively overloaded  but
     * I found it tricky to separate out the parts.
     * 
     * @param rc An array of the user's responses.
     * @return A pane with the image of the chosen room.
     */
    public Pane getRoomPane(String[] rc){
        StackPane roomPane;
        String room = "images/";
        switch (rc[0]) {
            case "seventies":
                room += "easytv4_70s_";
                break;
            case "eighties":
                room += "easytv4_80s_";
                break;
            case "nineties":
                room += "easytv4_90s_"; // to change when a 90s room is available
                break;
            default:
                break;
        }
        
        // Evaluate time of day
        switch (rc[2]) {
            case "morning":
                if (rc[0].equals("eighties") || rc[0].equals("nineties") && rc[1] .equals("yesXmas")){
                    room += "noon";
                }
                else{
                    room += "morning";
                }   break;
            case "noon":
                room += "noon";
                break;
            default:
                room += "night";
                break;
        }
        
        // Evaluate Christmasitude
        if (rc[1].equals("noXmas")){
            room += ".png";
        }
        else if (rc[1].equals("yesXmas") || rc[1].equals("onlyXmas")){
            room += "_xmas.png";
        }
        
        //Setup the viewing area
        Image livingRoom = new Image(room,1280,720,false,false);
        ImageView roomViewer = new ImageView(livingRoom);
        Rectangle playerSize = new Rectangle(768,576);
        blankScreen = new Rectangle(625, 480);
        blankScreen.setFill(Color.web("#1d1f22"));

        // Add a background for the knob
        Rectangle buttonBezel = new Rectangle(99,126);
        if(rc[2] == "night"){
            System.out.println("Tis indeed night");
            buttonBezel.setFill(Color.web("#1d1e22"));
        }
        else{
            buttonBezel.setFill(Color.DARKKHAKI);
        }
        StackPane bbPane = new StackPane(buttonBezel);
        bbPane.setPadding(new Insets(0,10,365,440));
        
        // Add a button for the bezel so that the channel can be changed
        Image tvKnob = new Image("images/tvdial1_sm.png",60, 60, false, false);
        ImageView dialViewer = new ImageView(tvKnob);
        
        Button channelKnob = new Button();
        channelKnob.setGraphic(dialViewer);
        channelKnob.setStyle("-fx-background-color:transparent");
        
        BorderPane buttonBox = new BorderPane();
        buttonBox.setMaxSize(bbPane.getWidth(), bbPane.getHeight());
        bbPane.getChildren().add(buttonBox);
        buttonBox.setTop(channelKnob);
        
        // Add another button for the bezel - this one turns the TV on and off!
        Image pwrKnob = new Image("images/tvdial2_sm.png",50,50,false,false);
        ImageView dial2Viewer = new ImageView(pwrKnob);
        Button powerKnob = new Button();
        powerKnob.setGraphic(dial2Viewer);
        powerKnob.setStyle("-fx-background-color:transparent");
        buttonBox.setBottom(powerKnob);
        
        
        // Set up the browser
        browser = setBrowser(rc);
        browser.browser.prefHeightProperty().bind(playerSize.heightProperty());
        browser.browser.prefWidthProperty().bind(playerSize.widthProperty());
        tvPane = new StackPane(browser);

        roomPane = new StackPane(tvPane,blankScreen,roomViewer, bbPane);
        tvPane.setPadding(new Insets(40,250,110,180));
        
        //Listener for channel knob
        channelKnob.setOnMouseClicked((event) -> {
            channelKnob.rotateProperty().bind(chRotate.angleProperty());
            chRotate.setAngle(chRotate.getAngle()+45);
            
            if (isOn){
                blankScreen.setOpacity(1.0);
                browser.webEngine.load(null);
                FadeTransition ft = new FadeTransition(Duration.millis(3000), blankScreen);
                ft.setFromValue(1.0);
                ft.setToValue(0.0);
                ft.play();
                browser = changeChannel();
                tvPane.getChildren().add(browser);
                stage.show();
            }
        });
        
        // Listener for powerKnob
        powerKnob.setOnMouseClicked((event) -> {
            powerKnob.rotateProperty().bind(pwrRotate.angleProperty());
            
            if (event.getButton().equals(MouseButton.PRIMARY)){
                if (pwrRotate.getAngle() == 90) {
                    pwrRotate.setAngle(0);;
                    blankScreen.setOpacity(1.0);
                    browser.webEngine.load(null);
                    isOn = false;
                }
                else{
                    pwrRotate.setAngle(90);
                    isOn = true;
                    browser.webEngine.load(playArray[channel]);
                    changeViewTime(playArray[channel]);
                    FadeTransition ft = new FadeTransition(Duration.millis(3000), blankScreen);
                    ft.setFromValue(1.0);
                    ft.setToValue(0.0);
                    ft.play();
                    blankScreen.setOpacity(0.0);
                }
            }  
        });
        return roomPane;
    }
    
    /** The changeChannel method simulates changing the channel by flipping through
     * the array of YouTube videos.
     * @return A browser node with the chosen video.
     */
    private Browser changeChannel(){
        System.out.println(channel);
        if (channel + 1 < playArray.length){
            channel++;
        }
        else{
            channel = 0;
        }
        
        System.out.println(Arrays.toString(playArray));
        playArray[channel] = changeViewTime(playArray[channel]);
        
        browser = new Browser(playArray[channel]);
        return browser;
    }
    
    /** The changeViewTime method adds 5 seconds to each video to simulate time passed
     * while channel-surfing.
     * @param pac The address of the current video
     * @return The video, updated with a multiple of 5 added to the time stamp
    */
    private String changeViewTime(String pac){
        int startPoint = pac.indexOf("t=");
        int endPoint = pac.length();
        int timeStamp = Integer.parseInt(pac.substring(startPoint+2, endPoint));
        timeStamp += (change * 5);
        change++;
        
        pac = 
            pac.replaceAll(pac.substring(startPoint+2,endPoint), Integer.toString(timeStamp));
        
        System.out.println(pac);
        System.out.println("startPoint = " + startPoint + " endpoint = " + endPoint);
        return pac;    
    }
    
    /** The setBrowser method sets the initial playback.
     * 
     * @param rc The array of user choices.
     * @return A browser object with a video array queued up.
     */
    private Browser setBrowser(String[] rc){
        int endpoint = 0; // to hold endpoint of primary array
        playArray = new String[getArrayLength(rc)];
        Random rand = new Random();
        
        
        // Only provide Christmas shows if chosen
        if (rc[1].equals("onlyXmas")){
            
            for (int i = 0; i < config.only_xmas.length; i++){
                playArray[i] = config.only_xmas[i];
            }
            while (playArray[channel] == null){
                channel = rand.nextInt(playArray.length);
            }
            if(isOn){
                browser = new Browser(playArray[channel]);
            }
            else{
                browser = new Browser(null);
            }
            return browser; 
        }
        
        // Account for all other cases
        if (rc[0].equals("seventies")){
            
            for (int i = 0; i < config.seventies.length; i++){
                playArray[i] = config.seventies[i];
            }
            endpoint = config.seventies.length;
            
            if (rc[1].equals("yesXmas")){
                for (int i = 0; i < config.seventies_xmas.length; i++){     
                        playArray[i+endpoint] = config.seventies_xmas[i];
                }
            }
        }
        else if (rc[0].equals("eighties")){
            
            for (int i = 0; i < config.eighties.length; i++){
                playArray[i] = config.eighties[i];
            }
            endpoint = config.eighties.length;
            
            if (rc[1].equals("yesXmas")){
                for (int i = 0; i < config.eighties_xmas.length; i++){
                    playArray[i+endpoint] = config.eighties_xmas[i];
                }
            }

        }
        else if (rc[0].equals("nineties")){
            
            for (int i = 0; i < config.nineties.length; i++){
                playArray[i] = config.nineties[i];
            }
            endpoint = config.nineties.length;
            if (rc[1].equals("yesXmas")){
                for (int i = 0; i < config.nineties_xmas.length; i++){
                        playArray[i+endpoint] = config.nineties_xmas[i];
                }
            }
        }
        
        channel = rand.nextInt(playArray.length);
        if(isOn){
                browser = new Browser(playArray[channel]);
            }
            else{
                browser = new Browser(null);
            }
        return browser;      
    }

    /** The createScene helper function just makes a scene.
     * 
     * @param mainPane Whatever pane you want to put into a scene.
     * @return The created scene.
     */
    private Scene createScene(Pane mainPane) {
        Scene scene = new Scene(mainPane);
        
        return scene;
    }
    
    /** The getArrayLength method gets the presumed length of an array based on
     * number of relevant videos and returns that number to make an array.
     * 
     * @param rc The array of user choices.
     * @return The number of videos present relevant to the choices.
     */
    private int getArrayLength(String[] rc){
        int len = 0;
        if (rc[0].equals("seventies")){
            len += config.seventies.length;
            if (rc[1].equals("yesXmas")){
                len += config.seventies_xmas.length;
            }
        }
        else if (rc[0].equals("eighties")){
            len += config.eighties.length;
            if (rc[1].equals("yesXmas")){
                len += config.eighties_xmas.length;
            }
        }
        else if (rc[0].equals("nineties")){
            len += config.nineties.length;
            if (rc[1].equals("yesXmas")){
                len += config.nineties_xmas.length;
            }
        }
        
        if (rc[1]=="onlyXmas"){
            len = config.only_xmas.length;
        }
        
        return len;
    }
}

/** The Browser class allows for easy making of webView nodes.
 * 
 * 
 */

class Browser extends Region {
    final WebView browser = new WebView();
    final WebEngine webEngine = browser.getEngine();

    public Browser(String link) {
        webEngine.load(link);
        getChildren().add(browser);
    }
}