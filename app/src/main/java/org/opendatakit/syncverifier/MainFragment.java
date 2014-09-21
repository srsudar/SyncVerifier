package org.opendatakit.syncverifier;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


/**
 * The main fragment for testing connections with the server.
 *
 * @author sudar.sam@gmail.com
 */
public class MainFragment extends Fragment {

  private static final String TAG = MainFragment.class.getSimpleName();


  protected EditText mEnterServerUrl;
  protected Spinner mAccountSpinner;
  protected CheckBox mUseAnonymousUserCheckBox;
  protected Button mSaveSettings;

  protected TextView mSavedServerUrlView;
  protected TextView mSavedUserView;

  protected Button mAuthorizeAccount;
  protected Button mGetTableList;

  private String mSavedAccountName;
  private String mSavedServerUrl;
  private boolean mUseAnonymousUser;

  public MainFragment() {
    this.mSavedAccountName = null;
    this.mSavedServerUrl = null;
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

    this.mUseAnonymousUserCheckBox = SyncVerifierUtil.getCheckBox(
        rootView,
        R.id.main_activity_user_anonymous_user
    );

    this.mSaveSettings = SyncVerifierUtil.getButton(
        rootView,
        R.id.main_activity_save_settings
    );

    this.mSavedServerUrlView = SyncVerifierUtil.getTextView(
        rootView,
        R.id.main_activity_saved_server_url
    );

    this.mSavedUserView = SyncVerifierUtil.getTextView(
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

  @Override
  public void onResume() {
    super.onResume();
    this.updateUI();
  }

  protected void updateUI() {

    this.mSaveSettings.setEnabled(this.canSaveSettings());

    this.updateSavedSettingsUI();

    this.mAuthorizeAccount.setEnabled(this.canAuthorizeAccount());

    this.mGetTableList.setEnabled(this.isAuthorized());

  }

  protected boolean isAuthorized() {
    Log.e(TAG, "[isAuthorized] unimplemented! returning true");
    return true;
  }

  /**
   * Update the saved values.
   */
  protected void updateSavedSettingsUI() {

    if (this.mSavedServerUrl == null) {
      this.mSavedServerUrlView.setText(R.string.enter_server_url);
    } else {
      this.mSavedServerUrlView.setText(this.mSavedServerUrl);
    }

    if (this.mUseAnonymousUser) {
      this.mSavedUserView.setText(R.string.anonymous_user);
    } else {
      if (this.mSavedAccountName == null) {
        this.mSavedUserView.setText(R.string.choose_account);
      } else {
        this.mSavedUserView.setText(this.mSavedAccountName);
      }
    }

  }

  protected boolean canAuthorizeAccount() {
    boolean savedServerIsValid = this.mSavedServerUrl != null;
    boolean savedUserIsValid =
        this.mUseAnonymousUser ||
            this.mSavedAccountName != null;

    return savedServerIsValid && savedUserIsValid;
  }

  protected boolean canSaveSettings() {
    boolean enteredServerIsValid =
        !this.mEnterServerUrl.getText().equals("") &&
            this.mEnterServerUrl.getText() != null;
    boolean selectedUserIsValid =
        this.mUseAnonymousUserCheckBox.isChecked() ||
            this.mAccountSpinner.getSelectedItem() != null;

    return enteredServerIsValid && selectedUserIsValid;
  }

  /**
   * Save the settings. Assumes that it is valid to save settings, as
   * determined by {@link #canSaveSettings()}.
   * @return
   */
  protected void saveSettings() {

    this.mSavedServerUrl = this.mEnterServerUrl.getText().toString();

    if (this.mUseAnonymousUserCheckBox.isChecked()) {
      this.mUseAnonymousUser = true;
      this.mSavedAccountName = null;
    } else {
      this.mUseAnonymousUser = false;
      this.mSavedAccountName = (String) this.mAccountSpinner.getSelectedItem();
    }

  }

}
