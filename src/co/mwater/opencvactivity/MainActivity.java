package co.mwater.opencvactivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static final String TAG = MainActivity.class.getSimpleName();
	private static int RESULT_OPENCV_ACTIVITY = 0;
	private static int RESULT_EC_PLATES_IMAGE = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_OPENCV_ACTIVITY && resultCode == RESULT_OK
				&& null != data) {
			String result = data.getStringExtra("result");
			Toast.makeText(this, result, Toast.LENGTH_LONG).show();
		}
		if (requestCode == RESULT_EC_PLATES_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();

			Intent intent = new Intent(this, OpenCVActivity.class);
			intent.putExtra("title", "EC Compact Dry Plate");
			intent.putExtra("processId", "ec-plate");
			intent.putExtra("processParams", new String[] { picturePath });
			startActivityForResult(intent, RESULT_OPENCV_ACTIVITY);
		}
	}

	public void testDemo(View view) {
		Intent intent = new Intent(this, OpenCVActivity.class);
		intent.putExtra("title", "Title");
		intent.putExtra("processId", "demo");
		intent.putExtra("processParams", new String[] { "param1" });
		startActivityForResult(intent, RESULT_OPENCV_ACTIVITY);
	}

	public void testECPlates(View view) {
		// Pick photo
		Intent i = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

		startActivityForResult(i, RESULT_EC_PLATES_IMAGE);
	}
}