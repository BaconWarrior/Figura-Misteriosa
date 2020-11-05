package com.example.myapplication2;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class MyGLSurfaceView extends  GLSurfaceView{

    private final MyGLRenderer renderer;

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float previousX;
    private float previousY;


    public MyGLSurfaceView(Context context) {
        super(context);

        //Creacion de la visualizacion de OpenGL
        setEGLContextClientVersion(2);

        renderer = new MyGLRenderer();

        //Se asigna en renderizado para que se dibujen las figuras en GLSurfaceVIew
        setRenderer(renderer);

        // Renderice la vista solo cuando haya un cambio en los datos del dibujo
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        //MotionEvent informa los detalles de entrada desde la pantalla táctil
        //y otros controles de entrada. En este caso, solo le
        //interesan los eventos donde la posición del toque cambió.

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:

                float dx = x - previousX;
                float dy = y - previousY;

                // sentido inverso de rotación por encima de la línea media
                if (y > getHeight() / 2) {
                    dx = dx * -1 ;
                }

                // dirección de rotación inversa a la izquierda de la línea media
                if (x < getWidth() / 2) {
                    dy = dy * -1 ;
                }

                renderer.setAngle(
                        renderer.getAngle() +
                                ((dx + dy) * TOUCH_SCALE_FACTOR));
                requestRender();
        }

        previousX = x;
        previousY = y;
        return true;
    }
}
