package cat.dam.alex.spacetrick;

import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;

public class SceneManager {
    private final ArrayList<Scene> scenes = new ArrayList<>();
    public static int ACTIVE_SCENE;
    public SceneManager(){
        //la primera escena será la primera de la lista scenes, la 0:
        ACTIVE_SCENE = 0;
        scenes.add(new GamePlayScene());
    }
    public void recieveTouch(MotionEvent event){
        scenes.get(ACTIVE_SCENE).recieveTouch(event);
    }
    public void update(){
        scenes.get(ACTIVE_SCENE).update();
    }
    public void draw(Canvas canvas){
        scenes.get(ACTIVE_SCENE).draw(canvas);
    }

}
