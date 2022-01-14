package com.tantan.myopengl;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

// 1. 创建一个GlSurfaceView
// 2. 为这个GlSurfaceView设置渲染
// 3. 在GlSurfaceView.renderer中绘制处理显示数据

// 在GlSurfaceView.Renderer中的绘制步骤：
//    1设置视图展示窗口(viewport) :在onSurfaceChanged中调用GLES20.glViewport(0, 0, width, height);
//    2创建图形类，确定好顶点位置和图形颜色，将顶点和颜色数据转换为OpenGl使用的数据格式
//    3加载顶点找色器和片段着色器用来修改图形的颜色，纹理，坐标等属性
//    4创建投影和相机视图来显示视图的显示状态，并将投影和相机视图的转换传递给着色器。
//    5创建项目(Program),连接顶点着色器片段着色器。
//    6将坐标数据传入到OpenGl ES程序中：
public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    OneGlSurfaceView glSurfaceView = new OneGlSurfaceView(this);
    setContentView(glSurfaceView);
  }
}
