package netdb.course.softwarestudion.myapplication;

import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
/**
 * Created by Waiting on 2015/6/25.
 */
public class OpenGLRenderer implements GLSurfaceView.Renderer
{
    private Square square;
    private float angle = 0;
    public OpenGLRenderer() {
        // 初始化
        square = new Square();
    }
    @Override
    public void onDrawFrame(GL10 gl) {
        // TODO Auto-generated method stub
        // 清除螢幕和深度緩衝區
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        // 以單位矩陣取代目前的矩陣
        gl.glLoadIdentity();
        // Z軸轉置 4 單位
        gl.glTranslatef(0, 0, -10);

        // 第一個方形
        // 存儲目前陣列
        gl.glPushMatrix();
        // 反時鐘旋轉
        gl.glRotatef(angle, 0, 0.5f, 0);
        // 畫出第一個方形
        square.draw(gl);
        // 復原成最後的矩陣
        gl.glPopMatrix();

        // 第二個方形
        // 存儲目前陣列
        gl.glPushMatrix();
        // 在移動前先旋轉, 讓第二個方形圍繞著第一個方形旋轉
        gl.glRotatef(-angle, 0, 0.5f, 0);
        // 移動第二個方形
        gl.glTranslatef(2, 0, 0);
        // 調整其大小為第一個方形的一半
        gl.glScalef(.5f, .5f, .5f);
        gl.glRotatef(angle*5, 0.5f, 1, 0);
        // 畫出第二個方形
        square.draw(gl);

        // 第三個方形
        // 存儲目前陣列
        gl.glPushMatrix();
        // 讓第三個方形圍繞著第二個方形旋轉
        gl.glRotatef(-angle, 0,0.5f, 0);
        // 移動第三個方形
        gl.glTranslatef(2, 0, 0);
        // 調整其大小為第二個方形的一半
        gl.glScalef(.5f, .5f, .5f);
        // 以自己為中心旋轉
        gl.glRotatef(angle * 10, 0, 0.5f ,0);
        // 畫出第三個方形.
        square.draw(gl);

        // 復原成第三個方形前的矩陣
        gl.glPopMatrix();
        // 復原成第二個方形前的矩陣.
        gl.glPopMatrix();

        // 增加角度
        angle++;
        // 畫出方形
       // square.draw(gl);


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
        gl.glClearColor(0.2f, 0.5f, 0.2f, 0.5f);
        // 設定流暢的陰影模式
        gl.glShadeModel(GL10.GL_SMOOTH);
        // 深度緩區的設定
        gl.glClearDepthf(1.0f);
        // 啟動深度的測試
        gl.glEnable(GL10.GL_DEPTH_TEST);
        // GL_LEQUAL深度函式測試
        gl.glDepthFunc(GL10.GL_LEQUAL);
        // 設定很好的角度計算模式
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
    }
    public void setBitmap(Bitmap bitmap) {
        // TODO Auto-generated method stub
        square.setBitmap(bitmap);
    }




}
