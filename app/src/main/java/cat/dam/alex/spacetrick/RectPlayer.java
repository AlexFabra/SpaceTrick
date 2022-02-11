package cat.dam.alex.spacetrick;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class RectPlayer implements GameObject {

    private Rect rectangle;
    private int color;

    public RectPlayer(Rect rectangle, int color){
        this.rectangle=rectangle;
        this.color = color;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(rectangle,paint);
    }

    @Override
    public void update() {

    }

    /** ubica el rectangle según las coordenadas de la instancia de Point
     * @param point objeto con coordenadas.
     */
    public void update(Point point){
        rectangle.set(
                point.x-rectangle.width()/2,
                point.y-rectangle.height()/2,
                point.x+rectangle.width()/2,
                point.y+rectangle.height()/2
        );
    }

    public Rect getRectangle(){
        return rectangle;
    }
}
