package insertnamehere.com.github.datagatherer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.JsonWriter;
import android.view.View;
import android.widget.TextView;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.DoubleStream;

public class DataGathererActivity extends SensorActivity {

    private TextView accelerometerCurrentValues, gyroscopeCurrentValues, magnetometerCurrentValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_gatherer);

        Logger.debug("DataGathererActivity#onCreate: Initializing TextView variables");
        accelerometerCurrentValues = findViewById(R.id.accelerometerTextView);
        gyroscopeCurrentValues = findViewById(R.id.gyroscopeTextView);
        magnetometerCurrentValues = findViewById(R.id.magnetometerTextView);
    }

    @Override
    protected void recordData(String sensorName, float[] values, long currentTimestamp, ArrayList<float[]> valueList, ArrayList<Long> timestampList) {
        super.recordData(sensorName, values, currentTimestamp, valueList, timestampList);
        String sensorText = floatXYZToString(sensorName, values);
        switch (sensorName) {
            case "Accelerometer": accelerometerCurrentValues.setText(sensorText); break;
            case "Gyroscope": gyroscopeCurrentValues.setText(sensorText); break;
            case "Magnetometer": magnetometerCurrentValues.setText(sensorText); break;
        }
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

    private void writeData(JsonWriter writer) throws IOException {
        writer.beginObject();

        writer.name("accelerometer"); writeXyzList(writer, ACCELEROMETER_VALUES);
        writer.name("accelerometer_timestamps"); writeTimestamps(writer, ACCELEROMETER_TIMESTAMPS);
        writer.name("accelerometer_std"); writeXyzArray(writer, getStandardDeviationArray(ACCELEROMETER_VALUES));
        writer.name("accelerometer_bias"); writeXyzArray(writer, BiasCalibrationActivity.ACCELEROMETER_BIAS);
        writer.name("gyroscope"); writeXyzList(writer, GYROSCOPE_VALUES);
        writer.name("gyroscope_timestamps"); writeTimestamps(writer, GYROSCOPE_TIMESTAMPS);
        writer.name("gyroscope_std"); writeXyzArray(writer, getStandardDeviationArray(GYROSCOPE_VALUES));
        writer.name("gyroscope_bias"); writeXyzArray(writer, BiasCalibrationActivity.GYROSCOPE_BIAS);
        writer.name("magnetometer"); writeXyzList(writer, MAGNETOMETER_VALUES);
        writer.name("magnetometer_timestamps"); writeTimestamps(writer, MAGNETOMETER_TIMESTAMPS);
        writer.name("magnetometer_std"); writeXyzArray(writer, getStandardDeviationArray(MAGNETOMETER_VALUES));
        writer.name("magnetometer_bias"); writeXyzArray(writer, BiasCalibrationActivity.MAGNETOMETER_BIAS);

        writer.endObject();
    }

    private void writeXyzList(JsonWriter writer, List<float[]> values) throws IOException {
        writer.beginArray();
        for (float[] value : values) {
            writer.value(String.format(Locale.ENGLISH, "%f;%f;%f", value[0], value[1], value[2]));
        }
        writer.endArray();
    }

    private void writeXyzArray(JsonWriter writer, float[] values) throws IOException {
        writer.value(String.format(Locale.ENGLISH, "%f;%f;%f", values[0], values[1], values[2]));
    }

    private void writeTimestamps(JsonWriter writer, ArrayList<Long> values) throws IOException {
        writer.beginArray();
        for (long value : values) {
            writer.value(value);
        }
        writer.endArray();
    }

    private float[] getStandardDeviationArray(ArrayList<float[]> values) {
        return new float[] {
                (float)getStandardDeviation(values, 0),
                (float)getStandardDeviation(values, 1),
                (float)getStandardDeviation(values, 2),
        };
    }

    private double getStandardDeviation(ArrayList<float[]> valueList, int index) {
        DoubleStream values = valueList.stream().mapToDouble(f -> f[index]);
        double mean = values.average().orElse(0);
        ArrayList<Double> squaredDiffs = new ArrayList<>();
        for (float[] value : valueList) {
            double diff = mean - value[index];
            squaredDiffs.add(Math.pow(diff, 2));
        }
        double sum = squaredDiffs.stream().mapToDouble(d -> d).sum();
        double variance = sum / (squaredDiffs.size()-1);
        return Math.sqrt(variance);
    }
}
