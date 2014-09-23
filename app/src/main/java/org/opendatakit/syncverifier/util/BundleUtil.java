package org.opendatakit.syncverifier.util;

import android.os.Bundle;

/**
 * @author sudar.sam@gmail.com
 */
public class BundleUtil {

  public static void putTargetUrlInBundle(
      Bundle bundle,
      String targetUrl) {
    bundle.putString(BundleArgs.TARGET_URL, targetUrl);
  }

  public static void putStatusCodeInBundle(
      Bundle bundle,
      int statusCode) {
    bundle.putInt(BundleArgs.STATUS_CODE, statusCode);
  }

  public static void putResponseBodyInBundle(
      Bundle bundle,
      String responseBody) {
    bundle.putString(BundleArgs.RESPONSE_BODY, responseBody);
  }

  public static void putHeadersRepresentationInBundle(
      Bundle bundle,
      String headersRepresentation) {
    bundle.putString(BundleArgs.HEADERS_REPRESENTATION, headersRepresentation);
  }

  public static void putExceptionClassNameInBundle(
      Bundle bundle,
      String className) {
    bundle.putString(BundleArgs.EXCEPTION_CLASS, className);
  }

  public static void putExceptionCauseInBundle(
      Bundle bundle,
      String exceptionCause) {
    bundle.putString(BundleArgs.EXCEPTION_CAUSE, exceptionCause);
  }

  public static void putExceptionMessageInBundle(
      Bundle bundle,
      String exceptionMessage) {
    bundle.putString(BundleArgs.EXCEPTION_MESSAGE, exceptionMessage);
  }

  public static void putExceptionStackTraceInBundle(
      Bundle bundle,
      String[] stackTrace) {
    bundle.putStringArray(BundleArgs.EXCEPTION_STACK_TRACE, stackTrace);
  }

  public static void putExceptionToStingInBundle(
      Bundle bundle,
      String exceptionToString) {
    bundle.putString(BundleArgs.EXCEPTION_TO_STRING, exceptionToString);
  }

  public static String getExceptionClassNameFromBundle(
      Bundle bundle,
      boolean throwIfNotPresent) {
    doThrowHelper(bundle, BundleArgs.EXCEPTION_CLASS, throwIfNotPresent);
    return bundle.getString(BundleArgs.EXCEPTION_CLASS);
  }

  public static String getHeadersRepresentationFromBundle(
      Bundle bundle,
      boolean throwIfNorPresent) {
    String key = BundleArgs.HEADERS_REPRESENTATION;
    doThrowHelper(bundle, key, throwIfNorPresent);
    return bundle.getString(key);
  }

  public static String getExceptionMessageFromBundle(
      Bundle bundle,
      boolean throwIfNotPresent) {
    doThrowHelper(bundle, BundleArgs.EXCEPTION_MESSAGE, throwIfNotPresent);
    return bundle.getString(BundleArgs.EXCEPTION_MESSAGE);
  }

  public static String getExceptionCauseFromBundle(
      Bundle bundle,
      boolean throwIfNotPresent) {
    doThrowHelper(bundle, BundleArgs.EXCEPTION_CAUSE, throwIfNotPresent);
    return bundle.getString(BundleArgs.EXCEPTION_CAUSE);
  }

  public static String[] getStackTraceFromBundle(
      Bundle bundle,
      boolean throwIfNotPresent) {
    doThrowHelper(bundle, BundleArgs.EXCEPTION_STACK_TRACE, throwIfNotPresent);
    return bundle.getStringArray(BundleArgs.EXCEPTION_STACK_TRACE);
  }

  public static String getExceptionToSringFromBundle(
      Bundle bundle,
      boolean throwIfNotPresent) {
    String key = BundleArgs.EXCEPTION_TO_STRING;
    doThrowHelper(bundle, key, throwIfNotPresent);
    return bundle.getString(key);
  }

  public static String getTargetUrlFromBundle(
      Bundle bundle,
      boolean throwIfNotPresent) {
    doThrowHelper(bundle, BundleArgs.TARGET_URL, throwIfNotPresent);
    return bundle.getString(BundleArgs.TARGET_URL);
  }

  public static int getStatusCodeFromBundle(
      Bundle bundle,
      boolean throwIfNotPresent) {
    doThrowHelper(bundle, BundleArgs.STATUS_CODE, throwIfNotPresent);
    return bundle.getInt(BundleArgs.STATUS_CODE);
  }

  public static String getResponseBodyFromBundle(
      Bundle bundle,
      boolean throwIfNotPresent) {
    doThrowHelper(bundle, BundleArgs.RESPONSE_BODY, throwIfNotPresent);
    return bundle.getString(BundleArgs.RESPONSE_BODY);
  }

  /**
   * Handles the logic of throwing an exception if the throwIfNotPresent is
   * true and the bundle does not contain the key.
   * @param bundle
   * @param key
   * @param throwIfNotPresent
   * @return
   */
  protected static void doThrowHelper(
      Bundle bundle,
      String key,
      boolean throwIfNotPresent) {
    if (throwIfNotPresent && !bundle.containsKey(key)) {
      throw new IllegalArgumentException("bundle does not contain: " + key);
    }
  }

}
