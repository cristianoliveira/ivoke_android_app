package com.app.ivoke.controllers.login;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.ivoke.R;
import com.app.ivoke.Router;
import com.app.ivoke.helpers.DebugHelper;
import com.app.ivoke.helpers.DeviceHelper;
import com.app.ivoke.helpers.MessageHelper;
import com.app.ivoke.models.FacebookModel;
import com.app.ivoke.objects.defaults.DefaultBackgroudWorker;
import com.app.ivoke.objects.defaults.DefaultOkListener;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Session.StatusCallback;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;

public class FacebookLoginActivity extends FragmentActivity {
	
	DebugHelper   debug     = new DebugHelper("FacebookLoginActivity");
	
	FacebookLoginCallBack callBack = new FacebookLoginCallBack();
	FacebookModel facebookModel    = new FacebookModel();
	
	private UiLifecycleHelper uiHelper;
	
	RequestFacebookUserBackground requestFaceUser;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		debug.method("onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.facebook_login_activity);
		uiHelper = new UiLifecycleHelper(this, callBack);
		uiHelper.onCreate(savedInstanceState);
		
		if(DeviceHelper.hasInternetConnection())
		{
			facebookModel.openSessionAsync(this, callBack);
			requestFaceUser = new RequestFacebookUserBackground(this);
		}
		else
		{
			MessageHelper.errorAlert(this)
			            .setMessage(R.string.def_error_msg_whitout_internet_connection)
			            .setButtonOk(new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								finish();
							}
						}).showDialog();
		}
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
		gotoIvokeLoginActivity(facebookModel.getActiveSession());
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
		requestFaceUser = new RequestFacebookUserBackground(this);
		requestFaceUser.setSession(pSession);
		if(requestFaceUser.getStatus() != AsyncTask.Status.RUNNING)
    		requestFaceUser.execute();
    	//gotoIvokeLoginActivity(session);
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
	
	private class RequestFacebookUserBackground extends DefaultBackgroudWorker
	{
		Session session;
		GraphUser fbUser;
		
		public RequestFacebookUserBackground(Activity pActivity) {
			super(pActivity);
		}

		@Override
		protected Object doInBackground(Object... params) {
			debug._class(this).method("doInBackground");
			fbUser = facebookModel.requestFacebookUser();
			
			return true;
		}
		
		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			
			if(fbUser!=null)
			{
				Router.gotoIvokeLogin(getActivityCaller(), session, fbUser);
				finish();
			
			}else
			{
				MessageHelper.errorAlert(getActivityCaller())
				             .setMessage(R.string.facebook_error_msg_request_error)
				             .setButtonOk(new DefaultOkListener(getActivityCaller()) {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									getActivityCaller().finish();
								}
							});
			}
		}
		
		public void setSession(Session pSession)
		{
			session = pSession;
		}
	}
}