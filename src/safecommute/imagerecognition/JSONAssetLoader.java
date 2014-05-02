package safecommute.imagerecognition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.opencv.core.Mat;

import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/* Uses googles gson library to load json images from assets */
public class JSONAssetLoader implements AssetLoader{
	
	private static final String TAG = "json_loader";

	@Override
	public Mat loadMatrix(String path) {
		String json = "";
		JsonParser parser = new JsonParser();
		JsonObject JsonObject = parser.parse(json).getAsJsonObject();

	    int rows = JsonObject.get("rows").getAsInt();
	    int cols = JsonObject.get("cols").getAsInt();
	    int type = JsonObject.get("type").getAsInt();

	    String dataString = JsonObject.get("data").getAsString();       
	    byte[] data = Base64.decode(dataString.getBytes(), Base64.DEFAULT); 

	    Mat mat = new Mat(rows, cols, type);
	    mat.put(0, 0, data);

	    return mat;
	}
	
	/* Only for testing and saving initial test matrices */
	/* DON'T USE IN FINAL!!! */
	public void saveMatrix(Mat matrix) {
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
	        
	        String fullPath = Environment.getExternalStorageDirectory().getPath() +
	                File.separator+ "testJson"+ ".txt";
	        try {
	        	saveDataWithFileStream(fullPath, json);
	        }
	        catch (IOException ex) {
	        	Log.e(TAG, "Couldn't write that shit");
	        }
	        
	    } else {
	        Log.e(TAG, "Mat not continuous.");
	    }
	}
	
	private void saveDataWithFileStream(String fullPath, String data) throws IOException{
		File file = new File(fullPath);
		FileOutputStream fs = null;
		OutputStreamWriter os = null;
		try {           
        	Log.e(TAG, fullPath);
            fs = new FileOutputStream(file);
            //fs.write(data.getBytes());
            os = new OutputStreamWriter(fs);
            os.write(data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	if(os != null) { os.close(); }
        	if(fs != null) { fs.close(); }
        }
	}

}
