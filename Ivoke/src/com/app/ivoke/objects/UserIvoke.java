package com.app.ivoke.objects;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.model.GraphPlace;
import com.google.android.gms.maps.model.LatLng;

public class UserIvoke implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int    ivokeID;
	private String name;
	private double localLatitude;
	private double localLongitude;
	
	private String facebookID;
	private String facebookPlaceId;
	private String facebookPlaceName;
	private double facebookPlaceLatitude;
	private double facebookPlaceLongitude;
	
	public String getFacebookID() {
		return facebookID;
	}
	public void setFacebookID(String facebookId) {
		this.facebookID = facebookId;
	}
	public String getName() {
		return name;
	}
	public void setName(String nome) {
		this.name = nome;
	}
	public LatLng getLocalization() {
		if(localLongitude == 0 || localLatitude == 0)
		return null;
		else
		return new LatLng(localLatitude, localLongitude);
	}
	public void setLocalization(LatLng pLatLng) {
		localLatitude  = pLatLng.latitude;
		localLongitude = pLatLng.longitude;
	}
	public int getIvokeID() {
		return ivokeID;
	}
	public void setIvokeID(int ivokeID) {
		this.ivokeID = ivokeID;
	}
	public String getLocalCheckingId() {
		return facebookPlaceId;
	}
	
	public String getLocalCheckingName()
	{
		return facebookPlaceName;
	}
	
	public LatLng getFacebookPlaceLatLng()
	{
		return new LatLng(facebookPlaceLatitude, facebookPlaceLongitude);
	}
	
	public void setLocalChecking(GraphPlace localChecking) {
		
		facebookPlaceName = localChecking.getName();
		facebookPlaceId   = localChecking.getId();
		facebookPlaceLatitude  = localChecking.getLocation().getLatitude();
		facebookPlaceLongitude = localChecking.getLocation().getLongitude();
	}
	
	public static UserIvoke castJson(String pJsonString) throws JSONException
	{
		 UserIvoke user;
		 JSONObject json = new JSONObject(pJsonString);
		 user = new UserIvoke();
		 user.setIvokeID(json.getInt("id"));
		 user.setName(json.getString("nome"));
		 user.setFacebookID(json.getString("facebook_id"));
		 
		 return user;
	}
	

}
