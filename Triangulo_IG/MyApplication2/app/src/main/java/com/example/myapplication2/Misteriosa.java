package com.example.myapplication2;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.GLES20;
public class Misteriosa {
    //sombreado de vertices de la forma
    private final String vertexShaderCode =
            "uniform mat4 uMVPMatrix;"+ //esta variable proporciona para manipular las coordenadas que ocupan
                    "attribute vec4 vPosition;" +//sombreado de vertices
                    "void main() {" +
                    // la matriz debe incluirse como modificador de gl_Position
                    // Tenga en cuenta que el factor uMVPMatrix * debe ser el primero * en orden
                    // para que el producto de la multiplicación de matrices sea correcto.
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    // se usa para acceder y configurar la transformación de vista
    private int vPMatrixHandle;

    private FloatBuffer vertexBuffer; //Posicion de vertices
    private ShortBuffer drawListBuffer;// Orden de dibujado


    static final int COORDS_PER_VERTEX = 3;
    static float squareCoords[] = {
            -0.6f, 0.2f, 0.0f,
            -0.4f, -0.5f, 0.0f,
            0.4f,  -0.5f, 0.0f,
            0.6f, 0.2f, 0.0f,
            0.0f, 0.6f,  0.0f};

    private short drawOrder[] = {
            0, 1, 2,
            4, 0, 2,
            3, 4, 2};

    float color[] = {0.6f, 0.8f, 0.2f, 0.0f};
    private final int mProgram;


    public Misteriosa() {
        ByteBuffer bb = ByteBuffer.allocateDirect(
                squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        ByteBuffer dlb = ByteBuffer.allocateDirect(
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        // ponemos el buffer para que lea la primera coordenada
        drawListBuffer.position(0);

        //Carga del sombrado en la generacion del dibujo
        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        // creamos un programa vacio OpenGL ES
        mProgram = GLES20.glCreateProgram();

        // añadimos el sombreado de vertices al programa
        GLES20.glAttachShader(mProgram, vertexShader);

        // añadimos el sombreado de los fragmentos al programa
        GLES20.glAttachShader(mProgram, fragmentShader);

        // Creamos el programa ejecutable de OpenGL ES
        GLES20.glLinkProgram(mProgram);

    }

    private int positionHandle;
    private int colorHandle;

    //contador de vertices
    private final int vertexCount = squareCoords.length / COORDS_PER_VERTEX;
    //Espacio de la matriz para los vertices
    private final int vertexStride = COORDS_PER_VERTEX * 4;

    public void draw(float[] mvpMatrix) {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        //obtener el identificador del miembro de vPosition del sombreador de vértices
        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        //Habilitar vertices del cuadrado
        GLES20.glEnableVertexAttribArray(positionHandle);

        // Preparamos los datos de las coordenadas del triangulo
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // Obtenemos el identificador del color del sombreado de los fragmentos
        colorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        // obtener el identificador de la matriz de transformación de la forma
        vPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        // Set color for drawing the square
        GLES20.glUniform4fv(colorHandle, 1, color, 0);
        // Se pasar la transformación de proyección y vista al sombreador
        GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0);
        //Dibujar Cuadrado
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vertexCount);

        // Deshabilitamos el arreglo de los vertices
        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}
