package com.example.myapplication2;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;

//Clase creada para definir un triangulo
public class Triangulo {
    /*para definir un triangulo se necesita asignarlo mediante coordenadas en un espacio tridimensional,
    para ello antes de ello se deben de definir las coordenadas */

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

    //se definira una matriz de vertices
    private FloatBuffer vertexBuffer;

    //se señalara el numero de coordenadas por vertices en el arreglo
    static final int COORDS_PER_VERTEX = 3;
    //final: asignacion de una variable una unica vez a un objeto o a un valor
    static float triangleCoords[] = { //Arreglo (matris) de las coordenadas de un triangulo
            //Se deberan poner las coordenadas en sentido antihorario
            0.0f,  0.622008459f, 0.0f,   // top
            -0.5f, -0.311004243f, 0.0f,   // bottom left
            0.5f, -0.311004243f, 0.0f    // bottom right
    };

    //Vamos a ponerle un color a nuestro tringulo orden de color rojos, verdes, azules y alphas
    float color[] = {0.63671875f, 0.76953125f, 0.22265625f, 0.0f};
    private final int mProgram;
    //inicializamos nuestra funcion triangulo
    public Triangulo(){
        //inicializamos los vertices para que sean guardados en la matriz creada
        ByteBuffer bb = ByteBuffer.allocateDirect(
                //se multiplicaran los vertices por 4 bytes por el flotante
                triangleCoords.length * 4);
        //se va a usar el hardware del dispositivo para el orden de los bytes
        bb.order(ByteOrder.nativeOrder());
        //creamos los puntos en el buffer
        vertexBuffer = bb.asFloatBuffer();
        // agregamos las coordenadas al buffer
        vertexBuffer.put(triangleCoords);
        // ponemos el buffer para que lea la primera coordenada
        vertexBuffer.position(0);

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

    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX; //contador de vertices
    //Espacio de la matriz para los vertices
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes por vertice

    // Funcion para dibujar el triangulo
    public void draw(float[] mvpMatrix) { //regresar la matriz de transformacion calculada
        // añadimos el programa al entorno de opengl
        GLES20.glUseProgram(mProgram);

        // obtenemos el identificador de los sombreados del los vertices a vPosition
        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // habilitamos el manejo de los vertices del triangulo
        GLES20.glEnableVertexAttribArray(positionHandle);

        // Preparamos los datos de las coordenadas del triangulo
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // Obtenemos el identificador del color del sombreado de los fragmentos
        colorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        // obtener el identificador de la matriz de transformación de la forma
        vPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");


        // Establecemos el color para el dibujo del triangulo
        GLES20.glUniform4fv(colorHandle, 1, color, 0);
        // Se pasar la transformación de proyección y vista al sombreador
        GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0);

        // SE dibuja el triangulo
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        // Deshabilitamos el arreglo de los vertices
        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}
