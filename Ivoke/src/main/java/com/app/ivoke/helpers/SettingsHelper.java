package com.app.ivoke.helpers;

import com.app.ivoke.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class SettingsHelper {
	
	public static final String SHARED_PREFERENCES = "com.app.ivoke.settings";
	
	public static float getMuralPostDistance(Activity pAct)
	{
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(pAct);
		float inMilles = 
				MetricHelper.converKmToMile(pref.getInt(pAct.getString(R.string.pkey_mural_post_distance), 10));
		return inMilles;
	}
	
	public static boolean askForChecking(Activity pAct)
	{
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(pAct);
		return pref.getBoolean(pAct.getString(R.string.pkey_ask_for_checking), true);
	}
	
	public static int frequencyRefreshMural(Activity pAct)
	{
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(pAct);
		return pref.getInt(pAct.getString(R.string.pkey_frequency_refresh_mural), 5)*1000*60;
	}
	
	public static void setValue(Activity pAct, String pPrefKey, String pString)
	{
		SharedPreferences prefs = pAct.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
        editor.putString(pPrefKey, pString);
        editor.commit();
	}
	
	public static void setValue(Activity pAct, String pPrefKey, int pInt)
	{
		SharedPreferences prefs = pAct.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(pPrefKey, pInt);
        editor.commit();
	}
	
	public static Editor getEditor(Context pContext)
	{
		SharedPreferences prefs = pContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return prefs.edit();
	}
	
	public static SharedPreferences getSharedPreference(Context pContext)
	{
		return pContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
	}
}