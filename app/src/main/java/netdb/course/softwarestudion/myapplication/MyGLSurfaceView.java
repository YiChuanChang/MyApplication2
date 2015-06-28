package netdb.course.softwarestudion.myapplication;

/**
 * Created by Waiting on 2015/6/25.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.SensorEvent;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.os.Message;
import android.view.MotionEvent;
import android.os.Handler;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

public class MyGLSurfaceView extends GLSurfaceView {

        public int score=0;

        private SharedPreferences settingsActivity;

        private Handler handler;

        public Timer timer;

        public boolean ifTimerStart;

        private final float TOUCH_SCALE_FACTOR = 180.0f/320;//角度縮放比例

        public int windowSizeX;

        public float timeCount;

        public int windowSizeY;

        private final float ROTATE_ANGLE = 3f;

        private final float SECTION_ANGLE = 45f;

        private final float BLOCK_LENGTH = 20f;

        private final float CYLINDER_RADIUS = 15f;

        private final float COUNT_DOWN = 5F;

        private float MOVING_CLOCK;

        private float MOVING_RATE;

        public  SceneRenderer mRenderer;//場景渲染器

        private int lightAngle=90;//燈的當前角度

        private static Canvas canvas;

        public Bitmap map0,  map1,  map2,  map3,  map4,  map5,  map6,  map7,  map8,  map9;

        public Bitmap map0p, map1p, map2p, map3p, map4p, map5p, map6p, map7p, map8p, map9p;


        public MyGLSurfaceView(Context context, SharedPreferences settingsActivity,Handler handler) {

                super(context);

                this.handler=handler;

                this.settingsActivity=settingsActivity;

                mRenderer = new SceneRenderer(); //創建場景渲染器

                setRenderer(mRenderer); //設置渲染器

                setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//設置渲染模式為主動渲染

                MOVING_CLOCK = 0;

                MOVING_RATE = 4;

                timeCount=0.0f;

                timer =new Timer();

                ifTimerStart = false;

                canvas = new Canvas();

                read_bitmap();
        }

//觸摸事件回調方法


    private class Timer extends Thread{
        private boolean isRunning = true;

        public void run(){
            super.run();
            while(isRunning){
                timeCount+=0.1f;
                try  {
                    Thread.sleep(100);//休息10ms再重繪
                }
                catch(Exception e)  {
                    e.printStackTrace();
                }
            }
        }
        public void stopThread()
        {
            this.isRunning = false;
        }

    }
        @Override

        public boolean onTouchEvent(MotionEvent e) {

            mRenderer.touchX = e.getX();
            mRenderer.touchY = e.getY();
            switch (e.getAction()) {
            case MotionEvent.ACTION_UP:
                if(MOVING_CLOCK==0){
                    mRenderer.isTouch=true;
                }
                if(!ifTimerStart){
                    timer.start();
                    ifTimerStart = true;
                }
        }
        return true;

    }

    public boolean onSensorEvent(SensorEvent e){
        if(e.values[0]>0.5 || e.values[0]<-0.5){
            for(int i=0;i<mRenderer.cylinderList.size();i++){
                // Log.d("cycles", Integer.toString(mRenderer.cylinderList.size())+" "+Integer.toString(mRenderer.blockList.size()) );
                mRenderer.cylinderList.get(i).mAngleX += e.values[0]*ROTATE_ANGLE;
            }
            for(int i=0;i<mRenderer.blockList.size();i++){
                //  Log.d("cycles", Integer.toString(mRenderer.cylinderList.size())+" "+Integer.toString(mRenderer.blockList.size()) );
                mRenderer.blockList.get(i).mAngleX += e.values[0]*ROTATE_ANGLE;
            }
        }

        return true;
    }

    private class SceneRenderer implements GLSurfaceView.Renderer
    {

        int textureId;//紋理名稱ID
        private boolean isTouch;
        private float touchX,touchY;
        ArrayList<DrawCylinder> cylinderList;
        ArrayList<DrawWhiteBlock> blockList;
        Square time_square_ten;
        Square time_square;
        Square time_square_point;
        public SceneRenderer()
        {
            isTouch=false;
            blockList=new ArrayList();
            blockList.add(new DrawWhiteBlock(BLOCK_LENGTH,CYLINDER_RADIUS,SECTION_ANGLE));
            blockList.add(new DrawWhiteBlock(BLOCK_LENGTH,CYLINDER_RADIUS,SECTION_ANGLE));
            blockList.add(new DrawWhiteBlock(BLOCK_LENGTH, CYLINDER_RADIUS, SECTION_ANGLE));
            blockList.get(1).deepX -= BLOCK_LENGTH;
            blockList.get(2).deepX -= 2*BLOCK_LENGTH;

            cylinderList=new ArrayList();
            cylinderList.add(new DrawCylinder(BLOCK_LENGTH,CYLINDER_RADIUS,SECTION_ANGLE,2));
            cylinderList.add(new DrawCylinder(BLOCK_LENGTH,CYLINDER_RADIUS,SECTION_ANGLE,2));
            cylinderList.add(new DrawCylinder(BLOCK_LENGTH,CYLINDER_RADIUS,SECTION_ANGLE,2));
            cylinderList.get(1).deepX -= BLOCK_LENGTH;
            cylinderList.get(2).deepX -= 2*BLOCK_LENGTH;

            time_square         = new Square();
            time_square_ten     = new Square();
            time_square_point   = new Square();

        }

        public void onDrawFrame(GL10 gl) {
            System.gc();
            if(COUNT_DOWN-timeCount<=0  ){
                int highScore=settingsActivity.getInt("HighScore",0);
                System.out.println(highScore);
                System.out.println(score);
                if(score>highScore){
                    SharedPreferences.Editor editor =settingsActivity.edit();
                    editor.putInt("HighScore", score);

                    editor.commit();
                }
                handler.sendMessage(Message.obtain(handler, 0));
            }
        //清除顏色緩存

            gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        //設置當前矩陣為模式矩陣

            gl.glMatrixMode(GL10.GL_MODELVIEW);

        //設置當前矩陣為單位矩陣

            gl.glLoadIdentity();

            gl.glPushMatrix();//保護變換矩陣現場

            float lx=0; //設定光源的位置

            float ly=(float)(7*Math.cos(Math.toRadians(lightAngle)));

            float lz=(float)(7*Math.sin(Math.toRadians(lightAngle)));

            float[] positionParamsRed={lx,ly,lz,0};

            gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_POSITION, positionParamsRed,0);

            initMaterial(gl);//初始化紋理

            //gl.glTranslatef(0, 0, -5f);//平移

            gl.glRotatef(-90, 0, 0, 1);//旋轉

            gl.glRotatef(-45, 0, 1, 0);//旋轉

            initLight(gl);//開燈



            for(int i=0;i<cylinderList.size();i++){

                autoGenerateBlock(gl,cylinderList.get(i),blockList.get(i));
               // Log.d(i+"=", Float.toString(cylinderList.get(i).deepX));
            }

            gl.glPushMatrix();
            gl.glTranslatef(-BLOCK_LENGTH, 0, 0);

            set_square();
            gl.glTranslatef(0, -0.5f, 1.1f);
            time_square.draw(gl);
            gl.glTranslatef(0,   -1f, 0);
            time_square_ten.draw(gl);
            gl.glTranslatef(0 , 2f, 0);
            time_square_point.draw(gl);
            gl.glPopMatrix();
            if(isTouch){

                ByteBuffer PixelBuffer = ByteBuffer.allocateDirect(4);
                PixelBuffer.order(ByteOrder.nativeOrder());
                PixelBuffer.position(0);
                int mTemp = 0;
               // System.out.println(touchY);
                gl.glReadPixels(Math.round(touchX), windowSizeY-Math.round(touchY), 1, 1, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, PixelBuffer);
                // Log.e("Picking", " xy: x" + x + " y"+ y);
                byte b [] = new byte[4];
                PixelBuffer.get(b);
               /* Log.e("Picking", " rgba: r" + PixelBuffer.get(0) + " g" + PixelBuffer.get(1) + " b" +
                        PixelBuffer.get(2) + " a" + PixelBuffer.get(3));*/
                if(PixelBuffer.get(0)!=0 && PixelBuffer.get(0)!=0 && PixelBuffer.get(0)!=0 ){
                    score++;
                    MOVING_CLOCK = BLOCK_LENGTH;
                    cylinderList.add(new DrawCylinder(BLOCK_LENGTH,CYLINDER_RADIUS,SECTION_ANGLE,2));
                    cylinderList.get(cylinderList.size()-1).deepX -= 3*BLOCK_LENGTH;
                    cylinderList.get(cylinderList.size()-1).mAngleX = cylinderList.get(cylinderList.size()-2).mAngleX;

                    blockList.add(new DrawWhiteBlock(BLOCK_LENGTH,CYLINDER_RADIUS,SECTION_ANGLE));
                    blockList.get(blockList.size()-1).deepX -= 3*BLOCK_LENGTH;
                    blockList.get(blockList.size()-1).mAngleX = blockList.get(blockList.size()-2).mAngleX;
                }
                else{
                    if(timer!=null && !timer.isInterrupted()){

                        timer.stopThread();
                        timer.interrupt();
                        System.out.println(timeCount);
                    }
                   handler.sendMessage(Message.obtain(handler, 0));

                }
                isTouch=false;
            }
            if(MOVING_CLOCK!=0){
                for(int i=0;i<cylinderList.size();i++){
                    cylinderList.get(i).deepX += MOVING_RATE;
                    blockList.get(i).deepX+=MOVING_RATE;
                }
                MOVING_CLOCK -= MOVING_RATE;
            }
            //closeLight(gl);//關燈
            gl.glPopMatrix();//恢復變換矩陣現場

        }
        public void onSurfaceChanged(GL10 gl, int width, int height) {

        //設置視窗大小及位置

            gl.glViewport(0, 0, width, height);

        //設置當前矩陣為投影矩陣

            gl.glMatrixMode(GL10.GL_PROJECTION);

        //設置當前矩陣為單位矩陣

            gl.glLoadIdentity();

        //計算透視投影的比例

            float ratio = (float) width / height;

        //調用此方法計算產生透視投影矩陣

            gl.glFrustumf(-ratio, ratio, -1, 1, 1, 100);

        }

   public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        //關閉抗抖動

            gl.glDisable(GL10.GL_DITHER);

        //設置特定Hint項目的模式，這裡為設置為使用快速模式

            gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,GL10.GL_FASTEST);

        //設置屏幕背景色黑色RGBA

            gl.glClearColor(0,0,0,0);

        //設置著色模型為平滑著色

            gl.glShadeModel(GL10.GL_SMOOTH);

        //啟用深度測試

            gl.glEnable(GL10.GL_DEPTH_TEST);

            //textureId=initTexture(gl,R.drawable.as);//紋理ID



        // //開啟一個線程自動旋轉光源

             new Thread(){
                 public void run(){
                    while(true){
                          lightAngle+=5;//轉動燈
                          //mRenderer.cylinder.mAngleY+=2*TOUCH_SCALE_FACTOR;//球沿Y軸轉動
                          requestRender();//重繪畫面
                          try  {
                          Thread.sleep(50);//休息10ms再重繪
                          }
                          catch(Exception e)  {
                          e.printStackTrace();
                          }
                      }
                  }
              }.start();

   }
        private void set_square(){

           /* Log.d("timeCount", Float.toString(timeCount));
            Log.d("timeCount_ten", Integer.toString((int)timeCount/10));
            Log.d("timeCount", Integer.toString((int)timeCount%10));
            Log.d("timeCount_ten", Integer.toString((int)(timeCount*10)%10));
*/
            //ten
            switch((int)(COUNT_DOWN-timeCount)/10){
                case 0:  time_square_ten.setBitmap(map0);break;
                case 1:  time_square_ten.setBitmap(map1);break;
                case 2:  time_square_ten.setBitmap(map2);break;
                case 3:  time_square_ten.setBitmap(map3);break;
                case 4:  time_square_ten.setBitmap(map4);break;
                case 5:  time_square_ten.setBitmap(map5);break;
                case 6:  time_square_ten.setBitmap(map6);break;
                case 7:  time_square_ten.setBitmap(map7);break;
                case 8:  time_square_ten.setBitmap(map8);break;
                case 9:  time_square_ten.setBitmap(map9);break;
            }
            //
            switch((int)(COUNT_DOWN-timeCount)%10){
                case 0:  time_square.setBitmap(map0);break;
                case 1:  time_square.setBitmap(map1);break;
                case 2:  time_square.setBitmap(map2);break;
                case 3:  time_square.setBitmap(map3);break;
                case 4:  time_square.setBitmap(map4);break;
                case 5:  time_square.setBitmap(map5);break;
                case 6:  time_square.setBitmap(map6);break;
                case 7:  time_square.setBitmap(map7);break;
                case 8:  time_square.setBitmap(map8);break;
                case 9:  time_square.setBitmap(map9);break;
            }
            //point
            switch((int)((COUNT_DOWN-timeCount)*10)%10){
                case 0:  time_square_point.setBitmap(map0p);break;
                case 1:  time_square_point.setBitmap(map1p);break;
                case 2:  time_square_point.setBitmap(map2p);break;
                case 3:  time_square_point.setBitmap(map3p);break;
                case 4:  time_square_point.setBitmap(map4p);break;
                case 5:  time_square_point.setBitmap(map5p);break;
                case 6:  time_square_point.setBitmap(map6p);break;
                case 7:  time_square_point.setBitmap(map7p);break;
                case 8:  time_square_point.setBitmap(map8p);break;
                case 9:  time_square_point.setBitmap(map9p);break;
            }

        }



    }

    public void freeBitmap(){
        map0.recycle();  map1.recycle();  map2.recycle();  map3.recycle();  map4.recycle();
        map5.recycle();  map6.recycle();  map7.recycle();  map8.recycle();  map9.recycle();
        map0p.recycle(); map1p.recycle(); map2p.recycle(); map3p.recycle(); map4p.recycle();
        map5p.recycle(); map6p.recycle(); map7p.recycle(); map8p.recycle(); map9p.recycle();
        System.gc();
    }

    public void autoGenerateBlock(GL10 gl,DrawCylinder cylinder,DrawWhiteBlock block){
        gl.glPushMatrix();
        cylinder.drawSelf(gl);
        gl.glPopMatrix();
        gl.glPushMatrix();
        block.drawSelf(gl);
        gl.glPopMatrix();

    }

 //初始化白色燈

    private void initLight(GL10 gl)

    {

        gl.glEnable(GL10.GL_LIGHTING);//允許光照

        gl.glEnable(GL10.GL_LIGHT1);//打開1號燈

    //環境光設置

        float[] ambientParams={0.27f,0.963f,0.953f,1.0f};//光參數 RGBA

        gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_AMBIENT, ambientParams,0);

    //散射光設置

        float[] diffuseParams={1f,1f,1f,1.0f};//光參數 RGBA

        gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_DIFFUSE, diffuseParams,0);

    //反射光設置

        float[] specularParams={1f,1f,1f,1.0f};//光參數 RGBA

        gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_SPECULAR, specularParams,0);

    }

    //關閉燈

    private void closeLight(GL10 gl)

    {

        gl.glDisable(GL10.GL_LIGHT1);

        gl.glDisable(GL10.GL_LIGHTING);

    }

    //初始化材質

    private void initMaterial(GL10 gl)

    {

    //環境光

        float ambientMaterial[] = {1.0f, 1.0f, 1.0f, 1.0f};

        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, ambientMaterial,0);

    //散射光

        float diffuseMaterial[] = {1f, 1f, 1f, 1.0f};

        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, diffuseMaterial,0);

    //高光材質

        float specularMaterial[] = {248f/255f, 242f/255f, 144f/255f, 1.0f};

        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, specularMaterial,0);

        gl.glMaterialf(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, 100.0f);

    }

    //初始化紋理

    public int initTexture(GL10 gl,int drawableId)//textureId

    {
    //生成紋理ID
        int[] textures = new int[1];

        gl.glGenTextures(1, textures, 0);

        int currTextureId=textures[0];

        gl.glBindTexture(GL10.GL_TEXTURE_2D, currTextureId);

        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR_MIPMAP_NEAREST);

        gl.glTexParameterf(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MAG_FILTER,GL10.GL_LINEAR_MIPMAP_LINEAR);

        ((GL11)gl).glTexParameterf(GL10.GL_TEXTURE_2D, GL11.GL_GENERATE_MIPMAP, GL10.GL_TRUE);

        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,GL10.GL_REPEAT);

        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,GL10.GL_REPEAT);

        InputStream is = this.getResources().openRawResource(drawableId);

        Bitmap bitmapTmp;

        try{
            bitmapTmp = BitmapFactory.decodeStream(is);
        }
        finally{
            try{
                is.close();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }

        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmapTmp, 0);

        bitmapTmp.recycle();

        return currTextureId;

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



}