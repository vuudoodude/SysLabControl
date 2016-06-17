/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package syslabcontrol;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;


/**
 *
 * @author USB_Room
 */


public class TimelinePlayer extends javax.swing.JPanel implements ComponentListener, SequenceViewerInterface {

    
   
    /**
     * Creates new form TimelinePlayer
     */
    
    ArrayList <TimelineView> timelineViewers;
    int colorIndex = 0;
    private final SequenceEditor parentFrame;
    
    Runnable playerThread;
    private Constant.PlayState playState;   
    private Constant.PlayState playStateRequest;
    
    ImageIcon playIcon, pauseIcon, stopIcon;
    
    public TimelinePlayer(SequenceEditor parentFrame) {
        
        this.parentFrame = parentFrame;
        timelineViewers = new <TimelineView> ArrayList();
        playState = Constant.PlayState.STOP;
        playStateRequest = Constant.PlayState.NO_CHANGE_REQUESTED;
        
        
        initComponents();
        buildPanel();
        setIcons();
        
        
    }
    
    //called when timeline view starts or when editing panel is closed. Shows multiple
    // timeline view.
    
    protected void buildPanel() {
        setName("Event Editor");
        viewerPanel.removeAll();
        
        viewerPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor = GridBagConstraints.WEST; 
        
        //find lastEvent to setup viewEnd... need to check all timelines.
        //because timelines are sorted lists, can check just the last element of each.
        
        Iterator i = parentFrame.sequence.groups.iterator();
        
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
                   if(e.getClickCount() > 1 && playState == Constant.PlayState.STOP)
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
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(60, 0), new java.awt.Dimension(60, 0), new java.awt.Dimension(60, 32767));
        playButton = new javax.swing.JButton();
        pauseButton = new javax.swing.JButton();
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
        jToolBar1.add(filler2);

        playButton.setBorderPainted(false);
        playButton.setContentAreaFilled(false);
        playButton.setFocusable(false);
        playButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        playButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        playButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(playButton);

        pauseButton.setBorderPainted(false);
        pauseButton.setContentAreaFilled(false);
        pauseButton.setFocusable(false);
        pauseButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        pauseButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        pauseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(pauseButton);

        stopButton.setBorderPainted(false);
        stopButton.setContentAreaFilled(false);
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
        parentFrame.sequence.saveState(this);

    }//GEN-LAST:event_saveButtonActionPerformed

    private void loadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadButtonActionPerformed
        parentFrame.sequence.loadState(this);
        parentFrame.updatePanelStates();

    }//GEN-LAST:event_loadButtonActionPerformed

    private void playButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playButtonActionPerformed
        if(null != playState)
        
        switch (playState) {
        //if we're presently paused, play sequence
            case PAUSE:
                playStateRequest = Constant.PlayState.PLAY;
                
                break;
        //if we're presently stopped, create new sequence and run it, set button to "play".
            case STOP:
                playStateRequest = Constant.PlayState.PLAY;
                
                runTestSequence();
                break;
            default:
                break;
        }   
    }//GEN-LAST:event_playButtonActionPerformed

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopButtonActionPerformed
        playStateRequest = Constant.PlayState.STOP;
        
    }//GEN-LAST:event_stopButtonActionPerformed

    private void pauseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pauseButtonActionPerformed
        playStateRequest = Constant.PlayState.PAUSE;
    }//GEN-LAST:event_pauseButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JButton loadButton;
    private javax.swing.JButton pauseButton;
    private javax.swing.JButton playButton;
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

    @Override
    public void updateViewer(int currentPosition, int sequenceLength, Constant.PlayState p) {
        //handle stop message first, highest priority.
        if (p == Constant.PlayState.STOP)
        {
            playState = Constant.PlayState.STOP;
            playStateRequest = Constant.PlayState.NO_CHANGE_REQUESTED;
            return;
        }

        //once request has been granted, reset request token
        if (playStateRequest == Constant.PlayState.NO_CHANGE_REQUESTED)
            return;
        if ( playStateRequest == p)
        {
            playState = p;
            playStateRequest = Constant.PlayState.NO_CHANGE_REQUESTED;
            return;
        }
        
    }
    
    @Override
    public Constant.PlayState requestedPlayState() {
        return playStateRequest;
    }

    private void runTestSequence() {
        TestSequence seq = parentFrame.sequence;
        
        seq.addViewer(this);
        for(TimelineView v : timelineViewers)
        {
            seq.addViewer(v);
        }
        seq.setTerminal(parentFrame.terminal);
        seq.playSequence();
    }
    
    private void setIcons() 
    {
        int iconSize = 24;
        try {
            BufferedImage img = null;
            URL url = getClass().getResource("pause_play_stop_black.png");
            img = ImageIO.read(new File(url.getPath()));
            
            Image pauseImage = ((img.getSubimage(0, 0, img.getWidth()/3, img.getHeight()))
                                        .getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH));
            Image playImage = ((img.getSubimage(1 * img.getWidth() / 3, 0, img.getWidth()/3, img.getHeight()))
                                        .getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH));
            Image stopImage = ((img.getSubimage(2 * img.getWidth() / 3, 0, img.getWidth()/3, img.getHeight()))
                                        .getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH));
            
            playButton.setIcon(new ImageIcon(playImage));
            pauseButton.setIcon(new ImageIcon(pauseImage));
            stopButton.setIcon(new ImageIcon(stopImage));
            
            
            
            
            int radius = (int) img.getHeight()/2;
            float dist[] = {0.0f, 0.95f};
            
            
            Graphics2D g2d = (Graphics2D) img.getGraphics();
            g2d.setPaint(new Color(0x4000030F, true));
            g2d.fill(new Rectangle2D.Double(0,0,(double)img.getWidth(), img.getHeight()));
            
            
            Image r_pauseImage = ((img.getSubimage(0, 0, img.getWidth()/3, img.getHeight()))
                                        .getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH));
            Image r_playImage = ((img.getSubimage(1 * img.getWidth() / 3, 0, img.getWidth()/3, img.getHeight()))
                                        .getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH));
            Image r_stopImage = ((img.getSubimage(2 * img.getWidth() / 3, 0, img.getWidth()/3, img.getHeight()))
                                        .getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH));
            
            playButton.setRolloverIcon(new ImageIcon(r_playImage));
            pauseButton.setRolloverIcon(new ImageIcon(r_pauseImage));
            stopButton.setRolloverIcon(new ImageIcon(r_stopImage));
            
            
            
            
            
            /*
            int radius = 15; float dist[] = {0.0f,0.95f};
            Paint p = new RadialGradientPaint(new Point2D.Double((double)x,(double)y), //center point
                                              radius,dist, 
                                              new Color[] {new Color(0xFFFFFF00, true), new Color(0x30FFFF00,true)});
            
            g2d.setPaint(p);
            g2d.fill(new Ellipse2D.Double(x-3*BUBBLE_RADIUS,y-3*BUBBLE_RADIUS,
                                      6*BUBBLE_RADIUS,6*BUBBLE_RADIUS));
            */
            
            
            
            
            //return new ImageIcon(img.getScaledInstance(10, 10, Image.SCALE_SMOOTH));
        } catch (IOException ex) {
            System.out.println("Could not find icon file for pause / play / stop buttons");
            return;
        }
    }
    

}
