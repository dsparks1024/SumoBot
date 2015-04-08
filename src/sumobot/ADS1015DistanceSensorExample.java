package sumobot;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  ADS1015DistanceSensorExample.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2014 Pi4J
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
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
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinAnalogInput;
import com.pi4j.io.i2c.I2CBus;

/**
 * <p>
 * This example code demonstrates how to use the ADS1015 Pi4J GPIO interface
 * for analog input pins.
 * </p>  
 * 
 * @author Robert Savage
 */
public class ADS1015DistanceSensorExample {
    
    
    public static void main(String args[]) throws InterruptedException, IOException {
        
        System.out.println("<--Pi4J--> ADS1015 Distance Sensor Example ... started.");

        // number formatters
        final DecimalFormat df = new DecimalFormat("#.##");
        final DecimalFormat pdf = new DecimalFormat("###.#");
        
        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();
        
        // create custom ADS1015 GPIO provider
        final ADS1015GpioProvider gpioProvider = new ADS1015GpioProvider(I2CBus.BUS_1, ADS1015GpioProvider.ADS1015_ADDRESS_0x48);
        
        // provision gpio analog input pins from ADS1015
        final GpioPinAnalogInput distanceSensorPin = gpio.provisionAnalogInputPin(gpioProvider, ADS1015Pin.INPUT_A3, "DistanceSensor-A0");
        
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
        gpioProvider.setEventThreshold(50, ADS1015Pin.ALL);

        
        // Define the monitoring thread refresh interval (in milliseconds).
        // This governs the rate at which the monitoring thread will read input values from the ADC chip
        // (a value less than 50 ms is not permitted)
        gpioProvider.setMonitorInterval(100);
        
        // create a distance sensor based on an analog input pin
        DistanceSensorComponent distanceSensor = new DistanceSensorComponent(distanceSensorPin);
        
        // build a distance coordinates mapping (estimated distance at raw values)
        distanceSensor.addCalibrationCoordinate(1169, 7);
        distanceSensor.addCalibrationCoordinate(779, 15);
        distanceSensor.addCalibrationCoordinate(642, 20);
        distanceSensor.addCalibrationCoordinate(500, 25);
        distanceSensor.addCalibrationCoordinate(430, 30);
        distanceSensor.addCalibrationCoordinate(360, 35); 
        distanceSensor.addCalibrationCoordinate(300, 40);
        distanceSensor.addCalibrationCoordinate(250, 45); 
        distanceSensor.addCalibrationCoordinate(230, 50);  
        distanceSensor.addCalibrationCoordinate(200, 55); 
        distanceSensor.addCalibrationCoordinate(180, 60); 

        distanceSensor.addListener(new DistanceSensorListener()
        {
            @Override
            public void onDistanceChange(DistanceSensorChangeEvent event)
            {
                // RAW value
                double value = event.getRawValue();

                // Estimated distance
                double distance =  event.getDistance();
                
                // percentage
                double percent =  ((value * 100) / ADS1015GpioProvider.ADS1015_RANGE_MAX_VALUE);
                
                // approximate voltage ( *scaled based on PGA setting )
                double voltage = gpioProvider.getProgrammableGainAmplifier(distanceSensorPin).getVoltage() * (percent/100);

                // display output
                System.out.print("\r DISTANCE=" + df.format(distance) + "cm : VOLTS=" + df.format(voltage) + "  | PERCENT=" + pdf.format(percent) + "% | RAW=" + value + "       ");
            }
        });
        
        // keep program running for 10 minutes 
        for (int count = 0; count < 600; count++) {
            Thread.sleep(1000);
        }
        
        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        gpio.shutdown();
        System.out.print("");
    }
}


