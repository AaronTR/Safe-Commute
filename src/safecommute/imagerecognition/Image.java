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
	
	private static byte[] data;
	
	public Image(byte[] data) {
		this.data = data.clone();
	}
	
	public Image(Drawable drawable) {
		this.data = Converters.convertDrawableToByteArray(drawable);
	}
	
	public Image(Bitmap bitmap) {
		this.data = Converters.convertBitmapToByteArray(bitmap);
	}
	
	public Mat toMat() {
		Bitmap bm = Converters.convertByteArrayToBitmap(this.data);
		Mat mat = new Mat();
		Utils.bitmapToMat(bm, mat);
		return mat;
	}
	
	public Bitmap toBitmap() {
	    return Converters.convertByteArrayToBitmap(this.data);
	}
	
}
