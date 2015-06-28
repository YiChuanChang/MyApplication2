package netdb.course.softwarestudion.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.graphics.PixelFormat;
import android.view.MotionEvent;

/**
 * Created by yi_chiuan on 2015/6/28.
 */
public class ScoreGLSurfaceView extends GLSurfaceView {

    OpenGLRenderer mRenderer;
    public int highScore;
    public int scoreNow;
    public Bitmap map0,  map1,  map2,  map3,  map4,  map5,  map6,  map7,  map8,  map9;
    public Bitmap map0p, map1p, map2p, map3p, map4p, map5p, map6p, map7p, map8p, map9p;
    Handler handler;

    public  ScoreGLSurfaceView(Context context,Handler handler) {

        super(context);

        read_bitmap();

        setEGLConfigChooser(8, 8, 8, 8, 16, 0);

        getHolder() .setFormat(PixelFormat.TRANSLUCENT);

        mRenderer = new OpenGLRenderer(); //創建場景渲染器

        setRenderer(mRenderer);

        this.handler=handler;

    }

    public boolean onTouchEvent(MotionEvent e) {


        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                handler.sendMessage(Message.obtain(handler, 1));
        }
        return true;

    }

    public void setScore(int HScore, int NScore){
        highScore = HScore;
        scoreNow  = NScore;
    }

    private void read_bitmap(){
        map1 = BitmapFactory.decodeResource(getResources(),
                R.drawable.b1);
        map2 = BitmapFactory.decodeResource(getResources(),
                R.drawable.b2);
        map3 = BitmapFactory.decodeResource(getResources(),
                R.drawable.b3);
        map4 = BitmapFactory.decodeResource(getResources(),
                R.drawable.b4);
        map5 = BitmapFactory.decodeResource(getResources(),
                R.drawable.b5);
        map6 = BitmapFactory.decodeResource(getResources(),
                R.drawable.b6);
        map7 = BitmapFactory.decodeResource(getResources(),
                R.drawable.b7);
        map8 = BitmapFactory.decodeResource(getResources(),
                R.drawable.b8);
        map9 = BitmapFactory.decodeResource(getResources(),
                R.drawable.b9);
        map0 = BitmapFactory.decodeResource(getResources(),
                R.drawable.b0);
        map1p = BitmapFactory.decodeResource(getResources(),
                R.drawable.bpoint1);
        map2p = BitmapFactory.decodeResource(getResources(),
                R.drawable.bpoint2);
        map3p = BitmapFactory.decodeResource(getResources(),
                R.drawable.bpoint3);
        map4p = BitmapFactory.decodeResource(getResources(),
                R.drawable.bpoint4);
        map5p = BitmapFactory.decodeResource(getResources(),
                R.drawable.bpoint5);
        map6p = BitmapFactory.decodeResource(getResources(),
                R.drawable.bpoint6);
        map7p = BitmapFactory.decodeResource(getResources(),
                R.drawable.bpoint7);
        map8p = BitmapFactory.decodeResource(getResources(),
                R.drawable.bpoint8);
        map9p = BitmapFactory.decodeResource(getResources(),
                R.drawable.bpoint9);
        map0p = BitmapFactory.decodeResource(getResources(),
                R.drawable.bpoint0);
    }



    //inner class part
    public class OpenGLRenderer implements GLSurfaceView.Renderer {
        private Cube cube;
        private Square scoreSquareTen;
        private Square scoreSquare;
        private Square highScoreSquareTen;
        private Square highScoreSquare;
        private float angle = 0;
        public OpenGLRenderer() {
            // 初始化
            cube           = new Cube();
            scoreSquareTen = new Square();
            scoreSquare    = new Square();
            highScoreSquareTen  = new Square();
            highScoreSquare= new Square();

        }

        @Override
        public void onDrawFrame(GL10 gl) {
            System.gc();
            // TODO Auto-generated method stub
            // 清除螢幕和深度緩衝區
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
            // 以單位矩陣取代目前的矩陣
            gl.glClearColor(0,0,0,0);
            gl.glLoadIdentity();
            // Z軸轉置 10 單位
            gl.glPushMatrix();
            gl.glTranslatef(0, 0, -10);
            gl.glRotatef(-90, 0, 0, 1);//旋轉


            // 存儲目前陣列
            gl.glPushMatrix();
            // 反時鐘旋轉
            gl.glRotatef(angle, 1, 1, 0);
            gl.glRotatef(angle/2, 0, 0, 1);
            // 畫出方形
            cube.draw(gl);
            // 復原成最後的矩陣
            gl.glPopMatrix();

            gl.glPushMatrix();
            gl.glRotatef(-45f, 0, 1, 0);
            //gl.glTranslatef(-2, 0, 0);

            set_square();
            gl.glTranslatef(-4f, 0f,  3);
            scoreSquare.draw(gl);
            gl.glTranslatef(0,   -1f,  0);
            scoreSquareTen.draw(gl);

            gl.glTranslatef(2.9f, 1f,  -2);
            highScoreSquare.draw(gl);
            gl.glTranslatef(0,   -1f,  0);
            highScoreSquareTen.draw(gl);
            gl.glPopMatrix();

            gl.glPopMatrix();

            // 增加角度
            angle++;
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            // TODO Auto-generated method stub
            // 設定新視域視窗的大小
            gl.glViewport(0, 0, width, height);
            // 選擇投射的陣列模式
            gl.glMatrixMode(GL10.GL_PROJECTION);
            // 重設投射陣
            gl.glLoadIdentity();
            // 計算視窗的寬高比率
            GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f, 100.0f);
            // 選擇MODELVIEW陣列
            gl.glMatrixMode(GL10.GL_MODELVIEW);
            // 重設MODELVIEW陣列
            gl.glLoadIdentity();
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            // TODO Auto-generated method stub
            // 設定背景顏色為黑色, 格式是RGBA
            gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
            // 設定流暢的陰影模式
            gl.glShadeModel(GL10.GL_SMOOTH);
            // 深度緩區的設定
            gl.glClearDepthf(1.0f);
            // 啟動深度的測試
            gl.glEnable(GL10.GL_DEPTH_TEST);
            // GL_LEQUAL深度函式測試
            gl.glDepthFunc(GL10.GL_LEQUAL);
            // 設定很好的角度計算模式
            gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);

        }

        private void set_square() {

          // Log.d("scoreNow", Float.toString(scoreNow%10));

            //ten
            switch (scoreNow/10) {
                case 0:scoreSquareTen.setBitmap(map0);break;
                case 1:scoreSquareTen.setBitmap(map1);break;
                case 2:scoreSquareTen.setBitmap(map2);break;
                case 3:scoreSquareTen.setBitmap(map3);break;
                case 4:scoreSquareTen.setBitmap(map4);break;
                case 5:scoreSquareTen.setBitmap(map5);break;
                case 6:scoreSquareTen.setBitmap(map6);break;
                case 7:scoreSquareTen.setBitmap(map7);break;
                case 8:scoreSquareTen.setBitmap(map8);break;
                case 9:scoreSquareTen.setBitmap(map9);break;
            }
            //
            switch (scoreNow%10) {
                case 0:scoreSquare.setBitmap(map0);break;
                case 1:scoreSquare.setBitmap(map1);break;
                case 2:scoreSquare.setBitmap(map2);break;
                case 3:scoreSquare.setBitmap(map3);break;
                case 4:scoreSquare.setBitmap(map4);break;
                case 5:scoreSquare.setBitmap(map5);break;
                case 6:scoreSquare.setBitmap(map6);break;
                case 7:scoreSquare.setBitmap(map7);break;
                case 8:scoreSquare.setBitmap(map8);break;
                case 9:scoreSquare.setBitmap(map9);break;
            }
            //ten
            switch (highScore%10) {
                case 0:highScoreSquare.setBitmap(map0);break;
                case 1:highScoreSquare.setBitmap(map1);break;
                case 2:highScoreSquare.setBitmap(map2);break;
                case 3:highScoreSquare.setBitmap(map3);break;
                case 4:highScoreSquare.setBitmap(map4);break;
                case 5:highScoreSquare.setBitmap(map5);break;
                case 6:highScoreSquare.setBitmap(map6);break;
                case 7:highScoreSquare.setBitmap(map7);break;
                case 8:highScoreSquare.setBitmap(map8);break;
                case 9:highScoreSquare.setBitmap(map9);break;
            }
            //
            switch (highScore/10) {
                case 0:highScoreSquareTen.setBitmap(map0);break;
                case 1:highScoreSquareTen.setBitmap(map1);break;
                case 2:highScoreSquareTen.setBitmap(map2);break;
                case 3:highScoreSquareTen.setBitmap(map3);break;
                case 4:highScoreSquareTen.setBitmap(map4);break;
                case 5:highScoreSquareTen.setBitmap(map5);break;
                case 6:highScoreSquareTen.setBitmap(map6);break;
                case 7:highScoreSquareTen.setBitmap(map7);break;
                case 8:highScoreSquareTen.setBitmap(map8);break;
                case 9:highScoreSquareTen.setBitmap(map9);break;
            }
        }

    }
}
