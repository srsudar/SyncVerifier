package org.opendatakit.syncverifier.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.opendatakit.syncverifier.fragment.MainFragment;
import org.opendatakit.syncverifier.R;


public class MainActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    if (savedInstanceState == null) {
      getFragmentManager().beginTransaction()
          .add(R.id.container, new MainFragment())
          .commit();
    }
  }

}
