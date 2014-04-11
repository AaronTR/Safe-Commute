package safecommute.imagerecognition;

import org.opencv.android.JavaCameraView;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.util.AttributeSet;
import android.util.Log;

public class MyCameraView extends JavaCameraView implements PictureCallback{

	private static final String TAG = "CAMERA_VIEW_SCHTUFF";
	public static final String STORAGE_FOLDER = "test_picture_storage";
	
	//TODO consider using temp folder or cache
	private Context mContext;
	private byte[] imageData;
	

    public MyCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    //taken from opencv tutorial
    public void takePicture() {
        Log.i(TAG, "Taking picture");
        
        //acknowledge that newImage will be updated
        //if this is not done, getCapturedImage will return the old newImage before updating since this is async
        this.imageData = null;
        
        // Postview and jpeg are sent in the same buffers if the queue is not empty when performing a capture.
        // Clear up buffers to avoid mCamera.takePicture to be stuck because of a memory issue
        mCamera.setPreviewCallback(null);

        // PictureCallback is implemented by the current class
        mCamera.takePicture(null, null, this);
    }
 
    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        
        // The camera preview was automatically stopped. Start it again.
        mCamera.startPreview();
        mCamera.setPreviewCallback(this);
        
        this.imageData = data.clone();
    }
    
    public byte[] getImageData() {
    	return this.imageData;
    }
	
}
