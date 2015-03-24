/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sumobot;

import com.pi4j.gpio.extension.ads.ADS1015GpioProvider;
import com.pi4j.gpio.extension.ads.ADS1015Pin;
import com.pi4j.gpio.extension.ads.ADS1x15GpioProvider;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinAnalogValueChangeEvent;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerAnalog;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.i2c.I2CBus;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author Dominick Sparks
 */
public class SumoBot implements Observer {

    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws InterruptedException, IOException {

        final GpioController gpio = GpioFactory.getInstance();

        // Motor Controller
        Pin input1 = RaspiPin.GPIO_00;
        Pin input2 = RaspiPin.GPIO_01;
        Pin input3 = RaspiPin.GPIO_02;
        Pin input4 = RaspiPin.GPIO_03;
        MotorController motor = new MotorController(gpio, input1, input2, input3, input4);
        /*motor.foward();
        Thread.sleep(3000);
        motor.stop();
        Thread.sleep(500);
        motor.left();
        Thread.sleep(500);
        motor.right();
        Thread.sleep(500);
        motor.reverse();
        Thread.sleep(3000);
        motor.stop();
        */
        // Hardware thread testing... (not possible)
        /*
         Pin sensor1Pin = RaspiPin.GPIO_01; 
         Pin sensor2Pin = RaspiPin.GPIO_04;
        
         LineSensor sensor1 = new LineSensor("test",sensor1Pin,gpio);
         LineSensor sensor2 = new LineSensor("test",sensor2Pin,gpio);
         */
        //sensor1.run();
        //sensor2.run();
        /* Testing the short range digital sensor 
         **
         */
        Pin lineFollower1 = RaspiPin.GPIO_28;
        Pin lineFollower2 = RaspiPin.GPIO_27;
        Pin lineFollower3 = RaspiPin.GPIO_26;
        Pin lineFollower4 = RaspiPin.GPIO_25;
        Pin longRangeSensor = RaspiPin.GPIO_24;
        Pin shortRangeSensor = RaspiPin.GPIO_29;

        //Check first line following sensor
        GpioPinDigitalInput line1 = gpio.provisionDigitalInputPin(lineFollower1);
        line1.addListener(new GpioPinListenerDigital(){
           @Override
           public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                if(event.getState() == PinState.LOW){
        //Check second linefollowing sensor            
                    GpioPinDigitalInput line2 = gpio.provisionDigitalInputPin(lineFollower2);
                    line2.addListener(new GpioPinListenerDigital(){
                     @Override
                        public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {                   
                            if(event.getState() == PinState.LOW){ 
                                motor.foward();
                            }
                            if(event.getState() == PinState.HIGH){
                                motor.left();    
                            }
                        }
                    });
                }                
                if(event.getState() == PinState.HIGH){
                
                    GpioPinDigitalInput line2 = gpio.provisionDigitalInputPin(lineFollower2);
                    line2.addListener(new GpioPinListenerDigital(){
                    @Override
                        public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {                   
                            if(event.getState() == PinState.LOW){ 
                                motor.reverse();
                            }
                            if(event.getState() == PinState.HIGH){
                                motor.right();    
                            }
                        }
                    });
                }
            }
        });
      /*  GpioPinDigitalInput shortRange = gpio.provisionDigitalInputPin(shortRangeSensor);
        shortRange.addListener(new GpioPinListenerDigital(){
                @Override
                public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                    if(event.getState() == PinState.LOW){
                                
                    }
                }
            });
       */ 
        
        //digitalSensor shortSensor = new digitalSensor("shortRange","sensor1",shortRangeSensor,gpio);
        
       // Pin shortRangeSensor2 = RaspiPin.GPIO_28;
        //digitalSensor shortSensor2 = new digitalSensor("shortRange","sensor2",shortRangeSensor2,gpio);
        
        //shortSensor.addObserver(new SumoBot());
        //shortSensor2.addObserver(new SumoBot());
        // Tesinting the A to D converter with the long range sensor
        //final DecimalFormat df = new DecimalFormat("#.##");
        //final DecimalFormat pdf = new DecimalFormat("###.#");
        
        // create gpio controller
        
        // create custom ADS1015 GPIO provider
        //final ADS1015GpioProvider gpioProvider = new ADS1015GpioProvider(I2CBus.BUS_1, ADS1015GpioProvider.ADS1015_ADDRESS_0x48);
        
        // provision gpio analog input pins from ADS1015
        /*GpioPinAnalogInput myInputs[] = {
                gpio.provisionAnalogInputPin(gpioProvider, ADS1015Pin.INPUT_A0, "MyAnalogInput-A0"),
                gpio.provisionAnalogInputPin(gpioProvider, ADS1015Pin.INPUT_A1, "MyAnalogInput-A1"),
                gpio.provisionAnalogInputPin(gpioProvider, ADS1015Pin.INPUT_A2, "MyAnalogInput-A2"),
                gpio.provisionAnalogInputPin(gpioProvider, ADS1015Pin.INPUT_A3, "MyAnalogInput-A3"),
            };
        */
        // ATTENTION !!          
        // It is important to set the PGA (Programmable Gain Amplifier) for all analog input pins. 
        // (You can optionally set each input to a different value)    
        // You measured input voltage should never exceed this value!
        //
        // In my testing, I am using a Sharp IR Distance Sensor (GP2Y0A21YK0F) whose voltage never exceeds 3.3 VDC
        // (http://www.adafruit.com/products/164)
        //
        // PGA value PGA_4_096V is a 1:1 scaled input, 
        // so the output values are in direct proportion to the detected voltage on the input pins
        //gpioProvider.setProgrammableGainAmplifier(ADS1x15GpioProvider.ProgrammableGainAmplifierValue.PGA_4_096V, ADS1015Pin.ALL);
                
        
        // Define a threshold value for each pin for analog value change events to be raised.
        // It is important to set this threshold high enough so that you don't overwhelm your program with change events for insignificant changes
        //gpioProvider.setEventThreshold(500, ADS1015Pin.ALL);

        
        // Define the monitoring thread refresh interval (in milliseconds).
        // This governs the rate at which the monitoring thread will read input values from the ADC chip
        // (a value less than 50 ms is not permitted)
        //gpioProvider.setMonitorInterval(100);
        
        
        // create analog pin value change listener
        /*GpioPinListenerAnalog listener = new GpioPinListenerAnalog()
        {
            @Override
            public void handleGpioPinAnalogValueChangeEvent(GpioPinAnalogValueChangeEvent event)
            {
                // RAW value
                double value = event.getValue();

                // percentage
                double percent =  ((value * 100) / ADS1015GpioProvider.ADS1015_RANGE_MAX_VALUE);
                
                // approximate voltage ( *scaled based on PGA setting )
                double voltage = gpioProvider.getProgrammableGainAmplifier(event.getPin()).getVoltage() * (percent/100);

                // display output
                System.out.print("\r (" + event.getPin().getName() +") : VOLTS=" + df.format(voltage) + "  | PERCENT=" + pdf.format(percent) + "% | RAW=" + value + "       ");
            }
        };
        
        myInputs[0].addListener(listener);
        myInputs[1].addListener(listener);
        myInputs[2].addListener(listener);
        myInputs[3].addListener(listener);
       
        */
       while(true){
           
       }
 
       // gpio.shutdown();
    }
    
    /*
    **  Method that is call when ever an event occurs from a
    **  "Observable" class. 
    **  - Will have to set up variable to track the state of the sensors?
    **  - Consider (ehh) with a polling implmentation to get sensor data
    */
    @Override

    public void update(Observable o, Object arg) {
        digitalSensor dS = (digitalSensor) o;
        System.out.println(dS.getName());
    }

}
