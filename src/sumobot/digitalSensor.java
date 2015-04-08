package sumobot;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;


/**
 *
 * @author Dominick Sparks
 */
public class digitalSensor {
    
    GpioController gpio;
    GpioPinDigitalInput input;
    String name;
    int sensorType;
    
    /**
     * @param sensorType -> "0 = shortRange" || "1 = lineFollower"
     * @param name
     * @param pinNumber
     * @param controller 
     */
    public digitalSensor(int sensorType, String name, Pin pinNumber, GpioController controller){
        this.gpio = controller;
        this.input = gpio.provisionDigitalInputPin(pinNumber);
        this.name = name;
        this.sensorType = sensorType;     
    }
    
    public String getName(){
        return this.name;
    }
    public int getType(){
        return this.sensorType;
    }
    public PinState getState(){
        return this.input.getState();
    }
}

