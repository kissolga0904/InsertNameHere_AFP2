package insertnamehere.com.github.datagatherer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final ArrayList<String> SENSOR_DELAYS = new ArrayList<>();

    static {
        SENSOR_DELAYS.add("Slowest"); // SensorManager.SENSOR_DELAY_NORMAL
        SENSOR_DELAYS.add("Slow"); // SensorManager.SENSOR_DELAY_UI
        SENSOR_DELAYS.add("Normal"); // SensorManager.SENSOR_DELAY_GAME
        SENSOR_DELAYS.add("Fast"); // SensorManager.SENSOR_DELAY_FASTEST
    }

    int selectedSensorDelay;
    Spinner sensorDelaySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Logger.debug("MainActivity#onCreate: Initializing Sensor Delay Spinner");
        sensorDelaySpinner = (Spinner) findViewById(R.id.sensorDelaySpinner);
        ArrayAdapter<String> sensorDelayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                SENSOR_DELAYS
        );
        sensorDelaySpinner.setAdapter(sensorDelayAdapter);
        sensorDelaySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedSensorDelay = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedSensorDelay = 0;
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getBoolean("exportError")) {
            Snackbar.make(findViewById(R.id.parent), R.string.export_error, Snackbar.LENGTH_SHORT).show();
        }
    }

    public void onStartGatheringClicked(View view) {
        Intent switchToDataGathererActivityIntent = new Intent(this, DataGathererActivity.class);
        switchToDataGathererActivityIntent.putExtra("sensorDelay", selectedSensorDelay);
        startActivity(switchToDataGathererActivityIntent);
        finish();
    }
}