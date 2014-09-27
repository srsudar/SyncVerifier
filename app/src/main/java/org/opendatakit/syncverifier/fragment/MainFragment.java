package org.opendatakit.syncverifier.fragment;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.opendatakit.syncverifier.R;
import org.opendatakit.syncverifier.task.QueryUrlTask;
import org.opendatakit.syncverifier.util.FragmentTags;
import org.opendatakit.syncverifier.util.HttpResponseWrapper;
import org.opendatakit.syncverifier.util.IOExceptionWithUrl;
import org.opendatakit.syncverifier.util.SyncVerifierPreferences;
import org.opendatakit.syncverifier.util.SyncVerifierUtil;
import org.opendatakit.syncverifier.task.GetAuthTokenTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * The main fragment for testing connections with the server.
 *
 * @author sudar.sam@gmail.com
 */
public class MainFragment extends Fragment implements
    GetAuthTokenTask.GetAuthTokenCallbacks,
    QueryUrlTask.QueryUrlTaskCallbacks {

  private static final String TAG = MainFragment.class.getSimpleName();


  protected EditText mEnterServerUrl;
  protected Spinner mAccountSpinner;
  protected CheckBox mUseAnonymousUserCheckBox;
  protected Button mSaveSettings;

  protected TextView mSavedServerUrlView;
  protected TextView mSavedUserView;
  protected TextView mAuthTokenView;

  protected Button mAuthorizeAccount;
  protected Button mGetTableList;

  private Account mSavedAccount;
  private String mSavedServerUrl;
  private boolean mUseAnonymousUser;

  private AccountManager mAccountManager;

  private String mAuthToken;

  public MainFragment() {
    this.mSavedAccount = null;
    this.mSavedServerUrl = null;
    this.mUseAnonymousUser = false;
    this.mAccountManager = null;
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

    this.mAuthTokenView = SyncVerifierUtil.getTextView(
        rootView,
        R.id.main_activity_auth_token
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

    SyncVerifierPreferences preferences =
        new SyncVerifierPreferences(this.getActivity());
    this.mSavedServerUrl = preferences.getSavedUrl();

  }

  @Override
  public void onResume() {
    super.onResume();

    Account[] accounts =
        this.mAccountManager.getAccountsByType(
            SyncVerifierUtil.ACCOUNT_TYPE_GOOGLE);
    List<String> accountNames = new ArrayList<String>(accounts.length);
    for (int i = 0; i < accounts.length; i++)
      accountNames.add(accounts[i].name);

    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
        this.getActivity(),
        android.R.layout.select_dialog_item,
        accountNames);

    this.mAccountSpinner.setAdapter(adapter);

    this.initListeners();

    if (this.mSavedServerUrl != null && !this.mSavedServerUrl.equals("")) {
      this.mEnterServerUrl.setText(this.mSavedServerUrl);
    }

    this.updateUI();
  }


  protected void updateUI() {

    this.mSaveSettings.setEnabled(this.canSaveSettings());

    this.mAccountSpinner.setEnabled(
        !this.mUseAnonymousUserCheckBox.isChecked());

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

    this.mGetTableList.setEnabled(
        this.isAuthorized() || this.mUseAnonymousUser);

  }

  protected boolean isAuthorized() {
    return this.mAuthToken != null;
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
      if (this.mSavedAccount == null) {
        this.mSavedUserView.setText(R.string.choose_account);
      } else {
        this.mSavedUserView.setText(this.mSavedAccount.name);
      }
    }

    if (this.mAuthToken == null) {
      this.mAuthTokenView.setText(R.string.not_authenticated);
    } else {
      this.mAuthTokenView.setText(this.mAuthToken);
    }

  }

  protected void initListeners() {

    this.mSaveSettings.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        MainFragment.this.saveSettings();
        MainFragment.this.mEnterServerUrl.clearFocus();
        InputMethodManager imm = (InputMethodManager) MainFragment.this.
            getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEnterServerUrl.getWindowToken(), 0);
        updateUI();
      }
    });

    this.mAuthorizeAccount.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        GetAuthTokenDialogFragment dialogFragment =
            new GetAuthTokenDialogFragment();

        GetAuthTokenTask getAuthTokenTask = new GetAuthTokenTask(
            mSavedAccount,
            getActivity().getApplicationContext(),
            MainFragment.this
        );

        dialogFragment.setTask(getAuthTokenTask);

        FragmentManager fragmentManager = getActivity().getFragmentManager();

        dialogFragment.show(
            fragmentManager,
            FragmentTags.GET_AUTH_TOKEN
        );

      }
    });

    this.mGetTableList.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        String url = SyncVerifierUtil.getTableListUrl(mSavedServerUrl);

        QueryUrlTask queryTask = new QueryUrlTask(
            url,
            MainFragment.this,
            MainFragment.this.mAuthToken);

        QueryUrlDialogFragment dialogFragment = new QueryUrlDialogFragment();

        dialogFragment.setTask(queryTask);

        FragmentManager fragmentManager = getActivity().getFragmentManager();

        dialogFragment.show(
            fragmentManager,
            FragmentTags.QUERY_URL
        );

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
            this.mSavedAccount != null;

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

    SyncVerifierPreferences preferences =
        new SyncVerifierPreferences(getActivity());
    preferences.saveUrl(this.mSavedServerUrl);

    if (this.mUseAnonymousUserCheckBox.isChecked()) {
      this.mUseAnonymousUser = true;
      this.mSavedAccount = null;
      this.mAuthToken = null;
    } else {
      this.mUseAnonymousUser = false;
      String accountName = (String) this.mAccountSpinner.getSelectedItem();
      this.mSavedAccount = new Account(
          accountName,
          SyncVerifierUtil.ACCOUNT_TYPE_GOOGLE
      );
    }

  }

  protected void dismissGetAuthTokenDialog() {
    GetAuthTokenDialogFragment dialogFragment =
        this.getAuthTokenDialogFragment();
    if (dialogFragment != null) {
      dialogFragment.dismiss();
    }
  }

  protected void dismissQueryUrlDialog() {
    QueryUrlDialogFragment dialogFragment = this.getQueryUrlFragment();
    if (dialogFragment != null) {
      dialogFragment.dismiss();
    }
  }

  /**
   * Get the auth token dialog fragment if it exists.
   * @return
   */
  protected GetAuthTokenDialogFragment getAuthTokenDialogFragment() {
    FragmentManager fragmentManager = this.getActivity().getFragmentManager();
    GetAuthTokenDialogFragment result = (GetAuthTokenDialogFragment)
        fragmentManager.findFragmentByTag(
            FragmentTags.GET_AUTH_TOKEN);
    return result;
  }

  protected QueryUrlDialogFragment getQueryUrlFragment() {
    FragmentManager fragmentManager = this.getActivity().getFragmentManager();
    QueryUrlDialogFragment dialogFragment = (QueryUrlDialogFragment)
        fragmentManager.findFragmentByTag(
            FragmentTags.QUERY_URL
        );
    return dialogFragment;
  }

  @Override
  public void onOperationCanceledException(OperationCanceledException e) {
    SyncVerifierUtil.toast(
        this.getActivity(),
        R.string.msg_on_operation_canceled_exception
    );

    this.handleResponseFromGetAuthTokenTask();
  }

  @Override
  public void onAuthenticatorException(AuthenticatorException e) {
    SyncVerifierUtil.toast(
        this.getActivity(),
        R.string.msg_on_authenticator_exception
    );

    this.handleResponseFromGetAuthTokenTask();
  }

  @Override
  public void onGetAuthTokenIOException(IOException e) {

    this.dismissGetAuthTokenDialog();

    List<String> stackTrace = new ArrayList<String>();

    for (StackTraceElement traceElement : e.getStackTrace()) {
      stackTrace.add(traceElement.toString());
    }

    ExceptionSummaryFragment summaryFragment =
        ExceptionSummaryFragment.newInstance(
            this.getActivity().getString(R.string.na),
            e.getClass().getName(),
            this.getMessageFromException(e),
            this.getCauseFromException(e),
            stackTrace.toArray(new String[0]),
            e.toString()
        );

    FragmentManager fragmentManager = this.getActivity().getFragmentManager();

    fragmentManager.beginTransaction().replace(
        R.id.container,
        summaryFragment,
        FragmentTags.EXCEPTION_SUMMARY
    ).addToBackStack(null)
        .commit();


  }

  protected String getMessageFromException(Exception e) {
    String result = e.getMessage() == null ?
        this.getString(R.string.na) :
        e.getMessage();
    return result;
  }

  protected String getCauseFromException(Exception e) {
    String result = e.getCause() == null ?
        this.getString(R.string.na) :
        e.getMessage();
    return result;
  }

  @Override
  public void onRetrievedAuthToken(String authToken) {

    if (authToken == null) {
      SyncVerifierUtil.toast(
          this.getActivity(),
          R.string.retrived_auth_token_is_null
      );
    } else {
      SyncVerifierUtil.toast(
          this.getActivity(),
          R.string.retrieved_auth_token_successfully
      );
    }

    this.mAuthToken = authToken;

    this.handleResponseFromGetAuthTokenTask();

  }

  protected void handleResponseFromGetAuthTokenTask() {
    this.dismissGetAuthTokenDialog();
    this.updateUI();
  }

  @Override
  public void onQueryIOException(IOExceptionWithUrl e) {
    this.dismissQueryUrlDialog();

    List<String> stackTrace = new ArrayList<String>();

    for (StackTraceElement traceElement : e.getIOException().getStackTrace()) {
      stackTrace.add(traceElement.toString());
    }

    ExceptionSummaryFragment summaryFragment =
        ExceptionSummaryFragment.newInstance(
            e.getTargetUrl(),
            e.getIOException().getClass().getName(),
            this.getMessageFromException(e.getIOException()),
            this.getCauseFromException(e.getIOException()),
            stackTrace.toArray(new String[0]),
            e.getIOException().toString()
        );

    FragmentManager fragmentManager = this.getActivity().getFragmentManager();

    fragmentManager.beginTransaction().replace(
        R.id.container,
        summaryFragment,
        FragmentTags.EXCEPTION_SUMMARY
    ).addToBackStack(null)
        .commit();

  }


  @Override
  public void onQueryComplete(HttpResponseWrapper httpResponseWrapper) {
    this.dismissQueryUrlDialog();

    HttpResponse httpResponse = httpResponseWrapper.getHttpResponse();

    int statusCode = httpResponse.getStatusLine().getStatusCode();

    String target = httpResponseWrapper.getTargetUrl();

    String headersRep = this.getActivity().getString(R.string.no_headers);
    if (httpResponseWrapper.getRequestHeaders().length > 0) {
      headersRep = Arrays.toString(httpResponseWrapper.getRequestHeaders());
    }

    QueryResponseFragment responseFragment = QueryResponseFragment.newInstance(
        target,
        statusCode,
        httpResponseWrapper.getEntityStr(),
        headersRep
    );

    FragmentManager fragmentManager = this.getActivity().getFragmentManager();

    fragmentManager.beginTransaction().replace(
        R.id.container,
        responseFragment,
        FragmentTags.QUERY_RESPONSE
    ).addToBackStack(null)
        .commit();

  }



}
