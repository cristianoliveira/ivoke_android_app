package com.app.ivoke.objects.adapters;

import java.util.List;

import com.app.ivoke.R;
import com.app.ivoke.objects.MuralPost;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MuralAdapter extends BaseAdapter {

	LayoutInflater lInflater;
	List<MuralPost> itens;
	int itemAtual;
	
    public MuralAdapter(Context pContext, List<MuralPost> pItens) {
    	lInflater = LayoutInflater.from(pContext);
    	itens     = pItens;
	}

    @Override
    public long getItemId(int position) {
      return itens.get(position).getId();
    }

    @Override
    public boolean hasStableIds() {
      return true;
    }

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return itens.size();
	}

	@Override
	public Object getItem(int position) {
		return itens.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View listTemplateView;

		if((listTemplateView = convertView) == null)
			listTemplateView = lInflater.inflate(R.layout.main_mural_list_template
					                            , null);
	    
		ImageView imgProfile = 
				(ImageView) listTemplateView.findViewById(R.id.main_mural_image);
		TextView txtNome     = 
				(TextView) listTemplateView.findViewById(R.id.main_mural_title);
		TextView txtMensagem = 
				(TextView) listTemplateView.findViewById(R.id.main_mural_message);
		TextView txtQuando = 
				(TextView) listTemplateView.findViewById(R.id.main_mural_when);
		
		Bitmap profileImg = itens.get(position).getProfileImage();
		
		if(profileImg!=null)
		   imgProfile.setImageBitmap(profileImg);
		
		txtNome.setText(itens.get(position).getNome());
		txtMensagem.setText(itens.get(position).getMensagem());
		txtQuando.setText(itens.get(position).getQuando());
		
		return listTemplateView;
		
	}
	
	class auxItemList{
		String nomeUsuario;
		String texto;
	}
}
