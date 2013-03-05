package co.mwater.opencvactivity;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.Window;

import co.mwater.opencvactivity.R;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

public class OpenCVActivity extends SherlockActivity {
	private static final String TAG = OpenCVActivity.class.getSimpleName();
	
	OpenCVView openCVView;
	Bitmap bm;
	Thread thread;

	String processId;
	String[] processParams;
	
	boolean aborted = false;
	
	static {
		System.loadLibrary("native_sample");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Get arguments
		setTitle(this.getIntent().getStringExtra("title"));
		processId = this.getIntent().getStringExtra("processId");
		processParams = this.getIntent().getStringArrayExtra("processParams");
		
		// Show progress spinner
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setSupportProgressBarIndeterminateVisibility(true);
		
		// Hide icon
		this.getSupportActionBar().setDisplayShowHomeEnabled(false);
		
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
		getSupportMenuInflater().inflate(R.menu.open_cv, menu);
		return true;
	}
	
	void startProcess(int width, int height) {
		// Prevent orientation changes
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
		
		// Create bitmap
		bm = Bitmap.createBitmap(width, height, Config.ARGB_8888);

		// Run in separate thread
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				final String ret = runProcess(processId, processParams, bm);
				Log.d(TAG, "return:" + ret);
				
				// Finish intent
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
					    Intent intent=new Intent();
					    intent.putExtra("result", ret);
					    setResult(RESULT_OK, intent);
					    finish();
					}
				});
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
					Log.d(TAG, "Invalidating...");
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

