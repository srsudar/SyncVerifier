package org.opendatakit.syncverifier.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by sudars on 9/21/14.
 */
public class SyncVerifierPreferences {

  private static final String PREF_FILE = "sync_verifier_preferences";

  static class PreferenceKeys {
    public static final String SERVER_URL = "server_url";
  }

  SharedPreferences mSharedPreferences;

  public SyncVerifierPreferences(Context context) {
    this.mSharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context);
  }

  /**
   * Get the url that has been saved.
   * @return
   */
  public String getSavedUrl() {
    return this.mSharedPreferences.getString(PreferenceKeys.SERVER_URL, null);
  }

  /**
   * Save the url. Passing null will remove it from the shared preferences.
   * @param url
   */
  public void saveUrl(String url) {
    SharedPreferences.Editor editor = this.mSharedPreferences.edit();
    if (url == null) {
      editor.remove(PreferenceKeys.SERVER_URL);
    } else {
      editor.putString(PreferenceKeys.SERVER_URL, url);
    }
    editor.commit();
  }

}
