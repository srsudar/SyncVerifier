package org.opendatakit.syncverifier.util;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by sudars on 9/21/14.
 */
public class SyncVerifierUtil {

  public static final String ACCOUNT_TYPE_GOOGLE = "com.google";

  public static void toast(Context context, int stringId) {
    Toast.makeText(context, stringId, Toast.LENGTH_SHORT).show();
  }

  public static void toast(Context context, String string) {
    Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
  }

  public static String getTableListUrl(String baseUrl) {
    return
        baseUrl + "/odktables/tables/tables";
  }

  public static EditText getEditText(View rootView, int id) {
    EditText result = (EditText) rootView.findViewById(id);
    return result;
  }

  public static Spinner getSpinner(View rootView, int id) {
    Spinner result = (Spinner) rootView.findViewById(id);
    return result;
  }

  public static Button getButton(View rootView, int id) {
    Button result = (Button) rootView.findViewById(id);
    return result;
  }

  public static TextView getTextView(View rootView, int id) {
    TextView result = (TextView) rootView.findViewById(id);
    return result;
  }

  public static CheckBox getCheckBox(View rootView, int id) {
    CheckBox result = (CheckBox) rootView.findViewById(id);
    return result;
  }

}
