package com.app.ivoke.models;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.util.Log;

import com.app.ivoke.helpers.JsonHelper;
import com.app.ivoke.helpers.MessageHelper;
import com.app.ivoke.helpers.WebHelper.NetworkException;
import com.app.ivoke.objects.MuralPost;
import com.app.ivoke.objects.UserIvoke;
import com.google.android.gms.maps.model.LatLng;

public class MuralModel extends WebServer {
	
	public List<MuralPost> getNearbyPosts(LatLng pLatLng, int pDistance)
	{
		List<MuralPost> listMuralPosts = new ArrayList<MuralPost>();
		String latLngDistance = String.valueOf(pLatLng.latitude).replace(".",",") + ";" 
				                + String.valueOf(pLatLng.longitude).replace(".",",") + ";"
				                + String.valueOf(pDistance).replace(".",",");
			try {
				JSONArray jsonMuralPosts = 
						web.getJsonArrayFromUrl(site(URL_MURAL_POSTS_GET_NEARBY), 
												  web.makeListParameter("lat_lng_distance"
													                   , latLngDistance));
				
				 for (int i = 0; i < jsonMuralPosts.length() ; i++) {
					 JSONObject muralPostJson = jsonMuralPosts.getJSONObject(i);
					 
					 JSONObject userJson =  muralPostJson.getJSONObject("usuario");
					 
					 Bitmap profileImage = null;
					
					try {
						profileImage= 
								 web.getImageFromUrl(String.format(URL_FACEBOOK_PROFILE_IMAGE, 
							                         userJson.getString("facebook_id")))
										             ;
					} catch (Exception e) {
						Log.d("#EXCEPTION#","MuralModel.getNearbyPosts erro:"+e.getMessage());
					}
				     
					 
					 listMuralPosts.add(
							 new MuralPost(i,
									 userJson.getString("nome"), 
									 muralPostJson.getString("message"), 
									 muralPostJson.getString("posted_at"),
									 profileImage)); 
					 
				 }
				 Log.d("##DEBUG##", "MuralModel.getNearbyPosts cast successful!");
					
			} catch (Exception e) {
				Log.d("##ERRO##", "MuralModel.getNearbyPosts error:"+e.getMessage());
				wsLog("getNearbyPosts - Exception:"+e.getMessage());
				e.printStackTrace();
				
				return listMuralPosts;
		    }
			
			
		
		
		return listMuralPosts;
	}
}
