package safecommute.imagerecognition;

import org.opencv.core.Mat;

public interface MatrixConverter {

	public Mat convertToMatrix(String label);
	
}
