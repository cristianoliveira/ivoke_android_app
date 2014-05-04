package com.app.ivoke.models;

import com.app.ivoke.R;
import com.app.ivoke.Router;
import com.app.ivoke.helpers.WebHelper;

public class WebServer {
	   
	   public final static String SITE_URL 				     = "http://162.243.30.153";//"http://10.0.2.2:3000";
	   //Usuario
	   //public final static String URL_USUARIO_ADD            = "/usuarios.json";
	   //public final static String URL_USER_GET               = "/usuarios/existe.json";
	   //Mural
	   //public final static String URL_MURAL_POSTS_GET_NEARBY = "/mural_posts/get_nearby";
	   //Debug
	   //public final static String URL_LOG_ADD                = "debug.json";
	   
	   //Facebook 
	   //public final static String URL_FACEBOOK_PROFILE_IMAGE = "https://graph.facebook.com/%s/picture";
	   
	   WebHelper web = new WebHelper();
	   
	   public String site(int resId)
	   {
		   return Router.currentContext.getResources().getString(R.string.ws_url)+ Router.currentContext.getResources().getString(resId);
	   }
}
