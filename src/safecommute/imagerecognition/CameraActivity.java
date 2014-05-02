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
import android.widget.Toast;

public class CameraActivity extends Activity implements CvCameraViewListener2, OnTouchListener{
	
	private static final String TAG = "SafeCommute: Image Recognition";
	private static final int EXTRACTOR_ID = DescriptorExtractor.ORB;
	private static final int MATCHER_ID = DescriptorMatcher.BRUTEFORCE;
	private static final int DETECTOR_ID = FeatureDetector.ORB;
	private static final int DEFAULT_SLEEP_TIME = 100;
	private static final int MAX_SLEEP_TIME = 10000;
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
	
	//this is necessary. not sure why
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
			
		ImageProcessor processor = new ConcreteImageProcessor(getApplicationContext() 
					, new JSONAssetLoader(), EXTRACTOR_ID, MATCHER_ID, DETECTOR_ID);
		new AsyncImageProcessor(this).execute(processor);

        return false;
	}
	
	private class AsyncImageProcessor extends AsyncTask<ImageProcessor, String, Boolean> {
		
		private ProgressDialog aDialog;
		private double similarityPercentage = 0.0;
		
		public AsyncImageProcessor(Activity activity) {
			this.aDialog = new ProgressDialog(activity);
		}
		
		@Override
		protected void onPreExecute() {
			this.aDialog.setMessage("Processing Image");
			this.aDialog.show();
		}
		
		@Override
		protected Boolean doInBackground(ImageProcessor... processors) {
			int totalSleepTime = 0;
			
			byte[] imageData = null;
			mOpenCvCameraView.takePicture();
			while(imageData == null) {
				try {
					if(totalSleepTime > MAX_SLEEP_TIME) {
						Log.e(TAG, "Image capture timed out");
						return false;
					}
					Log.d(TAG, "Thread Sleeping for " + DEFAULT_SLEEP_TIME);
					Thread.sleep(DEFAULT_SLEEP_TIME);
					imageData = mOpenCvCameraView.getImageData();
					totalSleepTime += DEFAULT_SLEEP_TIME;
				}
				catch(InterruptedException ie) {
					Log.d(TAG, "Thread inturrupted");
					ie.printStackTrace();
				}
			}
			
			Image image = new Image(imageData);
			this.similarityPercentage = processors[0].processAgainstAllImages(image);
			
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
