package safecommute.imagerecognition;

public interface ImageProcessor {

	public void processAgainstOneImage(Image image);
	
	public void processAgainstAllImages(Image image);
	
	public double calculateSimilarityPercentage();
	
	public Image resizeImage(Image image);
}
