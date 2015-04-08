package sumobot;

/* This class creates an object to interface with the ADS1015 AtoD converter.
*  The device commuinicates via the I2C bus using the address of 0x48
*
*  To set the device's address to 0x48, connect the ADDR pin to ground. 
*
*  !! In this implmenentation, the main method is alerted when a new value/distance
*      recorded. 
*/

import java.io.IOException;
import java.text.DecimalFormat;

import com.pi4j.component.sensor.DistanceSensorChangeEvent;
import com.pi4j.component.sensor.DistanceSensorListener;
import com.pi4j.component.sensor.impl.DistanceSensorComponent;
import com.pi4j.gpio.extension.ads.ADS1015GpioProvider;
import com.pi4j.gpio.extension.ads.ADS1015Pin;
import com.pi4j.gpio.extension.ads.ADS1x15GpioProvider.ProgrammableGainAmplifierValue;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinAnalogInput;
import com.pi4j.io.i2c.I2CBus;
import java.util.Observable;


public class LongRangeDistanceSensor extends Observable implements Runnable{
    
    GpioController gpio;
    double distance;
    double rawValue;
    double value;
    DistanceSensorComponent distanceSensor;
    
    public LongRangeDistanceSensor(GpioController GPIO) throws InterruptedException, IOException {
        
        this.gpio = GPIO;
        
        // number formatters
        final DecimalFormat df = new DecimalFormat("#.##");
        final DecimalFormat pdf = new DecimalFormat("###.#");
        
        // create gpio controller
        
        // create custom ADS1015 GPIO provider
        final ADS1015GpioProvider gpioProvider = new ADS1015GpioProvider(I2CBus.BUS_1, ADS1015GpioProvider.ADS1015_ADDRESS_0x48);
        
        // provision gpio analog input pins from ADS1015
        final GpioPinAnalogInput distanceSensorPin = gpio.provisionAnalogInputPin(gpioProvider, ADS1015Pin.INPUT_A0, "DistanceSensor-A0");
        
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
        gpioProvider.setProgrammableGainAmplifier(ProgrammableGainAmplifierValue.PGA_4_096V, ADS1015Pin.ALL);
                
        
        // Define a threshold value for each pin for analog value change events to be raised.
        // It is important to set this threshold high enough so that you don't overwhelm your program with change events for insignificant changes
        gpioProvider.setEventThreshold(150, ADS1015Pin.ALL);

        
        // Define the monitoring thread refresh interval (in milliseconds).
        // This governs the rate at which the monitoring thread will read input values from the ADC chip
        // (a value less than 50 ms is not permitted)
        gpioProvider.setMonitorInterval(51);
        
        // create a distance sensor based on an analog input pin
        this.distanceSensor = new DistanceSensorComponent(distanceSensorPin);
        
        // build a distance coordinates mapping (estimated distance at raw values)
        distanceSensor.addCalibrationCoordinate(21600, 13);
        distanceSensor.addCalibrationCoordinate(21500, 14);
        distanceSensor.addCalibrationCoordinate(21400, 15);
        distanceSensor.addCalibrationCoordinate(21200, 16);
        distanceSensor.addCalibrationCoordinate(21050, 17);
        distanceSensor.addCalibrationCoordinate(20900, 18); 
        distanceSensor.addCalibrationCoordinate(20500, 19);
        distanceSensor.addCalibrationCoordinate(20000, 20); 
        distanceSensor.addCalibrationCoordinate(15000, 30);  
        distanceSensor.addCalibrationCoordinate(12000, 40); 
        distanceSensor.addCalibrationCoordinate(9200,  50); 
        distanceSensor.addCalibrationCoordinate(8200,  60); 
        distanceSensor.addCalibrationCoordinate(6200,  70); 
        distanceSensor.addCalibrationCoordinate(4200,  80); 
        distanceSensor.addListener(new DistanceSensorListener()
        {
            @Override
            public void onDistanceChange(DistanceSensorChangeEvent event)
            {
                distance = event.getDistance();
                rawValue = event.getRawValue();
                //setChanged();
                //notifyObservers();
                
                // RAW value
                double value = event.getRawValue();

                // Estimated distance
                double distance =  event.getDistance();
                
                // percentage
                double percent =  ((value * 100) / ADS1015GpioProvider.ADS1015_RANGE_MAX_VALUE);
                
                // approximate voltage ( *scaled based on PGA setting )
                double voltage = gpioProvider.getProgrammableGainAmplifier(distanceSensorPin).getVoltage() * (percent/100);

                // display output
                //System.out.print("\r DISTANCE=" + df.format(distance) + "cm : VOLTS=" + df.format(voltage) + "  | PERCENT=" + pdf.format(percent) + "% | RAW=" + value + "       ");
                
            }
                       
        });
   
    }
    
    public double getDistance(){
        return this.distance;
    }
    
    public double getRawValue(){
        return this.rawValue;
    }

    @Override
    public void run() {
        while(true){
            System.out.println(this.distanceSensor.getDistance());
        }
    }
}


