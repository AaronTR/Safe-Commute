package safecommute.imagerecognition;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;

import safecommute.main.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;

public class CameraActivity extends Activity implements CvCameraViewListener2, OnTouchListener{
	
	private static final String TAG = "SafeCommute: Image Recognition";
	private static final int EXTRACTOR_ID = DescriptorExtractor.ORB;
	private static final int MATCHER_ID = DescriptorMatcher.BRUTEFORCE;
	private static final int DETECTOR_ID = FeatureDetector.ORB;
	private static final int DEFAULT_SLEEP_TIME = 100;
	private static final double ACCEPTABLE_THRESHOLD = 80.0;
	
	public static final String EXTRA_LABEL = "doesntmatter";
	
	private MyCameraView mOpenCvCameraView;
	
	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
	    @Override
	    public void onManagerConnected(int status) {
	        switch (status) {
	            case LoaderCallbackInterface.SUCCESS:
	            {
	                Log.i(TAG, "OpenCV loaded successfully");
	                mOpenCvCameraView.enableView();
	                mOpenCvCameraView.setOnTouchListener(CameraActivity.this);
	            } break;
	            default:
	            {
	                super.onManagerConnected(status);
	            } break;
	        }
	    }
	};
	
	static {
	    if (!OpenCVLoader.initDebug()) {
	        // Handle initialization error
	    }
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "called onCreate");
	    super.onCreate(savedInstanceState);
	    Log.i(TAG, "Trying to load OpenCV library");
        if (!OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_8, this, mLoaderCallback))
        {
          Log.e(TAG, "Cannot connect to OpenCV Manager");
        }
        else{ 
        	Log.i(TAG, "opencv successfull");
        }
	    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	    setContentView(R.layout.activity_camera);
	    mOpenCvCameraView = (MyCameraView) findViewById(R.id.HelloOpenCvView);
	    mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
	    mOpenCvCameraView.setCvCameraViewListener(this);
	}
	
	@Override
    public void onResume() {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_8, this, mLoaderCallback);
    }
	
	@Override
	protected void onPause() {
		super.onPause();
		if (mOpenCvCameraView != null)
	         mOpenCvCameraView.disableView();
	}
	
	@Override
	public void onDestroy() {
	     super.onDestroy();
	     if (mOpenCvCameraView != null)
	         mOpenCvCameraView.disableView();
	 }

	@Override
	public void onCameraViewStarted(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCameraViewStopped() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		return inputFrame.rgba();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
        //TODO take image and process
		byte[] imageData = null;
		mOpenCvCameraView.takePicture();
		while(imageData == null) {
			try {
				Thread.sleep(DEFAULT_SLEEP_TIME);
				imageData = mOpenCvCameraView.getImageData();
			}
			catch(InterruptedException ie) {
				Log.d(TAG, "Thread inturrupted");
				ie.printStackTrace();
			}
		}
		Image image = new Image(imageData);
		
		ImageProcessor processor = new ConcreteImageProcessor(getApplicationContext() 
				, EXTRACTOR_ID, MATCHER_ID, DETECTOR_ID);
		new AsyncImageProcessor(this, processor).execute(image);

        return false;
	}
	
	private class AsyncImageProcessor extends AsyncTask<Image, String, Boolean> {
		
		//for alpha display only
		private ImageProcessor aImageProcessor;
		private ProgressDialog aDialog;
		private double similarityPercentage;
		
		public AsyncImageProcessor(Activity activity, ImageProcessor imageProcessor) {
			this.aImageProcessor = imageProcessor;
			this.aDialog = new ProgressDialog(activity);
		}
		
		@Override
		protected void onPreExecute() {
			this.aDialog.setMessage("Connecting");
			this.aDialog.show();
		}
		
		@Override
		protected Boolean doInBackground(Image... images) {
			aImageProcessor.processAgainstOneImage(images[0]);
			this.similarityPercentage = aImageProcessor.calculateSimilarityPercentage();
			return true;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			
			if(this.aDialog.isShowing()) {
				this.aDialog.dismiss();
			}
			
			Intent intent = new Intent();
			intent.putExtra(EXTRA_LABEL, (this.similarityPercentage > ACCEPTABLE_THRESHOLD));
			setResult(RESULT_OK, intent);
			finish();
		}
		
	}

}
