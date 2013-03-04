package co.mwater.opencvactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends Activity {
	private static final String TAG = MainActivity.class.getSimpleName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
	}
	
	@Override
    public void onActivityResult(int requestCode,int resultCode,Intent data)
    {
		super.onActivityResult(requestCode, resultCode, data);
     	String result = data.getStringExtra("result");
     	Log.d(TAG, "result: " + result);
    }
	
	public void testDemo(View view) {
		Intent intent = new Intent(this, OpenCVActivity.class);
		intent.putExtra("title", "Title");
		intent.putExtra("processId", "demo");
		intent.putExtra("processParams", new String[] { "param1" });
		startActivityForResult(intent, 0);
	}

	public void testECPlates(View view) {
		Toast.makeText(this, "TEST", Toast.LENGTH_SHORT).show();
	}
}