package com.app.ivoke.controllers.checking;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.ivoke.R;
import com.app.ivoke.Router;
import com.app.ivoke.helpers.DebugHelper;
import com.app.ivoke.helpers.MessageHelper;
import com.app.ivoke.helpers.SettingsHelper;
import com.app.ivoke.models.FacebookModel;
import com.app.ivoke.objects.UserIvoke;
import com.facebook.Session;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphPlace;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
import com.google.android.gms.maps.model.LatLng;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class CheckActivity extends ActionBarActivity {
	
	public static final int RESULT_PLACE_ACT = 1;	
	
	public static final String PE_IVOKE_USER         = "CheckActivity.IvokeUser";
	public static final String PE_FACEBOOK_SESSION   = "CheckActivity.FacebookSession";
	public static final String PE_FACEBOOK_USER_JSON = "CheckActivity.FacebookUser";
	private static FacebookModel faceModel;
	private static UserIvoke  user;
	private static GraphUser  fbUser;
	
	DebugHelper d = new DebugHelper("CheckActivity");
	
	private static final Location CASA = new Location("") {
        {
        	//-29.165509,-51.173753
            setLatitude(-29.165509);
            setLongitude(-51.173753);
        }
    };
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		d.method("onCreate");
		   
		super.onCreate(savedInstanceState);
		setContentView(R.layout.check_activity);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		parameters();
		verifySettings();
	}
	
	private void parameters()
	{
		d.method("parameters");
		Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	Session session = 
        			(Session) extras.getSerializable(PE_FACEBOOK_SESSION);
        	
        	faceModel = new FacebookModel(this, session);
        	user   = (UserIvoke) extras.getSerializable(PE_IVOKE_USER);
        	
        	String jsonFbUser = extras.getString(PE_FACEBOOK_USER_JSON);
        	
         	d.var("jsonFbUser" , jsonFbUser);
            try {
            	JSONObject jsonObj = new JSONObject(jsonFbUser);
				fbUser = GraphObject.Factory.create(jsonObj, GraphUser.class);
			} catch (Exception e) {
				d.exception(e);
				MessageHelper.errorAlert(this)
				             .setMessage(R.string.check_msg_error_get_fb_user)
				             .showDialog();
				
				Router.gotoFacebookLogin(this);
			}
        	
        	d.var("session", session);
        	d.var("user"   , user);
        	d.var("fbUser" , fbUser);
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
		Log.d("#DEBUG#", "onActivityResult resultCode "+resultCode);
		
		if (resultCode == RESULT_PLACE_ACT)
		{
			try {
				Bundle bRetornos = data.getExtras();
				String jsonSelectPlace = bRetornos.getString(PlacesActivity.PE_JSON_SELECTED_PLACE);
				
				user.setLocalChecking(
							GraphObject.Factory.create(new JSONObject(jsonSelectPlace), GraphPlace.class)
						);
			
				setTextButtonSelecionaLocal();
			    
				Router.gotoMain(this, null);
				
			} catch (Exception e) {
				MessageHelper.errorAlert(this)
				             .setMessage(R.string.check_msg_erro_local_selecionado)
				             .showDialog();
			}
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
		Router.gotoPlaces(this, CASA);
	}
	
	public void onNextWhitoutCheckingClick(View pView) {
		//TODO Only for debug remove this!!
		user.setLocalization(new LatLng(CASA.getLatitude(), CASA.getLongitude()));
		Router.gotoMain(this, user);
	}

	private void verifySettings()
	{
		if(!SettingsHelper.askForChecking(this))
		{
			onNextWhitoutCheckingClick(null);
			this.finish();
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
	

	
	

}
