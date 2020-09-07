import edu.duke.DirectoryResource;
import edu.duke.ImageResource;
import edu.duke.Pixel;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        // write your code here
        selectAndConvert();
    }

    private static ImageResource makeInversion(ImageResource imgIn) {
//        returns a new image that is the inverse of the original image.
        ImageResource imgOut = new ImageResource(imgIn.getWidth(), imgIn.getHeight());
        for (Pixel px : imgOut.pixels()) {
            Pixel pixel = imgIn.getPixel(px.getX(), px.getY());
            px.setRed(255 - pixel.getRed());
            px.setGreen(255 - pixel.getGreen());
            px.setBlue(255 - pixel.getBlue());
        }
        return imgOut;
    }

    private static void selectAndConvert() {
//        This method allows the user to select several files and display the images.
        DirectoryResource dr = new DirectoryResource();
        for (File f : dr.selectedFiles()) {
            ImageResource imgIn = new ImageResource(f);
            String newFileName = "inverted-" + imgIn.getFileName();
            ImageResource imgOut = makeInversion(imgIn);
            imgOut.setFileName(newFileName);
            imgOut.draw();
            imgOut.save();
        }
    }
}
