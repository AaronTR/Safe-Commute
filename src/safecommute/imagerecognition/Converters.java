package safecommute.imagerecognition;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public final class Converters {

	public static final Bitmap convertByteArrayToBitmap(byte[] data) {
		Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
	    //convert to RGBA format
	    Bitmap bm32 = bm.copy(Bitmap.Config.ARGB_8888, true);
	    return bm32;
	}
	
	public static final byte[] convertDrawableToByteArray(Drawable drawable) {
		Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
		return convertBitmapToByteArray(bitmap);
	}
	
	//check for quality change
	//
	public static final byte[] convertBitmapToByteArray(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		return baos.toByteArray();
	}
}
