package insertnamehere.com.github.datagatherer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import insertnamehere.com.github.datagatherer.R;
import insertnamehere.com.github.datagatherer.util.DataExporter;
import insertnamehere.com.github.datagatherer.util.DataMath;
import insertnamehere.com.github.datagatherer.util.Logger;

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
        DataMath.clearDataList();
        DataMath.addData("Accelerometer", DataMath.createSensorData(accelerometerValues,
                accelerometerTimestamps, BiasCalibrationActivity.ACCELEROMETER_BIAS));
        DataMath.addData("Gyroscope", DataMath.createSensorData(gyroscopeValues,
                gyroscopeTimestamps, BiasCalibrationActivity.GYROSCOPE_BIAS));
        DataMath.addData("Magnetometer", DataMath.createSensorData(magnetometerValues,
                magnetometerTimestamps, BiasCalibrationActivity.MAGNETOMETER_BIAS));

        Intent switchToMainActivityIntent = new Intent(this, GraphSelectorActivity.class);

        boolean writeData = new DataExporter(this).write();
        switchToMainActivityIntent.putExtra("exportError", !writeData);

        startActivity(switchToMainActivityIntent);
        finish();
    }
}
