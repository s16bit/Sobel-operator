import java.awt.image.BufferedImage;


public class MultiCoreConvolution implements Runnable {
	
	private static int cpus = Runtime.getRuntime().availableProcessors();
	
	private static BufferedImage inputImage;
	private static BufferedImage resultImage;
	private static int kernel_sum;
	
	private static int horizontal_part, vertical_part;
	
	private static int[][] kernel;
	
	private int cpu_index;
	
	public MultiCoreConvolution(int cpu_index) {
		this.cpu_index = cpu_index;
	}
	
	public void apply() {
		/*
		int kernel_sum = 0;
		for(int kernel_row = 0; kernel_row < kernel.length; kernel_row++) {
			for(int kernel_column = 0; kernel_column < kernel[0].length; kernel_column++) {
				kernel_sum += kernel[kernel_row][kernel_column];
			}
		}
		if(kernel_sum <= 0) kernel_sum = 1;
		*/
		//System.out.println(kernel_sum);
    	int begin_width = cpu_index * horizontal_part;
    	int end_width = (cpu_index + 1) * horizontal_part;
    	
    	//int begin_height = cpu_index * vertical_part;
    	//int end_height= (cpu_index + 1) * vertical_part;
    	
    	BufferedImage inputImage = this.inputImage;
    	int[][] kernel = this.kernel;
    	int kernel_sum = MultiCoreConvolution.kernel_sum;
    	
    	
		for(int row = begin_width; row < end_width; row++) {
			for(int column = 0; column < inputImage.getHeight(); column++) {
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
				
				if(( (row + (kernel.length >> 1)) < inputImage.getWidth()) && ( (column + (kernel[0].length >> 1)) < inputImage.getHeight())) {
					
			          rSum = rSum / kernel_sum;
			          if (rSum < 0) rSum = 0;
			          if (rSum > 255) rSum = 255;
			          
			          
			          gSum = gSum / kernel_sum;
			          if (gSum < 0) gSum = 0;
			          if (gSum > 255) gSum = 255;

			          bSum = bSum / kernel_sum;
			          if (bSum < 0) bSum = 0;
			          if (bSum > 255) bSum = 255;
					
			          resultImage.setRGB(row + (kernel.length >> 1), column + (kernel[0].length >> 1), (rSum << 16) + (gSum << 8) + bSum);
				}
			}
			
		}
	}

	@Override
	public void run() {
		apply();
	}
	
	public static BufferedImage applyConvolution(BufferedImage inputImage, int[][] kernel) {
		
		MultiCoreConvolution.inputImage = inputImage;
		MultiCoreConvolution.resultImage = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(), inputImage.getType());
		MultiCoreConvolution.kernel = kernel;
		
		for(int kernel_row = 0; kernel_row < kernel.length; kernel_row++) {
			for(int kernel_column = 0; kernel_column < kernel[0].length; kernel_column++) {
				kernel_sum += kernel[kernel_row][kernel_column];
			}
		}
		if(kernel_sum <= 0) kernel_sum = 1;
		
		horizontal_part = inputImage.getWidth() / cpus;
		vertical_part = inputImage.getHeight() / cpus;
		
		Thread[] threads = new Thread[cpus];
		MultiCoreConvolution[] parts = new MultiCoreConvolution[cpus];
		
		for(int index = 0; index < cpus; index++) {
			parts[index] = new MultiCoreConvolution(index);
			threads[index] = new Thread(parts[index]);
			threads[index].setDaemon(true);
		}
		
		for(int index = 0; index < threads.length; index++) {
			threads[index].start();
		}
		
		for(int index = 0; index < threads.length; index++) {
			try {
				threads[index].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		kernel_sum = 0;
		return resultImage;
	}
	
}
