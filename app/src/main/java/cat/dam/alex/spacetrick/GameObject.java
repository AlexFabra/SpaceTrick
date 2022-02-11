package cat.dam.alex.spacetrick;

import android.graphics.Canvas;

/** Toda clase que gestione algo visible en la pantalla, debe implementar
 * esta interfaz para ser dibujada y actualizada
 */
public interface GameObject {
    public void draw(Canvas canvas);
    public void update();


}
