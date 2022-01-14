package com.tantan.myopengl;

import android.opengl.GLES20;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Triangle {
  // 顶点着色器
  private final String vertexShaderCode =
      "uniform mat4 uMVPMatrix;"
          + "attribute vec4 vPosition;"
          + "varying vec4 vColor;"
          + "attribute vec4 aColor;"
          + "void main() {"
          + "  gl_Position = uMVPMatrix * vPosition;"
          + " vColor = aColor;"
          + "}";

  // 片段着色器
  private final String fragmentShaderCode =
      "precision mediump float;"
          + "varying vec4 vColor;"
          + "void main() {"
          + "  gl_FragColor = vColor;"
          + "}";
  private int mMVPMatrixHandle;

  private FloatBuffer vertexBuffer;
  private FloatBuffer colorBuffer;

  // number of coordinates per vertex in this array
  static final int COORDS_PER_VERTEX = 3;
  static float triangleCoords[] = { // in counterclockwise order:
    0.0f,
    0.5f,
    0.0f, // top
    -0.5f,
    -0.5f,
    0.0f, // bottom left
    0.5f,
    -0.5f,
    0.0f // bottom right
  };

  // Set color with red, green, blue and alpha (opacity) values
  //  float color[] = {255, 0, 0, 1.0f};

  private final int mProgram;

  private int mPositionHandle;
  private int mColorHandle;

  private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
  private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
  float color[] = {
    1.0f, 0f, 0f, 1.0f,
    0f, 1.0f, 0f, 1.0f,
    0f, 0f, 1.0f, 1.0f
  };

  public Triangle() {
    // 初始化ByteBuffer，长度为arr数组的长度*4，因为一个float占4个字节
    ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length * 4);
    // 数组排列用nativeOrder
    bb.order(ByteOrder.nativeOrder());
    // 从ByteBuffer创建一个浮点缓冲区
    vertexBuffer = bb.asFloatBuffer();
    // 将坐标添加到FloatBuffer
    vertexBuffer.put(triangleCoords);
    // 设置缓冲区来读取第一个坐标
    vertexBuffer.position(0);
    int vertexShader = OneGlRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
    int fragmentShader = OneGlRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
    // 创建空的OpenGL ES程序
    mProgram = GLES20.glCreateProgram();

    // 添加顶点着色器到程序中
    GLES20.glAttachShader(mProgram, vertexShader);

    // 添加片段着色器到程序中
    GLES20.glAttachShader(mProgram, fragmentShader);

    // 创建OpenGL ES程序可执行文件
    GLES20.glLinkProgram(mProgram);

    ByteBuffer dd = ByteBuffer.allocateDirect(color.length * 4);
    dd.order(ByteOrder.nativeOrder());
    colorBuffer = dd.asFloatBuffer();
    colorBuffer.put(color);
    colorBuffer.position(0);
  }

  public void draw(float[] mvpMatrix) {
    // 将程序添加到OpenGL ES环境
    GLES20.glUseProgram(mProgram);

    // 获取顶点着色器的位置的句柄
    mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

    // 启用三角形顶点位置的句柄
    GLES20.glEnableVertexAttribArray(mPositionHandle);

    // 准备三角形坐标数据
    GLES20.glVertexAttribPointer(
        mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);

    /* // 获取片段着色器的vColor成员的句柄
    mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

    // 设置绘制三角形的颜色
    GLES20.glUniform4fv(mColorHandle, 1, color, 0);*/
    // 获取片段着色器的aColor成员的句柄
    mColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor");
    // 设置绘制三角形的颜色
    GLES20.glEnableVertexAttribArray(mColorHandle);
    GLES20.glVertexAttribPointer(mColorHandle, 4, GLES20.GL_FLOAT, false, 0, colorBuffer);
    // 得到形状的变换矩阵的句柄
    mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

    // 将投影和视图转换传递给着色器
    GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

    // 绘制三角形
    GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

    // 禁用顶点数组
    GLES20.glDisableVertexAttribArray(mPositionHandle);
  }
}
