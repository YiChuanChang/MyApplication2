package netdb.course.softwarestudion.myapplication;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Waiting on 2015/6/26.
 */
public class DrawWhiteBlock {

    float[] colors = {
            1f, 1f, 1f, 1f, // 左上角 0 red
            1f, 1f, 1f, 1f, // 左下角 1 green
            1f, 1f, 1f, 1f, // 右下角 2 blue
    };

    private FloatBuffer myVertexBuffer;//頂點坐標緩衝

    private FloatBuffer colorBuffer;

    int vCount;//頂點數量

    float length;//圓柱長度

    float circle_radius;//圓截環半徑

    float degreespan; //圓截環每一份的度數大小

    public float mAngleX;

    public float mAngleY;

    public float mAngleZ;

    public float deepX;
    public DrawWhiteBlock(float length,float circle_radius,float degreespan) {

        this.circle_radius = circle_radius;

        this.length = length;

        this.degreespan = degreespan;

        this.deepX = 0;

        float collength=(float)length;//圓柱每塊所佔的長度

        int spannum=(int)(360.0f/degreespan);

        ArrayList<Float> val=new ArrayList<Float>();//頂點存放列表

        Random random = new Random();

        int index=random.nextInt(spannum);

        float circle_degree=360.0f-index*60;

        float x1 = (-length);

        float y1 = (float) (circle_radius*Math.sin(Math.toRadians(circle_degree)));

        float z1 = (float) (circle_radius*Math.cos(Math.toRadians(circle_degree)));

        float x2 =(-length);

        float y2=(float) (circle_radius*Math.sin(Math.toRadians(circle_degree-degreespan)));

        float z2=(float) (circle_radius*Math.cos(Math.toRadians(circle_degree-degreespan)));

        float x3 =(float)(0);

        float y3=(float) (circle_radius*Math.sin(Math.toRadians(circle_degree-degreespan)));

        float z3=(float) (circle_radius*Math.cos(Math.toRadians(circle_degree-degreespan)));

        float x4 =(float)(0);

        float y4=(float) (circle_radius*Math.sin(Math.toRadians(circle_degree)));

        float z4=(float) (circle_radius*Math.cos(Math.toRadians(circle_degree)));

        val.add(x4);val.add(y4);val.add(z4);//兩個三角形，共6個頂點的坐標

        val.add(x1);val.add(y1);val.add(z1);

        val.add(x3);val.add(y3);val.add(z3);

        val.add(x3);val.add(y3);val.add(z3);

        val.add(x1);val.add(y1);val.add(z1);

        val.add(x2);val.add(y2);val.add(z2);

   /*     Log.d("B1=", Float.toString(y1) + " " + Float.toString(z1));
        Log.d("B2=", Float.toString(y2)+" "+Float.toString(z2));
        Log.d("B3=", Float.toString(y3)+" "+Float.toString(z3));
        Log.d("B4=", Float.toString(y4)+" "+Float.toString(z4));*/

        vCount=val.size()/3;//確定頂點數量
//頂點

        float[] vertexs=new float[vCount*3];

        for(int i=0;i<vCount*3;i++){
            vertexs[i]=val.get(i);
        }

        ByteBuffer vbb=ByteBuffer.allocateDirect(vertexs.length*4);

        vbb.order(ByteOrder.nativeOrder());

        myVertexBuffer=vbb.asFloatBuffer();

        myVertexBuffer.put(vertexs);

        myVertexBuffer.position(0);
//紋理
        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);

        cbb.order(ByteOrder.nativeOrder());

        colorBuffer = cbb.asFloatBuffer();

        colorBuffer.put(colors);

        colorBuffer.position(0);
    }

    public void drawSelf(GL10 gl)
    {
        gl.glRotatef(mAngleX, 1, 0, 0);//旋轉

        gl.glRotatef(mAngleY, 0, 1, 0);

        gl.glRotatef(mAngleZ, 0, 0, 1);

        gl.glTranslatef(deepX, 0, 0);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);//打開頂點緩衝

        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, myVertexBuffer);//指定頂點緩衝

        gl.glEnable(GL10.GL_TEXTURE_2D);

        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, colorBuffer);

        //gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);

        gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vCount);//繪製圖像面

        gl.glColor4f(1f, 0f, 0f, 1f);//繪製線的顏色

        gl.glDrawArrays(GL10.GL_LINES, 0, vCount);//繪製圖像線

        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);//關閉緩衝

        gl.glEnable(GL10.GL_TEXTURE_2D);

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

        gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);

    }



}
