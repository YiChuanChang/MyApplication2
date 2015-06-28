package netdb.course.softwarestudion.myapplication;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Waiting on 2015/6/28.
 */

public class Cube {

    private FloatBuffer mVertexBuffer;
    private IntBuffer mColorBuffer;
    private ByteBuffer mIndexBuffer;
    public Cube()
    {
        int one = 0x10000;
        float vertices[] = {
                // Front face
                -1,-1,1, 1,-1,1,  1,1,1,-1,1,1,
                // Right face
                1,-1,1, 1,-1,-1,  1,1,-1,1,1,1,
                // Back face
                1,-1,-1, -1,-1,-1,  -1,1,-1,1,1,-1,
                // Left face
                -1,-1,-1, -1,-1,1,  -1,1,1,-1,1,-1,
                // Bottom face
                -1,-1,-1, 1,-1,-1, 1,-1,1, -1,-1,1,
                // Top face
                -1,1,1, 1,1,1,  1,1,-1,-1,1,-1
       };

        int colors[] = {
                one,    one,    one,  one,
                one,    one,    one,  one,
                one,    one,    one,  one,
                one,    one,    one,  one,
                one,    one,    one,  one,
                one,    one,    one,  one,
                one,    one,    one,  one,
                one,    one,    one,  one,
        };

        byte indices[] = {
                0, 4, 5,    0, 5, 1,
                1, 5, 6,    1, 6, 2,
                2, 6, 7,    2, 7, 3,
                3, 7, 4,    3, 4, 0,
                4, 7, 6,    4, 6, 5,
                3, 0, 1,    3, 1, 2
        };

        // Buffers to be passed to gl*Pointer() functions
        // must be direct, i.e., they must be placed on the
        // native heap where the garbage collector cannot
        // move them.
        //
        // Buffers with multi-byte datatypes (e.g., short, int, float)
        // must have their byte order set to native order

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);

        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length*4);
        cbb.order(ByteOrder.nativeOrder());
        mColorBuffer = cbb.asIntBuffer();
        mColorBuffer.put(colors);
        mColorBuffer.position(0);

        mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
        mIndexBuffer.put(indices);
        mIndexBuffer.position(0);
    }

    public void draw(GL10 gl)
    {
        gl.glFrontFace(gl.GL_CW);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);//打開頂點緩衝

        gl.glVertexPointer(3, gl.GL_FLOAT, 0, mVertexBuffer);
        gl.glColor4f(1f, 1f, 1f, 1f);//繪製線的顏色
        gl.glColorPointer(4, gl.GL_FIXED, 0, mColorBuffer);
        gl.glDrawArrays(GL10.GL_LINE_LOOP, 0, 4);//繪製圖像線
        gl.glDrawArrays(GL10.GL_LINE_LOOP, 4, 4);
        gl.glDrawArrays(GL10.GL_LINE_LOOP, 8, 4);
        gl.glDrawArrays(GL10.GL_LINE_LOOP, 12, 4);
        gl.glDrawArrays(GL10.GL_LINE_LOOP, 16, 4);
        gl.glDrawArrays(GL10.GL_LINE_LOOP, 20, 4);


        // gl.glDrawElements(gl.GL_TRIANGLES, 36, gl.GL_UNSIGNED_BYTE, mIndexBuffer);
    }

}
