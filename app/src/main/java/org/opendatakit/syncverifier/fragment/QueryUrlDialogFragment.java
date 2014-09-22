package org.opendatakit.syncverifier.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;

import org.opendatakit.syncverifier.R;
import org.opendatakit.syncverifier.task.QueryUrlTask;

/**
 * @author sudar.sam@gmail.com
 */
public class QueryUrlDialogFragment extends DialogFragment {

  private QueryUrlTask mTask;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.setRetainInstance(true);

    if (this.mTask != null) {
      this.mTask.execute();
    }

  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {

    ProgressDialog dialog = new ProgressDialog(this.getActivity());

    String message = this.getActivity().getString(
        R.string.querying_url,
        this.mTask.getUrl()
    );

    dialog.setMessage(message);
    this.setCancelable(false);

    return dialog;

  }

  @Override
  public void onResume() {
    super.onResume();

    if (this.mTask == null) {
      this.dismiss();
    }

  }

  public void setTask(QueryUrlTask task) {
    this.mTask = task;
  }

  public void setTaskCallbacks(QueryUrlTask.QueryUrlTaskCallbacks callbacks) {
    if (this.mTask != null) {
      this.mTask.setCallbacks(callbacks);
    }
  }




}
