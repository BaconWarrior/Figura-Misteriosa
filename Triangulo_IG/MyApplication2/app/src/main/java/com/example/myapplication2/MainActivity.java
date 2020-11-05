package com.example.myapplication2;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private GLSurfaceView gLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gLView = new MyGLSurfaceView(this);
        setContentView(gLView);
    }

    /*
    @Override
    protected void onPause() {
        super.onPause();
        //La siguiente llamada pausa el hilo de renderizado.
        //Si su aplicación OpenGL consume mucha memoria,
        //debería considerar desasignar los objetos
        //que consumen mucha memoria aquí.

        gLView.onPause();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //La siguiente llamada reanuda
        //un hilo de procesamiento en pausa.
        //Si desasignó objetos gráficos
        //para onPause (), este es un buen
        //lugar para reasignarlos.
        gLView.onResume();
    }
    */
}