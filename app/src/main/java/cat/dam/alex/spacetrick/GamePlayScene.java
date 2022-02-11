package cat.dam.alex.spacetrick;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

public class GamePlayScene implements Scene{

    private RectPlayer player;
    private Point playerPoint;
    private ObstacleManager obstacleManager;
    private boolean movingPlayer=false;
    private boolean gameOver=false;
    private long gameOverTime;
    private Rect r =new Rect();

    public GamePlayScene() {
        player=new RectPlayer(new Rect(100,100,200,200), Color.rgb(255,0,0));
        //posición del jugador:
        playerPoint = new Point(Constants.SCREEN_WIDTH/2,3*Constants.SCREEN_HEIGHT/4);
        //posicionamos al jugador donde ubicamos el punto:
        player.update(playerPoint);
        obstacleManager = new ObstacleManager(200,350,75,Color.BLACK);
    }

    @Override
    public void terminate(){
        SceneManager.ACTIVE_SCENE=0;
    }

    @Override
    public void recieveTouch(MotionEvent event) {
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
    }

    @Override
    public void draw(Canvas canvas){
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
    @Override
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
