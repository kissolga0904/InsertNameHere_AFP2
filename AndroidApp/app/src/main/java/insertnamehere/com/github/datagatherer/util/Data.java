package insertnamehere.com.github.datagatherer.util;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.DoubleStream;

public class Data {
    List<float[]> data;
    List<Long> time;
    float[] bias;

    protected Data(List<float[]> data, List<Long> time) {
        this(data, time, null);
    }

    protected Data(List<float[]> data, List<Long> time, @Nullable float[] bias) {
        this.data = data;
        this.time = time;
        this.bias = bias;
    }

    public List<float[]> getData() {
        return data;
    }

    public List<Long> getTime() {
        return time;
    }

    @Nullable
    public float[] getBias() {
        return bias;
    }

    public Double[] getMeanArray() {
        List<Double> means = new ArrayList<>();
        for (float[] value : data) {
            float mean = getMean(value);
            means.add((double) mean);
        }
        return means.toArray(new Double[0]);
    }

    public Double[] getVarianceArray(Double[] mean) {
        List<Double> variances = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            Double variance = getVariance(data.get(i), mean[i]);
            variances.add(variance);
        }
        return variances.toArray(new Double[0]);
    }

    public Double[] getStandardDeviationArray(Double[] variance) {
        List<Double> stds = new ArrayList<>();
        for (double value : variance) {
            double std = getStandardDeviation(value);
            stds.add(std);
        }
        return stds.toArray(new Double[0]);
    }

    private static float getMean(float[] valuesArray) {
        float sum = 0;
        for (float f : valuesArray) sum += f;
        return sum / valuesArray.length;
    }

    private static double getVariance(float[] valuesArray, double mean) {
        ArrayList<Double> squaredDiffs = new ArrayList<>();
        for (float value : valuesArray) {
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
