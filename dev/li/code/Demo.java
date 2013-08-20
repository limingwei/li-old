package li.code;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

/**
 * http://nvcsz.gtimg.com/123123/7383631461139009302.gif?r=15022 tesseract
 */
public class Demo {
    public static void main(String[] args) throws Exception {
        File file = new File(System.getProperty("user.dir") + "/dev/li/code/1.gif");
        BufferedImage in = ImageIO.read(file);
        int width = in.getWidth();
        int height = in.getHeight();

        BufferedImage out = new BufferedImage(width, height, in.getType());

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rgb = in.getRGB(x, y);

                out.setRGB(x, y, 0x00FF0000);
            }
        }

        ImageIO.write(out, "PNG", new File("C:/Users/li/Desktop/out.png"));
    }

    public static int getAlpha(int pixel) {
        return (pixel & 0xFF000000) >> 24;
    }

    public static int getRed(int pixel) {
        return (pixel & 0x00FF0000) >> 16;
    }

    public static int getGreen(int pixel) {
        return (pixel & 0x0000FF00) >> 8;
    }

    public static int getBlue(int pixel) {
        return (pixel & 0x000000FF);
    }
}