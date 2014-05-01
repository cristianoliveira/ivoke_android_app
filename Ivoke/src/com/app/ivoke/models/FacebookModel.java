package com.app.ivoke.models;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.app.ivoke.helpers.DebugHelper;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Session.StatusCallback;
import com.facebook.android.Facebook;
import com.facebook.model.GraphUser;


public class FacebookModel {
	
	DebugHelper debug = new DebugHelper("#FACEBOOK MODEL#");
	
	public static int FACEBOOK_SSO_ACTIVITY_CODE = 64206;
	
	private GraphUser	 facebookUser;
	private Session 	 activeSession;
	private SessionState statusSession;
 	
	public FacebookModel(){}
	public FacebookModel(Activity pActivity, Session pActiveSession)
	{
		setSessaoAtiva(pActivity,pActiveSession);
	}
	
	public Session openSessionAsync(Activity pActivity, com.facebook.Session.StatusCallback pCallBack)
	{
		debug.setPrefix("openSessionAsync");
		try {
			
			Session session = Session.openActiveSession(pActivity, true, new StatusCallback() {
				public void call(Session session, SessionState state, Exception exception) {
					debug.log("DEFAULT CAllBACK");
					
					if (session.isOpened())
					{
						debug.log("session is opened");
						
						activeSession = session;
						statusSession = state;
					}
					else
					{
						debug.log("session is closed");
					};
				}
			});
			
			
			if(pCallBack!=null)
			{
				debug.log("session addicioned callback");
				session.addCallback(pCallBack);
			}
			
			return session;
		} catch (Exception e) {
			debug.log("Exception: "+e.getMessage());
			
			return null;
		}
	}
	
	public Session openActiveSession(Activity pActivity, com.facebook.Session.StatusCallback pCallBack)
	{
	    return Session.openActiveSession(pActivity, true, pCallBack);
	}
	
	public void requestUsuarioFacebook()
	{
		debug.setPrefix("requestUsuarioFacebook");
		if(activeSession !=null)
			debug.log("activeSession="+activeSession.toString());
		else
			debug.log("activeSession=NULL");
			
		Request.newMeRequest(activeSession, new  GraphUserCallback() {
			@Override
			public void onCompleted(GraphUser user, Response response) {
				facebookUser = user;
			}
		}).executeAndWait();
	}	
	
	public void requestFacebookUser(GraphUserCallback pGraphUserCallback)
	{
		debug.setPrefix("requestFacebookUser");
		if(activeSession !=null)
			debug.log("requestUsuarioFacebook: activeSession="+activeSession.toString());
		else
			debug.log("requestUsuarioFacebook: activeSession=NULL");
			
		Request.newMeRequest(getActiveSession(), pGraphUserCallback).executeAsync();
	}
	
	public GraphUser getFacebookUser()
	{	
		return facebookUser;
	}
	
	public Session getActiveSession()
	{
		activeSession = Session.getActiveSession();
		return activeSession;
	}
	
	public void setSessaoAtiva(Activity pActivity,Session pSession)
	{
		   Session.setActiveSession(pSession);
		   activeSession = pSession;
		   Request.newMeRequest(pSession, new GraphUserCallback() {
				public void onCompleted(GraphUser user, Response response) {
					facebookUser = user;
				}
			}).executeAsync();
	}
	
	public SessionState getStatusSessao()
	{
		return statusSession;
	}

	public boolean hasSessionActive()
	{
		return activeSession!=null;
	}
	public void setUser(GraphUser pUser) {
		facebookUser = pUser;
	}
}
