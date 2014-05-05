package com.app.ivoke.helpers;

import com.app.ivoke.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SettingsHelper {
	
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
}