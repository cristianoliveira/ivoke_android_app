package com.app.ivoke.controllers.welcome;

import com.app.ivoke.Common;
import com.app.ivoke.R;
import com.app.ivoke.Router;
import com.app.ivoke.helpers.DebugHelper;
import com.app.ivoke.helpers.GoogleServicesHelper;
import com.app.ivoke.helpers.MessageHelper;
import com.app.ivoke.libraries.GCMManager;
import com.app.ivoke.objects.DefaultWebCallback;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

public class WelcomeActivity extends Activity {
	 DebugHelper dbg = new DebugHelper("WelcomeActivity");
	 GCMManager gcmManager = new GCMManager();
	
	 RegisterDeviceCallback registerCallback;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		dbg.method("onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_activity);
		
		Router.current(this);
		
		if (GoogleServicesHelper.checkGooglePlayServices(this)) {
            String regid = gcmManager.getRegistrationId(this);
            dbg.var("regid", regid);
            if (regid.isEmpty()) {
            	registerCallback = new RegisterDeviceCallback(this);
            	gcmManager.registerInBackground(registerCallback);
            }else
            {
            	Router.gotoFacebookLogin(this);
            	this.finish();
            }
        }
	}
	
	class RegisterDeviceCallback extends DefaultWebCallback
	{
		Activity actCaller;
		
		public RegisterDeviceCallback(Activity pActivity)
		{
			actCaller = pActivity;
		}
		
		@Override
		public void onCompleteTask(Object pResult) {
			super.onCompleteTask(pResult);
			
			if(pResult!=null)
			{
				Router.gotoFacebookLogin(actCaller);
				actCaller.finish();
			}
		}
	}

}