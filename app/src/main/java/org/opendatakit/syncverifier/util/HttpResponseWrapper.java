package org.opendatakit.syncverifier.util;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

import java.util.Arrays;

/**
 * Very basic wrapper for an {@link org.apache.http.HttpResponse}.
 * @author sudar.sam@gmail.com
 */
public class HttpResponseWrapper {

  private String mTargetUrl;
  private HttpResponse mHttpResponse;
  private Header[] mRequestHeaders;

  /**
   * The response entity as a string. This could be calculated from the
   * {@link #mHttpResponse} object, but must be done so off the main thread,
   * so this is provided for convenience.
   */
  private String mEntityStr;

  public HttpResponseWrapper(
      HttpResponse response,
      String targetUrl,
      String entityStr,
      Header[] requestHeaders) {
    this.mHttpResponse = response;
    this.mTargetUrl = targetUrl;
    this.mEntityStr = entityStr;
    this.mRequestHeaders = requestHeaders;
  }


  public String getTargetUrl() {
    return mTargetUrl;
  }

  public HttpResponse getHttpResponse() {
    return mHttpResponse;
  }

  public String getEntityStr() {
    return this.mEntityStr;
  }

  public Header[] getRequestHeaders() {
    return this.mRequestHeaders;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    HttpResponseWrapper that = (HttpResponseWrapper) o;

    if (mEntityStr != null ? !mEntityStr.equals(that.mEntityStr) : that.mEntityStr != null)
      return false;
    if (mHttpResponse != null ? !mHttpResponse.equals(that.mHttpResponse) : that.mHttpResponse != null)
      return false;
    if (!Arrays.equals(mRequestHeaders, that.mRequestHeaders))
      return false;
    if (mTargetUrl != null ? !mTargetUrl.equals(that.mTargetUrl) : that.mTargetUrl != null)
      return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = mTargetUrl != null ? mTargetUrl.hashCode() : 0;
    result = 31 * result + (mHttpResponse != null ? mHttpResponse.hashCode() : 0);
    result = 31 * result + (mRequestHeaders != null ? Arrays.hashCode(mRequestHeaders) : 0);
    result = 31 * result + (mEntityStr != null ? mEntityStr.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "HttpResponseWrapper{" +
        "mTargetUrl='" + mTargetUrl + '\'' +
        ", mHttpResponse=" + mHttpResponse +
        ", mRequestHeaders=" + Arrays.toString(mRequestHeaders) +
        ", mEntityStr='" + mEntityStr + '\'' +
        '}';
  }

}
