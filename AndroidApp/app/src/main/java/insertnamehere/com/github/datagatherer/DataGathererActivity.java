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

    private final ArrayList<float[]> ACCELEROMETER_VALUES = new ArrayList<>(); // m/s^2 [1+3]
    private final ArrayList<float[]> GYROSCOPE_VALUES = new ArrayList<>(); // rad/s [1+3]
    private final ArrayList<float[]> MAGNETOMETER_VALUES = new ArrayList<>(); // uT [1+3]
    private final ArrayList<Long> ACCELEROMETER_TIMESTAMPS = new ArrayList<>();
    private final ArrayList<Long> GYROSCOPE_TIMESTAMPS = new ArrayList<>();
    private final ArrayList<Long> MAGNETOMETER_TIMESTAMPS = new ArrayList<>();

    private SensorManager sensorManager;
    private Sensor accelerometer, gyroscope, magnetometer;
    private TextView accelerometerCurrentValues, gyroscopeCurrentValues, magnetometerCurrentValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_gatherer);

        int sensorDelay = SensorManager.SENSOR_DELAY_NORMAL;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            sensorDelay = 3 - extras.getInt("sensorDelay", 0);
        }

        Logger.debug("DataGathererActivity#onCreate: Initializing TextView variables");
        accelerometerCurrentValues = findViewById(R.id.accelerometerTextView);
        gyroscopeCurrentValues = findViewById(R.id.gyroscopeTextView);
        magnetometerCurrentValues = findViewById(R.id.magnetometerTextView);

        Logger.debug("DataGathererActivity#onCreate: Initializing Sensor Services");
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        Logger.debug("DataGathererActivity#onCreate: Registering Sensor Listeners");
        sensorManager.registerListener(this, accelerometer, sensorDelay);
        sensorManager.registerListener(this, gyroscope, sensorDelay);
        sensorManager.registerListener(this, magnetometer, sensorDelay);
        Logger.debug("DataGathererActivity#onCreate: Initialization Complete!");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;

        // TODO We need to find out the bias error of the accelerometer by taking an average of its
        //   output while it is not undergoing any acceleration. Then we can just subtract it from the output
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            recordData(
                    "Accelerometer", accelerometerCurrentValues,
                    event.values, event.timestamp,
                    ACCELEROMETER_VALUES, ACCELEROMETER_TIMESTAMPS
            );
        }
        // TODO We need to find out the bias error of the gyroscope by taking an average of its
        //   output while it is not undergoing any rotation. Then we can just subtract it from the output
        if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            recordData(
                    "Gyroscope", gyroscopeCurrentValues,
                    event.values, event.timestamp,
                    GYROSCOPE_VALUES, GYROSCOPE_TIMESTAMPS
            );
        }
        if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            recordData(
                    "Magnetometer", magnetometerCurrentValues,
                    event.values, event.timestamp,
                    MAGNETOMETER_VALUES, MAGNETOMETER_TIMESTAMPS
            );
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}

    @Override
    protected void onDestroy() {
        ACCELEROMETER_VALUES.clear();
        GYROSCOPE_VALUES.clear();
        MAGNETOMETER_VALUES.clear();

        accelerometerCurrentValues = null;
        gyroscopeCurrentValues = null;
        magnetometerCurrentValues = null;
        sensorManager.unregisterListener(this);
        magnetometer = null;
        gyroscope = null;
        accelerometer = null;
        sensorManager = null;
        super.onDestroy();
    }

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

    private void recordData(String sensorName, TextView textView,
                            float[] values, long currentTimestamp,
                            ArrayList<float[]> valueList, ArrayList<Long> timestampList) {
        float[] array = {
                values[0],
                values[1],
                values[2]
        };
        textView.setText(floatXYZToString(sensorName, array));
        valueList.add(array);
        timestampList.add(currentTimestamp);
    }

    private void writeData(JsonWriter writer) throws IOException {
        writer.beginObject();

        writer.name("accelerometer"); writeXyzArray(writer, ACCELEROMETER_VALUES);
        writer.name("accelerometer_timestamps"); writeTimestamps(writer, ACCELEROMETER_TIMESTAMPS);
        writer.name("gyroscope"); writeXyzArray(writer, GYROSCOPE_VALUES);
        writer.name("gyroscope_timestamps"); writeTimestamps(writer, GYROSCOPE_TIMESTAMPS);
        writer.name("magnetometer"); writeXyzArray(writer, MAGNETOMETER_VALUES);
        writer.name("magnetometer_timestamps"); writeTimestamps(writer, MAGNETOMETER_TIMESTAMPS);

        writer.endObject();
    }

    private void writeXyzArray(JsonWriter writer, List<float[]> values) throws IOException {
        writer.beginArray();
        for (float[] value : values) {
            writer.value(String.format(Locale.ENGLISH, "%f;%f;%f", value[0], value[1], value[2]));
        }
        writer.endArray();
    }

    private void writeTimestamps(JsonWriter writer, ArrayList<Long> values) throws IOException {
        writer.beginArray();
        for (long value : values) {
            writer.value(value);
        }
        writer.endArray();
    }

    private String floatXYZToString(String sensorName, float[] values) {
        return String.format(Locale.ENGLISH, "%s:\n  x: %f\n  y: %f\n  z: %f", sensorName, values[0], values[1], values[2]);
    }
}
