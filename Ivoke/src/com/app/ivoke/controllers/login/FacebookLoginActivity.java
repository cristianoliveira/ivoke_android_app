package com.app.ivoke.controllers.login;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.ivoke.R;
import com.app.ivoke.Router;
import com.app.ivoke.helpers.DebugHelper;
import com.app.ivoke.models.FacebookModel;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Session.StatusCallback;
import com.facebook.UiLifecycleHelper;

public class FacebookLoginActivity extends FragmentActivity {
	
	DebugHelper   debug     = new DebugHelper("FacebookLoginActivity");
	
	FacebookLoginCallBack callBack = new FacebookLoginCallBack();
	FacebookModel facebookModel    = new FacebookModel();
	
	private UiLifecycleHelper uiHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		debug.method("onCreate");
		
		// Add code to print out the key hash
	    try {
	        PackageInfo info = getPackageManager().getPackageInfo(
	                "com.app.ivoke", 
	                PackageManager.GET_SIGNATURES);
	        for (Signature signature : info.signatures) {
	            MessageDigest md = MessageDigest.getInstance("SHA");
	            md.update(signature.toByteArray());
	            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
	            }
	    } catch (NameNotFoundException e) {
	    	debug.exception(e);
			
	    } catch (NoSuchAlgorithmException e) {
	    	debug.exception(e);
	    }
	    debug.method("onCreate");
		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.facebook_login_activity);
		uiHelper = new UiLifecycleHelper(this, callBack);
		uiHelper.onCreate(savedInstanceState);
		
		facebookModel.openSessionAsync(this, callBack);
		
		 

	}
	
	public View onCreateView(LayoutInflater inflater, 
	                         ViewGroup container, 
	                         Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.facebook_login_activity, container, false);

	    return view;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        debug.method("onActivityResult").par("requestCode",requestCode)
								        .par("resultCode",resultCode)
								        .par("data",data);
        
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
		//if is first time loggin facebook 
		
	}
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
	    debug.method("onSessionStateChange")
	            .par("session",session)
	            .par("state",state)
	            .par("exception",exception);
	    
	    if (state.isOpened()) {
	    	debug.log("Logged!");
	    	
	    	gotoIvokeLoginActivity(session);
			
	    } else if (state.isClosed()) {
	    	debug.log("Logged out...");
	    }
	}
	
	private void gotoIvokeLoginActivity(Session pSession)
	{
		Router.gotoIvokeLogin(this, pSession);
		finish();
	}
	
	public class FacebookLoginCallBack implements StatusCallback
	{
		@Override
		public void call(Session session, SessionState state, Exception exception) 
		{
			debug._class(this).method("call");
			onSessionStateChange(session, state, exception);
		}
	}
}
