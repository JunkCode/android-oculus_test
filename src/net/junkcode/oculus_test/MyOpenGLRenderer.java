package net.junkcode.oculus_test;

/**
 * original source: http://www.intransitione.com/blog/create-a-spinning-cube-with-opengl-es-and-android/
 */
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

public class MyOpenGLRenderer implements Renderer {

	 private Cube mCube;
     private float mCubeRotation;
	 private boolean left;
	 private AndroidRotationSensor sens;

     public MyOpenGLRenderer(Cube mCube, AndroidRotationSensor sens, boolean b){
    	 this.mCube = mCube;
    	 left= b;
    	 this.sens=sens;
     }
     
     @Override
     public void onSurfaceCreated(GL10 gl, EGLConfig config) {
         gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); 
             
         gl.glClearDepthf(1.0f);
         gl.glEnable(GL10.GL_DEPTH_TEST);
         gl.glDepthFunc(GL10.GL_LEQUAL);

         gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,
                   GL10.GL_NICEST);
             
     }

     @Override
     public void onDrawFrame(GL10 gl) {
         gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);        
         gl.glLoadIdentity();
         
         
         gl.glTranslatef(1.0f*(left?-1:+1), 0.0f, -15.0f);
         
         // rotate cube by info from orientation sensor
         gl.glRotatef(sens.getAzimuth(), 0.0f, -1.0f, 0.0f);
         gl.glRotatef(sens.getPitch(), 1.0f, 0.0f, 0.0f);
         gl.glRotatef(sens.getRoll(), 0.0f, 0.0f, 1.0f);
             
         mCube.draw(gl);
            
         gl.glLoadIdentity();                                    
             
         mCubeRotation -= 0.60f; 
     }

     @Override
     public void onSurfaceChanged(GL10 gl, int width, int height) {
         gl.glViewport(0, 0, width, height);
         gl.glMatrixMode(GL10.GL_PROJECTION);
         gl.glLoadIdentity();
         GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 100.0f);
         gl.glViewport(0, 0, width, height);

         gl.glMatrixMode(GL10.GL_MODELVIEW);
         gl.glLoadIdentity();
     }

}
