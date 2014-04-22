package com.app.ivoke.objects;

import java.io.Serializable;

import com.facebook.model.GraphPlace;
import com.google.android.gms.maps.model.LatLng;

public class UsuarioIvoke implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int    ivokeID;
	private String facebookID;
	private String nome;
	private LatLng localizacao;
	private GraphPlace localChecking;

	public String getFacebookID() {
		return facebookID;
	}
	public void setFacebookID(String facebookId) {
		this.facebookID = facebookId;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public LatLng getLocalizacao() {
		return localizacao;
	}
	public void setLocalizacao(LatLng localizacao) {
		this.localizacao = localizacao;
	}
	public int getIvokeID() {
		return ivokeID;
	}
	public void setIvokeID(int ivokeID) {
		this.ivokeID = ivokeID;
	}
	public GraphPlace getLocalChecking() {
		return localChecking;
	}
	public void setLocalChecking(GraphPlace localChecking) {
		this.localChecking = localChecking;
	}
	

}
