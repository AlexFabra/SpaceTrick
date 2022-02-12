package cat.dam.alex.spacetrick;

import android.graphics.Canvas;
import android.graphics.Rect;

/** gestor de Animaciones: selecciona la animación y el frame que se va a dibujar.
 */
public class AnimationManager {
    private final Animation[] animations;
    private int animationIndex=0;
    public AnimationManager(Animation[] animations){
        this.animations=animations;
    }

    /** llama a la funcion play de Animation pasándole como valor la ubicación de la animación que
     *  tiene que ejecutarse
     * @param index
     */
    public void playAnim(int index){
        for(int i=0;i<animations.length;i++){
            if(i==index) {
                if (!animations[index].isPlaying()) {
                    animations[i].play();
                }
            } else {
                animations[i].stop();
            }
        }
        animationIndex=index;
    }

    public void draw(Canvas canvas, Rect rect){
        if(animations[animationIndex].isPlaying()) {
            animations[animationIndex].draw(canvas, rect);
        }
    }
    public void update(){
        if(animations[animationIndex].isPlaying()) {
            animations[animationIndex].update();
        }
    }
}
