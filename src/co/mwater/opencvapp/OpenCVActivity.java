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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.open_cv, menu);
		return true;
	}
	
	void initialize(int width, int height) {
		// Create bitmap
		bm = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Log.d("co.mwater.opencvapp", "width: "+ bm.getWidth());

		runProcess(bm);
//		// SAMPLE
//		Canvas c = new Canvas(bm);
//		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//		paint.setColor(Color.RED);
//		paint.setStrokeWidth((float) 5.0);
//		c.drawCircle(100, 100, 20, paint);
	}

	public native void runProcess(Bitmap bitmap);
	
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
				initialize(getWidth(), getHeight());
			}

			// Copy bitmap
			Rect dest = new Rect(0, 0, getWidth(), getHeight());
			canvas.drawBitmap(bm, null, dest, paint);
		}
	}	
}

