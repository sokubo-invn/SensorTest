package com.example.sensortest;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer, gyroscope;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // センサーマネージャーを取得
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // 加速度センサーを取得
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // ジャイロセンサーを取得
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        // グラフを表示するViewを取得
        LineChart chart = findViewById(R.id.chart);
// グラフのデータセットを作成
        LineDataSet dataSet = new LineDataSet(new ArrayList<>(), "Gyroscope");
        dataSet.setColor(Color.BLUE);
        dataSet.setDrawValues(false);
// データセットをグラフに追加
        LineData data = new LineData();
        data.addDataSet(dataSet);
        chart.setData(data);
// グラフのオプションを設定
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(true);
        chart.getXAxis().setEnabled(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getAxisLeft().setAxisMinimum(-20f);
        chart.getAxisLeft().setAxisMaximum(20f);
        chart.getAxisRight().setAxisMinimum(-20f);
        chart.getAxisRight().setAxisMaximum(20f);
        chart.setAutoScaleMinMaxEnabled(true);
        chart.invalidate();
    }
    @Override
    protected void onResume() {
        super.onResume();
        // 加速度センサーのリスナーを登録
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);

        // ジャイロセンサーのリスナーを登録
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // リスナーの登録を解除
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // センサーのタイプによって表示する値を切り替え
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // センサーの値を取得
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            // 加速度センサーの値を表示
            Log.d("Accelerometer", "X: " + x + " Y: " + y + " Z: " + z);
            // 取得した値をグラフに追加
            dataSet.addEntry(new Entry(dataSet.getEntryCount(), x));
            dataSet.addEntry(new Entry(dataSet.getEntryCount(), y));
            dataSet.addEntry(new Entry(dataSet.getEntryCount(), z));
            // データセットが更新されたことをグラフに通知
            chart.notifyDataSetChanged();
            // 最新のデータを表示するようにグラフをスクロール
            chart.setVisibleXRangeMaximum(100);
            chart.moveViewToX(data.getEntryCount());
        } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            // センサーの値を取得
            float gx = event.values[0];
            float gy = event.values[1];
            float gz = event.values[2];
            // ジャイロセンサーの値を表示
            Log.d("Gyroscope", "X: " + gx + " Y: " + gy + " Z: " + gz);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // 精度が変更されたときの処理
    }
}