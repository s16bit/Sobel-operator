import java.awt.image.BufferedImage;
/*
import org.opencv.core.Core;
import org.opencv.core.Mat;
*/


public class Convolution {
	
	static {
		//System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	/*
	public static Mat apply(Mat inputImage, double[][] kernel) {
		
		double kernel_sum = 0;
		for(int kernel_row = 0; kernel_row < kernel.length; kernel_row++) {
			for(int kernel_column = 0; kernel_column < kernel[0].length; kernel_column++) {
				kernel_sum += kernel[kernel_row][kernel_column];
			}
		}
		
		if(kernel_sum <=0) kernel_sum = 1;
		
		for(int row = 0; row < inputImage.height(); row++) {
			for(int column = 0; column < inputImage.width(); column++) {
				//System.out.print(" " + inputImage.get(row, column)[0]);
				double sum = 0;
				for(int kernel_row = 0; kernel_row < kernel.length; kernel_row++) {
					for(int kernel_column = 0; kernel_column < kernel[0].length; kernel_column++) {
						if((row + kernel_row < inputImage.height()) && (column + kernel_column < inputImage.width())) {
							double[] gray = inputImage.get(row + kernel_row, column + kernel_column);
							sum = sum + gray[0] * kernel[kernel_row][kernel_column];
						}
					}
				}
				//;
				if((row + kernel.length / 2 < inputImage.height()) && (column + kernel[0].length / 2 < inputImage.width())) {
				
					
					
					inputImage.put(row + kernel.length / 2, column + kernel[0].length / 2, sum);
				}
			}
			//System.out.println();
		}
		return inputImage;
	}
	*/
	
	public static BufferedImage Apply(BufferedImage inputImage, int[][] kernel) {
		
		BufferedImage inputImage2 = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(), inputImage.getType());
		
		int kernel_sum = 0;
		for(int kernel_row = 0; kernel_row < kernel.length; kernel_row++) {
			for(int kernel_column = 0; kernel_column < kernel[0].length; kernel_column++) {
				kernel_sum += kernel[kernel_row][kernel_column];
			}
		}
		
		if(kernel_sum <= 0) kernel_sum = 1;
		
		for(int row = 0; row < inputImage.getWidth(); row++) {
			for(int column = 0; column < inputImage.getHeight(); column++) {
				int sum = 0;
				int rSum = 0, gSum = 0, bSum = 0;
				for(int kernel_row = 0; kernel_row < kernel.length; kernel_row++) {
					for(int kernel_column = 0; kernel_column < kernel[0].length; kernel_column++) {
						if( (row + kernel_row < inputImage.getWidth()) && (column + kernel_column < inputImage.getHeight())) {
							rSum = rSum + (inputImage.getRGB(row + kernel_row, column + kernel_column) >> 16 & 255) * kernel[kernel_row][kernel_column];
							gSum = gSum + (inputImage.getRGB(row + kernel_row, column + kernel_column) >> 8 & 255) * kernel[kernel_row][kernel_column];
							bSum = bSum + (inputImage.getRGB(row + kernel_row, column + kernel_column) & 255) * kernel[kernel_row][kernel_column];
						}
					}
				}
				
				if((row + kernel.length / 2 < inputImage.getWidth()) && (column + kernel[0].length / 2 < inputImage.getHeight())) {
					
			          rSum = rSum / kernel_sum;
			          if (rSum < 0) rSum = 0;
			          if (rSum > 255) rSum = 255;
			          
			          
			          gSum = gSum / kernel_sum;
			          if (gSum < 0) gSum = 0;
			          if (gSum > 255) gSum = 255;

			          bSum = bSum / kernel_sum;
			          if (bSum < 0) bSum = 0;
			          if (bSum > 255) bSum = 255;
					
					inputImage2.setRGB(row + kernel.length / 2, column + kernel[0].length / 2, (rSum << 16) + (gSum << 8) + bSum);
				}
			}
			
		}
		return inputImage2;
	}
	
  }


