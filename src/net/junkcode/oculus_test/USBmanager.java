package net.junkcode.oculus_test;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbRequest;
import android.util.Log;

public class USBmanager implements Runnable{
	final String TAG = "USBmanager";
	protected UsbDevice mDevice;
	private UsbManager manager;
	private UsbDeviceConnection connection;
	private UsbEndpoint endpoint;
	private UsbInterface uinterface;
	
	public USBmanager(Context context){
		manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
		final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
		final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {

			    public void onReceive(Context context, Intent intent) {
			        String action = intent.getAction();
			        if (ACTION_USB_PERMISSION.equals(action)) {
			            synchronized (this) {
			                UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

			                if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
			                    if(device != null){
			                    	mDevice = device;
			                    	Log.d(TAG,"Device Permission Acquired");
			                    	LetsTalk();
			                    }
			                }else{
			                    Log.d("USBPermision", "permission denied for device " + device);
			                }
			                context.unregisterReceiver(this);
			            }
			        }
			    }

			};
		PendingIntent mPermissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
		IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
		context.registerReceiver(mUsbReceiver, filter);
		
		HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
		if(deviceList!=null && deviceList.size()>0){
			for (String key : deviceList.keySet()) {
				UsbDevice device = deviceList.get(key);
				Log.i(TAG,"Conected usb: "+key+" ("+device.getProductId()+"/"+device.getVendorId()+")");
				manager.requestPermission(device, mPermissionIntent);
			}
		}else{
			Log.i(TAG,"No usb");
		}
		
	}
	protected void LetsTalk() {
		
		Log.d(TAG,"usb interfaces: "+mDevice.getInterfaceCount());
		if(mDevice.getInterfaceCount()==1){										//get interface
			uinterface = mDevice.getInterface(0);
			Log.d(TAG,"usb endpoints on i_0: "+uinterface.getEndpointCount());
			if(uinterface.getEndpointCount()==1){								//get endpoint
				endpoint = uinterface.getEndpoint(0);
				Log.d(TAG,"usb on i_0-e_0: "+endpoint.getType());
				
				connection = manager.openDevice(mDevice);						//get connection
				if (connection!=null && connection.claimInterface(uinterface, true)) {
					Log.i(TAG,"Let's read data");
					Thread thread = new Thread(this);
			        thread.start();  
				}
			}
		}
	}
	
	
	//TODO: get the communication right
	@Override
	public void run() {//here the main USB functionality is implemented
		//ByteBuffer buffer = ByteBuffer.allocate(1);
        //UsbRequest request = new UsbRequest();
        //request.initialize(connection, endpoint);
		byte[] message = new byte[56];
		//connection.controlTransfer(0x07, 0x9, 0x0, 0, message, message.length, 0);
		
		
        for (int i=0;i<10;i++) {
            // queue a request on the interrupt endpoint
            //request.queue(buffer, 1);
            synchronized (this) {
                if (connection != null) {
                   
                    byte[] data = new byte[256];
        			connection.bulkTransfer(endpoint, data, 256, 100);		
        			String str="";   ///DEBUG
					for(int j=0;j<data.length;j++)str+=""
							+Character.forDigit(((data[j]>>8)&0x0F),16)
							+Character.forDigit(((data[j])	 &0x0F),16)
							+",";
        			Log.i(TAG,str);
        			
        			byte[] acceldata = Arrays.copyOfRange(data,8, 16);

        			int x = (acceldata[0] << 13) | (acceldata[1] << 5) | ((acceldata[2] & 0xF8) >> 3);
        		    int y = ((acceldata[2] & 0x07) << 18) |(acceldata[3] << 10) | (acceldata[4] << 2) | ((acceldata[5] & 0xC0) >> 6);	 
        		    int z = ((acceldata[5] & 0x3F) << 15) | (acceldata[6] << 7) | (acceldata[7] >> 1);
        		    Log.i(TAG,"x:"+x+" y:"+y+" z:"+z);
                }
            }
        }

		connection.close();

	}
	
       

}
