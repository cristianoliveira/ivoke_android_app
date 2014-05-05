package com.app.ivoke.controllers.main;

import java.util.List;

import com.app.ivoke.R;
import com.app.ivoke.Router;
import com.app.ivoke.helpers.DebugHelper;
import com.app.ivoke.helpers.MessageHelper;
import com.app.ivoke.helpers.SettingsHelper;
import com.app.ivoke.models.FacebookModel;
import com.app.ivoke.models.MuralModel;
import com.app.ivoke.objects.MuralPost;
import com.app.ivoke.objects.UserIvoke;
import com.app.ivoke.objects.interfaces.IAsyncCallBack;

import android.renderscript.ProgramFragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.facebook.Session;

public class MainActivity extends ActionBarActivity {
    
	public static final String PE_USER_IVOKE       = "MainActivity.UserIvoke";
	public static final String PE_FACEBOOK_SESSION = "MainActivity.FacebookSession";
	
	DebugHelper dbg = new DebugHelper("MainActivity");
	
	/* Declare Models */
	MuralModel muralModel = new MuralModel();
	  
	/* Declare Fragments */
	MuralFragment muralFragment = new MuralFragment(); 
	ProcessingFragment processingFragment = new ProcessingFragment();
	
	/*  Declare other vars */
	UserIvoke user;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	dbg.method("onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	Session session = 
        			(Session) extras.getSerializable(PE_FACEBOOK_SESSION);
        	
        	user   = (UserIvoke) extras.getSerializable(PE_USER_IVOKE);
        	
        	dbg.var("session", session)
        	   .var("user", user);
        }
        
        if (savedInstanceState == null) {
        	showMuralFragment();
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
        
        switch (id) {
		case R.id.main_act_menu_mural:
			
			showMuralFragment();
		    
            break;
		case R.id.main_act_menu_contacts:
			
			showProcessingFragment();
			
			break;
		case R.id.main_act_menu_settings:
			
			Router.gotoSettings(this);
			
			break;
		default:
			break;
		}
        
        return super.onOptionsItemSelected(item);
    }
    
    private void showMuralFragment() {
    	try {
    	 	muralModel.asyncGetNearbyPosts( user.getLocalization() 
    	 			                      , SettingsHelper.getMuralPostDistance(this)
    	 			                      , new MuralCallback());
    	} catch (Exception e) {
			MessageHelper.errorAlert(this)
						 .setMessage(R.string.msg_error_ws_server_not_responding)
						 .showDialog();
		}
   }

	private void showProcessingFragment()
    {
		showFragment(processingFragment);
    }
    
    private void showFragment(Fragment pFragment)
    {
    	getSupportFragmentManager()
		.beginTransaction().replace(R.id.container, pFragment).commit();
    }
    
    public class MuralCallback implements IAsyncCallBack
    {	
    	List<MuralPost> posts;
    	@Override
		public void onCompleteTask(Object pResult) {
    		dbg._class(this).method("onCompleteTask").par("pResult", pResult);
    		
    		try {
    			
				muralFragment.setPosts(posts);
				showFragment(muralFragment);
				
	    	} catch (Exception e) {
				dbg.exception(e);
			}
			
		}

		@Override
		public void onPreExecute() {
			showProcessingFragment();
		}

		@Override
		public void onProgress(int pPercent, Object pObject) {}

		@Override
		public void onPreComplete(Object pResult) {
			try {
				String json = pResult.toString();
				posts = new MuralModel().getListPostsFromJSon(json);
			} catch (Exception e) {
				dbg.exception(e);
			}
		}

    }

	public void postMuralPost(String pMessage, IAsyncCallBack pCallback) {
		muralModel.postMuralPost(user, pMessage, pCallback);
	}
    
}
