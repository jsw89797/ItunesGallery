package cs1302.gallery;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.TilePane;
import cs1302.gallery.GalleryLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.event.ActionEvent;
import java.net.URLEncoder;
import java.net.URL;
import javafx.scene.image.Image;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.io.IOException;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import javafx.scene.control.ProgressBar;
import java.util.Random;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.event.EventHandler;
import javafx.util.Duration;
import javafx.scene.control.ProgressBar;
import javafx.application.Platform;
import java.lang.Runnable;
import javafx.scene.image.PixelReader;
//Imports

/**
 * Represents an iTunes GalleryApp.
 */
public class GalleryApp extends Application {
    HBox bottom = new HBox(10);
    HBox urlLayer;
    TilePane control;
    GalleryLoader[] images = new GalleryLoader[20];
    MenuBar mainBar = new MenuBar();
    Menu file = new Menu("File");
    MenuItem quit = new MenuItem("Exit");
    Button pause = new Button("Play");
    Button update = new Button("Update Images");
    TextField query = new TextField("rock");
    Text label = new Text("Search Query: ");
    Text itunes = new Text("Images provided courtesy of iTunes");
    String[] imageLocation;
    Image[] gallery;
    Timeline timeline;
    ProgressBar load = new ProgressBar(0);
    Boolean fire = false;
    //instance variables

/** Start method for GalleryApp, this method starts and calls all methods for galleryApp. */
    @Override

    public void start(Stage stage) {
        VBox pane = new VBox();
        control = new TilePane();
        control.setPrefColumns(5);
        bottom.getChildren().addAll(load,itunes);
        update.setOnAction(this::loadImages);
        pause.setOnAction(this::play);
        for (int i = 0; i < 20; i++) {
            images[i] = new GalleryLoader();
            control.getChildren().add(images[i]);
        } //for
        urlLayer = new HBox(10);
        urlLayer.getChildren().addAll(pause,label,query,update);
        menuGen();
        pane.getChildren().addAll(mainBar,urlLayer,control,bottom);
        Platform.runLater(() ->    initialLoad());
        Scene scene = new Scene(pane);
        stage.setMaxWidth(640);
        stage.setMaxHeight(640);
        stage.setTitle("GalleryApp!");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    } // start

/**  Generates the menu for the gallery. */
    public void menuGen() {
        quit.setOnAction(e -> System.exit(0));
        file.getItems().add(quit);
        mainBar.getMenus().add(file);
    } //menuGen

    /** Puts together the full string URL for image query.
        @param s String in TextField needed to be encoded.
        @return fullSite String url represnetation for image query.
        @throws UnsupportedEncodingException a
        @throws MalformedURLException b
*/
    public String encodeTerm(String s) throws UnsupportedEncodingException, MalformedURLException {
        try {
            String fullSite = "https://itunes.apple.com/search?term=";
            String termHolder;
            termHolder = URLEncoder.encode(s,"UTF-8");
            fullSite = "https://itunes.apple.com/search?term=" + termHolder;
            return fullSite;
        } catch (UnsupportedEncodingException e) {
            System.out.println(e);
        } //try-catch
        return null;
    } //encodeTerm

    /** Returns the appropriate URL to get the requested image query.
        @return url URL for search query.
        @throws MalformedURLException a */
    public URL getURL() throws MalformedURLException {
        try {
            URL url = new URL(encodeTerm(query.getText()));
            return url;
        } catch (UnsupportedEncodingException e) {
            System.out.println(e);
        } //try-catch
        return null;
    } //getURL

    /** Uses Json parsing to return the URL location of the image.
        @throws IOException a*/
    public void parse() throws IOException {
        try { //try-catch used for possible IOException
            InputStreamReader reader  = new InputStreamReader(getURL().openStream());
            JsonParser jp = new JsonParser();
            JsonElement je = jp.parse(reader);
            JsonObject root = je.getAsJsonObject();
            JsonArray results = root.getAsJsonArray("results");
            int numResults = results.size();
            imageLocation = new String[results.size()];
            for (int i = 0; i < numResults; i++) {
                JsonObject result = results.get(i).getAsJsonObject();
                JsonElement artworkUrl100 = result.get("artworkUrl100");
                if (artworkUrl100 != null) {
                    String artUrl = artworkUrl100.getAsString();
                    if (!(artUrl.equals(null))) {
                        imageLocation[i] = artUrl;
                    } //if
                } //if
            } //for
        } catch (IOException e) {
            System.out.println(e);
        } //try-catch
    } //parse

    /** Returns array of images containing images for gallery app. */
    public void getImageArray() {
        try {
            parse();
        } catch (IOException e) {
            System.out.println("OOPS!");
        } //try-catch
        Platform.runLater(() -> load.setProgress(0));
        gallery = new Image[imageLocation.length];
        for (int i = 0; i < imageLocation.length; i++) {
            final double prog = i;
            gallery[i] = new Image(imageLocation[i], 128, 120, false, false);
            Platform.runLater(() -> load.setProgress(prog / imageLocation.length));
        }
    } //getImageArray

    /** This method is the action for the UpdateImages button.
        It updates the images of the gallery app to the ones matching the new term.
        @param e ActionEvent allowing for method reference.
    */
    public void loadImages(ActionEvent e) {
        Runnable r = () -> {
            Platform.runLater(() -> {
                if (fire == false) {
                    pause.fire();
                    fire = true;
                }
            });
            getImageArray();
            if (imageLocation.length >= 21) {
                Random num1 = new Random();
                Random num2 = new Random();
                int n1 = num1.nextInt(images.length);
                int n2 = num2.nextInt(imageLocation.length);
                Platform.runLater(() -> load.setProgress(0));
                for (int i = 0; i < images.length; i++) {
                    n2 = num2.nextInt(imageLocation.length);
                    images[i].setImage(gallery[n2], n2);
                } //for
            } else {
                Platform.runLater(() -> {
                    Alert error1 = new Alert(AlertType.ERROR);
                    error1.setHeaderText("Not enough results!");
                    error1.setContentText("Please search a different term!");
                    error1.showAndWait();
                });
            } //else
        }; // Runnable
        Thread t = new Thread(r);
        t.setDaemon(true);
        t.start();
    } //loadImages

    /** This method loads the initial query of pictures into the gallery app. */
    public void initialLoad() {
        getImageArray();
        if (imageLocation.length >= 21) {
            Random num1 = new Random();
            Random num2 = new Random();
            int n1 = num1.nextInt(images.length);
            int n2 = num2.nextInt(imageLocation.length);
            for (int i = 0; i < images.length; i++) {
                n2 = num2.nextInt(imageLocation.length);
                images[i].setImage(gallery[n2], n2);
            } //for
        } else {
            Alert error1 = new Alert(AlertType.ERROR);
            error1.setHeaderText("Not enough results!");
            error1.setContentText("Please search a different term!");
            error1.showAndWait();
        } //else
    } //initialLoad
    /** This is the play method. It is responsible for making the pictures change
        when play button is pressed.
        @param e ActionEvent allowing method reference.
    */

    public void play(ActionEvent e) {
        if (pause.getText() == "Pause") {
            pause.setText("Play");
            fire = false;
            timeline.stop();
        } else if (pause.getText() == "Play") {
            pause.setText("Pause");
            fire = true;
            EventHandler<ActionEvent> handler = event -> {
                Random num1 = new Random();
                int n1 = num1.nextInt(images.length);
                int n2 = num1.nextInt(imageLocation.length);
                for (int i = 0; i < images.length; i++) {
                    if (images[i].getIdentity() == n2) {
                        n2 = num1.nextInt(imageLocation.length);
                        i = 0;
                    } //if
                } //for
                images[n1].setImage(gallery[n2], n2);
            };
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(2), handler);
            timeline = new Timeline();
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.getKeyFrames().add(keyFrame);
            timeline.play();
        } //if
    } //play
} // GalleryApp
