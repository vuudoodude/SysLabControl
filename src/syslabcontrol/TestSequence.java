/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package syslabcontrol;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.awt.Component;
import java.awt.FileDialog;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *  
 * @author USB_Room
 */



public class TestSequence implements Runnable{
    
    String sequenceName, sequenceDescription;
    ArrayList <ChannelGroup> groups;
    transient Terminal terminal;
    transient ArrayList <SequenceViewerInterface> viewerList;
    
    public TestSequence(ArrayList <ChannelGroup> groups)
    {
        //to facilitate converting old save format to new.
        if(sequenceName == null)
            sequenceName = "testSequenceNameHolder";
        if(sequenceDescription == null)
            sequenceDescription = "Placeholder for future description field";
        
        this.groups = groups;
        viewerList = new ArrayList();
    }
    public TestSequence()
    {
        groups = new <ChannelGroup> ArrayList();
        viewerList = new ArrayList();
    }
    public void setTerminal(Terminal t)
    {
        this.terminal = t;
    }
    public void addViewer(SequenceViewerInterface v)
    {
       if(!viewerList.contains(v)) //avoid duplicates
        viewerList.add(v);
    }
    public void removeViewer(SequenceViewerInterface v)
    {
        viewerList.remove(v);
    }
    

//Component c is a reference for filedialog
    public void loadState(Component c)
    {
        
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(c);
        FileDialog fd = new FileDialog(parent, "Load settings file...", FileDialog.LOAD);
        fd.setFilenameFilter(new FilenameFilter(){
            @Override
            public boolean accept(File dir, String name) {
                if(name.endsWith(".json"))
                    return true;
                else
                    return false;
            }
            
        });
        fd.setVisible(true);
        String filename = fd.getDirectory() + fd.getFile();
        
        
        
        try {
            String jsonString = "";
            String line;
            BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
            while ((line = br.readLine()) != null)
            {
                jsonString = jsonString + line;
            }
            //figure out how many groups the string contains...
            String tempString = jsonString;
            jsonString = jsonString.trim();
           
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new StringReader(jsonString));
            reader.setLenient(true);
            
            /*groups = new <ChannelGroup>ArrayList();
            
            while (reader.peek() != JsonToken.END_DOCUMENT)
            {
                ChannelGroup g = gson.fromJson(reader, ChannelGroup.class);
                groups.add(g);
                g.associateEventsWithGroup();
            }*/
            
            TestSequence tsq = gson.fromJson(reader, TestSequence.class);
            
            for(ChannelGroup g : tsq.groups)
                g.associateEventsWithGroup();
            
            this.sequenceName = tsq.sequenceName;
            this.sequenceDescription = tsq.sequenceDescription;
            this.groups = tsq.groups;
           
            
// TODO: updatePanelStates(); Will need to update GUI in calling method
            
        } catch (FileNotFoundException ex) {
            System.out.println("FileNotFound Error.");
        } catch (IOException ex) {
            System.out.println("Error reading file.");
        }
    }
    
//Component c is a reference for filedialog
    public void saveState(Component c)
    {
        sequenceName = "testSequenceNameHolder";
        sequenceDescription = "Placeholder for future description field";
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        
        String jsonString = "";
        
        Iterator i = groups.iterator();
        while(i.hasNext())
        {
            jsonString = jsonString + gson.toJson((ChannelGroup)i.next());
        }
        System.out.print(jsonString);
        
        
        jsonString = gson.toJson((TestSequence)this);
        System.out.print(jsonString);
        
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(c);
        FileDialog fd = new FileDialog(parent, "Save as...", FileDialog.SAVE);
        fd.setFilenameFilter(new FilenameFilter(){
            @Override
            public boolean accept(File dir, String name) {
                if(name.endsWith(".json"))
                    return true;
                else
                    return false;
            }
            
        });
        fd.setVisible(true);
        String filename = fd.getDirectory() + fd.getFile();
        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.write(jsonString);
            writer.close();
            
        } catch (IOException ex) {
            Logger.getLogger(DefinitionsPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    
    
    
    public void playSequence()
    {
        Thread t = new Thread(this);
        t.start();
        
    }
    
    @Override
    public void run() {
        
        //check that terminal is defined and connected. 
        if(!TerminalConnected()){        
            for (SequenceViewerInterface viewer : viewerList) 
                viewer.updateViewer(0, 0, Constant.PlayState.STOP);
            return;
        }
        
        //collapse all events into a single list of events
        LinkedList <Event> playList = new LinkedList();
        for (ChannelGroup g : groups)         
            for(Event e : g.timeline.events)            
                playList.add(e);
        
        if(playList.isEmpty())        
            return;
        
        Collections.sort(playList);

        //playlist built --run it:
        long milliCounter = 0;
        long prevMillis = System.currentTimeMillis();
        long currMillis, nextMillis;
        Constant.PlayState playState = Constant.PlayState.PLAY;
        
        Iterator playlistIterator = playList.iterator();
        Event nextEvent = (Event)playlistIterator.next();
        nextMillis = nextEvent.time.toMillis();
        
        while (playState != Constant.PlayState.STOP)
        {
            try 
            {
                //so the thread isn't constantly running.
                Thread.sleep(200);
             
                //update gui, check for a signal that sequence has been canceled. Implement this as an interface.
                for (SequenceViewerInterface viewer : viewerList)
                {
                    viewer.updateViewer((int)milliCounter, playList.getLast().time.toMillis(),playState);
                    if(viewer.requestedPlayState() != Constant.PlayState.NO_CHANGE_REQUESTED)
                        playState = viewer.requestedPlayState();
                }
                
                //update timer 
                if(playState == Constant.PlayState.PLAY)
                {
                    currMillis = System.currentTimeMillis();
                    milliCounter = milliCounter + (currMillis - prevMillis);
                    prevMillis = currMillis;
                    
                    //is it time for next event in queue? If so, process it.
                    //using while loop in case some events are concurrent.
                    while (milliCounter > nextMillis)
                    {
                        doEvent(nextEvent);

                        //increment nextEvent. If no nextEvent, stop the sequence.
                        if(playlistIterator.hasNext())
                        {
                            nextEvent = (Event)playlistIterator.next();
                            nextMillis = nextEvent.time.toMillis();
                        }
                        else
                        {
                            playState = Constant.PlayState.STOP;
                            for(SequenceViewerInterface viewer : viewerList)
                            {
                                viewer.updateViewer(0, playList.getLast().time.toMillis(), playState);
                            }
                            break;
                        }
                    }
                }
                else if (playState == Constant.PlayState.PAUSE)
                {
                    //don't update the milliCounter;
                    prevMillis = System.currentTimeMillis();
                }
            } 
            catch(InterruptedException ex)
            {
                System.out.println("Thread interrupted: sequence" + sequenceName);
            }
        }
        
        //let the viewers know we've stopped
        for (SequenceViewerInterface viewer : viewerList)
        {
            viewer.updateViewer((int)milliCounter, playList.getLast().time.toMillis(),playState);
        }
                
    }

    private void doEvent(Event nextEvent) {
            String commandString  = nextEvent.group.buildCommandString(nextEvent.command);

            terminal.sendCommand(commandString);
        }
    private boolean TerminalConnected()
    {
        if (terminal == null)
        {
            System.out.println("Terminal undefined. Exiting sequence: " + sequenceName);
            
            return false;
        }
        if (!terminal.isConnected())
        {
            System.out.println("Terminal is not connected. You can build a sequence, but you can't run it.");
            return false;
        }
        return true;
    }
            
}


