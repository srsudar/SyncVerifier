package org.opendatakit.syncverifier.task;

import android.os.AsyncTask;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.opendatakit.syncverifier.util.HttpResponseWrapper;
import org.opendatakit.syncverifier.util.IOExceptionWithUrl;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Created by sudars on 9/21/14.
 */
public class QueryUrlTask extends AsyncTask<Void, Void, HttpResponseWrapper> {

  protected static final String AUTHORIZATION_HEADER_KEY = "Authorization";

  public interface QueryUrlTaskCallbacks {

    public void onQueryIOException(IOExceptionWithUrl e);
    public void onQueryComplete(HttpResponseWrapper httpResponse);

  }

  /**
   * Get a single threadsafe httpclient. Based on code here:
   * http://www.androider.me/2013/11/solve-one-issue-because-of-thread-safe.html
   * @return
   */
  protected static DefaultHttpClient getThreadSafeHttpClient() {
    DefaultHttpClient result = new DefaultHttpClient();
    ClientConnectionManager mgr = result.getConnectionManager();
    HttpParams params = result.getParams();
    result = new DefaultHttpClient(
        new ThreadSafeClientConnManager(params, mgr.getSchemeRegistry()),
        params);
    return result;
  }


  private String mUrlToQuery;

  private IOException mIOException;

  private QueryUrlTaskCallbacks mCallbacks;

  private String mAuthToken;

  /**
   *
   * @param urlToQuery
   * @param callbacks
   * @param authToken if null, authorization will not be used
   */
  public QueryUrlTask(
      String urlToQuery,
      QueryUrlTaskCallbacks callbacks,
      String authToken) {
    this.mUrlToQuery = urlToQuery;
    this.mCallbacks = callbacks;
    this.mAuthToken = authToken;
  }

  public String getUrl() {
    return this.mUrlToQuery;
  }

  public void setCallbacks(QueryUrlTaskCallbacks callbacks) {
    this.mCallbacks = callbacks;
  }

  /**
   * Add the authorization header to the request.
   * @param request
   * @param authToken
   */
  protected void addODKAuthentication(HttpRequest request, String authToken) {
    String headerValue = getAuthorizationHeaderValueForToken(authToken);
    request.addHeader(AUTHORIZATION_HEADER_KEY, headerValue);
  }

  protected String getAuthorizationHeaderValueForToken(String authToken) {
    String headerValuePrefix = "Bearer ";
    return headerValuePrefix + authToken;
  }

  @Override
  protected HttpResponseWrapper doInBackground(Void... params) {

    final HttpGet get = new HttpGet(this.mUrlToQuery);

    try {

      if (this.mAuthToken != null) {
        this.addODKAuthentication(get, this.mAuthToken);
      }

      final HttpResponse response = getThreadSafeHttpClient().execute(get);

      String entityStr =
          EntityUtils.toString(response.getEntity());

      HttpResponseWrapper result = new HttpResponseWrapper(
          response,
          this.mUrlToQuery,
          entityStr,
          get.getAllHeaders()
      );

      return result;

    } catch (IOException e) {
      this.mIOException = e;
    }

    return null;
  }

  @Override
  protected void onPostExecute(HttpResponseWrapper httpResponse) {

    if (this.mIOException != null) {
      IOExceptionWithUrl exception = new IOExceptionWithUrl(
          this.mUrlToQuery,
          this.mIOException
      );
      this.mCallbacks.onQueryIOException(exception);
    } else {
        this.mCallbacks.onQueryComplete(httpResponse);
    }

  }
}
