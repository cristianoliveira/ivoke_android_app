package com.app.ivoke.objects;

import android.graphics.Bitmap;

public class MuralPost{
	
	int muralPostId;
	int userId;
    Bitmap profileImg;
    String userName;
	String message;
	String createdAt;
	float  distance;
    
    public MuralPost( int    pMuralPostId
    		        , int    pUserId
    		        , String pUserName
    		        , String pMessage
    		        , String pCreatedAt
    		        , double pDistance
    		        , Bitmap pProfileImage)
    {
    	muralPostId    = pMuralPostId;
        userName       = pUserName;
    	message        = pMessage;
    	createdAt      = pCreatedAt;
    	distance       = (float) pDistance;
    	profileImg     = pProfileImage;
    }
    
    public int getId()
    {
    	return muralPostId;
    }
    
    public String getNome()
    {
    	return userName;
    }
    
    public String getMessage()
    {
    	return message;
    }
    
    public String getQuando()
    {
    	return createdAt;
    }
    
    public Bitmap getProfileImage()
    {
    	return profileImg;
    }
    
}

