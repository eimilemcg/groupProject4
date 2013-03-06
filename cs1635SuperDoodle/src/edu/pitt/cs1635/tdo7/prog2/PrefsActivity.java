/*
 * Tim O'Shea
 * 
 * © 2013 All rights reserved
 */

package edu.pitt.cs1635.tdo7.prog2;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class PrefsActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs);
	} // end of on create
}
