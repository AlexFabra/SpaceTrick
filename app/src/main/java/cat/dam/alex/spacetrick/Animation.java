package cat.dam.alex.spacetrick;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.widget.CalendarView;


public class Animation {
    private Bitmap[ ] frames;
    private int frameIndex;
    private boolean isPlaying=false;
    private float frameTime;
    private long lastFrame;

    /**
     *
     * @param frames
     * @param animTime valor en segundos
     */
    public Animation(Bitmap[]frames,float animTime){
        this.frames=frames;
        frameIndex=0;
        frameTime=animTime/frames.length;
        lastFrame=System.currentTimeMillis();
    }

    public void draw(Canvas canvas, Rect destination){
        if(!isPlaying){
            return;
        }
        scaleRect(destination);
        //pinta el frame de la animación:
        canvas.drawBitmap(frames[frameIndex],null,destination,new Paint());
    }

    public void update(){
        if(!isPlaying){
            return;
        }
        if(System.currentTimeMillis()-lastFrame>frameTime*1000){
            frameIndex++; //ir al siguiente frame.
            //si frameIndex equivale a la longitud máxima de frames, se le asigna valor 0:
            frameIndex = frameIndex >= frames.length ? 0 : frameIndex;
            lastFrame = System.currentTimeMillis();
        }
    }

    /** escala el rectangulo donde se ubica el bitmap del player
     * @param rect
     */
    private void scaleRect(Rect rect){
        float whRatio = (float) frames[frameIndex].getWidth()/frames[frameIndex].getHeight();
        if(rect.width()>rect.height()){
            rect.left=rect.right-(int)(rect.height()*whRatio);
        } else {
            rect.top=rect.bottom-(int)(rect.width()*(1/whRatio));
        }
    }

    public boolean isPlaying(){
        return isPlaying;
    }

    public void play(){
        isPlaying=true;
        frameIndex=0;
        lastFrame=System.currentTimeMillis();
    }
    public void stop(){
        isPlaying=false;
    }

}
