package insertnamehere.com.github.datagatherer.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import insertnamehere.com.github.datagatherer.R;
import insertnamehere.com.github.datagatherer.util.Logger;
import insertnamehere.com.github.datagatherer.util.Data;

public class GraphActivity extends AppCompatActivity {

    private static Data DATA;
    private static String DATA_NAME;

    LineChart lineChart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        lineChart = findViewById(R.id.lineChart);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        List<float[]> dataValues = DATA.getData();
        List<Long> time = DATA.getTime();
        int lineAmount = dataValues.get(0).length;
        for (int i = 0; i < lineAmount; i++) {
            ArrayList<Entry> entries = new ArrayList<>();
            for (int j = 0; j < dataValues.size(); j++) {
                entries.add(new Entry(time.get(j), dataValues.get(j)[i]));
            }
            String label = lineAmount == 1 ? "values" :
                    i == 0 ? "x" : i == 1 ? "y" : "z";
            LineDataSet dataSet = new LineDataSet(entries, label);
            dataSet.setFillAlpha(110);
            dataSet.setColor(i == 0 ? Color.GREEN : i == 1 ? Color.RED : Color.BLUE);
            dataSet.setDrawCircles(false);
            dataSet.setDrawCircleHole(false);
            dataSets.add(dataSet);
        }

        LineData data = new LineData(dataSets);

        lineChart.setData(data);

        TextView dataNameTextView = findViewById(R.id.dataNameTextView);
        dataNameTextView.setText(DATA_NAME);
        TextView extraDataInfoTextView = findViewById(R.id.extraDataInfoTextView);
        Double[] mean = DATA.getMeanArray();
        Double[] variance = DATA.getVarianceArray(mean);
        Double[] standardDeviation = DATA.getStandardDeviationArray(variance);
        String extraInfo;
        boolean oneLine = lineAmount == 1;
        if (oneLine) {
            extraInfo = String.format(Locale.ENGLISH,
                    "Mean: %1$.2f" +
                    "\nVariance: %2$.2f" +
                    "\nStandard Deviation: %3$.2f", mean[0], variance[0], standardDeviation[0]
            );
        } else {
            extraInfo = String.format(Locale.ENGLISH,
                    "Means: %1$.2f, %2$.2f, %3$.2f" +
                            "\nVariances: %4$.2f, %5$.2f, %6$.2f" +
                            "\nStandard Deviations: %7$.2f, %8$.2f, %9$.2f",
                    mean[0], mean[1], mean[2],
                    variance[0], variance[1], variance[2],
                    standardDeviation[0], standardDeviation[1], standardDeviation[2]
            );
        }
        if (DATA.getBias() != null) {
            if (oneLine) {
                extraInfo += String.format(Locale.ENGLISH, "\nBias: %1$.2f", DATA.getBias()[0]);
            } else {
                extraInfo += String.format(Locale.ENGLISH, "\nBias: %1$.2f, %2$.2f, %3$.2f", DATA.getBias()[0], DATA.getBias()[1], DATA.getBias()[2]);
            }
        }
        extraDataInfoTextView.setText(extraInfo);
    }

    public static void setSelectedData(Data data) {
        DATA = data;
    }

    public static void setDataName(String name) {
        DATA_NAME = name;
    }
}
