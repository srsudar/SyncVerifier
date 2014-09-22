package org.opendatakit.syncverifier.util;

import java.io.IOException;

/**
 * Created by sudars on 9/21/14.
 */
public class IOExceptionWithUrl {

  private String mTargetUrl;
  private IOException mIOException;

  public IOExceptionWithUrl(String targetUrl, IOException ioException) {
    this.mTargetUrl = targetUrl;
    this.mIOException = ioException;
  }

  public String getTargetUrl() {
    return mTargetUrl;
  }

  public void setTargetUrl(String targetUrl) {
    this.mTargetUrl = targetUrl;
  }

  public IOException getIOException() {
    return mIOException;
  }

  public void setIOException(IOException iOException) {
    this.mIOException = iOException;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    IOExceptionWithUrl that = (IOExceptionWithUrl) o;

    if (mIOException != null ? !mIOException.equals(that.mIOException) : that.mIOException != null)
      return false;
    if (mTargetUrl != null ? !mTargetUrl.equals(that.mTargetUrl) : that.mTargetUrl != null)
      return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = mTargetUrl != null ? mTargetUrl.hashCode() : 0;
    result = 31 * result + (mIOException != null ? mIOException.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "IOExceptionWithUrl{" +
        "mTargetUrl='" + mTargetUrl + '\'' +
        ", mIOException=" + mIOException +
        '}';
  }

}
