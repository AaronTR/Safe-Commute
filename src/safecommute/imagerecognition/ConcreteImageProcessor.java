package safecommute.imagerecognition;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;

import safecommute.main.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

public class ConcreteImageProcessor implements ImageProcessor{

//	 From opencv source. Use DescriptorExtractor.WHATEVER
//	 Will try to focus on OBR detector and extractor with BRUTEFORCE matcher
//********************************************************
//	 private static final int
//     	OPPONENTEXTRACTOR = 1000;
//
//
//	 public static final int
//	     SIFT = 1,
//	     SURF = 2,
//	     ORB = 3,
//	     BRIEF = 4,
//	     BRISK = 5,
//	     FREAK = 6,
//	     OPPONENT_SIFT = OPPONENTEXTRACTOR + SIFT,
//	     OPPONENT_SURF = OPPONENTEXTRACTOR + SURF,
//	     OPPONENT_ORB = OPPONENTEXTRACTOR + ORB,
//	     OPPONENT_BRIEF = OPPONENTEXTRACTOR + BRIEF,
//	     OPPONENT_BRISK = OPPONENTEXTRACTOR + BRISK,
//	     OPPONENT_FREAK = OPPONENTEXTRACTOR + FREAK;
	
//	 DescriptorMatcher
//********************************************************
//	public static final int
//    	FLANNBASED = 1,
//    	BRUTEFORCE = 2,
//    	BRUTEFORCE_L1 = 3,
//    	BRUTEFORCE_HAMMING = 4,
//    	BRUTEFORCE_HAMMINGLUT = 5,
//    	BRUTEFORCE_SL2 = 6;
	
//	FeatureDetector
//********************************************************
//  private static final int
//    	GRIDDETECTOR = 1000,
//    	PYRAMIDDETECTOR = 2000,
//    	DYNAMICDETECTOR = 3000;
//
//	public static final int
//    	FAST = 1,
//    	STAR = 2,
//    	SIFT = 3,
//    	SURF = 4,
//    	ORB = 5,
//    	MSER = 6,
//    	GFTT = 7,
//    	HARRIS = 8,
//    	SIMPLEBLOB = 9,
//    	DENSE = 10,
//    	BRISK = 11,
//    	GRIDRETECTOR = 1000,
//    	GRID_FAST = GRIDDETECTOR + FAST,
//    	GRID_STAR = GRIDDETECTOR + STAR,
//    	GRID_SIFT = GRIDDETECTOR + SIFT,
//    	GRID_SURF = GRIDDETECTOR + SURF,
//    	GRID_ORB = GRIDDETECTOR + ORB,
//    	GRID_MSER = GRIDDETECTOR + MSER,
//    	GRID_GFTT = GRIDDETECTOR + GFTT,
//    	GRID_HARRIS = GRIDDETECTOR + HARRIS,
//    	GRID_SIMPLEBLOB = GRIDDETECTOR + SIMPLEBLOB,
//    	GRID_DENSE = GRIDDETECTOR + DENSE,
//    	GRID_BRISK = GRIDDETECTOR + BRISK,
//    	PYRAMID_FAST = PYRAMIDDETECTOR + FAST,
//    	PYRAMID_STAR = PYRAMIDDETECTOR + STAR,
//    	PYRAMID_SIFT = PYRAMIDDETECTOR + SIFT,
//    	PYRAMID_SURF = PYRAMIDDETECTOR + SURF,
//    	PYRAMID_ORB = PYRAMIDDETECTOR + ORB,
//    	PYRAMID_MSER = PYRAMIDDETECTOR + MSER,
//    	PYRAMID_GFTT = PYRAMIDDETECTOR + GFTT,
//    	PYRAMID_HARRIS = PYRAMIDDETECTOR + HARRIS,
//    	PYRAMID_SIMPLEBLOB = PYRAMIDDETECTOR + SIMPLEBLOB,
//    	PYRAMID_DENSE = PYRAMIDDETECTOR + DENSE,
//    	PYRAMID_BRISK = PYRAMIDDETECTOR + BRISK,
//    	DYNAMIC_FAST = DYNAMICDETECTOR + FAST,
//    	DYNAMIC_STAR = DYNAMICDETECTOR + STAR,
//    	DYNAMIC_SIFT = DYNAMICDETECTOR + SIFT,
//    	DYNAMIC_SURF = DYNAMICDETECTOR + SURF,
//    	DYNAMIC_ORB = DYNAMICDETECTOR + ORB,
//    	DYNAMIC_MSER = DYNAMICDETECTOR + MSER,
//    	DYNAMIC_GFTT = DYNAMICDETECTOR + GFTT,
//    	DYNAMIC_HARRIS = DYNAMICDETECTOR + HARRIS,
//    	DYNAMIC_SIMPLEBLOB = DYNAMICDETECTOR + SIMPLEBLOB,
//    	DYNAMIC_DENSE = DYNAMICDETECTOR + DENSE,
//    	DYNAMIC_BRISK = DYNAMICDETECTOR + BRISK;

	public static final String TAG = "image_processor";
	
	private static final String ASSET_BASE = "";
	private static final int[] JSON_IDS = {R.raw.testjson_0, R.raw.testjson_1, R.raw.testjson_2, R.raw.testjson_3};
	
	private Context mContext;
	private MatrixConverter mConverter;
	private DescriptorExtractor mExtractor;
	private DescriptorMatcher mMatcher;
	private FeatureDetector mDetector;
	private float mSimilarity;
	
	//just for alpha
	private Bitmap comparisonImage;
	
	
	public ConcreteImageProcessor(Context context, MatrixConverter converter, int extractorType, int matcherType, int detectorType) {
		mContext = context;
		mConverter = converter;
		mExtractor = DescriptorExtractor.create(extractorType);
		mMatcher = DescriptorMatcher.create(matcherType);
		mDetector = FeatureDetector.create(detectorType);
	}
	
	
	/* Decided that our best option is to load images from json files in assets
	 * this now returns the best found similarity percentage based on the calculate function
	 * @see safecommute.imagerecognition.ImageProcessor#processAgainstOneImage(safecommute.imagerecognition.Image, java.lang.String)
	 */
	@Override
	public double processAgainstAllImages(Image image) {
		double bestPercentage = 0;
		
		Mat newDescriptors = computeImageDescriptors(image.toMat());
		for(int i=0; i < JSON_IDS.length; i++) {
			String json = "";
			try {
				json = loadJsonFromResources(mContext, JSON_IDS[i]);
				Log.d(TAG, json);
				Mat testImage = mConverter.convertToMatrix(json);
				Log.d(TAG, testImage.toString());
				double percentMatch = 0;
				try {
					Mat testDescriptors = computeImageDescriptors(testImage);
					
					MatOfDMatch matches = getMatchesFromDescriptors(newDescriptors, testDescriptors);
					percentMatch = calculateSimilarityPercentage(matches);
				}
				catch (CvException e) {
					e.printStackTrace();
				}
				
				if(percentMatch > bestPercentage) {
					bestPercentage = percentMatch;
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		return bestPercentage;
	}

	@Override
	public double calculateSimilarityPercentage(MatOfDMatch matches) {
		
		return 100.0;
	}

	@Override
	public Image resizeImage(Image image) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Mat computeImageDescriptors(Mat imageMat) {
		MatOfKeyPoint keypoints = new MatOfKeyPoint();
		mDetector.detect(imageMat, keypoints);
		Mat descriptors = new Mat();
		mExtractor.compute(imageMat, keypoints, descriptors);
		keypoints.release();
		imageMat.release();
		
		return descriptors;
	}
	
	private MatOfDMatch getMatchesFromDescriptors(Mat descOne, Mat descTwo) {
		MatOfDMatch matches = new MatOfDMatch();
		mMatcher.match(descOne, descTwo, matches);
		descOne.release();
		descTwo.release();
		
		return matches;
	}
	
	private String loadJsonFromResources(Context context, int id) throws IOException{
		InputStream is = context.getResources().openRawResource(id);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		int counter;
		String json = null;
		try {
		    counter = is.read();
		    while (counter != -1) {
		        baos.write(counter);
		        counter = is.read();
		    }
		    json = baos.toString();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		finally {
			baos.close();
			is.close();
		}
		
		return json;
	}
}
