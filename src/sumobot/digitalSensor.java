package sumobot;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import java.util.Observable;


/**
 *
 * @author Dominick Sparks
 */
public class digitalSensor extends Observable {
    
    GpioController gpio;
    GpioPinDigitalInput input;
    String name;
    String sensorType;
    
    /**
     * @param sensorType -> "shortRange" || "lineFollower"
     * @param pinNumber
     * @param controller 
     */
    public digitalSensor(String sensorType, String name, Pin pinNumber, GpioController controller){
        this.gpio = controller;
        this.input = gpio.provisionDigitalInputPin(pinNumber);
        this.name = name;
        this.sensorType = sensorType;     
        
        /* This is fired every time the state changes on the given pin
        **  (might be benefitial to only trigger on a HIGH or LOW which ever
        **   is preferred)
        **  ** LOW when a object is detected within < 10cm
        **  ** HIGH when nothing is detected 
        **
        **  ! The main method will be notifed when the the given objcect's
        **    pin goes low.
        */  
        if(sensorType.equals("shortRange")){
            input.addListener(new GpioPinListenerDigital(){
                @Override
                public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                    if(event.getState() == PinState.LOW){
                        setChanged();
                        notifyObservers();
                    }
                }
            });
        }
    }
    
    public String getName(){
        return this.name;
    }
}

