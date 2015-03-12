/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sumobot;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dominick Sparks
 */
public class LineSensor implements Runnable {
    
    GpioController gpio;
    GpioPinDigitalOutput output;
    
    @Override
    public void run() {
        try {
            output.high();
            Thread.sleep(1500);
            output.low();
            Thread.sleep(1500);
        } catch (InterruptedException ex) {
            Logger.getLogger(LineSensor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public LineSensor(String name, Pin pinNumber,GpioController GPIO){
        gpio = GPIO;
        output = gpio.provisionDigitalOutputPin(pinNumber,PinState.LOW);
    }
    
}
