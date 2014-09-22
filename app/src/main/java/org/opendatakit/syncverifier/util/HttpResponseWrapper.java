package org.opendatakit.syncverifier.util;

import org.apache.http.HttpResponse;

/**
 * Very basic wrapper for an {@link org.apache.http.HttpResponse}.
 * @author sudar.sam@gmail.com
 */
public class HttpResponseWrapper {

  private String mTargetUrl;
  private HttpResponse mHttpResponse;

  public HttpResponseWrapper(HttpResponse response, String targetUrl) {
    this.mHttpResponse = response;
    this.mTargetUrl = targetUrl;
  }


  public String getTargetUrl() {
    return mTargetUrl;
  }

  public void setTargetUrl(String targetUrl) {
    this.mTargetUrl = targetUrl;
  }

  public HttpResponse getHttpResponse() {
    return mHttpResponse;
  }

  public void setHttpResponse(HttpResponse httpResponse) {
    this.mHttpResponse = httpResponse;
  }

  @Override
  public String toString() {
    return "HttpResponseWrapper{" +
        "mTargetUrl='" + mTargetUrl + '\'' +
        ", mHttpResponse=" + mHttpResponse +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    HttpResponseWrapper that = (HttpResponseWrapper) o;

    if (mHttpResponse != null ? !mHttpResponse.equals(that.mHttpResponse) : that.mHttpResponse != null)
      return false;
    if (mTargetUrl != null ? !mTargetUrl.equals(that.mTargetUrl) : that.mTargetUrl != null)
      return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = mTargetUrl != null ? mTargetUrl.hashCode() : 0;
    result = 31 * result + (mHttpResponse != null ? mHttpResponse.hashCode() : 0);
    return result;
  }
}
