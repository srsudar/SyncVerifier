package org.opendatakit.syncverifier.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;

import org.opendatakit.syncverifier.task.GetAuthTokenTask;
import org.opendatakit.syncverifier.R;

/**
 * @author sudar.sam@gmail.com
 */
public class GetAuthTokenDialogFragment extends DialogFragment {

  private GetAuthTokenTask mTask;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.setRetainInstance(true);
    if (this.mTask != null) {
      this.mTask.execute();
    }
  }

  @Override
  public void onResume() {
    super.onResume();

    if (this.mTask == null) {
      this.dismiss();
    }
  }

  @Override
  public Dialog onCreateDialog(final Bundle savedInstanceState) {
    ProgressDialog dialog = new ProgressDialog(this.getActivity());
    dialog.setMessage(getActivity().getString(R.string.getting_token));
    this.setCancelable(false);
    return dialog;
  }

  public void setTask(GetAuthTokenTask task) {
    this.mTask = task;
  }

  public void setTaskCallbacks(
      GetAuthTokenTask.GetAuthTokenCallbacks callbacks) {
    if (this.mTask != null) {
      this.mTask.setCallbacks(callbacks);
    }
  }

}
