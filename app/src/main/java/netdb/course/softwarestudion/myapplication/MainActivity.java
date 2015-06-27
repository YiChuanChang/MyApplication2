package netdb.course.softwarestudion.myapplication;

import android.app.Activity;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;


public class MainActivity extends Activity implements SensorEventListener {
    private MyGLSurfaceView mGLSurfaceView;
    private SensorManager mgr;
    private Sensor gyro;
    @Override

        public void onCreate (Bundle savedInstanceState){

            super.onCreate(savedInstanceState);

            requestWindowFeature(Window.FEATURE_NO_TITLE);

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

            mGLSurfaceView = new MyGLSurfaceView(this);
            setContentView(mGLSurfaceView);

            Display display = getWindowManager().getDefaultDisplay();

            Point size = new Point();

            display.getSize(size);

            mGLSurfaceView.windowSizeX=size.x;

            mGLSurfaceView.windowSizeY=size.y;

            Log.d("x:",Integer.toString(size.x) );



            mGLSurfaceView.setFocusableInTouchMode(true);//設置為可觸控

            mGLSurfaceView.requestFocus();//獲取焦點

            mgr  = (SensorManager) this.getSystemService(SENSOR_SERVICE);

            gyro = mgr.getDefaultSensor(Sensor.TYPE_GYROSCOPE);//取得陀螺儀的偵測

        }

        @ Override
        protected void onResume(){
            super.onResume();
            mgr.registerListener(this, gyro, SensorManager.SENSOR_DELAY_GAME);
            mGLSurfaceView.onResume();
        }

        @ Override
        protected void onPause(){
            super.onPause();
            mgr.unregisterListener(this, gyro);
            mGLSurfaceView.onPause();
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy){

        }

        public void onSensorChanged(SensorEvent event){
            mGLSurfaceView.onSensorEvent(event);
        }

    }

