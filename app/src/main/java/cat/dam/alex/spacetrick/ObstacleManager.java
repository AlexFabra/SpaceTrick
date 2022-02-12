package cat.dam.alex.spacetrick;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

public class ObstacleManager {
    private ArrayList<Obstacle> obstacles;
    private int playerGap;
    private int obstacleGap;
    private int obstacleHeight;
    private int color;
    // startTime se usará para optener el tiempo exacto del ultimo update:
    private long startTime;
    //initTime se usará para obtener el tiempo exacto de cuando se crea el ObstacleManager:
    private long initTime;
    private int score=0;

    public ObstacleManager(int playerGap, int obstacleGap, int  obstacleHeight, int color){
        //medida del jugador:
        this.playerGap = playerGap;
        //separación entre obstáculos:
        this.obstacleGap = obstacleGap;
        //alto de obstáculo:
        this.obstacleHeight=obstacleHeight;
        //color de este:
        this.color=color;
        startTime= initTime = System.currentTimeMillis();
        obstacles = new ArrayList<>();
        populateObstacles();
    }

    public boolean playerCollide(RectPlayer player){
        for(Obstacle ob: obstacles){
            if(ob.playerCollide(player)){
                return true;
            }
        }
        return false;
    }

    /** populateObstacles crea obstáculos y les da una posición determinada:
     *  guarda los objetos Obstacle en el arrayList obstacles
     */
    private void populateObstacles() {
        // cuanto más alto el valor numérico de position,
        //más bajo en la escena
        int position = -5 * Constants.SCREEN_HEIGHT / 4;

        while (position<0) {
            int obstacleWidth = (int) (Math.random() * (Constants.SCREEN_WIDTH - playerGap));
            obstacles.add(new Obstacle(obstacleHeight,position,obstacleWidth,playerGap,color));
            //a la posicion le sumamos el alto del obstaculo y la separacion definida entre uno y otro:
            position += obstacleHeight + obstacleGap;
        }
    }

    /** update actualizará las variables para que la escena de los obstáculos se vaya moviendo
     */
    public void update(){
        //esta condición reiniciará el tiempo cada vez que volvamos a la app:
        if(startTime< Constants.INIT_TIME){
            startTime=Constants.INIT_TIME;
        }
        int elapsedTime = (int)(System.currentTimeMillis()-startTime);
        startTime = System.currentTimeMillis();
        float speed = //(float) (Math.sqrt(1+(startTime-initTime)/2000.0))*Constants.SCREEN_HEIGHT/(1000.0f);
                Constants.SCREEN_HEIGHT/(10000.0f - (float)(500.0*Math.log((startTime-initTime)/1000.0)));
        for(Obstacle ob: obstacles){
            //hacemos que las coordenadas verticales de los obstáculos incrementen:
            ob.incrementY(speed*elapsedTime);
        }
        //si el ultimo obstaculo está en una posición menor al alto de pantalla:
        if(obstacles.get(obstacles.size()-1).getRectangle().top>=Constants.SCREEN_HEIGHT){
            //irá generando obstáculos:
            int obstacleWidth = (int) (Math.random() * (Constants.SCREEN_WIDTH - playerGap));
            obstacles.add(0,new Obstacle(obstacleHeight,obstacles.get(0).getRectangle().top - obstacleHeight - obstacleGap,obstacleWidth,playerGap,color));
            obstacles.remove(obstacles.size()-1);
            score++;
            System.out.println("tiempo inicio"+initTime);
        }
    }

    /** pinta los obstáculos creados en la escena
     * @param canvas utilizado para pintar los obstaculos en la escena.
     */
    public void draw(Canvas canvas){
        for(Obstacle obstacle: obstacles){
            obstacle.draw(canvas);
        }
        Paint paint = new Paint();
        paint.setTextSize(100);
        paint.setColor(Color.MAGENTA);
        canvas.drawText(""+score,50,50+ paint.descent()-paint.ascent(),paint);
    }
}
