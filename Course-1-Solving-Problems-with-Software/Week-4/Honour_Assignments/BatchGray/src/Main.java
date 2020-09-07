import edu.duke.DirectoryResource;
import edu.duke.ImageResource;
import edu.duke.Pixel;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        DirectoryResource dr = new DirectoryResource();
        for (File f : dr.selectedFiles()) {
            ImageResource imgIn = new ImageResource(f);
            String newFileName = "gray-" + imgIn.getFileName();
            ImageResource imgOut = new ImageResource(imgIn.getWidth(), imgIn.getHeight());
            for (Pixel px : imgOut.pixels()) {
                Pixel pixel = imgIn.getPixel(px.getX(), px.getY());
                int grayVal = (pixel.getRed() + pixel.getBlue() + pixel.getGreen()) / 3;
                px.setRed(grayVal);
                px.setGreen(grayVal);
                px.setBlue(grayVal);
            }
            imgOut.setFileName(newFileName);
            imgOut.draw();
            imgOut.save();
        }
    }
}
