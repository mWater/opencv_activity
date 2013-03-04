package co.mwater.opencvactivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends Activity {
	private static final String TAG = MainActivity.class.getSimpleName();
	static int PETRI_IMAGE_REQUEST = 1;

	boolean autoAnalysing = false; // TODO this could be done better

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
	}
	
	public void testECPlates(View view) {
		Toast.makeText(this, "TEST", Toast.LENGTH_SHORT).show();
	}
}