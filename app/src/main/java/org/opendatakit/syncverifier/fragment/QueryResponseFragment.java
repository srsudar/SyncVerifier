package org.opendatakit.syncverifier.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.opendatakit.syncverifier.R;
import org.opendatakit.syncverifier.util.BundleUtil;
import org.opendatakit.syncverifier.util.SyncVerifierUtil;

/**
 * Created by sudars on 9/21/14.
 */
public class QueryResponseFragment extends Fragment {

  private TextView mTargetUrlView;
  private TextView mStatusCodeView;
  private TextView mResponseBodyView;

  private String mTargetUrl;
  private int mStatusCode;
  private String mResponseBody;

  public static QueryResponseFragment newInstance(
      String targetUrl,
      int statusCode,
      String responseBody) {
    Bundle arguments = new Bundle();

    BundleUtil.putTargetUrlInBundle(arguments, targetUrl);
    BundleUtil.putStatusCodeInBundle(arguments, statusCode);
    BundleUtil.putResponseBodyInBundle(arguments, responseBody);

    QueryResponseFragment result = new QueryResponseFragment();

    result.setArguments(arguments);

    return result;

  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Bundle arguments = this.getArguments();

    this.mTargetUrl = BundleUtil.getTargetUrlFromBundle(arguments, true);
    this.mStatusCode = BundleUtil.getStatusCodeFromBundle(arguments, true);
    this.mResponseBody = BundleUtil.getResponseBodyFromBundle(arguments, true);

  }

  @Override
  public View onCreateView(
      LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {

    View rootView = inflater.inflate(
        R.layout.fragment_query_response,
        container,
        false
    );

    this.mTargetUrlView = SyncVerifierUtil.getTextView(
        rootView,
        R.id.query_fragment_target_url
    );

    this.mStatusCodeView = SyncVerifierUtil.getTextView(
        rootView,
        R.id.query_fragment_status_code
    );

    this.mResponseBodyView = SyncVerifierUtil.getTextView(
        rootView,
        R.id.query_fragment_response_body
    );

    return rootView;

  }

  @Override
  public void onResume() {
    super.onResume();

    this.mTargetUrlView.setText(this.mTargetUrl);

    this.mStatusCodeView.setText(Integer.toString(this.mStatusCode));

    this.mResponseBodyView.setText(this.mResponseBody);

  }
}
