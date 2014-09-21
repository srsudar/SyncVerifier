package org.opendatakit.syncverifier;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by sudars on 9/21/14.
 */
public class SyncVerifierUtil {


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
