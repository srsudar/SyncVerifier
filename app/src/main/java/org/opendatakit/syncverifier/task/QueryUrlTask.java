package org.opendatakit.syncverifier.task;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;
import org.opendatakit.syncverifier.util.HttpResponseWrapper;
import org.opendatakit.syncverifier.util.IOExceptionWithUrl;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Created by sudars on 9/21/14.
 */
public class QueryUrlTask extends AsyncTask<Void, Void, HttpResponseWrapper> {

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

  public QueryUrlTask(String urlToQuery, QueryUrlTaskCallbacks callbacks) {
    this.mUrlToQuery = urlToQuery;
    this.mCallbacks = callbacks;
  }

  public String getUrl() {
    return this.mUrlToQuery;
  }

  public void setCallbacks(QueryUrlTaskCallbacks callbacks) {
    this.mCallbacks = callbacks;
  }

  @Override
  protected HttpResponseWrapper doInBackground(Void... params) {

    final HttpGet get = new HttpGet(this.mUrlToQuery);

    try {

      final HttpResponse response = getThreadSafeHttpClient().execute(get);
      HttpResponseWrapper result = new HttpResponseWrapper(
          response,
          this.mUrlToQuery
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
