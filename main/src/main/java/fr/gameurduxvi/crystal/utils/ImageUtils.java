package fr.gameurduxvi.crystal.utils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageUtils {


    public static Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {
        int original_width = imgSize.width;
        int original_height = imgSize.height;
        int bound_width = boundary.width;
        int bound_height = boundary.height;
        int new_width;
        int new_height;

        if (original_width > original_height) {
            new_width = bound_width;
            new_height = (new_width*original_height)/original_width;
        } else {
            new_height = bound_height;
            new_width = (new_height*original_width)/original_height;
        }

        return new Dimension(new_width, new_height);
    }

    public static BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

        Dimension new_dim = getScaledDimension(new Dimension(originalImage.getWidth(), originalImage.getHeight()), new Dimension(width,height));

        BufferedImage resizedImage = new BufferedImage((int) new_dim.getWidth(), (int) new_dim.getHeight(), type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, (int) new_dim.getWidth(), (int) new_dim.getHeight(), null);
        g.dispose();

        return resizedImage;
    }
}
