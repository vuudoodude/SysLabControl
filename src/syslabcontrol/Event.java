/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package syslabcontrol;

/**
 *
 * @author USB_Room
 */
public class Event implements Comparable{
    public String title;
    public String description;
    public String command;
    public TimeIndex time;
    
    
    
    transient public ChannelGroup group;
    public final transient TimeIndex TIME_ZERO = new TimeIndex(0);
    
   
    public Event(ChannelGroup group, String title, String command, String description)
    {
        this.group = group;
        this.title = title;
        this.command = command;
        this.description = description;
        time = new TimeIndex();
    }
    
    @Override
    public int compareTo(Object o) {
        
        return Integer.compare(this.time.toMillis(), ((Event)o).time.toMillis());
        
    }
     public void setGroup(ChannelGroup g)
        {
            this.group = g;
        }

    boolean isHighlighted() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
    public  class TimeIndex
    {
        int hours, minutes, seconds;
        int millis;
        
        public  TimeIndex()
        {
            //with no argument, time = time zero.
            hours = minutes = seconds = millis = 0;
        }
        public TimeIndex(int hours, int minutes, int seconds)
        {
            this.hours= hours; this.minutes = minutes; this.seconds = seconds;
            millis = this.toMillis();
        }
        public TimeIndex(int millis)
        {
            this.millis = millis;
            hours = millis / (3600 * 1000);
            int rem = millis % (3600 / 1000);
            minutes = rem / (60 * 1000);
            rem = millis % (60 * 1000);
            seconds = rem / 1000;
        }
        public void setTime(int hours, int minutes, int seconds)
        {
            this.hours = hours; this.minutes = minutes; this.seconds = seconds;
            millis = toMillis();
        }

        public int toMillis() {
            return (hours * 3600 + minutes * 60 + seconds) * 1000;
        }    
        public String toString()
        {
            return  String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
        }
       
    }
    
    
}
