package netdb.course.softwarestudion.myapplication;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Waiting on 2015/6/25.
 */

public class DrawCylinder

{

    private FloatBuffer myVertexBuffer;//頂點坐標緩衝

    private FloatBuffer myNormalBuffer;//法向量緩衝

    private FloatBuffer myTexture;//紋理緩衝

    int textureId;

    int vCount;//頂點數量

    float length;//圓柱長度

    float circle_radius;//圓截環半徑

    float degreespan; //圓截環每一份的度數大小

    public float mAngleX;

    public float mAngleY;

    public float mAngleZ;

    public float deepX;

    public DrawCylinder(float length,float circle_radius,float degreespan,int textureId)

    {

        this.circle_radius=circle_radius;

        this.length=length;

        this.degreespan=degreespan;

        this.textureId=textureId;

        float collength=(float)length;//圓柱每塊所佔的長度

        int spannum=(int)(360.0f/degreespan);

        ArrayList<Float> val=new ArrayList<Float>();//頂點存放列表

        ArrayList<Float> ial=new ArrayList<Float>();//法向量存放列表

        for(float circle_degree=360.0f;circle_degree>0.0f;circle_degree-=degreespan)//循環行

        {

            float x1 = (float)(-length/2);

            float y1 = (float) (circle_radius*Math.sin(Math.toRadians(circle_degree)));

            float z1 = (float) (circle_radius*Math.cos(Math.toRadians(circle_degree)));

         /*   float a1=0;

            float b1=y1;

            float c1=z1;

            float l1=getVectorLength(a1, b1, c1);//模長

           /a1=a1/l1;//法向量規格化

            b1=b1/l1;

            c1=c1/l1;*/

            float x2 =(float)(-length/2);

            float y2=(float) (circle_radius*Math.sin(Math.toRadians(circle_degree-degreespan)));

            float z2=(float) (circle_radius*Math.cos(Math.toRadians(circle_degree-degreespan)));

         /*   float a2=0;

            float b2=y2;

            float c2=z2;

            float l2=getVectorLength(a2, b2, c2);//模長

            a2=a2/l2;//法向量規格化

            b2=b2/l2;

            c2=c2/l2;*/

            float x3 =(float)(length/2);

            float y3=(float) (circle_radius*Math.sin(Math.toRadians(circle_degree-degreespan)));

            float z3=(float) (circle_radius*Math.cos(Math.toRadians(circle_degree-degreespan)));

      /*      float a3=0;

            float b3=y3;

            float c3=z3;

            float l3=getVectorLength(a3, b3, c3);//模長

           a3=a3/l3;//法向量規格化

           b3=b3/l3;

            c3=c3/l3;*/

            float x4 =(float)(length/2);

            float y4=(float) (circle_radius*Math.sin(Math.toRadians(circle_degree)));

            float z4=(float) (circle_radius*Math.cos(Math.toRadians(circle_degree)));

     /*      float a4=0;

            float b4=y4;

            float c4=z4;

            float l4=getVectorLength(a4, b4, c4);//模長

            a4=a4/l4;//法向量規格化

            b4=b4/l4;

           c4=c4/l4;*/

            val.add(x1);val.add(y1);val.add(z1);//兩個三角形，共6個頂點的坐標

            val.add(x2);val.add(y2);val.add(z2);

            val.add(x4);val.add(y4);val.add(z4);

            val.add(x2);val.add(y2);val.add(z2);

            val.add(x3);val.add(y3);val.add(z3);

            val.add(x4);val.add(y4);val.add(z4);

          /*  ial.add(a1);ial.add(b1);ial.add(c1);//頂點對應的法向量

            ial.add(a2);ial.add(b2);ial.add(c2);

            ial.add(a4);ial.add(b4);ial.add(c4);

            ial.add(a2);ial.add(b2);ial.add(c2);

            ial.add(a3);ial.add(b3);ial.add(c3);

            ial.add(a4);ial.add(b4);ial.add(c4);*/

        }

        vCount=val.size()/3;//確定頂點數量

//頂點

        float[] vertexs=new float[vCount*3];

        for(int i=0;i<vCount*3;i++)

        {

            vertexs[i]=val.get(i);

        }

        ByteBuffer vbb=ByteBuffer.allocateDirect(vertexs.length*4);

        vbb.order(ByteOrder.nativeOrder());

        myVertexBuffer=vbb.asFloatBuffer();

        myVertexBuffer.put(vertexs);

        myVertexBuffer.position(0);

//法向量

     /*   float[] normals=new float[vCount*3];

        for(int i=0;i<vCount*3;i++)

        {

            normals[i]=ial.get(i);

        }

        ByteBuffer ibb=ByteBuffer.allocateDirect(normals.length*4);

        ibb.order(ByteOrder.nativeOrder());

        myNormalBuffer=ibb.asFloatBuffer();

        myNormalBuffer.put(normals);

        myNormalBuffer.position(0);*/

//紋理

        float[] textures=generateTexCoor(spannum);

        ByteBuffer tbb=ByteBuffer.allocateDirect(textures.length*4);

        tbb.order(ByteOrder.nativeOrder());

        myTexture=tbb.asFloatBuffer();

        myTexture.put(textures);

        myTexture.position(0);

    }


    public void drawSelf(GL10 gl)
    {
        gl.glRotatef(mAngleX, 1, 0, 0);//旋轉

        gl.glRotatef(mAngleY, 0, 1, 0);

        gl.glRotatef(mAngleZ, 0, 0, 1);

        gl.glTranslatef(deepX, 0, 0);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);//打開頂點緩衝

        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, myVertexBuffer);//指定頂點緩衝

     //   gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);//打開法向量緩衝

      //  gl.glNormalPointer(GL10.GL_FLOAT, 0, myNormalBuffer);//指定法向量緩衝

        gl.glEnable(GL10.GL_TEXTURE_2D);

        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, myTexture);

        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);

        gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vCount);//繪製圖像

        gl.glColor4f(1f, 1f, 1f, 1f);//繪製線的顏色

        gl.glDrawArrays(GL10.GL_LINES, 0, vCount);//繪製圖像

        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);//關閉緩衝

        gl.glEnable(GL10.GL_TEXTURE_2D);

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

        gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);

    }

//法向量規格化，求模長度
/*
    public float getVectorLength(float x,float y,float z)
    {

        float pingfang=x*x+y*y+z*z;

        float length=(float) Math.sqrt(pingfang);

        return length;
    }
*/
//自動切分紋理產生紋理數組的方法

    public float[] generateTexCoor(int bh){

        float[] result=new float[bh*6*2];

        float REPEAT=2;

        float sizeh=1.0f/bh;//行數

        int c=0;

        for(int i=0;i<bh;i++){
//每行列一個矩形，由兩個三角形構成，共六個點，12個紋理坐標
            float t=i*sizeh;

            result[c++]=0;

            result[c++]=t;

            result[c++]=0;

            result[c++]=t+sizeh;

            result[c++]=REPEAT;

            result[c++]=t;

            result[c++]=0;

            result[c++]=t+sizeh;

            result[c++]=REPEAT;

            result[c++]=t+sizeh;

            result[c++]=REPEAT;

            result[c++]=t;

        }
        for(int i=0;i<bh*6*2;i+=2) {
            System.out.println(result[i]+" "+result[i+1]);
        }
        return result;

    }

}
