package cat.dam.alex.spacetrick;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //para que no salga el título de la aplicación:
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Con displayMetrics obtenemos las métricas de la pantalla, en este caso ancho y alto, en píxeles:
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        //para trabajar con las medidas las asignaremos a constantes:
        Constants.SCREEN_WIDTH= dm.widthPixels;
        Constants.SCREEN_HEIGHT=dm.heightPixels;

        //declaramos un GamePanel y lo mostramos como View:
        setContentView(new GamePanel(this));
    }
}