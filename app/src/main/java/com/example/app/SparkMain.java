package com.example.app;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SparkMain extends ActionBarActivity {
    TextView inputCmdText, inputResultText;
    private static final String TAG_SPARK_CORE = "SPARK_CORE";
    private static final String TAG_RETROFIT = "RETROFIT";
    public String
            access_token = "access_token",
            device_id = "device_id";

    private Thread timerThread;
    private Handler mHandler = new Handler();
    public final String SensorType = "input";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spark_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.spark_main, new PlaceholderFragment())
                    .commit();
        }
        inputCmdText = (TextView) findViewById(R.id.input_cmd_text);
        inputResultText = (TextView) findViewById(R.id.input_result_text);

//        readSensorData("input");


/*        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(500);
                        readSensorData(SensorType);
                        // unable to setText, app crashes despite context view being set
//                        inputResultText.setText("test");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });*/
        timerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // Errors frequently
                while (true) {
                    try {
                        Thread.sleep(1000);
                        readSensorData(SensorType);
                        // unable to setText, app crashes despite context view being set
//                        inputResultText.setText("test");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        timerThread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    public void activateButton(View view) {
        readSensorData(SensorType);
//        inputResultText.setText("set");
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        public PlaceholderFragment() {
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.spark_main_fragment, container, false);
        }
    }

    public void readSensorData(final String SensorType) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://api.spark.io")
                .build();
        SparkService apiManager = restAdapter.create(SparkService.class);

        apiManager.getSensorData(device_id, SensorType, access_token,
                new Callback<SparkCoreData>() {

                    public void success(SparkCoreData sensorData, Response response) {
                        Log.d(TAG_SPARK_CORE, "SensorData for " + SensorType + " is " + sensorData.result);
//                        inputCmdText.setText(sensorData.cmd);
//                        inputResultText.setText(sensorData.result);
//                        inputCmdText.setText("command");
//                        inputResultText.setText("result");
                    }
                    //fails typically after 0~15 sequential executions
                    public void failure(RetrofitError retrofitError){
                        //returns retrofit.RetrofitError
//                        Log.d(TAG_RETROFIT, "= " + retrofitError);

                        //returns null
//                        Log.d(TAG_RETROFIT, "= " + retrofitError.getMessage());
                        //returns com.example.app.SparkCoreData@532b66c0
                        Log.d(TAG_RETROFIT, "body = " + retrofitError.getBody());
                        //returns retrofit.client.Response@53297904
                        Log.d(TAG_RETROFIT, "response = " + retrofitError.getResponse());
                        //returns correct URL:
                        //https://api.spark.io/v1/devices/48ff72065067555045311387/input?access_token='accesstoken'
                        Log.d(TAG_RETROFIT, "URL = " + retrofitError.getUrl());
                    }
                });
    }
}
