package sumobot;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

/**
 *
 * @author Dominick Sparks
 */
public class digitalSensor {
    
    GpioController gpio;
    GpioPinDigitalInput input;
    
    /**@
     * @param pinNumber
     * @param controller 
     */
    public digitalSensor(Pin pinNumber, GpioController controller){
        gpio = controller;
        input = gpio.provisionDigitalInputPin(pinNumber);
        input.addListener(new sensorStateChange());
    }
    
    /* This is fired every time the state changes on the given pin
    **  (might be benefitial to only trigger on a HIGH or LOW which ever
    **   is preferred)
    **  ** LOW when a object is detected within < 10cm
    **  ** HIGH when nothing is detected 
    */  
    public static class sensorStateChange implements GpioPinListenerDigital {
    @Override
    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
        // display pin state on console
        System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = "
                + event.getState());
    }

    }
}
