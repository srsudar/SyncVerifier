package syncverifier.opendatakit.org.syncverifier;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * The main fragment for testing connections with the server.
 * @author sudar.sam@gmail.com
 */
public class MainFragment extends Fragment {


    protected EditText mEnterServerUrl;
    protected Spinner mAccountSpinner;
    protected CheckBox mUseAnonymousUser;
    protected Button mSaveSettings;

    protected TextView mSavedServerUrl;
    protected TextView mSavedUser;

    protected Button mAuthorizeAccount;
    protected Button mGetTableList;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        return rootView;
    }
}
