package li.code;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

/**
 * http://nvcsz.gtimg.com/123123/7383631461139009302.gif?r=15022
 */
public class Demo {
    public static void main(String[] args) throws Exception {
        File file = new File(System.getProperty("user.dir") + "/dev/li/code/1.gif");
        BufferedImage bufferedImage = ImageIO.read(file);
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rgb = bufferedImage.getRGB(x, y);
                int r = (int) (((rgb >> 16) & 0xFF) * 1.1 + 30);
                int g = (int) (((rgb >> 8) & 0xFF) * 1.1 + 30);
                int b = (int) (((rgb >> 0) & 0xFF) * 1.1 + 30);

                System.out.println(r + "\t" + g + "\t" + b);
            }
        }
    }
}