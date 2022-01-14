package com.tantan.myopengl;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OneGlRenderer implements GLSurfaceView.Renderer {
  // 系统调用这个方法一次创建时GLSurfaceView。使用此方法来执行只需要发生一次的操作，
  // 比如设置OpenGL的环境参数或初始化的OpenGL图形对象。
  private Triangle mTriangle;
  private Square mSquare;
  private final float[] mMVPMatrix = new float[16];
  private final float[] mProjectionMatrix = new float[16];
  private final float[] mViewMatrix = new float[16];
  private float[] mRotationMatrix = new float[16];

  @Override
  public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
    // 设置背景颜色
    GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    // 初始化triangle
    mTriangle = new Triangle();
    // 初始化 square
    mSquare = new Square();
  }

  // 系统调用此方法时的GLSurfaceView几何形状的变化，包括尺寸变化GLSurfaceView或设备屏幕的取向。
  // 例如，当设备从纵向变为横向的系统调用这个方法。使用此方法可以在变化做出反应GLSurfaceView容器。
  @Override
  public void onSurfaceChanged(GL10 gl10, int width, int height) {
    GLES20.glViewport(0, 0, width, height);
    float ratio = (float) width / height;
    // 这个投影矩阵被应用于对象坐标在onDrawFrame（）方法中
    Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
  }

  // 系统调用上的每个重绘此方法GLSurfaceView。使用此方法作为主要执行点用于绘制（和重新绘制）的图形对象。
  @Override
  public void onDrawFrame(GL10 gl10) {
    float[] scratch = new float[16];
    // Redraw background color
    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    // Set the camera position (View matrix)
    Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

    // Calculate the projection and view transformation
    Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

    // 创建一个旋转矩阵
    long time = SystemClock.uptimeMillis() % 4000L;
    float angle = 0.090f * ((int) time);
    Matrix.setRotateM(mRotationMatrix, 0, angle, 0, 0, -1.0f);
    // 将旋转矩阵与投影和相机视图组合在一起
    // Note that the mMVPMatrix factor *must be first* in order
    // for the matrix multiplication product to be correct.
    Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);

    // Draw shape
    mTriangle.draw(scratch);
  }

  public static int loadShader(int type, String shaderCode) {

    // 创造顶点着色器类型(GLES20.GL_VERTEX_SHADER)
    // 或者是片段着色器类型 (GLES20.GL_FRAGMENT_SHADER)
    int shader = GLES20.glCreateShader(type);
    // 添加上面编写的着色器代码并编译它
    GLES20.glShaderSource(shader, shaderCode);
    GLES20.glCompileShader(shader);
    return shader;
  }
}
