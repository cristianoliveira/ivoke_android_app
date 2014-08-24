package com.app.ivoke.models;

import java.util.ArrayList;

import org.json.JSONObject;

import android.util.Log;

import com.app.ivoke.R;
import com.app.ivoke.helpers.DebugHelper;
import com.app.ivoke.helpers.WebHelper;
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
	
	public void asyncGetIvokeUser(String pFacebookId, IAsyncCallBack pCallBack) throws Exception
	{
		debug.method("asyncGetIvokeUser").par("pFacebookId", pFacebookId).par("pCallBack", pCallBack);
		
		ArrayList<WebParameter> parametros = new ArrayList<WebParameter>();
		parametros.add(web.parameter("facebook_id", pFacebookId));
		
		web.doAsyncPostRequest(site(R.string.ws_url_user_get), parametros, pCallBack);
		debug.log("FIM");
	}
	
	public UserIvoke create(String pName, String pFacebookId) throws Exception
	{     
		UserIvoke user = new UserIvoke();
		try 
			{
				ArrayList<WebParameter>  parametros = new ArrayList<WebParameter>();
				parametros.add(web.parameter("name"       , pName));
				parametros.add(web.parameter("facebook_id", pFacebookId));
				
				String jsonString = web.doPostRequest(site(R.string.ws_url_user_add), parametros);
				
				JSONObject json = new JSONObject(jsonString);
				
				Log.d("DEBUG", "RETORNO WEB "+jsonString);
				
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
	
	public class UserIvokeCallBack implements IAsyncCallBack{
		
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
			 }
			
		}

		@Override
		public void onProgress(int pPercent, Object pObject) {}

		@Override
		public void onPreExecute() {}

		@Override
		public void onPreComplete(Object pResult) {}
	} 
}