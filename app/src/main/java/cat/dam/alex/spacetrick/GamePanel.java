package cat.dam.alex.spacetrick;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
//SurfaceView es una vista como un Layout, que puede mostrar cosas por pantalla
//dibuja la superficie en un subproceso de renderizado
//SurfaceHolder gestiona el SurfaceView (creación, destrucción, etc.
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback{
    private MainThread thread;

    private final SceneManager manager;

    public GamePanel (Context context){
        super(context);
        //metodo de SurfaceView para obtener el surfaceHoler
        getHolder().addCallback(this);
        Constants.CURRENT_CONTEXT=context;
        thread= new MainThread(getHolder(),this);
        manager=new SceneManager();


        //setFocusable(true); //para activar el evento de enfoque de la vista (activado por defecto)
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder){
        thread = new MainThread(getHolder(),this);
        Constants.INIT_TIME = System.currentTimeMillis();
        thread.setRunning(true);
        thread.start();
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        while(retry){
            try{
                thread.setRunning(false);
                thread.join();
            }catch(Exception e){e.printStackTrace();}
            retry=false;
        }
    }
    @Override
    /** para que el personaje del jugador se mueva al lugar tocado.
     *
     */
    public boolean onTouchEvent(MotionEvent event){
        manager.recieveTouch(event);
        //return super.onTouchEvent(event);
        return true;
    }

    public void update(){
        manager.update();
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
        manager.draw(canvas);
    }





}
