package com.app.ivoke.models;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import com.app.ivoke.helpers.DebugHelper;
import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Session.StatusCallback;
import com.facebook.android.Facebook;
import com.facebook.model.GraphUser;


public class FacebookModel {

    DebugHelper debug = new DebugHelper("FacebookModel");

    public static int FACEBOOK_SSO_ACTIVITY_CODE = 64206;

    private GraphUser     facebookUser;
    private Session      activeSession;
    private SessionState statusSession;

    public FacebookModel(){
    }
    public FacebookModel(Activity pActivity, Session pActiveSession)
    {
        setSessaoAtiva(pActivity,pActiveSession);
    }

    public Session openSessionAsync(Activity pActivity, com.facebook.Session.StatusCallback pCallBack)
    {

        debug.method("openSessionAsync").par("pActivity", pActivity).par("pCallBack", pCallBack);
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

    public GraphUser requestFacebookUser()
    {
        debug.method("requestUsuarioFacebook");
        debug.var("activeSession",activeSession);

        Request.newMeRequest(activeSession, new  GraphUserCallback() {
            @Override
            public void onCompleted(GraphUser user, Response response) {
                debug.method("onCompleted").par("user", user).par("response", response);
                facebookUser = user;
            }
        }).executeAndWait();

        return facebookUser;
    }

    public void requestFacebookUser(GraphUserCallback pGraphUserCallback)
    {
        debug.method("requestFacebookUser");
        debug.var("activeSession", activeSession);

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

    public void setUser(String pUserJson) {
        try {
            facebookUser = (GraphUser) new JSONObject(pUserJson);
        } catch (JSONException e) {
            facebookUser = null;
        }
    }

    public boolean hasSessionOpened()
    {
        return activeSession != null? activeSession.isOpened() : false;
    }

    public void logout() {
        if(activeSession != null? activeSession.isOpened() : false)
        {
            activeSession.closeAndClearTokenInformation();
            activeSession.close();
            Session.getActiveSession();
            Session.setActiveSession(null);
        }
    }

}
