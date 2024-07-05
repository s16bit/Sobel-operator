import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
/*
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
*/

public class Sobel_operator {

	//private Mat image;
	private BufferedImage image;

	static int[][] sharpen = {
		{-2, -1, 0},
		{-1, 1, 1},
		{0, 1, 2}
	};
	
	
	static int[][] coreY = {
		{-1, -2, -1},
		{0, 0, 0},
		{1, 2, 1}
	};
	
	static int[][] coreX = {
		{-1, 0, 1},
		{-2, 0, 2},
		{-1, 0, 1}
	};
	
	static {
		//System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	/*
    public Sobel_operator(String path) {
    	
        image = Highgui.imread(path,
        		Highgui.CV_LOAD_IMAGE_GRAYSCALE);
    }
    */
    public Sobel_operator(String path) {
        try {
			image = ImageIO.read(new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /*
    public Mat getImage() {
    	return image;
    }
    */
    public BufferedImage getImage() {
    	return image;
    }
    
    public BufferedImage c() {
    	
    	//return convertInBufferedImage(Convolution.apply(image, sharpen));
    	
    	/*
    	try {
			ImageIO.write((RenderedImage)Convolution.Apply(image, sharpen), "png", new File("C:/t.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
    	BufferedImage imageY = MultiCoreConvolution.applyConvolution(image, coreY);
    	BufferedImage imageX = MultiCoreConvolution.applyConvolution(image, coreX);
    	
    	return combineSobel(imageX, imageY);
    	
    	//return MultiCoreConvolution.applyConvolution(image, sharpen);
    	//return Convolution.Apply(image, sharpen);
    	//return image;
    }
    
    public BufferedImage combineSobel(BufferedImage imageX, BufferedImage imageY) {
    	
        int height = image.getHeight();
        int width = image.getWidth();
        
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        
        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
            	
                
                int imageX_Red = imageX.getRGB(x, y) >> 16 & 255;
				int imageX_Green = imageX.getRGB(x, y) >> 8 & 255;
				int imageX_Blue = imageX.getRGB(x, y) & 255;
				
                int imageY_Red = imageY.getRGB(x, y) >> 16 & 255;
				int imageY_Green = imageY.getRGB(x, y) >> 8 & 255;
				int imageY_Blue = imageY.getRGB(x, y) & 255;
                	Color resultColor = new Color(
                			(int)Math.min(Math.sqrt(imageX_Red*imageX_Red + imageY_Red*imageY_Red), 255),
                			(int)Math.min(Math.sqrt(imageX_Green*imageX_Green + imageY_Green*imageY_Green), 255),
                			(int)Math.min(Math.sqrt(imageX_Blue*imageX_Blue + imageY_Blue*imageY_Blue), 255)
                			);
                result.setRGB(x, y, resultColor.getRGB());
            }
        }
        return result;
    }
	/*
    public static BufferedImage convertInBufferedImage(Mat opencvImage) {
    	//opencvImage = Convolution.apply(opencvImage, sharpen);
    	
        int type = BufferedImage.TYPE_BYTE_GRAY;
        
        if ( opencvImage.channels() > 1 ) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        
        BufferedImage image = new BufferedImage(opencvImage.cols(),opencvImage.rows(), type);
        opencvImage.get(0, 0,((DataBufferByte)image.getRaster().getDataBuffer()).getData());
        return image;
    }
	*/
}
