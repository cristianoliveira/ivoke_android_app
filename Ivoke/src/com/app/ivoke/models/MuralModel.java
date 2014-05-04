package com.app.ivoke.models;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.util.Log;

import com.app.ivoke.R;
import com.app.ivoke.Router;
import com.app.ivoke.helpers.DebugHelper;
import com.app.ivoke.helpers.JsonHelper;
import com.app.ivoke.helpers.MessageHelper;
import com.app.ivoke.helpers.WebHelper.NetworkException;
import com.app.ivoke.objects.MuralPost;
import com.app.ivoke.objects.UserIvoke;
import com.app.ivoke.objects.interfaces.IAsyncCallBack;
import com.google.android.gms.maps.model.LatLng;

public class MuralModel extends WebServer {
	
	DebugHelper dbg = new DebugHelper("MuralModel");
	
	public List<MuralPost> getNearbyPosts(LatLng pLatLng, int pDistance)
	{
		dbg.method("getNearbyPosts")
		   .par("pLatLng", pLatLng)
		   .par("pDistance", pDistance);
		
		List<MuralPost> listMuralPosts = new ArrayList<MuralPost>();
		String latLngDistance = String.valueOf(pLatLng.latitude).replace(".",",") + ";" 
				                + String.valueOf(pLatLng.longitude).replace(".",",") + ";"
				                + String.valueOf(pDistance).replace(".",",");
			try {
				JSONArray jsonMuralPosts = 
						web.getJsonArrayFromUrl(site(R.string.ws_url_mural_posts_get_nearby), 
												  web.makeListParameter("lat_lng_distance"
													                   , latLngDistance));
				
				 for (int i = 0; i < jsonMuralPosts.length() ; i++) {
					 JSONObject muralPostJson = jsonMuralPosts.getJSONObject(i);
					 
					 JSONObject userJson =  muralPostJson.getJSONObject("usuario");
					 
					 Bitmap profileImage = null;
					
					try {
						
						String url_face_img = Router.currentContext.getString(R.string.ws_url_facebook_profile_img);
						
						profileImage = 
								 web.getImageFromUrl(String.format(url_face_img, userJson.getString("facebook_id")));
					} catch (Exception e) {
						dbg.exception(e);
					}
				     
					 
					 listMuralPosts.add(
							 new MuralPost(i,
									 userJson.getString("nome"), 
									 muralPostJson.getString("message"), 
									 muralPostJson.getString("posted_at"),
									 profileImage)); 
					 
				 }
				 dbg.log("cast sucess!");
					
			} catch (Exception e) {
				dbg.exception(e);
				e.printStackTrace();
				
				return listMuralPosts;
		    }
		
		return listMuralPosts;
	}
	
	public void asyncGetNearbyPosts(LatLng pLatLng, int pDistance, IAsyncCallBack pCallback)
	{
		dbg.method("getNearbyPosts")
		   .par("pLatLng", pLatLng)
		   .par("pDistance", pDistance);
		
		  String latLngDistance = String.valueOf(pLatLng.latitude).replace(".",",") + ";" 
				                + String.valueOf(pLatLng.longitude).replace(".",",") + ";"
				                + String.valueOf(pDistance).replace(".",",");
			try {
			   
				web.doAsyncRequest(site(R.string.ws_url_mural_posts_get_nearby), 
								   web.makeListParameter("lat_lng_distance", latLngDistance),
								   pCallback);
				
					
			} catch (Exception e) {
				Log.d("##ERRO##", "MuralModel.getNearbyPosts error:"+e.getMessage());
				e.printStackTrace();
				
		    }
			
	}
	
	public List<MuralPost> getListPostsFromJSon(String pJsonString)
	{	
		dbg.method("getListPostsFromJSon").par("pJsonString", pJsonString);
		
		List<MuralPost> listMuralPosts = new ArrayList<MuralPost>();
		
		if(pJsonString !=null)
		try {
				JSONArray jsonMuralPosts = new JSONArray(pJsonString);
				
				 for (int i = 0; i < jsonMuralPosts.length() ; i++) {
					 JSONObject muralPostJson = jsonMuralPosts.getJSONObject(i);
					 
					 JSONObject userJson =  muralPostJson.getJSONObject("usuario");
					 
					 Bitmap profileImage = null;
					
					 
					 
					try {
						
						String url_face_img = Router.currentContext.getString(R.string.ws_url_facebook_profile_img);
						
						profileImage = 
								 web.getImageFromUrl(String.format(url_face_img, userJson.getString("facebook_id")));
						
					} catch (Exception e) {
						dbg.exception(e);
					}
				     
					 listMuralPosts.add(
							 new MuralPost(i,
									 userJson.getString("nome"), 
									 muralPostJson.getString("message"), 
									 muralPostJson.getString("posted_at"),
									 profileImage)); 
					 
				 }
		
				 dbg.log("Cast sucess.");
				 
			} catch (Exception e) {
				dbg.exception(e);
				e.printStackTrace();
				
				return listMuralPosts;
		    }
		return listMuralPosts;
	}
}
