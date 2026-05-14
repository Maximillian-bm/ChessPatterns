import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageExporter {

    public static void exportIntArrayToPNG(int[][] pixels, String[] colors, String outputPath) throws IOException {

        int height = pixels.length;
        int width = pixels[0].length;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = pixels[y][x];
                String color;
                if(pixel == -1){
                    color = "#ffffff00";
                }else {
                    color = colors[pixel];
                }
                int rgb = hexToRGB(color);
                image.setRGB(x, y, rgb);
            }
        }
        ImageIO.write(image, "png", new File(outputPath));
    }

    private static int hexToRGB(String hex) {
        hex = hex.replace("#", "");
        int r = Integer.valueOf(hex.substring(0, 2), 16);
        int g = Integer.valueOf(hex.substring(2, 4), 16);
        int b = Integer.valueOf(hex.substring(4, 6), 16);
        int a = 255;
        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}