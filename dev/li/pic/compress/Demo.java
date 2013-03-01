package li.pic.compress;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

import li.pic.compress.Scalr.Mode;

public class Demo {
    public static void main(String[] args) throws Exception {

        // File file = new File("C:/Users/li/Desktop/3_2.gif");
        // FileInputStream fis = new FileInputStream(file);
        // BufferedImage bufferedImg = ImageIO.read(fis);
        // int imgWidth = bufferedImg.getWidth();
        // int imgHeight = bufferedImg.getHeight();
        //
        // System.out.println(imgHeight + "\t" + imgWidth);

        // System.out.println(getRate(new File("C:/Users/li/Desktop/1.gif")));
        // System.out.println(getRate(new File("C:/Users/li/Desktop/2.gif")));
        // System.out.println(getRate(new File("C:/Users/li/Desktop/3.gif")));
        // System.out.println(getRate(new File("C:/Users/li/Desktop/3_2.gif")));

        File src = new File("C:/Users/li/Desktop/1.gif");
        File dest = new File("C:/Users/li/Desktop/1_2.gif");
        Double rate = getRate(src);
        compress(src, dest, rate);
        System.out.println(rate + "\tdone");
    }

    public static Double getRate(File file) throws Exception {
        Double ExpectedLength = (double) 245;
        Double rate = (double) 0;

        FileInputStream fis = new FileInputStream(file);
        BufferedImage bufferedImg = ImageIO.read(fis);
        Integer width = bufferedImg.getWidth();
        Integer height = bufferedImg.getHeight();

        Double length = (double) (width > height ? width : height);

        rate = ExpectedLength / length;

        fis.close();
        return rate;
    }

    /**
     * 压缩一个gif图片
     * 
     * @param src 原图片
     * @param dest 输出图片
     * @param rate 压缩倍率 如 0.8
     * @throws FileNotFoundException
     */
    public static void compress(File src, File dest, Double rate) throws FileNotFoundException {
        GifDecoder gd = new GifDecoder();
        int status = gd.read(new FileInputStream(src));
        if (status != GifDecoder.STATUS_OK) {
            return;
        }

        AnimatedGifEncoder ge = new AnimatedGifEncoder();
        ge.start(new FileOutputStream(dest));
        ge.setRepeat(0);

        for (int i = 0; i < gd.getFrameCount(); i++) {
            BufferedImage frame = gd.getFrame(i);
            int width = frame.getWidth();
            int height = frame.getHeight();

            width = (int) (width * rate);
            height = (int) (height * rate);

            BufferedImage rescaled = Scalr.resize(frame, Mode.FIT_EXACT, width, height);

            int delay = gd.getDelay(i);

            ge.setDelay(delay);
            ge.addFrame(rescaled);
        }
        ge.finish();
    }
}