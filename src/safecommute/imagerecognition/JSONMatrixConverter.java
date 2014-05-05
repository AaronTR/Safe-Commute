package safecommute.imagerecognition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import android.content.Context;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/* Uses googles gson library to load json images from assets */
public class JSONMatrixConverter implements MatrixConverter{
	
	private static final String TAG = "json_converter";

	@Override
	public Mat convertToMatrix(String json) {
		JsonParser parser = new JsonParser();
		JsonObject JsonObject = parser.parse(json).getAsJsonObject();

	    int rows = JsonObject.get("rows").getAsInt();
	    int cols = JsonObject.get("cols").getAsInt();

	    String dataString = JsonObject.get("data").getAsString();
	    
	    //json is encoded in hex since it cannot store binary data
	    byte[] data = Base64.decode(dataString.getBytes(), Base64.DEFAULT);
	    Log.d(TAG, "data size: " + data.length);
	    Log.d(TAG, "type: " + CvType.CV_8U);
	    
	    
	    Mat mat = new Mat(rows, cols, CvType.CV_8U);
	    mat.put(0, 0, data);

	    return mat;
	}
	
	/* Only for testing and saving initial test matrices */
	/* DON'T USE IN FINAL!!! */
	public void saveMatrix(Mat matrix, String tag) {
		JsonObject obj = new JsonObject();

	    if(matrix.isContinuous()){
	        int cols = matrix.cols();
	        int rows = matrix.rows();
	        int elemSize = (int) matrix.elemSize();    

	        byte[] data = new byte[cols * rows * elemSize];

	        matrix.get(0, 0, data);

	        obj.addProperty("rows", matrix.rows()); 
	        obj.addProperty("cols", matrix.cols()); 
	        obj.addProperty("type", matrix.type());

	        // We cannot set binary data to a json object, so:
	        // Encoding data byte array to Base64.
	        String dataString = new String(Base64.encode(data, Base64.DEFAULT));

	        obj.addProperty("data", dataString);            

	        Gson gson = new Gson();
	        String json = gson.toJson(obj);
	        
	        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "jsons");
	        dir.mkdirs();
	        File file = new File(dir, "testJson_0" + ".txt");
	        
	        try {
	        	saveDataWithFileStream(file, json);
	        }
	        catch (IOException ex) {
	        	Log.e(TAG, "Couldn't write that shit");
	        }
	        
	    } else {
	        Log.e(TAG, "Mat not continuous.");
	    }
	}
	
	private void saveDataWithFileStream(File file, String data) throws IOException{
		FileOutputStream fs = null;
		PrintWriter pw = null;
		//OutputStreamWriter os = null;
		try {           
        	Log.e(TAG, file.getAbsolutePath());
            fs = new FileOutputStream(file);
            pw = new PrintWriter(fs);
            pw.append(data);
            pw.flush();
            //fs.write(data.getBytes());
            //os = new OutputStreamWriter(fs, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	if(pw != null) { pw.close(); }
        	if(fs != null) { fs.close(); }
        }
	}

}
