package net.junkcode.oculus_test;

import net.appsdoneright.riftlib.util.Quaternion;
import net.appsdoneright.riftlib.util.RiftHandler;
import net.appsdoneright.riftlib.util.Vector3;

public class SimpleRiftHandler implements RiftHandler{

	private volatile Quaternion mQuaternion = new Quaternion();
	
	@Override
	public void onDataReceived(Quaternion q, int frequency) {
		mQuaternion.set(q);
	}

	@Override
	public void onKeepAlive(boolean result) {
		// TODO Auto-generated method stub
		
	}

	public Quaternion getQuaternion(){
		return new Quaternion(mQuaternion);
	}
	
	public float[] getMatrix(){
		return mQuaternion.toMatrix();
	}

	public Vector3 getAngles(){
		return mQuaternion.toAngles();
	}
}
