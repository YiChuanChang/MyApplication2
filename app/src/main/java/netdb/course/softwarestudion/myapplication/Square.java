package netdb.course.softwarestudion.myapplication;

/**
 * Created by Waiting on 2015/6/25.
 */

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.opengl.GLUtils;


public class Square {
    // 點的陣列
    private float vertices[] = {0.0f, 0.0f, -1.0f,  // 0, 左上角
            0.0f, 0.0f, -3.0f,  // 1, 左下角
            0.0f, 1.0f, -3.0f,  // 2, 右下角
            0.0f, 1.0f, -1.0f, // 3, 右上角
    };

    // 將顏色資訊對應到點陣列上
    float[] colors = { 1f, 1f, 1f, 0f, // 左上角 0 red
            1f, 1f, 1f, 0f, // 左下角 1 green
            1f, 1f, 1f, 0f, // 右下角 2 blue
            1f, 1f, 1f, 0f, // 右上角 3 magenta
    };

    // 質地坐標
    float texture[] = { 0.0f, 0.0f, //
            0.0f, 1.0f, //
            1.0f, 1.0f, //
            1.0f, 0.0f, //
    };
    // 連接點的次序
    private short[] indices = { 0, 1, 2, 0, 2, 3 };

    // 點的緩衝區
    private FloatBuffer vertexBuffer;

    // 索引值緩衝區
    private ShortBuffer indexBuffer;

    // 顏色緩衝區
    private FloatBuffer colorBuffer;

    // 質地緩衝區
    private FloatBuffer textureBuffer;

    Bitmap bitmap;

    public Square() {
        // 浮點數是4位元組因此需要把點陣列長度乘以4
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        // 短整數是2位元組因此需要把點陣列長度乘以2
        ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
        ibb.order(ByteOrder.nativeOrder());
        indexBuffer = ibb.asShortBuffer();
        indexBuffer.put(indices);
        indexBuffer.position(0);

        // 浮點數是4位元組，顏色(RGBA)
        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        colorBuffer = cbb.asFloatBuffer();
        colorBuffer.put(colors);
        colorBuffer.position(0);

        ByteBuffer byteBuf = ByteBuffer.allocateDirect(texture.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        textureBuffer = byteBuf.asFloatBuffer();
        textureBuffer.put(texture);
        textureBuffer.position(0);
    }

    /**
     * 畫圖函式
     *
     * @param gl
     */
    public void draw(GL10 gl) {
        // 逆時鐘
        gl.glFrontFace(GL10.GL_CCW);
        // 啟動CULL_FACE
        gl.glEnable(GL10.GL_CULL_FACE);
        // 刪除多邊形的背景
        gl.glCullFace(GL10.GL_BACK);

        // 啟動點的緩衝區
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        // 指定位置和資料格式
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

        // 在渲染期間啟用顏色緩衝區
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

        // 指定顏色緩衝區。
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);

        // 建立質地陣列
        int[] textures = new int[1];

        // 告訴OpenGL產生第幾個質地
        gl.glGenTextures(1, textures, 0);

        // 指定要用那一質地
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);

        // 載入質地位元圖
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

        gl.glTexParameterf(GL10.GL_TEXTURE_2D,
                GL10.GL_TEXTURE_MIN_FILTER,
                GL10.GL_NEAREST);

        gl.glTexParameterf(GL10.GL_TEXTURE_2D,
                GL10.GL_TEXTURE_MAG_FILTER,
                GL10.GL_NEAREST);

        // 啟動質地功能
        gl.glEnable(GL10.GL_TEXTURE_2D);

        // 使用UV坐標
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

        // 指定質地緩衝區
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);  // 以三點劃出三角形
        gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
                GL10.GL_UNSIGNED_SHORT, indexBuffer);

        // 除能點的緩衝區
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        // 除能CULL_FACE
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);

        gl.glDisable(GL10.GL_CULL_FACE);

        // 除能UV坐標
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

        // 除能質地的使用
        gl.glDisable(GL10.GL_TEXTURE_2D);
    }

    public void setBitmap(Bitmap bitmap) {
        // TODO Auto-generated method stub
        this.bitmap = bitmap;
    }}
