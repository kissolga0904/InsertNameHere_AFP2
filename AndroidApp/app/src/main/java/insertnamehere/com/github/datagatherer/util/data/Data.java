package insertnamehere.com.github.datagatherer.util.data;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.DoubleStream;

public class Data {
    double[][] data;
    long[] time;
    float[] bias;

    public Data(double[][] data, long[] time) {
        this(data, time, null);
    }

    public Data(double[][] data, long[] time, @Nullable float[] bias) {
        this.data = data;
        this.time = time;
        this.bias = bias;
    }

    @Nullable
    public float[] getBias() {
        return bias;
    }



    public Double[] getMeanArray() {
        List<Double> means = new ArrayList<>();
        for (double[] value : data) {
            Double mean = getMean(value);
            means.add(mean);
        }
        return means.toArray(new Double[0]);
    }

    public Double[] getVarianceArray(double[] mean) {
        List<Double> variances = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            Double variance = getVariance(data[i], mean[i]);
            variances.add(variance);
        }
        return variances.toArray(new Double[0]);
    }

    public Double[] getStandardDeviationArray(double[] variance) {
        List<Double> stds = new ArrayList<>();
        for (double value : variance) {
            Double std = getStandardDeviation(value);
            stds.add(std);
        }
        return stds.toArray(new Double[0]);
    }

    private static double getMean(double[] valuesArray) {
        DoubleStream values = Arrays.stream(valuesArray);
        return values.average().orElse(0);
    }

    private static double getVariance(double[] valuesArray, double mean) {
        ArrayList<Double> squaredDiffs = new ArrayList<>();
        for (double value : valuesArray) {
            double diff = mean - value;
            squaredDiffs.add(Math.pow(diff, 2));
        }
        double sum = squaredDiffs.stream().mapToDouble(d -> d).sum();
        return sum / (squaredDiffs.size()-1);
    }

    private static double getStandardDeviation(double variance) {
        return Math.sqrt(variance);
    }
}
