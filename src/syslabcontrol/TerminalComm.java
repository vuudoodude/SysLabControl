package syslabcontrol;


import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */




/**
 *
 * @author USB_Room
 */
public class TerminalComm 
{
    OutputStream textOutput;
    SerialWriter writer;
    boolean connected;
    
    
    
    public TerminalComm(OutputStream textOut)
    {
        super();
        textOutput = textOut;
        
    }
    
    void Connect (String portName) throws Exception
    {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if (portIdentifier.isCurrentlyOwned())
        {
            System.out.print("Error: port currently in use.");
            connected = false;
           
        }
        else
        {
            CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);
            if (commPort instanceof SerialPort)
            {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                InputStream in = serialPort.getInputStream();
                OutputStream out = serialPort.getOutputStream();
                
                (new Thread(new SerialReader(in,textOutput))).start();
                 writer = new SerialWriter(out);
                (new Thread(writer)).start();
                connected = true;
                
            }
            else
            {
                System.out.print("Error: Selected device must be a serial port.");
                connected = false;
            }
        }
    }
    void Write(byte[]b)
    {
        if (writer != null)
            writer.Write(b);
    }
    
    
    
   
    public static class SerialReader implements Runnable
    {
        InputStream in; //buffer for the serial device;
        OutputStream textOutput; //to the terminal display;
        
        public SerialReader (InputStream in, OutputStream textOutput)
        {
            this.textOutput = textOutput;
            this.in =in;
        }
        
        public void run()
        {
            byte[] buffer = new byte[1024];
            int len = -1;
            try
            {
                while ((len = this.in.read(buffer)) > -1)
                {                    
                    textOutput.write(buffer,0,len);                    
                    //System.out.print(new String(buffer,0,len));
                }
            } catch (IOException e)
            {
                System.out.println("IOException: Error in SerialReader");
            } 
        }    
    }
    
    public static class SerialWriter implements Runnable
    {
        OutputStream out; //this is the Serial output data stream... data to be written to device.
        byte[] buff;
        
        public SerialWriter (OutputStream out)
        {
            this.out = out;
            buff = new byte[0];
        }
        public void Write(byte[] b)
        {
            buff = b;
        }
        
        public void run()
        {
            try
            {
                while(true)
                {
                    while (buff.length > 0) //test for data to be written
                    {
                        this.out.write(buff);
                        buff = new byte[0];
                    }
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(TerminalComm.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        
    }

    public boolean isConnected() {
        return connected;
    } 
}
