package com.example.myapplication2;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;


public class MyGLRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = "MyGLRenderer";
    //locacion del triangulo
    private Triangulo mtriangulo;
    private Misteriosa mmisteriosa;

    // vPMatrix es la abreviacion de "Model View Projection Matrix"
    private final float[] vPMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];

    private float[] rotationMatrix = new float[16];
    //variable a la cual se le indica al sistema que su valor dentro del bloque de memoria puede cambiar
    public volatile float mAngle;

    public float getAngle() {
        return mAngle;
    }

    public void setAngle(float angle) {
        mAngle = angle;
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //Se asigna el color del fondo de la palicacion
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        //inicializando el triangulo
        mtriangulo = new Triangulo();
        mmisteriosa = new Misteriosa();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        float[] scratch = new float[16];

        //dibujar el color del fondo
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        Matrix.setIdentityM(rotationMatrix, 0);

        //Matrix.setRotateM(rotationMatrix, 0, mAngle, 0, 0, -1.0f);
        Matrix.scaleM(rotationMatrix, 0, (mAngle/300), (mAngle/300), 0);

        // Configuramos la posicion de la camara (View matrix)
        Matrix.setLookAtM(viewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculamos la trasnformacion de la proyeccion y la vista
        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

        // Combina la matriz de rotación con la vista de proyección y cámara
        // Tenga en cuenta que el factor vPMatrix * debe ser el primero * en orden
        // para que el producto de la multiplicación de matrices sea correcto.
        Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0);

        mmisteriosa.draw(scratch);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        // Esta matris de proyeccion es aplicada a las coordenadas del objeto
        // En el metodo onDrawFrame()
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
        //Este código propaga una matriz de proyección, mProjectionMatrix, que luego puedes combinar
        //Con una transformación de vista de cámara en el método onDrawFrame(),
    }

    //funcion para cargar el sombreado de una figura
    public static int loadShader(int type, String shaderCode){

        // creamos el tipo de sombreado de los vertices (GLES20.GL_VERTEX_SHADER)
        // o los tipos de sombreado de los fragmentos (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // Añadimos el codigo fuente a los sombreados y lo compilamos
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        //La funcion regresara el valor de los sombreados generados
        return shader;
    }
}
