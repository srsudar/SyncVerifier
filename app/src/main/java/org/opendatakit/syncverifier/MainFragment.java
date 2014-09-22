package org.opendatakit.syncverifier;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * The main fragment for testing connections with the server.
 *
 * @author sudar.sam@gmail.com
 */
public class MainFragment extends Fragment {

  private static final String TAG = MainFragment.class.getSimpleName();

  private static final String ACCOUNT_TYPE_GOOGLE = "com.google";


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

  private AccountManager mAccountManager;

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
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    this.mAccountManager = AccountManager.get(this.getActivity());
  }

  @Override
  public void onResume() {
    super.onResume();

    Account[] accounts =
        this.mAccountManager.getAccountsByType(ACCOUNT_TYPE_GOOGLE);
    List<String> accountNames = new ArrayList<String>(accounts.length);
    for (int i = 0; i < accounts.length; i++)
      accountNames.add(accounts[i].name);

    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
        this.getActivity(),
        android.R.layout.select_dialog_item,
        accountNames);

    this.mAccountSpinner.setAdapter(adapter);

    this.initListeners();

    this.updateUI();
  }


  protected void updateUI() {

    this.mSaveSettings.setEnabled(this.canSaveSettings());

    this.mAccountSpinner.setEnabled(
        this.mUseAnonymousUserCheckBox.isChecked());

    this.updateSavedSettingsUI();

    // we only want to authorize the account if the we're not using the
    // anonymous user.
    if (this.validSettingsAreSaved()) {
      if (this.mUseAnonymousUser) {
        // we don't need auth
        this.mAuthorizeAccount.setEnabled(false);
      } else {
        this.mAuthorizeAccount.setEnabled(true);
      }
    } else {
      this.mAuthorizeAccount.setEnabled(false);
    }

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

  protected void initListeners() {

    this.mSaveSettings.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        MainFragment.this.saveSettings();
        updateUI();
      }
    });

    this.mAuthorizeAccount.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast.makeText(
            getActivity(), "clicked authorize", Toast.LENGTH_SHORT).show();
        updateUI();
      }
    });

    this.mGetTableList.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast.makeText(
            getActivity(),
             "clicked get list",
            Toast.LENGTH_SHORT).show();
        updateUI();
      }
    });

    this.mUseAnonymousUserCheckBox.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        updateUI();
      }
    });

    this.mEnterServerUrl.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // no op
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        updateUI();
      }

      @Override
      public void afterTextChanged(Editable s) {
        // no op
      }
    });



  }

  protected boolean validSettingsAreSaved() {
    boolean savedServerIsValid = this.mSavedServerUrl != null;
    boolean savedUserIsValid =
        this.mUseAnonymousUser ||
            this.mSavedAccountName != null;

    return savedServerIsValid && savedUserIsValid;
  }

  protected boolean canSaveSettings() {
    boolean enteredServerIsValid =
        !this.mEnterServerUrl.getText().toString().equals("") &&
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
