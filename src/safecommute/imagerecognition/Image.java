package safecommute.imagerecognition;

import java.io.ByteArrayOutputStream;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class Image {
	
	private String absolutePath;
	private static byte[] data;
	private MatOfKeyPoint keypoints;
	
	public Image(byte[] data) {
		this.data = data.clone();
	}
	
	public Image(Drawable drawable) {
		this.data = convertDrawableToByteArray(drawable);
	}
	
	public Image(Bitmap bitmap) {
		this.data = convertBitmapToByteArray(bitmap);
	}
	
	public Mat toMat() {
		Bitmap bm = convertByteArrayToBitmap();
		Mat mat = new Mat();
		Utils.bitmapToMat(bm, mat);
		return mat;
	}
	
	public Bitmap toBitmap() {
	    return convertByteArrayToBitmap();
	}
	
	public void setAbsolutePath(String path) {
		this.absolutePath = path;
	}
	
	public String getAbsolutePath() {
		return this.absolutePath;
	}
	
	private Bitmap convertByteArrayToBitmap() {
		Bitmap bm = BitmapFactory.decodeByteArray(this.data, 0, this.data.length);
	    //convert to RGBA format
	    Bitmap bm32 = bm.copy(Bitmap.Config.ARGB_8888, true);
	    return bm32;
	}
	
	private byte[] convertDrawableToByteArray(Drawable drawable) {
		Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
		return convertBitmapToByteArray(bitmap);
	}
	
	//check for quality change
	//
	private byte[] convertBitmapToByteArray(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		return baos.toByteArray();
	}
}
