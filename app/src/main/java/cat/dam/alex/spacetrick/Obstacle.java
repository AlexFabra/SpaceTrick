package cat.dam.alex.spacetrick;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Obstacle implements GameObject {
    private Rect rectangle;
    private Rect rectangle2;
    private int color;

    public Obstacle(int rectHeight, int startY, int startX, int playerGap, int color){
        this.color = color;
        rectangle= new Rect(0,startY,startX,startY+rectHeight);
        rectangle2= new Rect(startX+playerGap,startY,Constants.SCREEN_WIDTH,startY+rectHeight);
    }

    /** detecta colisiones entre el jugador y el obstáculo.
     * @param player
     * @return boolean true si hay colisión, de lo contrario false.
     */
    public boolean playerCollide(RectPlayer player){
        //colisionarán si hay una intersección entre ellos:
        return Rect.intersects(rectangle,player.getRectangle())||Rect.intersects(rectangle2,player.getRectangle());
    }

    @Override
    public void draw(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(rectangle,paint);
        canvas.drawRect(rectangle2,paint);
    }

    @Override
    public void update(){}

    public Rect getRectangle(){
        return rectangle;
    }

    /** las coordenadas verticales del obstaculo incrementan, lo que lo mueve hacia abajo en el canvas.
     * @param y coordenada vertical.
     */
    public void incrementY(float y){
        rectangle.top +=y;
        rectangle.bottom +=y;
        rectangle2.top +=y;
        rectangle2.bottom +=y;
    }
}
