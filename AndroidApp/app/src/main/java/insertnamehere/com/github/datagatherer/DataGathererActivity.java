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

import insertnamehere.com.github.datagatherer.util.DataExporter;

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

        boolean writeData = new DataExporter(this).write();
        switchToMainActivityIntent.putExtra("exportError", writeData);

        startActivity(switchToMainActivityIntent);
        finish();
    }
}
