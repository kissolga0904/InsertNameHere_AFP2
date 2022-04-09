package insertnamehere.com.github.datagatherer;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.DoubleStream;

public class BiasCalibrationActivity extends SensorActivity {

    protected static float[] ACCELEROMETER_BIAS, GYROSCOPE_BIAS, MAGNETOMETER_BIAS;
    private float[] gravity;
    private TextView accelerometerBiasTextView, gyroscopeBiasTextView, magnetometerBiasTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bias_calibration);

        Logger.debug("BiasCalibrationActivity#onCreate: Initializing TextView variables");
        accelerometerBiasTextView = findViewById(R.id.accelerometerTextView);
        gyroscopeBiasTextView = findViewById(R.id.gyroscopeTextView);
        magnetometerBiasTextView = findViewById(R.id.magnetometerTextView);

        TextView countdownTextView = findViewById(R.id.countdownTextView);

        new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countdownTextView.setText(String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                Intent switchToMainActivityIntent = new Intent(BiasCalibrationActivity.this, MainActivity.class);
                startActivity(switchToMainActivityIntent);
                finish();
            }
        }.start();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        super.onSensorChanged(event);
        if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
            this.gravity = event.values;
        }
    }

    @Override
    protected void recordData(String sensorName, float[] values, long currentTimestamp, ArrayList<float[]> valueList, ArrayList<Long> timestampList) {
        float[] array = {
                values[0],
                values[1],
                values[2]
        };
        valueList.add(array);
        float[] bias = {
                (float)getBias(valueList, 0),
                (float)getBias(valueList, 1),
                (float)getBias(valueList, 2)
        };
        switch (sensorName) {
            case "Accelerometer":
                ACCELEROMETER_BIAS = gravity == null ? bias : new float[]{
                        (float)getBias(valueList, 0, gravity),
                        (float)getBias(valueList, 1, gravity),
                        (float)getBias(valueList, 2, gravity)
                };
                accelerometerBiasTextView.setText(floatXYZToString(sensorName, ACCELEROMETER_BIAS));
                break;
            case "Gyroscope":
                GYROSCOPE_BIAS = bias;
                gyroscopeBiasTextView.setText(floatXYZToString(sensorName, GYROSCOPE_BIAS));
                break;
            case "Magnetometer":
                MAGNETOMETER_BIAS = bias;
                magnetometerBiasTextView.setText(floatXYZToString(sensorName, MAGNETOMETER_BIAS));
                break;
        }
    }

    private double getBias(ArrayList<float[]> valueList, int index) {
        return getBias(valueList, index, new float[]{0, 0, 0});
    }

    private double getBias(ArrayList<float[]> valueList, int index, float[] knownBias) {
        DoubleStream values = valueList.stream().mapToDouble(f -> f[index]);
        return values.average().orElse(0) - knownBias[index];
    }
}
