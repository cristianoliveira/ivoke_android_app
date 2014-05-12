package com.app.ivoke.controllers.checking;

import org.json.JSONObject;

import com.app.ivoke.R;
import com.app.ivoke.Router;
import com.app.ivoke.helpers.DebugHelper;
import com.app.ivoke.helpers.LocationHelper;
import com.app.ivoke.helpers.MessageHelper;
import com.app.ivoke.helpers.SettingsHelper;
import com.app.ivoke.objects.UserIvoke;
import com.facebook.Session;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphPlace;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
import com.google.android.gms.maps.model.LatLng;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class CheckActivity extends ActionBarActivity {
	
	public static final int RESULT_PLACE_ACT   = 1;	
	public static final int RESULT_GPS_SETTING = 2;
	
	public static final String PE_IVOKE_USER         = "CheckActivity.IvokeUser";
	public static final String PE_FACEBOOK_SESSION   = "CheckActivity.FacebookSession";
	public static final String PE_FACEBOOK_USER_JSON = "CheckActivity.FacebookUser";
	
	private static UserIvoke     user;
	private static GraphUser     fbUser;
	LocationHelper.Listener      locationProvider = new LocationHelper.Listener();
	
	DebugHelper dbg = new DebugHelper("CheckActivity");
	
    private Location userLocation;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		dbg.method("onCreate");
		   
		super.onCreate(savedInstanceState);
		setContentView(R.layout.check_activity);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		parameters();
		findUserLocation(1);
		verifySettings();
	}
	
	private void findUserLocation(int pTimesToTry) {

		ProgressDialog dialog = MessageHelper.ProgressAlert(this, R.string.check_msg_info_find_location);
		dialog.show();
		for (int i = 0; i < pTimesToTry; i++) {
			locationProvider = LocationHelper.getLocationListener(this);
			locationProvider.listenerForUser(user);
			userLocation = locationProvider.getCurrentLocation();
		}
		
		dialog.dismiss();
	}

	private void parameters()
	{
		dbg.method("parameters");
		Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	Session session = 
        			(Session) extras.getSerializable(PE_FACEBOOK_SESSION);
        	
        	user   = (UserIvoke) extras.getSerializable(PE_IVOKE_USER);
        	
        	String jsonFbUser = extras.getString(PE_FACEBOOK_USER_JSON);
        	
         	dbg.var("jsonFbUser" , jsonFbUser);
            try {
            	JSONObject jsonObj = new JSONObject(jsonFbUser);
				fbUser = GraphObject.Factory.create(jsonObj, GraphUser.class);
			} catch (Exception e) {
				dbg.exception(e);
				MessageHelper.errorAlert(this)
				             .setMessage(R.string.check_msg_error_get_fb_user)
				             .showDialog();
				
				Router.gotoFacebookLogin(this);
			}
        	
        	dbg.var("session", session);
        	dbg.var("user"   , user);
        	dbg.var("fbUser" , fbUser);
        }
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		setTextButtonSelecionaLocal();
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    getMenuInflater().inflate(R.menu.check, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		dbg.method("onActivityResult").par("requestCode", requestCode).par("resultCode", resultCode).par("data",data);
		
		if (requestCode == RESULT_PLACE_ACT)
		{
			try {
				Bundle bRetornos = data.getExtras();
				String jsonSelectPlace = bRetornos.getString(PlacesActivity.PE_JSON_SELECTED_PLACE);
				
				user.setLocalChecking(
							GraphObject.Factory.create(new JSONObject(jsonSelectPlace), GraphPlace.class)
						);
			
				setTextButtonSelecionaLocal();
			    
				Router.gotoMain(this, user);
				
			} catch (Exception e) {
				MessageHelper.errorAlert(this)
				             .setMessage(R.string.check_msg_erro_local_selecionado)
				             .showDialog();
			}
		}
		
		if(requestCode == RESULT_GPS_SETTING)
		{
			findUserLocation(5);
			onNextWhitoutCheckingClick(null);
		}
	}
	
	private void setTextButtonSelecionaLocal()
	{
		Button btnSelecioneLocal = (Button) findViewById(R.id.check_btn_seleciona_local);
	   
		if (btnSelecioneLocal == null)
			return;
		
		if(user.getLocalCheckingId() != null)
	    	btnSelecioneLocal.setText(user.getLocalCheckingName());
	    else
	    	btnSelecioneLocal.setText(getResources().getString(R.id.check_btn_seleciona_local));
	}
	
	public void onSelecLocalClick(View pView) {
		Router.gotoPlaces(this, locationProvider.getCurrentLocation());
	}
	
	public void onNextWhitoutCheckingClick(View pView) {
		if(userLocation!=null){
		    user.setLocalization(new LatLng(userLocation.getLatitude(), userLocation.getLongitude()));
		    Router.gotoMain(this, user);
		    this.finish();
		}
		else
		{
			
			MessageHelper.askYesNoAlert( this
					                    , R.string.check_msg_info_on_get_user_local
					                    , new AskOpenGpsSettingsListener());
		    
		}
	}

	private void verifySettings()
	{
		if(!SettingsHelper.askForChecking(this))
		{
			onNextWhitoutCheckingClick(null);
		}
	}
	
	public static class PlaceholderFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.check_fragment, container, false);
		
		ProfilePictureView profilePictureView = 
				(ProfilePictureView) rootView.findViewById(R.id.check_img_foto_usuario_facebook);
	    profilePictureView.setProfileId(fbUser.getId());
	    
	    TextView  txtNomeUsuario = 
	    		(TextView) rootView.findViewById(R.id.check_lbl_nome_usuario);
	    txtNomeUsuario.setText(fbUser.getName());
	    		  
		return rootView;
			
	}
	
	}
	
	private class AskOpenGpsSettingsListener implements DialogInterface.OnClickListener
	{
		@Override
		public void onClick(DialogInterface dialog, int which) {
			
			if(which == MessageHelper.DIALOG_RESULT_YES)
			{
				Intent i = LocationHelper.getIntentGpsSettings();
				startActivityForResult(i, RESULT_GPS_SETTING);
			}
			else
			{
				finish();
			}
			
		}
		
	}
}
