package insertnamehere.com.github.datagatherer.util;

import android.util.Log;

public class Logger {

    private static final String TAG = "InsertNameHere-DataGatherer";

    public static void debug(String message) {
        Log.d(TAG, message);
    }

    public static void error(String message) {
        Log.e(TAG, message);
    }

    public static void info(String message) {
        Log.i(TAG, message);
    }

    public static void verbose(String message) {
        Log.v(TAG, message);
    }

    public static void warn(String message) {
        Log.w(TAG, message);
    }

    public static void wtf(String message) {
        Log.wtf(TAG, message);
    }
}
