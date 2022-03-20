package insertnamehere.com.github.datagatherer;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getBoolean("exportError")) {
            Snackbar.make(findViewById(R.id.parent), R.string.export_error, Snackbar.LENGTH_SHORT).show();
        }
    }

    public void onStartGatheringClicked(View view) {
        Intent switchToDataGathererActivityIntent = new Intent(this, DataGathererActivity.class);
        startActivity(switchToDataGathererActivityIntent);
        finish();
    }
}