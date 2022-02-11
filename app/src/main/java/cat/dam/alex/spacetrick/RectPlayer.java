package cat.dam.alex.spacetrick;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class RectPlayer implements GameObject {

    private Rect rectangle;
    private int color;
    private Animation idle;
    private Animation runRight;
    private Animation runLeft;
    private Animation runTop;
    private AnimationManager animManager;

    public RectPlayer(Rect rectangle, int color){
        this.rectangle=rectangle;
        this.color = color;
        //gestor de bitmaps: producir, decodificar bitmaps, etc.
        BitmapFactory bf = new BitmapFactory();
        Bitmap idleImg = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(),R.drawable.hamibasic);
        Bitmap run1Img=bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(),R.drawable.correr1);
        Bitmap run2Img=bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(),R.drawable.correr2);
        Bitmap runTopImg=bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(),R.drawable.correrfondo);
        Bitmap runTop2Img=bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(),R.drawable.correrfondo2);
        Bitmap runLeftImg=flipBitMap(bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(),R.drawable.correr1));
        Bitmap runLeft2Img=flipBitMap(bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(),R.drawable.correr2));

        idle = new Animation(new Bitmap[]{idleImg},2);
        runTop = new Animation(new Bitmap[]{runTopImg,runTop2Img},0.1f);
        runRight = new Animation(new Bitmap[]{run1Img,run2Img},0.1f);
        runLeft = new Animation(new Bitmap[]{runLeftImg,runLeft2Img},0.1f);

        animManager = new AnimationManager(new Animation[]{runTop,runRight,runLeft});

    }

    /** invierte horizontalmente un bitmap.
     * @param toFlip el bitmap a invertir.
     * @return el bitmap invertido.
     */
    public Bitmap flipBitMap(Bitmap toFlip){
        Matrix m = new Matrix();
        m.preScale(-1, 1);
        Bitmap flippedBitmap = Bitmap.createBitmap(toFlip, 0, 0, toFlip.getWidth(), toFlip.getHeight(), m, false);
        return flippedBitmap;
    }

    @Override
    public void draw(Canvas canvas) {
        //Paint paint = new Paint();
        //paint.setColor(color);
        //canvas.drawRect(rectangle,paint);
        animManager.draw(canvas,rectangle);
    }

    @Override
    public void update() {
        animManager.update();
    }

    /** ubica el rectangle según las coordenadas de la instancia de Point
     * @param point objeto con coordenadas.
     * selecciona la animación que mostrará según los movimientos del usuario
     */
    public void update(Point point){
        float oldLeft = rectangle.left;
        rectangle.set(
                point.x-rectangle.width()/2,
                point.y-rectangle.height()/2,
                point.x+rectangle.width()/2,
                point.y+rectangle.height()/2
        );
        //state representa el movimiento del personaje (recto=0,derecha=1 o izquierda=2)
        int state = 0;
        if(rectangle.left-oldLeft>1){ //1 pixel de diferencia para reconocer el cambio de direccion
            state=2;
        }else if(rectangle.left-oldLeft<-1){
            state=1;
        }
        animManager.playAnim(state);
        animManager.update();
    }

    public Rect getRectangle(){
        return rectangle;
    }
}
