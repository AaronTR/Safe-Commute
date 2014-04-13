package safecommute.imagerecognition;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Scalar;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgproc.Imgproc;

import safecommute.main.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

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
	
	private Context mContext;
	private DescriptorExtractor mExtractor;
	private DescriptorMatcher mMatcher;
	private FeatureDetector mDetector;
	private float mSimilarity;
	
	//just for alpha
	private Bitmap comparisonImage;
	
	public ConcreteImageProcessor(Context context, int extractorType, int matcherType, int detectorType) {
		mContext = context;
		mExtractor = DescriptorExtractor.create(extractorType);
		mMatcher = DescriptorMatcher.create(matcherType);
		mDetector = FeatureDetector.create(detectorType);
	}
	
	@Override
	public void processAgainstOneImage(Image image) {
		
		//testing purposes only
		Drawable testDraw = mContext.getResources().getDrawable(R.drawable.testplane);
		Image testImage = new Image(testDraw);
		Mat testMat = image.toMat();
		MatOfKeyPoint testKeypoints = new MatOfKeyPoint();
		mDetector.detect(testMat, testKeypoints);
		Mat testDesc = new Mat();
		mExtractor.compute(testMat, testKeypoints, testDesc);
		testKeypoints.release();
		testMat.release();
		
		Mat imageMat = image.toMat();
		MatOfKeyPoint keypoints = new MatOfKeyPoint();
		mDetector.detect(imageMat, keypoints);
		Mat descriptors = new Mat();
		mExtractor.compute(imageMat, keypoints, descriptors);
		keypoints.release();
		imageMat.release();
		
		MatOfDMatch matches = new MatOfDMatch();
		mMatcher.match(testDesc, descriptors, matches);
		matches.release();
		descriptors.release();
		testDesc.release();
		
		
	}

	@Override
	public void processAgainstAllImages(Image image) {
		// TODO repeated AgainstOne
	}

	@Override
	public double calculateSimilarityPercentage() {
		// TODO Auto-generated method stub
		return 100.0;
	}

	@Override
	public Image resizeImage(Image image) {
		// TODO Auto-generated method stub
		return null;
	}
}
