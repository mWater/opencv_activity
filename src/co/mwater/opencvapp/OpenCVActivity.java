package co.mwater.opencvapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class OpenCVActivity extends Activity {
	OpenCVView openCVView;
	Bitmap bm;
	Thread thread;
	
	boolean aborted = false;
	
	static {
		System.loadLibrary("native_sample");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_open_cv);
		
		openCVView = new OpenCVView(this);
		setContentView(openCVView);
	}

	@Override
	protected void onStart() {
		super.onStart();
		aborted = false;
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		aborted = true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.open_cv, menu);
		return true;
	}
	
	void startProcess(int width, int height) {
		// Create bitmap
		bm = Bitmap.createBitmap(width, height, Config.ARGB_8888);

		// Run in separate thread
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				String ret = runProcess("test", new String[] { "input" }, bm);
				Log.d("co.mwater.opencvapp", "return:"+ret);
			};
		});
		thread.start();
	}

	public native String runProcess(String id, String[] params, Bitmap bitmap);

	/* Updates the bitmap of the screen */
	void updateScreen() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (!aborted) {
					Log.d("co.mwater.opencvapp", "Invalidating...");
					openCVView.invalidate();
				}
			}
		});
	}

	class OpenCVView extends View {
		Paint paint;
		
		public OpenCVView(Context context) {
			super(context);
			
			paint = new Paint();
			paint.setFilterBitmap(true);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);

			// Initialize on first draw
			if (bm == null) {
				startProcess(getWidth(), getHeight());
			}

			// Copy bitmap
			Rect dest = new Rect(0, 0, getWidth(), getHeight());
			canvas.drawBitmap(bm, null, dest, paint);
		}
	}
}

