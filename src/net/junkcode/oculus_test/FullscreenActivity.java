package net.junkcode.oculus_test;

//library for oculus from https://github.com/sebastianherp/riftlibrary/tree/master/riftlibrary
import net.appsdoneright.riftlib.RiftActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;


public class FullscreenActivity extends RiftActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                 WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        
		setContentView(R.layout.activity_fullscreen);
		
		
		/** get usb **/
		//USBmanager usb = new USBmanager(this);
		SimpleRiftHandler simpleRiftHandler = new SimpleRiftHandler();
		setRiftHandler(simpleRiftHandler);
		
		/** setup scene **/
		MyGLSurfaceView glSurfaceView_left = (MyGLSurfaceView)findViewById(R.id.surfaceviewclass_1);
		MyGLSurfaceView glSurfaceView_right = (MyGLSurfaceView)findViewById(R.id.surfaceviewclass_2);
        		
		/** Model **/
		RiftOnDroid model = new RiftOnDroid();
		
		/** Android sensor **/
		final AndroidRotationSensor androidSensor = new AndroidRotationSensor(this);
		
		/** Set viewports **/
		final MyOpenGLRenderer left = new MyOpenGLRenderer(model,androidSensor,simpleRiftHandler,true);
		final MyOpenGLRenderer right = new MyOpenGLRenderer(model,androidSensor,simpleRiftHandler,false);
		glSurfaceView_left.setRenderer(left);
		glSurfaceView_right.setRenderer(right);
		
		/** Link GUI **/
		((Button) findViewById(R.id.buttonSwapper)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				androidSensor.swapAxes();
			}
		});
		((SeekBar) findViewById(R.id.zoomSeekBar)).setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {	
				left.setZoom(progress);
				right.setZoom(progress);
			}
		});
	}
	
	


}
