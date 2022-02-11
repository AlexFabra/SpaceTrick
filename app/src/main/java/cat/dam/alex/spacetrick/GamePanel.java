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

    private RectPlayer player;
    private Point playerPoint;
    private ObstacleManager obstacleManager;
    private boolean movingPlayer=false;
    private boolean gameOver=false;
    private long gameOverTime;
    private Rect r =new Rect();

    public GamePanel (Context context){
        super(context);
        //metodo de SurfaceView para obtener el surfaceHoler
        getHolder().addCallback(this);
        thread= new MainThread(getHolder(),this);
        player=new RectPlayer(new Rect(100,100,200,200), Color.rgb(255,0,0));
        //posición del jugador:
        playerPoint = new Point(Constants.SCREEN_WIDTH/2,3*Constants.SCREEN_HEIGHT/4);
        //posicionamos al jugador donde ubicamos el punto:
        player.update(playerPoint);

        obstacleManager = new ObstacleManager(200,350,75,Color.BLACK);

        //setFocusable(true); //para activar el evento de enfoque de la vista (activado por defecto)
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder){
        thread = new MainThread(getHolder(),this);
        thread.setRunning(true);
        thread.start();
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        while(true){
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
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(!gameOver && player.getRectangle().contains((int)event.getX(),(int)event.getY())){
                    movingPlayer=true;
                }
                if(gameOver && System.currentTimeMillis()- gameOverTime >= 2000){
                    gameOver=false;
                    reset();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(!gameOver && movingPlayer){
                    // ubicamos playerPoint donde ha tocado el dedo:
                    playerPoint.set((int)event.getX(),(int)event.getY());
                }
                break;
            case MotionEvent.ACTION_UP: //si se deja de pulsar.
                movingPlayer=false;
                break;
        }
        //return super.onTouchEvent(event);
        return true;
    }

    public void update(){
        if(!gameOver){
            player.update(playerPoint);
            obstacleManager.update();
            if(obstacleManager.playerCollide(player)){
                gameOver=true;
                gameOverTime=System.currentTimeMillis();
                System.out.println("muerto!");
            }
        }

    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
        //pintamos el layout de blanco:
        canvas.drawColor(Color.rgb(0,0,100));
        //dibujamos el jugador en el canvas pasandole a la funcion draw el lugar donde tienen que dibujarse:
        player.draw(canvas);
        //dibujamos los obstaculos :
        obstacleManager.draw(canvas);

        if(gameOver){
            Paint paint = new Paint();
            paint.setTextSize(100);
            paint.setColor(Color.MAGENTA);
            drawCenterText(canvas,paint,"Has perdido");
        }
    }

    public void reset(){
        //posición del jugador:
        playerPoint = new Point(Constants.SCREEN_WIDTH/2,3*Constants.SCREEN_HEIGHT/4);
        player.update(playerPoint);
        obstacleManager = new ObstacleManager(200,350,75,Color.BLACK);
        movingPlayer=false;
    }

    private void drawCenterText(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom;
        canvas.drawText(text, x, y, paint);
    }

}
