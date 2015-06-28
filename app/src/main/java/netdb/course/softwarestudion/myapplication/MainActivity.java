package netdb.course.softwarestudion.myapplication;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;


public class MainActivity extends Activity implements SensorEventListener {
    private MyGLSurfaceView mGLSurfaceView;
    private SensorManager mgr;
    private Sensor gyro;
    private Handler handler;
    SharedPreferences settingsActivity;


    @Override

        public void onCreate (Bundle savedInstanceState){

            super.onCreate(savedInstanceState);
         handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        onGameOver();
                        break;
                    case 1:
                        finish();
                        break;
                }
            }
        };

            requestWindowFeature(Window.FEATURE_NO_TITLE);

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            onGameStart();


        }
    public void onGameStart(){
        settingsActivity = getPreferences(MODE_PRIVATE);

        mGLSurfaceView = new MyGLSurfaceView(this,settingsActivity,handler);

        setContentView(mGLSurfaceView);

        Display display = getWindowManager().getDefaultDisplay();

        Point size = new Point();

        display.getSize(size);

        mGLSurfaceView.windowSizeX=size.x;

        mGLSurfaceView.windowSizeY=size.y;

        Log.d("x:", Integer.toString(size.x));

        mGLSurfaceView.setFocusableInTouchMode(true);//設置為可觸控

        mGLSurfaceView.requestFocus();//獲取焦點

        mgr  = (SensorManager) this.getSystemService(SENSOR_SERVICE);

        gyro = mgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);//取得陀螺儀的偵測
    }
    public void onGameOver(){
        ScoreGLSurfaceView view = new ScoreGLSurfaceView(this,handler);
        // 載入位元圖
        int highScore = settingsActivity.getInt("HighScore",0);
        view.setScore(highScore, mGLSurfaceView.score);
        //mGLSurfaceView.mRenderer= null;
        //mGLSurfaceView = null;
        System.gc();
        setContentView(view);
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
            if(mGLSurfaceView!=null){
                mGLSurfaceView.onSensorEvent(event);
            }
        }

    }

