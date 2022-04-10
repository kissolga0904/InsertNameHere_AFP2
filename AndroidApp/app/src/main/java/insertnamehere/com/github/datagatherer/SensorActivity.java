package insertnamehere.com.github.datagatherer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {
    protected final ArrayList<float[]> ACCELEROMETER_VALUES = new ArrayList<>(); // m/s^2 [1+3]
    protected final ArrayList<float[]> GYROSCOPE_VALUES = new ArrayList<>(); // rad/s [1+3]
    protected final ArrayList<float[]> MAGNETOMETER_VALUES = new ArrayList<>(); // uT [1+3]
    protected final ArrayList<Long> ACCELEROMETER_TIMESTAMPS = new ArrayList<>();
    protected final ArrayList<Long> GYROSCOPE_TIMESTAMPS = new ArrayList<>();
    protected final ArrayList<Long> MAGNETOMETER_TIMESTAMPS = new ArrayList<>();

    private SensorManager sensorManager;
    protected Sensor accelerometer, gyroscope, magnetometer, gravity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int sensorDelay = SensorManager.SENSOR_DELAY_NORMAL;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            sensorDelay = 3 - extras.getInt("sensorDelay", 0);
        }

        Logger.debug("SensorActivity#onCreate: Initializing Sensor Services");
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        gravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        Logger.debug("SensorActivity#onCreate: Registering Sensor Listeners");
        sensorManager.registerListener(this, accelerometer, sensorDelay);
        sensorManager.registerListener(this, gyroscope, sensorDelay);
        sensorManager.registerListener(this, magnetometer, sensorDelay);
        sensorManager.registerListener(this, gravity, sensorDelay);
        Logger.debug("SensorActivity#onCreate: Initialization Complete!");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;

        // TODO We need to find out the bias error of the accelerometer by taking an average of its
        //   output while it is not undergoing any acceleration. Then we can just subtract it from the output
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            recordData(
                    "Accelerometer",
                    event.values, event.timestamp,
                    ACCELEROMETER_VALUES, ACCELEROMETER_TIMESTAMPS
            );
        }
        // TODO We need to find out the bias error of the gyroscope by taking an average of its
        //   output while it is not undergoing any rotation. Then we can just subtract it from the output
        if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            recordData(
                    "Gyroscope",
                    event.values, event.timestamp,
                    GYROSCOPE_VALUES, GYROSCOPE_TIMESTAMPS
            );
        }
        if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            recordData(
                    "Magnetometer",
                    event.values, event.timestamp,
                    MAGNETOMETER_VALUES, MAGNETOMETER_TIMESTAMPS
            );
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}

    protected void recordData(String sensorName,
                            float[] values, long currentTimestamp,
                            ArrayList<float[]> valueList, ArrayList<Long> timestampList) {
        float[] array = {
                values[0],
                values[1],
                values[2]
        };
        valueList.add(array);
        timestampList.add(currentTimestamp);
    }

    @Override
    protected void onDestroy() {
        ACCELEROMETER_VALUES.clear();
        GYROSCOPE_VALUES.clear();
        MAGNETOMETER_VALUES.clear();

        sensorManager.unregisterListener(this);
        magnetometer = null;
        gyroscope = null;
        accelerometer = null;
        sensorManager = null;
        super.onDestroy();
    }

    protected String floatXYZToString(String sensorName, float[] values) {
        return String.format(Locale.ENGLISH, "%s:\n  x: %f\n  y: %f\n  z: %f", sensorName, values[0], values[1], values[2]);
    }
}