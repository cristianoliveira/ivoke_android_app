package com.app.ivoke.models;

import com.app.ivoke.helpers.WebHelper;
import com.app.ivoke.helpers.WebHelper.NetworkException;
import com.app.ivoke.helpers.WebHelper.ServerException;

public class WebServer {
	   
	   public final static String SITE_URL 				     = "http://10.0.2.2:3000";
	   //Usuario
	   public final static String URL_USUARIO_ADD            = "/usuarios.json";
	   public final static String URL_USER_GET               = "/usuarios/existe.json";
	   //Mural
	   public final static String URL_MURAL_POSTS_GET_NEARBY = "/mural_posts/get_nearby";
	   //Debug
	   public final static String URL_LOG_ADD                = "debug.json";
	   
	   //Facebook 
	   public final static String URL_FACEBOOK_PROFILE_IMAGE = "https://graph.facebook.com/%s/picture";
	   
	   WebHelper web = new WebHelper();
	   
	   public String site(String pUrlAccess)
	   {
		   return SITE_URL+pUrlAccess;
	   }
	   
	   public void wsLog(String pMessageLog)
	   {
		   try {
			   web.doPostRequest(URL_LOG_ADD, web.makeListParameter("log", pMessageLog));
			} catch (ServerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NetworkException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	   }
}
