package com.app.ivoke.controllers.main;

import com.app.ivoke.R;
import com.app.ivoke.controllers.login.LoginActivity;
import com.app.ivoke.models.FacebookModel;
import com.app.ivoke.objects.UsuarioIvoke;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.facebook.Session;

public class MainActivity extends ActionBarActivity {
    
	FacebookModel faceModel;
	UsuarioIvoke usuario;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.act_menu_configuracoes) {
            return true;
        }
        
        switch (id) {
		case R.id.act_menu_mural:
			getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.container, new MuralFragment()).commit();
            break;
		case R.id.act_menu_contatos:
			getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.container, new ContatoFragment()).commit();
        break;
		case R.id.act_menu_locais:
			getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.container, new PlaceholderFragment()).commit();
            break;
		
		default:
			break;
		}
        
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.main_fragment, container, false);
            return rootView;
        }
    }

}
