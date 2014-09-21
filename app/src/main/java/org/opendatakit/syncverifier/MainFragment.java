package org.opendatakit.syncverifier;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.opendatakit.syncverifier.SyncVerifierUtil;

/**
 * The main fragment for testing connections with the server.
 *
 * @author sudar.sam@gmail.com
 */
public class MainFragment extends Fragment {


  protected EditText mEnterServerUrl;
  protected Spinner mAccountSpinner;
  protected CheckBox mUseAnonymousUser;
  protected Button mSaveSettings;

  protected TextView mSavedServerUrl;
  protected TextView mSavedUser;

  protected Button mAuthorizeAccount;
  protected Button mGetTableList;

  public MainFragment() {
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View rootView = inflater.inflate(
        R.layout.fragment_main,
        container,
        false);

    this.mEnterServerUrl = SyncVerifierUtil.getEditText(
        rootView,
        R.id.main_activity_enter_server_url
    );

    this.mAccountSpinner = SyncVerifierUtil.getSpinner(
        rootView,
        R.id.main_activity_account_picker
    );

    this.mUseAnonymousUser = SyncVerifierUtil.getCheckBox(
        rootView,
        R.id.main_activity_user_anonymous_user
    );

    this.mSaveSettings = SyncVerifierUtil.getButton(
        rootView,
        R.id.main_activity_save_settings
    );

    this.mSavedServerUrl = SyncVerifierUtil.getTextView(
        rootView,
        R.id.main_activity_saved_server_url
    );

    this.mSavedUser = SyncVerifierUtil.getTextView(
        rootView,
        R.id.main_activity_saved_user
    );

    this.mAuthorizeAccount = SyncVerifierUtil.getButton(
        rootView,
        R.id.main_activity_authorize_account
    );

    this.mGetTableList = SyncVerifierUtil.getButton(
        rootView,
        R.id.main_activity_get_table_list
    );

    return rootView;
  }

  protected void updateUI() {


  }

  protected boolean serverIsSet() {
    return
        !this.mEnterServerUrl.getText().equals("") &&
            this.mEnterServerUrl.getText() != null;
  }

}
