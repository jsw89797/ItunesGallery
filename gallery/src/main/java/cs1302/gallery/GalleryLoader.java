package cs1302.gallery;

import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.layout.TilePane;
import java.net.URLEncoder;
import java.net.URL;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.io.IOException;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import javafx.scene.control.ProgressBar;

/** GalleryLoader class responsible for containing the imageView components in GalleryApp. */
public class GalleryLoader extends VBox {

    /** A default image which loads when the application starts. */
    private String defaulti =
        "https://w7.pngwing.com/pngs/901/413/png-transparent-gallery-images-photos-thumbnail.png";

    /** Default height and width for Images. */

    VBox vbox;
    HBox urlLayer;
    TextField urlField;
    ImageView imgView;
    int id;

    /** Creates new loader object for gallery app.
        Each loader object is made up of a VBox containing an image and imageview.
    */
    public GalleryLoader() {
        super();
        vbox = new VBox();
        Image img = new Image(defaulti, 128, 120, false, false);
        imgView = new ImageView(img);
        imgView.setPreserveRatio(true);
        this.getChildren().addAll(imgView);
    } //ImageLoader

    /** Sets the image in Loader's imageView.
        @param i Image being set in imageView.
        @param n Location of image in image array.
    */
    public void setImage(Image i, int n) {
        imgView.setImage(i);
        this.setId(n);
    } //setImage

    /** Sets the identity (location of image in image array) of the loader.
        @param n Location of image in the main image array.*/
    public void setId(int n) {
        this.id = n;
    } //setId

    /** Returns the identity (location of image in image array) of the loader.
        @return The identity set to imageView (Location of image in main image array.*/
    public int getIdentity() {
        return this.id;
    } //getId

} //ImageLoader
