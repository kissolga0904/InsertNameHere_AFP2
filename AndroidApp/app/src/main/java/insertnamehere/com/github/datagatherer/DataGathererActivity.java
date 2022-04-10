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

        writeSensorData(writer, "accelerometer", ACCELEROMETER_VALUES, ACCELEROMETER_TIMESTAMPS);
        writeSensorData(writer, "gyroscope", GYROSCOPE_VALUES, GYROSCOPE_TIMESTAMPS);
        writeSensorData(writer, "magnetometer", MAGNETOMETER_VALUES, MAGNETOMETER_TIMESTAMPS);

        writer.endObject();
    }

    private void writeSensorData(JsonWriter writer, String sensorName, ArrayList<float[]> values, ArrayList<Long> timestamps) throws IOException {
        writer.name(sensorName); writer.beginObject();

        writer.name("values"); writeXyzList(writer, values);
        writer.name("timestamps"); writeTimestamps(writer, timestamps);
        float[] mean = getMeanArray(values);
        writer.name("mean"); writeXyzArray(writer, mean);
        float[] variance = getVarianceArray(values, mean);
        writer.name("variance"); writeXyzArray(writer, variance);
        float[] standardDeviation = getStandardDeviationArray(variance);
        writer.name("standard_deviation"); writeXyzArray(writer, standardDeviation);
        float[] bias = BiasCalibrationActivity.getBias(sensorName);
        if (bias != null) {
            writer.name("bias"); writeXyzArray(writer, bias);
        }

        writer.endObject();
    }

    private void writeXyzList(JsonWriter writer, ArrayList<float[]> values) throws IOException {
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

    private float[] getMeanArray(ArrayList<float[]> values) {
        return new float[] {
                (float)getMean(values, 0),
                (float)getMean(values, 1),
                (float)getMean(values, 2)
        };
    }

    private float[] getVarianceArray(ArrayList<float[]> values, float[] mean) {
        return new float[] {
                (float)getVariance(values, 0, mean[0]),
                (float)getVariance(values, 1, mean[1]),
                (float)getVariance(values, 2, mean[2])
        };
    }

    private float[] getStandardDeviationArray(float[] variance) {
        return new float[] {
                (float)getStandardDeviation(variance[0]),
                (float)getStandardDeviation(variance[1]),
                (float)getStandardDeviation(variance[2])
        };
    }

    private double getMean(ArrayList<float[]> valueList, int index) {
        DoubleStream values = valueList.stream().mapToDouble(f -> f[index]);
        return values.average().orElse(0);
    }

    private double getVariance(ArrayList<float[]> valueList, int index, double mean) {
        ArrayList<Double> squaredDiffs = new ArrayList<>();
        for (float[] value : valueList) {
            double diff = mean - value[index];
            squaredDiffs.add(Math.pow(diff, 2));
        }
        double sum = squaredDiffs.stream().mapToDouble(d -> d).sum();
        return sum / (squaredDiffs.size()-1);
    }

    private double getStandardDeviation(double variance) {
        return Math.sqrt(variance);
    }
}
