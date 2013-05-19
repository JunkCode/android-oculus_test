package net.junkcode.oculus_test;

//library for oculus from https://github.com/sebastianherp/riftlibrary/tree/master/riftlibrary
import net.appsdoneright.riftlib.RiftActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;


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
        		
		Cube cube = new Cube();
		AndroidRotationSensor androidSensor = new AndroidRotationSensor(this);
		
		glSurfaceView_left.setRenderer(new MyOpenGLRenderer(cube,androidSensor,simpleRiftHandler,true));
		glSurfaceView_right.setRenderer(new MyOpenGLRenderer(cube,androidSensor,simpleRiftHandler,false));
		
		
	}


}
