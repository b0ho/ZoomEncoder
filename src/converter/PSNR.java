package converter;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

/**
 * PSNR 값을 계산합니다.
 */
public class PSNR {
	
	public static double mesurePSNR(File original, File target) {
		if (original == null) {
			return Double.NaN;
		}
		if (target == null) {
			return Double.NaN;
		}
		
		BufferedImage originalBuf = null;
		BufferedImage targetBuf = null;
		
		try {
			originalBuf = ImageIO.read(original);
			targetBuf = ImageIO.read(target);
		} catch (IOException e) {
			return Double.NaN;
		}
		
		if (originalBuf.getWidth() != targetBuf.getWidth() || originalBuf.getHeight() != targetBuf.getHeight()) {
			return Double.NaN;
		}
		
		double mse = 0;
		
		for (int y = 0; y < originalBuf.getHeight(); ++y) {
			for (int x = 0; x < originalBuf.getWidth(); ++x) {
				Color o = new Color(originalBuf.getRGB(x, y));
				Color t = new Color(targetBuf.getRGB(x, y));
				
				mse += pow(o.getRed() - t.getRed());
				mse += pow(o.getGreen() - t.getGreen());
				mse += pow(o.getBlue() - t.getBlue());
			}
		}

		mse /= originalBuf.getWidth()*originalBuf.getHeight()*3;
		
		if (mse == 0) {
			return Double.NaN;
		}
		
		double psnr = 20 * Math.log10(255) - 10 * Math.log10(mse);
		
		return psnr;
	}
	
	private static double pow(int n) {
		return n*n;
	}
}