package cat.dam.alex.spacetrick;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

/** MainThread conecta con GamePanel, actualizándolo.
 *
 */
public class MainThread extends Thread{
    //determinamos los frames por segundo:
    public static final int MAX_FPS=30;
    private double averageFPS;
    private final SurfaceHolder surfaceHolder;
    private final GamePanel gamePanel;
    private boolean running;
    public static Canvas canvas;

    public void setRunning(Boolean running){
        this.running=running;
    }

    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel){
        super();
        this.surfaceHolder=surfaceHolder;
        this.gamePanel=gamePanel;
    }
    @Override
    public void run(){
        long startTime;
        //por cada segundo {MAX_FPS} frames:
        long timeMillis = 1000/MAX_FPS;
        long waitTime;
        int frameCount = 0;
        long totalTime = 0;
        long targetTime = 1000/MAX_FPS;

        while (running){
            startTime= System.nanoTime();
            canvas = null;
            try{
                //obtenemos el lienzo mediante lockCanvas();
                canvas= this.surfaceHolder.lockCanvas();
                //
                synchronized (surfaceHolder){
                    //actualizamos el gamePanel
                    this.gamePanel.update();
                    //repintamos el canvas con las nuevas coordenadas:
                    this.gamePanel.draw(canvas);
                }
            }catch(Exception e){
                e.printStackTrace();
            } finally {
                if(canvas!=null){
                    try{
                        // Después de dibujar envía el canvas a la vista para su visualización
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
            //obtenemos la diferencia entre el tiempo de inicio del bucle y el de fin:
            timeMillis = (System.nanoTime() - startTime)/1000000;
            waitTime = targetTime-timeMillis;
            try{
                if(waitTime>0){
                    //paramos el thread los milisegundos de waitTime:
                    sleep(waitTime);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if(frameCount == MAX_FPS){ //cada vez que se llegue a MAX_FRAMES se ejecutará esta condición:
                averageFPS = 1000/((totalTime/frameCount)/1000000);
                frameCount = 0;
                totalTime = 0;
                //System.out.println(averageFPS);
            }
        }
    }
}
