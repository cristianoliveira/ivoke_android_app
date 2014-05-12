package com.app.ivoke.models;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.app.ivoke.R;
import com.app.ivoke.helpers.DebugHelper;
import com.app.ivoke.objects.Conversation;
import com.app.ivoke.objects.ConvesationMessage;
import com.app.ivoke.objects.DefaultWebCallback;
import com.app.ivoke.objects.MuralPost;
import com.app.ivoke.objects.UserIvoke;
import com.app.ivoke.objects.interfaces.IAsyncCallBack;

public class ConversationModel extends WebServer {
	DebugHelper dbg = new DebugHelper("ConversationModel");
	
	public boolean getAsyncConvesations(UserIvoke pUser, DefaultWebCallback pCallBack)
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

	public List<Conversation> getListFromJson(String pJsonString) throws Exception {
		List<Conversation> list = new ArrayList<Conversation>();
		
		JSONArray jsonMuralPosts = new JSONArray(pJsonString);
		
		 for (int i = 0; i < jsonMuralPosts.length() ; i++) {
			 JSONObject convJson = jsonMuralPosts.getJSONObject(i);
			 
			 
			 Conversation c = new Conversation();
			 c.setId(convJson.getInt("id"));
			 c.setUserOneId(convJson.getInt("user_one_id"));
			 c.setUserTwoId(convJson.getInt("user_two_id"));
			 c.setLastMessage(getConversationMessageFromJSon(convJson.getJSONObject("last_message")));
			 
			 list.add(c);
		 }
		
		return null;
	}
	
	public ConvesationMessage getConversationMessageFromJSon(String JSON)
	{
		JSONObject cmJson;
		try {
			cmJson = new JSONObject(JSON);
		} catch (JSONException e1) {
			cmJson = null;
		}
		
		return getConversationMessageFromJSon(cmJson);
	}
	
	public ConvesationMessage getConversationMessageFromJSon(JSONObject pJSON)
	{	
		ConvesationMessage cm = new ConvesationMessage();
		try {
			cm.setId(pJSON.getInt("id"));
			cm.setConversationId(pJSON.getInt("conversation_id"));
			cm.setUserId(pJSON.getInt("user_id"));
			cm.setMessage(pJSON.getString("message"));
			cm.setCreateAt(pJSON.getString("create_at"));
		} catch (Exception e) {}
		return cm;
	}
	
}
