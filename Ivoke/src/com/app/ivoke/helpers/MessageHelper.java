package com.app.ivoke.helpers;

import com.app.ivoke.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;

public class MessageHelper {
	
	public static MessageAlert errorAlert(Context pContext)
	{
		MessageAlert alert = new MessageAlert(pContext);
		alert.getAlertDialog().setIcon(android.R.drawable.ic_dialog_alert);
		
		return alert;
	}
	
	public static MessageAlert getErrorAlert(Context pContext,String pTitulo, String pMensagem)
	{
		MessageAlert alert = errorAlert(pContext);
		alert.getAlertDialog().setIcon(android.R.drawable.ic_dialog_alert);
		
		return alert;
	}
	
	public static MessageAlert infoAlert(Context pContext)
	{
		MessageAlert alert = new MessageAlert(pContext);
		alert.getAlertDialog().setIcon(android.R.drawable.ic_dialog_info);
		
		return alert;
	}

	public static MessageAlert infoAlert(Context pContext, String pTitulo, String pMensagem)
	{
		MessageAlert alert = infoAlert(pContext);
		alert.getAlertDialog().setTitle(pTitulo);
		alert.getAlertDialog().setMessage(pMensagem);
		
		return alert;
	}
	
	public static AlertDialog warningAlert(Context pContext)
	{
		AlertDialog alert = new AlertDialog.Builder(pContext).create();
		alert.setIcon(android.R.drawable.ic_dialog_alert);
		
		return alert;
	}
	
	@SuppressLint("ShowToast")
	public static Toast toastMessage(Context pContext, int pStringResourceID)
	{
		return Toast.makeText(pContext, pContext.getResources().getString(pStringResourceID), 5000);
	}
	
	@SuppressLint("ShowToast")
	public static Toast toastMessage(Context pContext, int pStringResourceID, Object args)
	{
		String resString = pContext.getResources().getString(pStringResourceID);
		
		if(resString.indexOf("%")>0)
		{
			return Toast.makeText(pContext, String.format(resString, args) , 5000);
		}
		
		return Toast.makeText(pContext, resString , 5000);
	}
	
	@SuppressLint("ShowToast")
	public static Toast getToastMessage(Context pContext, String pMessage)
	{
		return Toast.makeText(pContext, pMessage, 5000);
	}
	
	/* Message BUILDER */
	public static class MessageAlert 
	{
		private AlertDialog alertDialog;
		private static Context msgAlertContext;
		private static Resources resource;
		private static String message;
	
   	    protected MessageAlert(Context context) {
			setAlertDialog(new AlertDialog.Builder(context).create());
			msgAlertContext = context;
			resource = context.getResources();
		}
		
		public void showDialog()
		{
			getAlertDialog().show();
		}
		
		public MessageAlert setTitle(String pTitle)
		{
			getAlertDialog().setTitle(pTitle);
		    return this;
		}
		
		public MessageAlert setTitle(int pStringResourceID)
		{
			getAlertDialog().setTitle(resource.getString(pStringResourceID));
		    return this;
		}
		
		public MessageAlert setMessage(String pMessage)
		{
			message = pMessage;
			getAlertDialog().setMessage(message);
			return this;
		}
		
		public MessageAlert setMessage(int pStringResourceID)
		{
			message = resource.getString(pStringResourceID);
			getAlertDialog().setMessage(message);
			return this;
		}
		
		public MessageAlert setMessage(int pStringResourceID, Object... args)
		{
			message = String.format(resource.getString(pStringResourceID), args);
			getAlertDialog().setMessage(message);
			return this;
		}
		
		public MessageAlert formatMessage(Object args)
		{
			getAlertDialog().setMessage(String.format(message, args));
			return this;
		}
		
		private AlertDialog getAlertDialog() {
			
			if(alertDialog==null)
			{
				alertDialog = new AlertDialog.Builder(msgAlertContext).create();
			}
			
			return alertDialog;
		}

		private void setAlertDialog(AlertDialog pAlertDialog) {
			alertDialog = pAlertDialog;
		}
	}

	public static void unexpectedException(Activity pActivity, Exception e) {
		  errorAlert(pActivity).setMessage(R.string.error_msg_unexpected_exception).showDialog();
		  e.printStackTrace();
	}
}
