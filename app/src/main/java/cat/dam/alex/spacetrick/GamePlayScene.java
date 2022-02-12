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
    private OrientationData orientationData; //gestor de sensores
    private long frameTime; //para controlar el lapso entre frames

    public GamePlayScene() {
        player=new RectPlayer(new Rect(100,100,200,200), Color.rgb(255,0,0));
        //posición del jugador:
        playerPoint = new Point(Constants.SCREEN_WIDTH/2,3*Constants.SCREEN_HEIGHT/4);
        //posicionamos al jugador donde ubicamos el punto:
        player.update(playerPoint);
        obstacleManager = new ObstacleManager(200,500,75,Color.BLACK);
        orientationData=new OrientationData();
        orientationData.register(); //ejecutamos registro de los sensores giroscópicos
        frameTime=System.currentTimeMillis();

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
                    orientationData.newGame();
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
        canvas.drawColor(Color.rgb(0,0,200));
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
            //esta condición reinicia el controlador de tiempo cuando la aplicación se reactiva:
            if(frameTime<Constants.INIT_TIME){
                frameTime=Constants.INIT_TIME;
            }
            //obtiene el lapso entre un frame y otro.
            int elapsedTime = (int)(System.currentTimeMillis()-frameTime);
            frameTime=System.currentTimeMillis();

            if(orientationData.getOrientation()!=null && orientationData.getStartOrientation()!=null) {
                float pitch = orientationData.getOrientation()[1]-orientationData.getStartOrientation()[1];
                float roll = orientationData.getOrientation()[2]-orientationData.getStartOrientation()[2];

                float xSpeed = 2 * roll * Constants.SCREEN_WIDTH/1000f;
                float ySpeed = pitch * Constants.SCREEN_HEIGHT/1000f;

                //limitamos la respuesta al movimiento de manos según si el movimiento del usuario ha sido notable o no:
                playerPoint.x+=Math.abs(xSpeed*elapsedTime)>7 ? xSpeed*elapsedTime:0;
                playerPoint.y-=Math.abs(ySpeed*elapsedTime)>7 ? ySpeed*elapsedTime:0;

            }
            //limitamos el movimiento del player point para que se ubique siempre en la pantalla:
            if(playerPoint.x<0){
                playerPoint.x=0;
            } else if (playerPoint.x>Constants.SCREEN_WIDTH){
                playerPoint.x=Constants.SCREEN_WIDTH;
            }
            if(playerPoint.y<0){
                playerPoint.y=0;
            } else if (playerPoint.y>Constants.SCREEN_HEIGHT){
                playerPoint.y=Constants.SCREEN_HEIGHT;
            }

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
