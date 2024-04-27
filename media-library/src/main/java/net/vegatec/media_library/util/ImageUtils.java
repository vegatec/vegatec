package net.vegatec.media_library.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public final class ImageUtils {
    public static void scale(String src, int width, int height, String dest)
        throws IOException {
        BufferedImage bsrc = ImageIO.read(new File(src));
        BufferedImage bdest =
            new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bdest.createGraphics();
        AffineTransform at =
            AffineTransform.getScaleInstance((double)width/bsrc.getWidth(),
                (double)height/bsrc.getHeight());
        g.drawRenderedImage(bsrc,at);
        ImageIO.write(bdest,"JPG",new File(dest));
    }


//    public static void main(String[] args) {
//        if (args.length == 4) {
//            try {
//                ImageUtils.scale
//                    (args[0], Integer.parseInt(args[1]),
//                        Integer.parseInt(args[2]), args[3]);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            System.out.println("\nUsage: java ScaleJPG src width height dest\n");
//        }
//    }

}






