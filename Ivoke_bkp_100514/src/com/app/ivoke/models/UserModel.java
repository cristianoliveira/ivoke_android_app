package com.app.ivoke.models;

import java.util.ArrayList;

import org.json.JSONObject;

import android.util.Log;

import com.app.ivoke.R;
import com.app.ivoke.Router;
import com.app.ivoke.helpers.DebugHelper;
import com.app.ivoke.objects.DefaultWebCallback;
import com.app.ivoke.objects.UserIvoke;
import com.app.ivoke.objects.WebParameter;
import com.app.ivoke.objects.interfaces.IAsyncCallBack;

public class UserModel extends WebServer {
	
	DebugHelper debug = new DebugHelper("UserModel");
	
	private UserIvoke user;

	public UserIvoke getUsuario() {
		return user;
	}

	public void setUsuario(UserIvoke usuario) {
		this.user = usuario;
	}
	
	public boolean existe(String pFacebookId) throws Exception
	{
		ArrayList<WebParameter> parametros = new ArrayList<WebParameter>();
		parametros.add(web.parameter("facebook_id", pFacebookId));
		
		String retorno = null;
		
		retorno = web.doPostRequest(site(R.string.ws_url_user_get), parametros);
		
		 try {
			 
			 user = UserIvoke.castJson(retorno);
			 
		 } catch (Exception e) {
			return false;
		 }
		 
		 return true;
		
	}
	
	public void asyncGetIvokeUser(String pFacebookId, DefaultWebCallback pCallBack) throws Exception
	{
		debug.method("asyncGetIvokeUser").par("pFacebookId", pFacebookId).par("pCallBack", pCallBack);
		
		ArrayList<WebParameter> parametros = new ArrayList<WebParameter>();
		parametros.add(web.parameter("facebook_id", pFacebookId));
		
		web.doAsyncPostRequest(site(R.string.ws_url_user_get), parametros, pCallBack);
		debug.log("FIM");
	}
	
	public DefaultWebCallback asyncRegisterDevice(UserIvoke pUser, String pRegistrationId)
	{
		DefaultWebCallback callback = new DefaultWebCallback();
		
		if(!pRegistrationId.isEmpty())
		{
			ArrayList<WebParameter> parameters = new ArrayList<WebParameter>();
			
			parameters.add(new WebParameter("user_id"      , pUser.getIvokeID()));
			parameters.add(new WebParameter("device_reg_id", pRegistrationId));
			
			web.doAsyncPostRequest(site(R.string.ws_url_user_register_device), parameters, callback);
		}
		else
		{
			callback.onError(Router.currentContext.getString(R.string.def_error_msg_device_not_registred), null);
		}
		
		return callback;
	}
	
	public DefaultWebCallback asyncUnregisterDevice(UserIvoke pUser, String pRegistrationId)
	{
		DefaultWebCallback callback = new DefaultWebCallback();
		
		if(!pRegistrationId.isEmpty())
		{
			ArrayList<WebParameter> parameters = new ArrayList<WebParameter>();
			
			parameters.add(new WebParameter("user_id"      , pUser.getIvokeID()));
			parameters.add(new WebParameter("device_reg_id", pRegistrationId));
			
			web.doAsyncPostRequest(site(R.string.ws_url_user_register_device), parameters, callback);
		}
		else
		{
			callback.onError(Router.currentContext.getString(R.string.def_error_msg_device_not_registred), null);
		}
		
		return callback;
	}
	
	public UserIvoke create(String pName, String pFacebookId) throws Exception
	{   
		debug.method("create").par("pName", pName).par("pFacebookId", pFacebookId);
		UserIvoke user = new UserIvoke();
		try 
			{
				ArrayList<WebParameter>  parametros = new ArrayList<WebParameter>();
				parametros.add(web.parameter("name"       , pName));
				parametros.add(web.parameter("facebook_id", pFacebookId));
				
				String jsonString = web.doPostRequest(site(R.string.ws_url_user_add), parametros);
				
				JSONObject json = new JSONObject(jsonString);
				
				debug.var("jsonString",jsonString);
				
				user.setIvokeID(json.getInt("id"));
				user.setName(pName);
				user.setFacebookID(pFacebookId);
				
				return user;
			} 
			catch (Exception e) 
			{
				Log.d("ERROR", "ERRO:"+e.getMessage());
				throw e;
			}	
	}
	
	public class UserIvokeCallBack extends DefaultWebCallback {
		
		UserIvoke user;
		
		@Override
		public void onCompleteTask(Object pResult) {
			 try {
				 JSONObject json = new JSONObject((String) pResult);
				 user = new UserIvoke();
				 user.setIvokeID(json.getInt("id"));
				 user.setName(json.getString("name"));
				 user.setFacebookID(json.getString("facebook_id"));
			 } catch (Exception e) {
				 user = null;
				 super.onError(null, e);
			 }
			
		}
	} 
	
}
