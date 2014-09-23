package org.opendatakit.syncverifier.util;

/**
* Created by sudars on 9/21/14.
*/
public class BundleArgs {

  public static final String AUTH_TOKEN = getPrefixedKey("auth_token");

  public static final String ACCOUNT_NAME = getPrefixedKey("account_name");

  public static final String TARGET_URL = getPrefixedKey("target_url");

  public static final String STATUS_CODE = getPrefixedKey("status_code");

  public static final String RESPONSE_BODY = getPrefixedKey("response_body");

  public static final String EXCEPTION_CLASS =
      getPrefixedKey("exception_class");

  public static final String EXCEPTION_CAUSE =
      getPrefixedKey("exception_cause");

  public static final String EXCEPTION_MESSAGE =
      getPrefixedKey("exception_message");

  public static final String EXCEPTION_STACK_TRACE =
      getPrefixedKey("exception_stack_trace");

  public static final String EXCEPTION_TO_STRING =
      getPrefixedKey("exception_to_string");

  public static final String HEADERS_REPRESENTATION =
      getPrefixedKey("headers_representation");

  private static String getPrefixedKey(String key) {
    return "org.opendatakit.syncverifier." + key;
  }


}
