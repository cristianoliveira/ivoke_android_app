package com.app.ivoke.controllers.main;

import java.util.ArrayList;
import java.util.List;

import com.app.ivoke.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.app.ivoke.controllers.checking.CheckActivity;
import com.app.ivoke.models.MuralModel;
import com.app.ivoke.objects.*;
import com.app.ivoke.objects.adapters.MuralAdapter;
import com.google.android.gms.maps.model.LatLng;

public class MuralFragment extends Fragment {
	  
	  MainActivity parentActivity;
	 
	  MuralAdapter adapter;
	  MuralModel muralModel = new MuralModel();
	  
	  @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.main_mural_fragment, container, false);
	    
	    parentActivity = (MainActivity) getActivity();
	    
	    List<MuralPost> postagens = muralModel.getNearbyPosts(parentActivity.user.getLocalization() ,100);
	    
	    ListView listView = (ListView) view.findViewById(R.id.muralListView);
	    adapter = new MuralAdapter(view.getContext(), postagens);
	    listView.setAdapter(adapter);
	    
	    return view;
	  }
}
