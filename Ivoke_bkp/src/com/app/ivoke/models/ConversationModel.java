package com.app.ivoke.models;

import com.app.ivoke.R;
import com.app.ivoke.helpers.DebugHelper;
import com.app.ivoke.objects.UserIvoke;
import com.app.ivoke.objects.interfaces.IAsyncCallBack;

public class ConversationModel extends WebServer {
	DebugHelper dbg = new DebugHelper("ConversationModel");
	
	public boolean getAsyncConvesations(UserIvoke pUser, IAsyncCallBack pCallBack)
	{
		dbg.method("getAsyncConvesations").par("pUser",pUser).par("pCallBack", pCallBack);
		try {
			
			web.doAsyncPostRequest( site(R.string.ws_url_convesations_get)
					              , web.makeListParameter("user_id", pUser.getIvokeID())
					              , pCallBack);
			return true;
		} catch (Exception e) {
			dbg.exception(e);
			return false;
		}
	}
	
}
