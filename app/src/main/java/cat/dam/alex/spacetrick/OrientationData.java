package cat.dam.alex.spacetrick;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Cuando los sensores obtengan nuevos valores esta clase los captará
 */
public class OrientationData implements SensorEventListener {

    private SensorManager manager;
    private Sensor accelerometer;
    private Sensor magnometer;

    private float[] accelOutput;
    private float[] magOutput;

    private float[]orientation= new float[3];
    private float[]getOrientation;
    public float[]startOrientation=null;

    public float[] getOrientation(){
        return orientation;
    }

    public float[] getStartOrientation(){
        return startOrientation;
    }

    public void newGame(){
        startOrientation=null; //reseteamos el punto de referencia.
    }

    public OrientationData(){
        manager = (SensorManager)Constants.CURRENT_CONTEXT.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnometer = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    /** escucha los cambios de los sensores con una frecuencia determinada
     *  por SENSOR_DELAY_GAME
     */
    public void register(){
        manager.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_GAME);
        manager.registerListener(this,magnometer,SensorManager.SENSOR_DELAY_GAME);
    }

    /** para detener la escucha a los sensores cuando no sean necesarios
     */
    public void pause(){
        manager.unregisterListener(this);
    }

    //dado que este metodo puede sobrecargar nuestro telefono al ser llamado constantemente
    //vamos a utilizar nuestro método register para declarar los listeners que queremos activar
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            accelOutput = event.values;
        }
        else if (event.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD){
            magOutput = event.values;
        }
        if(accelOutput!=null && magOutput!=null){
            //metricas de rotación 3x3
            float[] R = new float[9];
            //metricas de inclinacion 3x3
            float[] I = new float[9];
            //para obtener la posición giroscópica necesitamos pasarle los arrays y los datos
            //del acelerometro y el magnetómetro. Si procesa los datos correctamente retorna true.
            boolean success=SensorManager.getRotationMatrix(R,I,accelOutput,magOutput);
            if(success){
                SensorManager.getOrientation(R,orientation);
                if(startOrientation==null){
                    startOrientation=new float[orientation.length];
                    System.arraycopy(orientation,0,startOrientation,0,orientation.length);
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
