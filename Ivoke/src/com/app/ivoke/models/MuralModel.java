package com.app.ivoke.models;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.app.ivoke.R;
import com.app.ivoke.helpers.DebugHelper;
import com.app.ivoke.helpers.WebHelper.NetworkException;
import com.app.ivoke.objects.DefaultWebCallback;
import com.app.ivoke.objects.WebParameter;
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
					 
					 JSONObject userJson =  muralPostJson.getJSONObject("user");
					
					 listMuralPosts.add(
							 new MuralPost( muralPostJson.getInt("id")
									      , userJson.getInt("id")
									      , userJson.getString("name")
									      , muralPostJson.getString("message")
									      , muralPostJson.getString("created_at")
									      , muralPostJson.getDouble("distance")
									      , userJson.getString("facebook_id"))); 
					 
				 }
				 dbg.log("cast sucess!");
					
			} catch (Exception e) {
				dbg.exception(e);
				e.printStackTrace();
				
				return listMuralPosts;
		    }
		
		return listMuralPosts;
	}
	
	public void asyncGetNearbyPosts(LatLng pLatLng, float pDistance, IAsyncCallBack pCallback) throws NetworkException
	{
		dbg.method("getNearbyPosts")
		   .par("pLatLng", pLatLng)
		   .par("pDistance", pDistance);
		
		  String latLngDistance = String.valueOf(pLatLng.latitude).replace(".",",") + ";" 
				                + String.valueOf(pLatLng.longitude).replace(".",",") + ";"
				                + String.valueOf(pDistance).replace(".",",");

		  web.doAsyncRequest(site(R.string.ws_url_mural_posts_get_nearby), 
					         web.makeListParameter("lat_lng_distance", latLngDistance),
					         pCallback);
	
	}
	
	public List<MuralPost> getListPostsFromJSon(String pJsonString) throws Exception
	{	
		dbg.method("getListPostsFromJSon").par("pJsonString", pJsonString);
		
		List<MuralPost> listMuralPosts = new ArrayList<MuralPost>();
		
		if(pJsonString !=null)
		{		
			JSONArray jsonMuralPosts = new JSONArray(pJsonString);
				
			 for (int i = 0; i < jsonMuralPosts.length() ; i++) {
				 JSONObject muralPostJson = jsonMuralPosts.getJSONObject(i);
				 
				 JSONObject userJson =  muralPostJson.getJSONObject("user");
 
				 listMuralPosts.add(
						 new MuralPost( muralPostJson.getInt("id")
								      , userJson.getInt("id")
								      , userJson.getString("name")
								      , muralPostJson.getString("message")
								      , muralPostJson.getString("created_at")
								      , muralPostJson.getDouble("distance")
								      , userJson.getString("facebook_id"))); 
				 
			 }
		
				 dbg.log("Cast sucess.");
		 }		 
		return listMuralPosts;
	}
	
	public boolean postMuralPost(UserIvoke pUser, String pMessage, DefaultWebCallback pCallback)
	{
		//:usuario_id, :from, :latitude, :longitude, :message, :posted_at
		if(!pMessage.isEmpty())
		try {
			ArrayList<WebParameter> parameters = new ArrayList<WebParameter>();
			
			parameters.add(new WebParameter("user_id"    , pUser.getIvokeID()));
			parameters.add(new WebParameter("latitude"   , pUser.getLocalization().latitude));
			parameters.add(new WebParameter("longitude"  , pUser.getLocalization().longitude));
			parameters.add(new WebParameter("message"    , pMessage));
			    
			web.doAsyncPostRequest(site(R.string.ws_url_mural_posts_create), parameters, pCallback);
		
		} catch (Exception e) {
			dbg.exception(e);
			return false;
		}
		
		return true;
	}

	public boolean deleteMuralPost(MuralPost pMuralPost) {
		
		try {
			ArrayList<WebParameter> parameters = new ArrayList<WebParameter>();
			parameters.add(new WebParameter("id"    , pMuralPost.getId()));
			    
			web.doAsyncPostRequest(site(R.string.ws_url_mural_posts_delete), parameters, new DefaultWebCallback());
			
			return true;
		
		} catch (Exception e) {
			dbg.exception(e);
			return false;
		}
		
	}
}
