package insertnamehere.com.github.datagatherer.util;

import android.os.Environment;
import android.util.JsonWriter;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import insertnamehere.com.github.datagatherer.activities.BiasCalibrationActivity;
import insertnamehere.com.github.datagatherer.activities.SensorActivity;

public class DataExporter {

    public static final String FILE_NAME = "/inh_datagatherer_export.json";

    private String path;
    private final ArrayList<float[]> accelerometerValues, gyroscopeValues, magnetometerValues;
    private final ArrayList<Long> accelerometerTimestamps, gyroscopeTimestamps, magnetometerTimestamps;

    public DataExporter(SensorActivity activity) {
        this(activity, null);
    }

    public DataExporter(SensorActivity activity, @Nullable String path) {
        this(activity.accelerometerValues, activity.gyroscopeValues, activity.magnetometerValues,
                activity.accelerometerTimestamps, activity.gyroscopeTimestamps, activity.magnetometerTimestamps,
                path);
    }

    public DataExporter(ArrayList<float[]> accelerometerValues,
                        ArrayList<float[]> gyroscopeValues,
                        ArrayList<float[]> magnetometerValues,
                        ArrayList<Long> accelerometerTimestamps,
                        ArrayList<Long> gyroscopeTimestamps,
                        ArrayList<Long> magnetometerTimestamps,
                        @Nullable String path) {
        this.accelerometerValues = accelerometerValues;
        this.gyroscopeValues = gyroscopeValues;
        this.magnetometerValues = magnetometerValues;
        this.accelerometerTimestamps = accelerometerTimestamps;
        this.gyroscopeTimestamps = gyroscopeTimestamps;
        this.magnetometerTimestamps = magnetometerTimestamps;
        if (path == null)
            this.path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + FILE_NAME;
        else if (!path.endsWith(FILE_NAME))
            this.path = path + FILE_NAME;
    }

    public boolean write() {
        try {
            File file = new File(path).getParentFile();
            if (file != null && !file.exists()) {
                file.mkdirs();
            }

            JsonWriter writer = new JsonWriter(new FileWriter(path));

            writer.setIndent("    ");
            writeData(writer);
            writer.flush();
            writer.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void writeData(JsonWriter writer)
            throws IOException {
        writer.beginObject();

        writeSensorData(writer, "accelerometer", accelerometerValues, accelerometerTimestamps);
        writeSensorData(writer, "gyroscope", gyroscopeValues, gyroscopeTimestamps);
        writeSensorData(writer, "magnetometer", magnetometerValues, magnetometerTimestamps);

        writer.endObject();
    }

    private void writeSensorData(JsonWriter writer, String sensorName, ArrayList<float[]> values, ArrayList<Long> timestamps) throws IOException {
        writer.name(sensorName); writer.beginObject();

        writer.name("values"); writeXyzList(writer, values);
        writer.name("timestamps"); writeTimestamps(writer, timestamps);
        float[] mean = DataMath.getMeanArray(values);
        writer.name("mean"); writeXyzArray(writer, mean);
        float[] variance = DataMath.getVarianceArray(values, mean);
        writer.name("variance"); writeXyzArray(writer, variance);
        float[] standardDeviation = DataMath.getStandardDeviationArray(variance);
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
}
