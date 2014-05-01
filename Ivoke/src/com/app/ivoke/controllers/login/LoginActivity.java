package com.app.ivoke.controllers.login;

import org.json.JSONException;

import com.app.ivoke.R;
import com.app.ivoke.controllers.checking.CheckActivity;
import com.app.ivoke.controllers.main.MainActivity;
import com.app.ivoke.helpers.MessageHelper;
import com.app.ivoke.helpers.DebugHelper;
import com.app.ivoke.helpers.ViewHelper;
import com.app.ivoke.helpers.WebHelper.NetworkException;
import com.app.ivoke.helpers.WebHelper.ServerException;
import com.app.ivoke.models.FacebookModel;
import com.app.ivoke.models.UserModel;
import com.app.ivoke.objects.UserIvoke;
import com.app.ivoke.objects.interfaces.IAsyncCallBack;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request.GraphUserCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.UiLifecycleHelper;
import com.facebook.android.Facebook;
import com.facebook.model.GraphUser;
import com.facebook.SessionState;

public class LoginActivity extends android.support.v4.app.FragmentActivity {
	
	static DebugHelper debug = new DebugHelper("#LoginActivity#");
	
	/* Declaração dos parametros (putExtra) desta Activity */
	public static String PE_FACEBOOK_SESSION = "FacebookSession";
	public static String PE_USUARIO_IVOKE   = "UsuarioIvoke";
	
	private Activity loginActivity    = this;
	private FacebookModel faceModel   = new FacebookModel();
	private UserModel usuarioModel =  new UserModel();
	private UserIvoke usuario;
	private Intent checkingIntent;

	private Button btnAcessIvoke;
	private TextView lblFacebook;

	private com.facebook.widget.LoginButton btnFacebook;
	
	private UiLifecycleHelper uiHelper;
	
	private FacebookLoginCallBack faceCallBack = new FacebookLoginCallBack();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		debug.setPrefix("onCreate").log("Inicio.");
		
		if(faceModel.getActiveSession()==null){
			debug.log("Not exists active session.");
		   faceModel.openSessionAsync(loginActivity, new FacebookLoginCallBack());
		}else
		{
			Session.openActiveSession(loginActivity, true, faceCallBack);
			requestFacebookUser();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		debug.setPrefix("onActivityResult").log("requestCode="+requestCode);
		if (requestCode == FacebookModel.FACEBOOK_SSO_ACTIVITY_CODE) {

            // Successfully redirected.
            if (resultCode == Activity.RESULT_OK) {

                    // Check OAuth 2.0/2.10 error code.
                    String error = data.getStringExtra("error");
                    if (error != null) {
                        error = data.getStringExtra("error_type");
                        
                    }
                    else {// SSO successful
                        String access_token = data.getStringExtra("access_token");
                        // from here on, you can do whatever you need
                        debug.log("SSO successful access_token:"+access_token);
                        
                        requestFacebookUser();
                     }
                    debug.log("error="+error);
            }
	     }
		else
		{
			requestFacebookUser();
		}
	}
	   
	public static class PlaceholderFragment extends Fragment {
     	@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.login_fragment, container, false);
			return rootView;
		}
	}
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
	    if (state.isOpened()) {
	        debug.setPrefix("onSessionStateChange").log("Session Logged");
	        btnAcessIvoke = (Button) findViewById(R.id.login_button_login_ivoke);
			
			
	    } else if (state.isClosed()) {
	    	debug.setPrefix("onSessionStateChange").log("Logged out...");
	    }
	    
	    requestFacebookUser();
	}
	
	public void OnClickListener(View pview)
	{
		callCheckingIntent();
	}
	
	private void requestFacebookUser()
	{
		faceModel.requestFacebookUser(new GraphUserCallback() {
			@Override
			public void onCompleted(GraphUser user, Response response) {
				
				if(user!=null){
					faceModel.setUser(user);
					String nome    = user.getName();

				    lblFacebook = (TextView) findViewById(R.id.login_mensagem_facebook_login);
				    lblFacebook.setText("Logado como: "+nome);

				    ivokeLogin();
				    
				    if(checkingIntent==null)
					{
						checkingIntent = new Intent(loginActivity, CheckActivity.class);
						callCheckingIntent();
					}
				}
			}
		});
	}
	
	private void callCheckingIntent()
	{
		debug.log("callCheckingIntent: INICIO");
		
		if(usuario == null)
		{
			debug.log("callCheckingIntent: usuarioIvoke is not null");
			ivokeLogin();
			return;
		
		}else{
		
			debug.log("callCheckingIntent: before test Facebook session test");
			
			if (faceModel.hasSessionActive())
			{
				debug.log("callCheckingIntent: exists facebook session");
				checkingIntent.putExtra(PE_FACEBOOK_SESSION, faceModel.getActiveSession());
				checkingIntent.putExtra(PE_USUARIO_IVOKE  , usuario);
				debug.log("callCheckingIntent: before start checkingActivity");
				startActivity(checkingIntent);
			}
			else
			{
				MessageHelper.toastMessage(this, R.string.login_msg_session_error, "facebook").show();
				debug.log("callCheckingIntent: facebook session not active");
			}
		}

	}

	public void ivokeLogin()
	{
		debug.setPrefix("ivokeLogin");
		
	    String facebookID = faceModel.getFacebookUser().getId();
	    
	    debug.log("facebookID "+facebookID);
	    
		try {
    	    usuarioModel.asyncGetIvokeUser(facebookID, new IAsyncCallBack() {
				@Override
				public void onProgress(int pPercent, Object pObject) {}
				
				@Override
				public void onCompleteTask(Object pResult) {
					String result = pResult.toString();
					
					try {
						usuario = UserIvoke.castJson(result);
					} catch (JSONException e) {
						usuario = null;
					}
					
				}
			});
			
			if(!usuarioModel.existe(facebookID))
				usuario = usuarioModel.create(faceModel.getFacebookUser().getName(),facebookID);
    		else
		    	usuario = usuarioModel.getUsuario();
        	
    		btnAcessIvoke.setText(getString(R.string.login_btn_login_ivoke_continue));

    	}
    	catch (Exception e) 
    	{
    		MessageHelper.errorAlert(this).setMessage(e.getMessage()).showDialog();

    		btnAcessIvoke.setText("Login Ivoke.");

			e.printStackTrace();
		}
	}

	public class FacebookLoginCallBack implements StatusCallback
	{
		@Override
		public void call(Session session, SessionState state, Exception exception) 
		{
			onSessionStateChange(session, state, exception);
		}
	}

}