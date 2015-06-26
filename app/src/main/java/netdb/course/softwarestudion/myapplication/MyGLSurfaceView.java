package netdb.course.softwarestudion.myapplication;

/**
 * Created by Waiting on 2015/6/25.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.SensorEvent;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.view.MotionEvent;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

public class MyGLSurfaceView extends GLSurfaceView {

    private final float TOUCH_SCALE_FACTOR = 180.0f/320;//角度縮放比例



    private final float BLOCK_LENGTH = 20f;

    private SceneRenderer mRenderer;//場景渲染器

    private SceneRenderer mRenderer2;

    private float mPreviousY;//上次的觸控位置Y坐標

    private float mPreviousX;//上次的觸控位置Y坐標

    private int lightAngle=90;//燈的當前角度

    public MyGLSurfaceView(Context context) {

        super(context);

        mRenderer = new SceneRenderer(); //創建場景渲染器

        setRenderer(mRenderer); //設置渲染器

        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//設置渲染模式為主動渲染

    }

//觸摸事件回調方法

    @Override

    public boolean onTouchEvent(MotionEvent e) {

        float y = e.getY();

        float x = e.getX();

        switch (e.getAction()) {

            /*case MotionEvent.ACTION_MOVE:

                float dy = y - mPreviousY;//計算觸控筆Y位移

                float dx = x - mPreviousX;//計算觸控筆Y位移

                mRenderer.cylinder.mAngleX += dy * TOUCH_SCALE_FACTOR;//設置沿x軸旋轉角度

                mRenderer.cylinder.mAngleZ += dx * TOUCH_SCALE_FACTOR;//設置沿z軸旋轉角度
            */
            case MotionEvent.ACTION_UP:

                for(DrawCylinder cylinder:mRenderer.cylinderList){
                   cylinder.deepX += BLOCK_LENGTH/2;
                }
                mRenderer.isTouch=true;
                requestRender();//重繪畫面

        }

        requestRender();

        mPreviousY = y;//記錄觸控筆位置

        mPreviousX = x;//記錄觸控筆位置

        return true;

    }

    public boolean onSensorEvent(SensorEvent e){
        for(DrawCylinder cylinder:mRenderer.cylinderList){
            cylinder.mAngleX += e.values[1]*10;
        }
        requestRender();
        return true;
    }

    private class SceneRenderer implements GLSurfaceView.Renderer
    {
        int textureId;//紋理名稱ID
        private boolean isTouch;
        ArrayList<DrawCylinder> cylinderList;
        DrawCylinder cylinder;//創建圓柱體
        DrawCylinder cylinder2;
        public SceneRenderer()
        {
            isTouch=false;
            cylinderList=new ArrayList();
            cylinderList.add(new DrawCylinder(BLOCK_LENGTH,20f,60f,2));
            cylinderList.add(new DrawCylinder(BLOCK_LENGTH,20f,60f,2));
            cylinderList.add(new DrawCylinder(BLOCK_LENGTH,20f,60f,2));
           // cylinder =new DrawCylinder(BLOCK_LENGTH,20f,60f,2) ;//創建圓柱體 length, circle_radius, degreespan, textureId
        }
        public void onDrawFrame(GL10 gl) {
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

            //gl.glTranslatef(0, 0, -10f);//平移

            gl.glRotatef(-90, 0, 0, 1);//旋轉

            gl.glRotatef(-45, 0, 1, 0);//旋轉

            initLight(gl);//開燈
            if(isTouch){
                cylinderList.add(new DrawCylinder(BLOCK_LENGTH,20f,60f,2));
                isTouch=false;
            }

            for(int i=0;i<cylinderList.size();i++){
                autoGenerateBlock(gl,cylinderList.get(i));
            }
          //  cylinder.drawSelf(gl);//繪製


          //  gl.glTranslatef(-BLOCK_LENGTH-1, 0, 0);
           // cylinder2.drawSelf(gl);
           // cylinder.drawSelf(gl);






            closeLight(gl);//關燈

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

            textureId=initTexture(gl,R.drawable.as);//紋理ID



        // //開啟一個線程自動旋轉光源

             new Thread(){
                 public void run(){
                    while(true){
                          lightAngle+=5;//轉動燈
                          //mRenderer.cylinder.mAngleY+=2*TOUCH_SCALE_FACTOR;//球沿Y軸轉動
                          requestRender();//重繪畫面
                          try  {
                          Thread.sleep(500);//休息10ms再重繪
                          }
                          catch(Exception e)  {
                          e.printStackTrace();
                          }
                      }
                  }
              }.start();

        }

    }
    public void autoGenerateBlock(GL10 gl,DrawCylinder cylinder){
        cylinder.drawSelf(gl);
        gl.glTranslatef(-BLOCK_LENGTH+1, 0, 0);
    }

 //初始化白色燈

    private void initLight(GL10 gl)

    {

        gl.glEnable(GL10.GL_LIGHTING);//允許光照

        gl.glEnable(GL10.GL_LIGHT1);//打開1號燈

    //環境光設置

        float[] ambientParams={0.2f,0.2f,0.2f,1.0f};//光參數 RGBA

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

        float ambientMaterial[] = {248f/255f, 242f/255f, 144f/255f, 1.0f};

        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, ambientMaterial,0);

    //散射光

        float diffuseMaterial[] = {248f/255f, 242f/255f, 144f/255f, 1.0f};

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


}