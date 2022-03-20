package insertnamehere.com.github.datagatherer;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.util.JsonWriter;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DataGathererActivity extends AppCompatActivity implements SensorEventListener {

    private final ArrayList<float[]> ACCELEROMETER_VALUES = new ArrayList<>();

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private TextView accelerometerCurrentValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_gatherer);

        Logger.debug("DataGathererActivity#onCreate: Initializing TextView variables");
        accelerometerCurrentValues = findViewById(R.id.accelerometerTextView);

        Logger.debug("DataGathererActivity#onCreate: Initializing Sensor Services");
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        Logger.debug("DataGathererActivity#onCreate: Registering Sensor Listeners");
        int sensorDelay = SensorManager.SENSOR_DELAY_NORMAL;
        sensorManager.registerListener(this, accelerometer, sensorDelay);
        Logger.debug("DataGathererActivity#onCreate: Initialization Complete!");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;

        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // using event.values instead of this replaces all elements of the list when writing to it...
            float[] accelerometer = {event.values[0], event.values[1], event.values[2]};
            accelerometerCurrentValues.setText(floatXYZToString("Accelerometer", accelerometer));
            ACCELEROMETER_VALUES.add(accelerometer);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}

    public void onExportButtonClicked(View view) {
        Intent switchToMainActivityIntent = new Intent(this, MainActivity.class);

        try {
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)+"/inh_datagatherer_export.json";
            JsonWriter writer = new JsonWriter(new FileWriter(path));

            writer.setIndent("    ");
            writeData(writer);
            writer.flush();
            writer.close();

            Logger.info("Exported data to " + path);
            switchToMainActivityIntent.putExtra("exportError", false);
        } catch (Exception e) {
            Logger.warn("Something went wrong with exporting the recorded data...");
            Logger.error(e.getMessage());

            switchToMainActivityIntent.putExtra("exportError", true);
        }

        startActivity(switchToMainActivityIntent);
        finish();
    }

    @Override
    protected void onDestroy() {
        ACCELEROMETER_VALUES.clear();

        accelerometerCurrentValues = null;
        sensorManager.unregisterListener(this);
        accelerometer = null;
        sensorManager = null;
        super.onDestroy();
    }

    private void writeData(JsonWriter writer) throws IOException {
        writer.beginObject();

        writer.name("accelerometer"); writeXyzArray(writer, ACCELEROMETER_VALUES);

        writer.endObject();
    }

    private void writeXyzArray(JsonWriter writer, List<float[]> values) throws IOException {
        writer.beginArray();
        for (float[] value : values) {
            writer.value(String.format(Locale.ENGLISH, "%f;%f;%f", value[0], value[1], value[2]));
        }
        writer.endArray();
    }

    private String floatXYZToString(String sensorName, float[] values) {
        return String.format(Locale.ENGLISH, "%s:\n  x: %f\n  y: %f\n  z: %f", sensorName, values[0], values[1], values[2]);
    }
}
