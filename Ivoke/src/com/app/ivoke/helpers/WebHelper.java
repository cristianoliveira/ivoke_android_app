package com.app.ivoke.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

public class WebHelper {

	String requestCache;
	
	public String doRequest(String url) throws NetworkException
	{
		List<NameValuePair> postParamtros;
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpGet httpget = new HttpGet(url); 
	    HttpResponse response;
	    
	    try {
	    	response = httpclient.execute(httpget);
		    HttpEntity entity = response.getEntity();
		    
		    if (entity != null) {
		        InputStream instream = entity.getContent();
		        requestCache = convertStreamToString(instream);
		        instream.close();
		    }
		} catch (Exception e) {
			throw new NetworkException("Erro de conexão. Motivo:"+e.getMessage(), e);
		}
	    

	     return requestCache;
		   
	}
    
	public String doPostRequest(String url, ArrayList<WebParametro> pParametros) throws ServerException, NetworkException, Exception
	{
		ArrayList<NameValuePair> postParamtros = new ArrayList<NameValuePair>(2);
		for (WebParametro pParametro : pParametros) {
			postParamtros.add(new BasicNameValuePair(pParametro.getKey(), pParametro.getValor()));
		}
		
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(url);
	    HttpResponse response = null;
    	try {
    		httppost.setEntity(new UrlEncodedFormEntity(postParamtros));; 
    	    response = httpclient.execute(httppost);
        } catch (Exception e) {
        	throw new NetworkException("Houve um erro de conexão. Verifique se existe acesso a internet.", e);
		}
        
    	HttpEntity entity = response.getEntity();
        
    	if (entity != null) {
            InputStream instream = entity.getContent();
            requestCache = convertStreamToString(instream);
            instream.close();
        }
    	
    	if(requestCache.contains("[ERROR]"))
    		throw new ServerException("Erro no servidor. Motivo:"+requestCache, null);
        
	    return requestCache;
		   
	}
	
	public String getRequestCache()
	{
		return requestCache;
	}
	
	private static String convertStreamToString(InputStream is) {
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();

	    String line = null;
	    try {
	        while ((line = reader.readLine()) != null) {
	            sb.append(line + "\n");
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            is.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return sb.toString();
	}
	
	public WebParametro newParametro(String pKey, String pValor)
	{
		return new WebParametro(pKey, pValor);
	}
	
	public class WebParametro
	{
		private String key;
		private String valor;
		
		public WebParametro(String pKey, String pValor)
		{
			setKey(pKey);
			setValor(pValor);
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public String getValor() {
			return valor;
		}

		public void setValor(String valor) {
			this.valor = valor;
		}
	}
	
	public class NetworkException extends Exception
	{
		private Exception InnerException;
		private static final long serialVersionUID = 1L;
		
		public NetworkException(String pMensagem, Exception pEx)
		{
			super(pMensagem);
			this.InnerException = pEx;
		}
		
		public Exception getInnerException()
		{
			return InnerException;
		}
	}
	
	public class ServerException extends Exception
	{
		private Exception InnerException;
		private static final long serialVersionUID = 1L;
		
		public ServerException(String pMensagem, Exception pEx)
		{
			super(pMensagem);
			this.InnerException = pEx;
		}
		
		public Exception getInnerException()
		{
			return InnerException;
		}
	}
}
