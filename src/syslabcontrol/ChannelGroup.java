/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package syslabcontrol;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;


/**
 *
 * @author USB_Room
 */
public class ChannelGroup {
    
    String groupName;
    ArrayList <Channel> channels;
    EventList timeline;
    //an element of ChannelGroup can be either a channel or a subgroup.
    //This permits nesting groups.
    
    transient Color viewColor;
    
    public ChannelGroup(String name)        
    {
        super();
        viewColor = Color.black;
        this.groupName = name;
        channels = new ArrayList();
        timeline = new EventList();
        
    }
    public void associateEventsWithGroup()
    {
        Iterator i = timeline.iterator();
        while(i.hasNext())
        {
            ((Event)(i.next())).setGroup(this);
        }
    }
    
    public ArrayList getChannels()
    {
        return channels;
    }
    
    
    public void add(Channel c)
    {
        this.channels.add(c);
        c.setGroup(groupName);
    }
    
    public String buildCommandString(String command)
    {
        String commandOut = "";
        String[] address;
        
        String A = "~~";
        String B = "~~SS";
        
        Channel currentChannel;
        
        Iterator channelIterator = channels.iterator();
        while (channelIterator.hasNext())
        {
             currentChannel = (Channel)channelIterator.next();
             {
                 address = currentChannel.address.split("[ABC]");
                 if(!A.equals(address[1]))
                 {
                     A = address[1];
                     commandOut += "A" + A;
                 }
                 if(!B.equals(address[2]))
                 {
                     B = address[2];
                     commandOut += "B" + B;
                 }
                 commandOut += "C" + address[3] + command;
             }
        }
        System.out.println("Length: " + commandOut.length() + " -- Command: " + commandOut);
        return commandOut;
    }
    
    public int getTimelineLengthInMillis()
    {
        if (this.timeline == null || this.timeline.isEmpty())
            return 0;
        else
            return timeline.getLast().time.toMillis();
                    
    }
     
    
    
    
    
    protected class EventList  
    {
        LinkedList <Event>events; 
        
        public EventList()
        {
            events = new LinkedList();
            
        }
        
        public void add(Event e) {
            events.add(e);
            Collections.sort(events);
        }
        public Event getElementNearestTime(int time)
        {
            Iterator i = events.iterator();
            int deltaTime = Integer.MAX_VALUE;
            Event e = null;
            while (i.hasNext())
            {
                e = (Event)i.next();
                int t = Math.abs(time - e.time.toMillis());
                if (t < deltaTime)
                    deltaTime = t;
                else
                    return e;      
            }
            return e;
        }
        public Iterator iterator()
        {
            return events.iterator();
        }
        public Iterator descendingIterator()
        {
            return events.descendingIterator();
        }
        public boolean isEmpty()
        {
            return events.isEmpty();
        }
        public Event getLast()
        {
            return (Event) events.getLast();
        }
        public void sort()
        {
            Collections.sort(events);
            
        }
        
    }
    
    

    
    
    
    
   
}
