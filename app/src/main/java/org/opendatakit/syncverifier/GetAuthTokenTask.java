package org.opendatakit.syncverifier;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;

/**
 * Created by sudars on 9/21/14.
 */
public class GetAuthTokenTask extends AsyncTask<Void, Void, String> {

  public interface GetAuthTokenCallbacks {

    public void onOperationCanceledException(OperationCanceledException e);
    public void onAuthenticatorException(AuthenticatorException e);
    public void onIOException(IOException e);
    public void onRetrievedAuthToken(String authToken);

  }


  private static final String AUTH_TOKEN_TYPE =
      "oauth2:https://www.googleapis.com/auth/userinfo.email";


  private Account mAccount;
  private AccountManager mAccountManager;
  private GetAuthTokenCallbacks mCallbacks;

  private OperationCanceledException mOperationCanceledException;
  private AuthenticatorException mAuthenticatorException;
  private IOException mIOException;


  public GetAuthTokenTask(
      Account account,
      Context applicationContext,
      GetAuthTokenCallbacks callbacks) {
    this.mAccount = account;
    this.mAccountManager =
        AccountManager.get(applicationContext.getApplicationContext());
    this.mCallbacks = callbacks;

    this.mOperationCanceledException = null;
    this.mAuthenticatorException = null;
    this.mIOException = null;

  }

  public void setCallbacks(GetAuthTokenCallbacks callbacks) {
    this.mCallbacks = callbacks;
  }

  @Override
  protected String doInBackground(Void... params) {

    try {

      String authToken = this.blockingGetAuthToken();

      // This can return a cached, stale, invalid token. We don't know this
      // until we try to use it. Rather than put up with this, we'll just
      // invalidate it right away and then get a fresh one. This is marginally
      // inefficient, but greatly simplifies the workflow.
      this.mAccountManager.invalidateAuthToken(
          SyncVerifierUtil.ACCOUNT_TYPE_GOOGLE,
          authToken);

      authToken = this.blockingGetAuthToken();

      return authToken;

    } catch (OperationCanceledException e) {
      this.mOperationCanceledException = e;
    } catch (AuthenticatorException e) {
      this.mAuthenticatorException = e;
    } catch (IOException e) {
      this.mIOException = e;
    }

    return null;
  }

  protected String blockingGetAuthToken()
      throws OperationCanceledException,
      AuthenticatorException,
      IOException {
    String result = this.mAccountManager.blockingGetAuthToken(
        this.mAccount,
        AUTH_TOKEN_TYPE,
        true
    );
    return result;
  }

  @Override
  protected void onPostExecute(String authToken) {

    if (this.mOperationCanceledException != null) {
      this.mCallbacks.onOperationCanceledException(
          this.mOperationCanceledException);
    } else if (this.mAuthenticatorException != null) {
      this.mCallbacks.onAuthenticatorException(this.mAuthenticatorException);
    } else if (this.mIOException != null) {
      this.mCallbacks.onIOException(this.mIOException);
    } else {
      this.mCallbacks.onRetrievedAuthToken(authToken);
    }

  }
}
