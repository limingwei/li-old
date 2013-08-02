package li.mvc;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Captcha {
    private static final int WIDTH = 1000, HEIGHT = 100;
    private static final String[] strArr = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "0",//
            "A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "M", "N", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y",//
            "a", "b", "c", "d", "e", "f", "g", "h", "j", "k", "m", "n", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y" };

    static int fontSize = 50;
    static int len = 5;

    public static void main(String[] args) throws Exception {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        String vCode = drawGraphic(image);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpeg", byteArrayOutputStream);
        System.out.println(vCode);
        showWin(byteArrayOutputStream.toByteArray());
    }

    public static void showWin(final byte[] data) {
        try {
            JFrame frame = new JFrame("验证码(请不要关这个窗口,关了程序就停了)");
            frame.setLocationRelativeTo(null);
            JPanel imagePanel = (JPanel) frame.getContentPane();

            ImageIcon imageIcon = new ImageIcon(data);
            JLabel label = new JLabel(imageIcon);

            label.setBounds(0, 0, imageIcon.getIconWidth(), imageIcon.getIconHeight());

            imagePanel = (JPanel) frame.getContentPane();
            imagePanel.setOpaque(false);
            imagePanel.setLayout(new FlowLayout());

            frame.getLayeredPane().setLayout(null);
            frame.getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 300);
            frame.setVisible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String drawGraphic(BufferedImage image) {
        Graphics g = image.createGraphics(); // 获取图形上下文
        Random random = new Random(); // 生成随机类
        g.setColor(getRandColor(200, 250)); // 设定背景色
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setFont(new Font("Times New Roman", Font.PLAIN, fontSize)); // 设定字体
        g.setColor(getRandColor(160, 200)); // 随机产生155条干扰线，使图象中的认证码不易被其它程序探测到
        for (int i = 0; i < 155; i++) {
            int x = random.nextInt(WIDTH);
            int y = random.nextInt(HEIGHT);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }
        String sRand = ""; // 取随机产生的认证码(6位数字)
        for (int i = 0; i < len; i++) {
            String rand = String.valueOf(strArr[random.nextInt(strArr.length)]);
            sRand += rand;
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110))); // 将认证码显示到图象中
            g.drawString(rand, fontSize * i + 6, fontSize); // 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
        }
        g.dispose(); // 图象生效
        return sRand;
    }

    /*
     * 给定范围获得随机颜色
     */
    private static Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }
}