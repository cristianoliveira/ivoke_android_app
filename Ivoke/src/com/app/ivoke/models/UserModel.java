package com.app.ivoke.models;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.app.ivoke.helpers.WebHelper;
import com.app.ivoke.helpers.WebHelper.WebParameter;
import com.app.ivoke.objects.UserIvoke;
import com.app.ivoke.objects.interfaces.IAsyncCallBack;

public class UserModel extends WebServer {
	
	private UserIvoke user;

	public UserIvoke getUsuario() {
		return user;
	}

	public void setUsuario(UserIvoke usuario) {
		this.user = usuario;
	}
	
	public boolean existe(String pFacebookId) throws Exception
	{
		ArrayList<WebHelper.WebParameter> parametros = new ArrayList<WebHelper.WebParameter>();
		parametros.add(web.parameter("facebook_id", pFacebookId));
		
		String retorno = null;
		
		retorno = web.doPostRequest(site(URL_USER_GET), parametros);
		
		 try {
			 
			 user = UserIvoke.castJson(retorno);
			 
		 } catch (Exception e) {
			return false;
		 }
		 
		 return true;
		
	}
	
	public void asyncGetIvokeUser(String pFacebookId, IAsyncCallBack pCallBack) throws Exception
	{
		ArrayList<WebHelper.WebParameter> parametros = new ArrayList<WebHelper.WebParameter>();
		parametros.add(web.parameter("facebook_id", pFacebookId));
		
		web.doAsyncPostRequest(site(URL_USER_GET), parametros, pCallBack);
	}
	
	public UserIvoke create(String pName, String pFacebookId) throws Exception
	{     
		UserIvoke user = new UserIvoke();
		try 
			{
				ArrayList<WebParameter>  parametros = new ArrayList<WebParameter>();
				parametros.add(web.parameter("nome"       , pName));
				parametros.add(web.parameter("facebook_id", pFacebookId));
				
				String jsonString = web.doPostRequest(site(URL_USUARIO_ADD), parametros);
				
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
				 user.setName(json.getString("nome"));
				 user.setFacebookID(json.getString("facebook_id"));
			 } catch (Exception e) {
				 user = null;
			 }
			
		}

		@Override
		public void onProgress(int pPercent, Object pObject) {
			
		}} 
}
