/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package syslabcontrol;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author USB_Room
 */


public class TimelinePlayer extends javax.swing.JPanel implements ComponentListener {

    
   
    /**
     * Creates new form TimelinePlayer
     */
    
    ArrayList <TimelineView> timelineViewers;
    int colorIndex = 0;
    private final SysLabControl parentFrame;
    
    public boolean playing, paused;
    
    Runnable playerThread;
    
    
    public TimelinePlayer(SysLabControl parentFrame) {
        
        this.parentFrame = parentFrame;
        timelineViewers = new <TimelineView> ArrayList();
        playing = paused = false; 
        
        
        initComponents();
        buildPanel();
        
        
    }
    
    //called when timeline view starts or when editing panel is closed. Shows multiple
    // timeline view.
    
    protected void buildPanel() {
        setName("Timeline");
        viewerPanel.removeAll();
        
        viewerPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor = GridBagConstraints.WEST; 
        
        //find lastEvent to setup viewEnd... need to check all timelines.
        //because timelines are sorted lists, can check just the last element of each.
        
        Iterator i = parentFrame.groups.iterator();
        
        int endView = 0;
        
        while(i.hasNext())
        {
            ChannelGroup g = (ChannelGroup)i.next();
            TimelineView view = new TimelineView(g);
            endView = Math.max(endView, g.getTimelineLengthInMillis());
            
            
            if(view.getTimelineColor() == null || view.getTimelineColor() == Color.black)
            {
                view.setTimelineColor(getNextColor());
            }
            
            
            view.addMouseListener(new MouseListener(){
                @Override
                public void mouseClicked(MouseEvent e) {
                   if(e.getClickCount() > 1 && !playing && !paused)
                   {
                       editTimeline(view);
                   }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                }

                @Override
                public void mouseExited(MouseEvent e) {
                }

            
            }); 
            
            
            timelineViewers.add(view);
            viewerPanel.add(view,c);
        }
        
        //setViewWidth for each TimnelineView
        endView = (int)(endView * 1.1);
        i = timelineViewers.iterator();
        while (i.hasNext())
        {
            ((TimelineView)i.next()).viewEnd = endView;                
        }
        this.resizePlayerWindow();
    }
   
    
    private void remove(TimelineView v)
    {
        timelineViewers.remove(v);
        viewerPanel.remove(v);
    }
    private Color getNextColor()
    {
        int x = (colorIndex ++) % 10;
        switch (x)
        {
            case 0:
                return new Color(0x9E213C,false); // burgundy
            case 1:
            return new Color(0x7A75D1,false); // dark lilac
            case 2:
                return new Color(0x82E0A3,false); //seafoam green
            case 3:
                return new Color(0xFF8861,false); //peachy orange
            case 4:
                return new Color(0xBF2222,false); //cherry red
            case 5:
                return new Color(0x22BFBF,false); //blue-green
            case 6:
                return new Color(0x71BF22,false); //green
            case 7:
                return new Color(0x7122BF,false); //violet
            case 8:
                return new Color(0x24355C,false); //navy 
            case 9:
                return new Color(0x13472A,false); //hunter green
            default:
                return Color.black;
                
        }
    }
 
    
    //called when timeline is doubleclicked. Shows editing window.
    
    private void editTimeline(TimelineView view) {
        viewerPanel.removeAll();
        
        
        viewerPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor = GridBagConstraints.NORTHWEST; 
        
        viewerPanel.add(view,c);
        
        c.anchor = GridBagConstraints.CENTER; 
        
        EventEditorPanel eep = new EventEditorPanel(view, this);
        viewerPanel.add(eep,c);
        
        validate();
        
        
        
        
        
    }
     private void updateViewers(int millis, boolean playing, boolean paused) {
        Iterator i = this.timelineViewers.iterator();
        {
            while(i.hasNext())
            {
                TimelineView tlv = (TimelineView) i.next();
                tlv.updatePlayingPosition(this.playing, this.paused, millis);
                tlv.repaint();
               
            }
        }
    }

    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        viewerPanel = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        saveButton = new javax.swing.JButton();
        loadButton = new javax.swing.JButton();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(250, 0), new java.awt.Dimension(250, 0), new java.awt.Dimension(250, 32767));
        playPauseButton = new javax.swing.JButton();
        stopButton = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createEtchedBorder());

        viewerPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout viewerPanelLayout = new javax.swing.GroupLayout(viewerPanel);
        viewerPanel.setLayout(viewerPanelLayout);
        viewerPanelLayout.setHorizontalGroup(
            viewerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 983, Short.MAX_VALUE)
        );
        viewerPanelLayout.setVerticalGroup(
            viewerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 482, Short.MAX_VALUE)
        );

        jToolBar1.setRollover(true);

        saveButton.setText("Save");
        saveButton.setFocusable(false);
        saveButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        saveButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(saveButton);

        loadButton.setText("Load");
        loadButton.setFocusable(false);
        loadButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        loadButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        loadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(loadButton);
        jToolBar1.add(filler1);

        playPauseButton.setText("Play");
        playPauseButton.setFocusable(false);
        playPauseButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        playPauseButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        playPauseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playPauseButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(playPauseButton);

        stopButton.setText("Stop");
        stopButton.setFocusable(false);
        stopButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        stopButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(stopButton);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(viewerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(viewerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        parentFrame.saveState();

    }//GEN-LAST:event_saveButtonActionPerformed

    private void loadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadButtonActionPerformed
        parentFrame.loadState();

    }//GEN-LAST:event_loadButtonActionPerformed

    private void playPauseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playPauseButtonActionPerformed
        
        if(!playing && !paused)
        {
            
            //collapse all timelineevents into a single list of events
            LinkedList <Event> playList = new LinkedList();
            Iterator i = parentFrame.groups.iterator();
            while (i.hasNext())
            {
                ChannelGroup g = (ChannelGroup)i.next();
                Iterator eventIterator = g.timeline.iterator();
                while(eventIterator.hasNext())
                {
                    playList.add((Event)eventIterator.next());
                }

            }
            if(playList.isEmpty())
            {
                return;
            }
            Collections.sort(playList);
            
            //instantiate new thread if previously stopped.
            
            playerThread = new PlayerRunnable(playList, this, parentFrame.getTerminal());
            
            //playerThread = new Thread(new PlayerThread(playList, this, parentFrame.getTerminal()));
            new Thread(playerThread).start();
        
        }
        if(playing)
        {
            playing = false;
            paused = true;
            
            playPauseButton.setText("Play");
            
            
        }else
        {
            playing = true;
            paused = false;
            
            playPauseButton.setText("Pause");   
        } 
        
    }//GEN-LAST:event_playPauseButtonActionPerformed

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopButtonActionPerformed
        playing = false;
        paused = false;
        
        playPauseButton.setText("Play");
    }//GEN-LAST:event_stopButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JButton loadButton;
    private javax.swing.JButton playPauseButton;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton stopButton;
    private javax.swing.JPanel viewerPanel;
    // End of variables declaration//GEN-END:variables
 


//ComponentListener Interface... for when the window is resized, etc.
   
    @Override
    public void componentResized(ComponentEvent e) {
        resizePlayerWindow();
        getLayout().layoutContainer(viewerPanel);
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        componentResized(e);
    }

    @Override
    public void componentShown(ComponentEvent e) {
        componentResized(e);
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        componentResized(e);
    }
    private void resizePlayerWindow()
    {
        this.jToolBar1.setPreferredSize(new Dimension(this.getWidth(),jToolBar1.getHeight()));
        Iterator i = timelineViewers.iterator();
        while (i.hasNext())
        {
            TimelineView tlv = (TimelineView)i.next();
            tlv.setPreferredSize(new Dimension(this.getWidth(), tlv.getPreferredSize().height));
        }
    }

   

   
    
     protected class PlayerRunnable implements Runnable{
         boolean repeat;
         boolean playing;
         boolean paused;
         boolean stop;
         int playPosition;
         TimelinePlayer player;
         Terminal terminal;
         LinkedList <Event> playList;
         
         protected PlayerRunnable(LinkedList playList, TimelinePlayer player, Terminal terminal)
         {
             repeat = false;
             stop = false;
             playing = false;
             paused = true; //starts paused.
             playPosition = 0;
             this.player = player;
             this.terminal = terminal;
             this.playList = playList;
             
         }
         public void updateState()
         {
             playing = player.playing;
             paused = player.paused;
             stop = (!playing && ! paused);
             
         }
        

        //Every 500 msec, wake up, check to see if it's time to do the next event.
        //if so, send the relevant data through the terminal.
        //either way, update the view and go back to sleep.

        @Override
        public void run() {
            
            //how long have we been running? Using this method instead of absolute time allows us to pause.
            long milliAccumulator = 0;
            
            long prevMillis = System.currentTimeMillis();
            long currMillis;
            
            Iterator playlistIterator = playList.iterator();
            Event nextEvent = (Event)playlistIterator.next();
            
            long nextMillis = (long) nextEvent.time.toMillis();
            
            
            while(!stop)
            {   
                try {
                    updateState();
                    //wake thread every second.
                    Thread.sleep(200);
                    if(playing)
                    {
                        //accumulate time on the counter
                        currMillis = System.currentTimeMillis();
                        milliAccumulator = milliAccumulator + (currMillis - prevMillis);
                        prevMillis = currMillis;
                        
                        player.updateViewers((int)milliAccumulator, playing, paused);
                        
                        
                        //is it time for next Event in queue?
                        //while loop ensures that if several events have the same time stamp,
                        //they all get handled together.
                        while (milliAccumulator > nextMillis)
                        {
                            doEvent(nextEvent);
                            
                            if (playlistIterator.hasNext())
                            {
                                
                                nextEvent = (Event)playlistIterator.next();
                                nextMillis = nextEvent.time.toMillis();
                            }
                            else
                            {
                                //end playing, clean up
                                
                                player.playing = false;
                                player.paused = false;
                                player.playPauseButton.setText("Play");
                                updateState();
                                player.updateViewers((int)milliAccumulator, playing, paused);
                                break;
                            }
                        }
                    }
                    if(paused)
                    {
                        //increment current time without adding to the accumulator
                        currMillis = System.currentTimeMillis();
                        prevMillis = currMillis;
                    }
                    
                } catch (InterruptedException ex) {
                    Logger.getLogger(TimelinePlayer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        private void doEvent(Event nextEvent) {
            String commandString  = nextEvent.group.buildCommandString(nextEvent.command);
            
            terminal.sendCommand(commandString);
        }

        
    }
    
}
