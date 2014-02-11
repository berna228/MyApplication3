package com.example.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.logging.Handler;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SparkMain extends ActionBarActivity {
    TextView inputCmdText, inputResultText;
    private static final String MY_TAG = "SPARK_CORE_TAG";
    public String
            access_token = "558b85ab95e7481b0b85e8bc1bd9a37c3430e6cb",
            device_id = "48ff72065067555045311387";

//    private Thread timerThread;
    public Handler handler;
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

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(500);
                        readSensorData(SensorType);
                        // unable to setText, app crashes despite context view being set
                        inputResultText.setText("test");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
/*        timerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // Errors frequently
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
        });
        timerThread.start();*/
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
                        Log.d(MY_TAG, "SensorData for " + SensorType + " is " + sensorData.result);
//                        inputCmdText.setText(sensorData.cmd);
//                        inputResultText.setText(sensorData.result);
//                        inputCmdText.setText("command");
//                        inputResultText.setText("result");
                    }
                    public void failure(RetrofitError retrofitError){
                        Log.d(MY_TAG, "retrofitError = " + retrofitError);
                    }
                });
    }

}
