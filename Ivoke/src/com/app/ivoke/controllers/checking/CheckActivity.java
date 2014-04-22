package com.app.ivoke.controllers.checking;

import org.json.JSONObject;

import com.app.ivoke.R;
import com.app.ivoke.controllers.login.LoginActivity;
import com.app.ivoke.controllers.main.MainActivity;
import com.app.ivoke.helpers.AlertHelper;
import com.app.ivoke.helpers.ViewHelper;
import com.app.ivoke.models.FacebookModel;
import com.app.ivoke.objects.UsuarioIvoke;
import com.facebook.Session;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphPlace;
import com.facebook.widget.PlacePickerFragment;
import com.facebook.widget.ProfilePictureView;
import com.google.android.gms.maps.model.LatLng;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CheckActivity extends ActionBarActivity {
	
	public static final int PE_RESULT_PLACE_ACT = 1;	
	
	GraphPlace localChecking;
	
	private static final Location CASA = new Location("") {
        {
        	//-29.165509,-51.173753
            setLatitude(-29.165509);
            setLongitude(-51.173753);
        }
    };
	
	private static FacebookModel faceModel;
	private UsuarioIvoke  usuario;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.check_activity);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	Session session = 
        			(Session) extras.getSerializable(LoginActivity.PE_FACEBOOKSESSION);
        	
        	faceModel = new FacebookModel(this, session);
        	usuario   = (UsuarioIvoke) extras.getSerializable(LoginActivity.PE_USUARIO_IVOKE);
        }
    }
	
	@Override
	protected void onStart() {
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
	
	public void selecionaLocal(Location pLocalUsuario)
	{
		  Intent i = new Intent(this, PlacesActivity.class);
		  i.putExtra(PlacePickerFragment.LOCATION_BUNDLE_KEY   , pLocalUsuario);
		  //i.putExtra(PlacePickerFragment.SEARCH_TEXT_BUNDLE_KEY, "Teste");
		  i.putExtra(PlacePickerFragment.RADIUS_IN_METERS_BUNDLE_KEY, 500);
		  
		  startActivityForResult(i, PE_RESULT_PLACE_ACT);
	}
	
	private Location getLocalUsuario()
	{
		if(localChecking != null)
		{
			return (Location) localChecking.getLocation();
		}
		
		return null;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d("#DEBUG#", "onActivityResult resultCode "+resultCode);
		
		if (resultCode == PE_RESULT_PLACE_ACT)
		{
			Bundle bRetornos = data.getExtras();
			String jsonSelectPlace = bRetornos.getString(PlacesActivity.PE_JSON_SELECTED_PLACE);
			
			try {
				localChecking = GraphObject.Factory.create(new JSONObject(jsonSelectPlace), GraphPlace.class);
				setTextButtonSelecionaLocal();
			} catch (Exception e) {
				AlertHelper.getErrorAlert(this)
				           .setMessage(R.string.check_msg_erro_local_selecionado)
				           .showDialog();
			}
			
			goToMainActivity();
		}
	}
	
	private void setTextButtonSelecionaLocal()
	{
		Button btnSelecioneLocal = (Button) findViewById(R.id.check_btn_seleciona_local);
	   
		if(localChecking != null)
	    	btnSelecioneLocal.setText(localChecking.getName());
	    else
	    	btnSelecioneLocal.setText(getResources().getString(R.id.check_btn_seleciona_local));
	}
	
	public void onSelecLocalClick(View pView) {
		// TODO Auto-generated method stub
		CASA.setAccuracy(1);
		selecionaLocal(CASA);
	}
	
	public void goToMainActivity()
	{
		Intent main = new Intent(this, MainActivity.class);
		Location localUsuario = getLocalUsuario();
		if(localUsuario!=null)
		{
			usuario.setLocalizacao(new LatLng(localUsuario.getLatitude(), localUsuario.getLongitude()));
			main.putExtra(LoginActivity.PE_USUARIO_IVOKE, usuario);
			startActivity(main);
		}
		else
		{
			AlertHelper.getErrorAlert(this).setTitle(R.string.msg_alert_error_title)
			           					   .setMessage(R.string.check_msg_erro_local_selecionado)
			           					   .showDialog();
		}
		
	}
	
	public static class PlaceholderFragment extends Fragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.check_fragment, container, false);
			
			ProfilePictureView profilePictureView = (ProfilePictureView) rootView.findViewById(R.id.check_img_foto_usuario_facebook);
		                       profilePictureView.setProfileId(faceModel.getUsuarioFacebook().getId());
		    
		    TextView  txtNomeUsuario = (TextView) rootView.findViewById(R.id.check_lbl_nome_usuario);
		    		  txtNomeUsuario.setText(faceModel.getUsuarioFacebook().getName());
		    		  
			return rootView;
				
		}
		
	}
	

	
	

}
