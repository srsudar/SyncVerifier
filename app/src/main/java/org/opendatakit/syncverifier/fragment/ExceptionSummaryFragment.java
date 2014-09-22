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

import java.util.Arrays;

/**
 * @author sudar.sam@gmail.com
 */
public class ExceptionSummaryFragment extends Fragment {

  private TextView mTargetUrlView;
  private TextView mExceptionClassView;
  private TextView mExceptionMessageView;
  private TextView mExceptionCauseView;
  private TextView mExceptionStackTraceView;
  private TextView mExceptionToStringView;

  private String mTargetUrl;
  private String mExceptionClassName;
  private String mExceptionMessage;
  private String mExceptionCause;
  private String[] mExceptionStackTrace;
  private String mExceptionToString;

  public static ExceptionSummaryFragment newInstance(
      String targetUrl,
      String exceptionClassName,
      String exceptionMessage,
      String exceptionCause,
      String[] exceptionStackTrace,
      String exceptionToString) {

    Bundle args = new Bundle();

    BundleUtil.putTargetUrlInBundle(args, targetUrl);
    BundleUtil.putExceptionClassNameInBundle(args, exceptionClassName);
    BundleUtil.putExceptionMessageInBundle(args, exceptionMessage);
    BundleUtil.putExceptionCauseInBundle(args, exceptionCause);
    BundleUtil.putExceptionStackTraceInBundle(args, exceptionStackTrace);
    BundleUtil.putExceptionToStingInBundle(args, exceptionToString);

    ExceptionSummaryFragment result = new ExceptionSummaryFragment();

    result.setArguments(args);

    return result;

  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Bundle args = this.getArguments();

    this.mTargetUrl =
        BundleUtil.getTargetUrlFromBundle(args, true);

    this.mExceptionClassName =
        BundleUtil.getExceptionClassNameFromBundle(args, true);

    this.mExceptionCause =
        BundleUtil.getExceptionCauseFromBundle(args, true);

    this.mExceptionMessage =
        BundleUtil.getExceptionMessageFromBundle(args, true);

    this.mExceptionStackTrace =
        BundleUtil.getStackTraceFromBundle(args, true);

    this.mExceptionToString =
        BundleUtil.getExceptionToSringFromBundle(args, true);

  }

  @Override
  public View onCreateView(
      LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {

    View rootView = inflater.inflate(
        R.layout.fragment_exception_summary,
        container,
        false
    );

    this.mTargetUrlView = SyncVerifierUtil.getTextView(
        rootView,
        R.id.fragment_exception_summary_target_url
    );

    this.mExceptionClassView = SyncVerifierUtil.getTextView(
        rootView,
        R.id.fragment_exception_summary_exception_class
    );

    this.mExceptionMessageView = SyncVerifierUtil.getTextView(
        rootView,
        R.id.fragment_exception_summary_exception_message
    );

    this.mExceptionCauseView = SyncVerifierUtil.getTextView(
        rootView,
        R.id.fragment_exception_summary_exception_cause
    );

    this.mExceptionStackTraceView = SyncVerifierUtil.getTextView(
        rootView,
        R.id.fragment_exception_summary_exception_stack_trace
    );

    this.mExceptionToStringView = SyncVerifierUtil.getTextView(
        rootView,
        R.id.fragment_exception_summary_exception_to_string
    );

    return rootView;

  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    this.mTargetUrlView.setText(this.mTargetUrl);

    this.mExceptionClassView.setText(this.mExceptionClassName);

    this.mExceptionMessageView.setText(this.mExceptionMessage);

    this.mExceptionCauseView.setText(this.mExceptionCause);

    this.mExceptionToStringView.setText(this.mExceptionToString);

    this.mExceptionStackTraceView.setText(
        Arrays.toString(this.mExceptionStackTrace)
    );


  }
}
