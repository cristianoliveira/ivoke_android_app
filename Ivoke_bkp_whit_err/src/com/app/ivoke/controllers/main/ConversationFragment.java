package com.app.ivoke.controllers.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import com.app.ivoke.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.app.ivoke.helpers.DebugHelper;
import com.app.ivoke.helpers.MessageHelper;
import com.app.ivoke.models.ConversationModel;
import com.app.ivoke.objects.*;
import com.app.ivoke.objects.adapters.ConversationAdapter;
import com.app.ivoke.objects.interfaces.IAsyncCallBack;

public class ConversationFragment extends Fragment {
	  DebugHelper dbg = new DebugHelper("ConversationFragment");
	  List<Conversation> conversations;
	  ConversationAdapter adapter;
	  MainActivity mainAct;
	  
	  Timer                 muralRefreshTimer;
	  
	  ConversationModel model = new ConversationModel();
	  
	  ConversationCallback callback = new ConversationCallback();
	  
	  ListView conversationLV;
	  
	  @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.main_conversation_fragment, container, false);
	    dbg.method("onCreateView");
	    
	    if(mainAct == null)
		   mainAct = (MainActivity) getActivity();
	    
	    conversationLV = (ListView) view.findViewById(R.id.muralListView);
	    adapter = new ConversationAdapter(view.getContext(), null);
	    conversationLV.setAdapter(adapter);
	    
	    return view;
	  }
	  
	  public void refreshListView()
	  {
		  model.getAsyncConvesations(mainAct.user, callback);
	  }
	  
	  public void showErro(String pErro)
	  {
		  MessageHelper.errorAlert(mainAct)
		               .setMessage(R.string.msg_error_ws_server_not_responding)
		               .showDialog();
	  }
	  
	  private class ConversationCallback implements IAsyncCallBack
	  {
		private String error = "";
		
		@Override
		public void onPreExecute() {
			mainAct.showProcessingFragment();
		}

		@Override
		public void onPreComplete(Object pResult) {
			dbg._class(this).method("onPreComplete").par("pResult", pResult);
			try {
				conversations = model.getListFromJson(pResult.toString());
			} catch (Exception e) {
				e.printStackTrace();
				error = e.getLocalizedMessage();
				dbg._class(this).exception(e);
			}
		}

		@Override
		public void onCompleteTask(Object pResult) {
			adapter.setData(conversations);
			adapter.notifyDataSetChanged();
			
			mainAct.showConversationFragment();
		}

		@Override
		public void onProgress(int pPercent, Object pObject) {}
		
		public String getErrorMessage()
		{
			return error;
		}
		
	  }
	  
	  private class ContatoRefreshCallback implements IAsyncCallBack
	  {
		@Override
		public void onPreExecute() {}

		@Override
		public void onPreComplete(Object pResult) {
			try {
				conversations = model.getListFromJson(pResult.toString());
			} catch (Exception e) {
				showErro(e.getMessage());
			}
		}

		@Override
		public void onCompleteTask(Object pResult) {
			adapter.setData(conversations);
			adapter.notifyDataSetChanged();
		}

		@Override
		public void onProgress(int pPercent, Object pObject) {
			// TODO Auto-generated method stub
			
		}
		  
	  }
}
