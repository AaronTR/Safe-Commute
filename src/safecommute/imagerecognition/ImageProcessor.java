package safecommute.imagerecognition;

import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;

public interface ImageProcessor {

	public Mat computeImageDescriptors(Mat image);
	
	public double processAgainstAllImages(Image image);
	
	public double calculateSimilarityPercentage(MatOfDMatch matches);
	
	public Image resizeImage(Image image);
}
