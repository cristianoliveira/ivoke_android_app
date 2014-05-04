package com.app.ivoke.controllers.main;

import java.util.List;

import com.app.ivoke.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.app.ivoke.helpers.DebugHelper;
import com.app.ivoke.objects.*;
import com.app.ivoke.objects.adapters.MuralAdapter;

public class MuralFragment extends Fragment {
	  
	  MainActivity mainAct;
	 
	  MuralAdapter adapter;
	 
	  DebugHelper dbg = new DebugHelper("MuralFragment");
	  
	  List<MuralPost> postagens;
	  
	  public void setPosts(List<MuralPost> pPostagens)
	  {
		  postagens = pPostagens;
	  }
	  
	  @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		dbg.method("onCreateView");
	    View view = inflater.inflate(R.layout.main_mural_fragment, container, false);
	    
	    mainAct = (MainActivity) getActivity();
	    
	    ListView listView = (ListView) view.findViewById(R.id.main_mural_posts_list);
	    
	    dbg.log("Before instance adapter");
	    dbg.var("postagens", postagens);
	    
	    adapter = new MuralAdapter(view.getContext(), postagens);
	    dbg.var("adapter", adapter);
	    
	    listView.setAdapter(adapter);
	    
	    return view;
	  }
}
