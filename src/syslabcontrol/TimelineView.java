package syslabcontrol;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.Iterator;




/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author USB_Room
 */
public class TimelineView extends javax.swing.JPanel implements SequenceViewerInterface{

    
    
    int viewStart, viewEnd;
    ChannelGroup group;
    Event highlightedEvent;
    Constant.PlayState playState;
    
    
    
    /**
     * Creates new form TimelineView
     */
    public TimelineView(ChannelGroup group) {
        
        this.group = group;
        
        initComponents();
        initPanel();  
        
        this.viewStart = 0;
        this.viewEnd = 500;
        currentPosition = 0;
        sequenceLength = 500;
        
        playState = Constant.PlayState.STOP;
    }

  
    //Constants for tweaking drawing behaviour
    
    private static final Color BACKGROUND_COLOR = Color.white;
    private static final double Y_PERCENT = 0.45;
    private static final int    BUBBLE_RADIUS = 7;
    private static final int    LINE_WIDTH  = 5; 
    
    //variables related to player behaviour
    private final Color playingColor = new Color(0xAAAEE3F5, true); //should be a bluish Alpha highlight
    private final Color pausedColor = new Color(0xAA688D99, true); //should be a darker bluish Alpha highlight
    private int currentPosition, sequenceLength; 
    
    
    
    @Override
    public void paintComponent(Graphics g)
    {
        
        Graphics2D g2d = (Graphics2D) g;
        
        paintBackground(g2d);
        
        Iterator i = group.timeline.iterator();
        while(i.hasNext())
        {
            paintEvent((Event)i.next(), g2d);
        }
        if(playState != Constant.PlayState.STOP)
        {
            paintPlayerPosition(g2d);
        }
        
        
    }
    public void paintBackground(Graphics2D g2d)
    {
        //paint background color;
        g2d.setPaint(BACKGROUND_COLOR);
        
        System.out.println("Painting background : (x,y)  (" + getX() + "," + getY() + ") -- (width, height) (" + getWidth() + "," + getHeight() + ")");
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        
        //paint Line
        g2d.setPaint(group.viewColor);
        g2d.setStroke(new BasicStroke(LINE_WIDTH));
        int y = (int)(getHeight() * Y_PERCENT);
        g2d.drawLine(0, y, getWidth(), y);
        
        //paint title
        g2d.setFont(new Font("default", Font.PLAIN, 16));
        g2d.setPaint(Color.black);
        g2d.drawString("Group: " + group.groupName, (int)(getWidth()*.02), (int)(getHeight()*.02 + g2d.getFontMetrics().getHeight()));
        g2d.setFont(new Font("default", Font.PLAIN, 11));
        
        
        //drawBorder (do this last)
        g2d.setPaint(Color.gray);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(0, 0, getWidth(), getHeight());
        
    }
    public void paintEvent(Event event, Graphics2D g2d)
    {
        //check that event is in bounds of viewport.
        if(event.time.millis < this.viewStart || event.time.millis > this.viewEnd)
            return;
        
        double tOffset = event.time.millis - viewStart;
        double fullView = viewEnd - viewStart;
        double ratio = tOffset / fullView;
        int fullX = getWidth() - getX();
        int x = (int)(ratio * fullX);
        
        
        int y = (int)((getHeight() * Y_PERCENT));
        
        System.out.println("painting Event at " + x + ", " + y);
        g2d.setPaint(BACKGROUND_COLOR);
        g2d.fill(new Ellipse2D.Double(x-BUBBLE_RADIUS,y-BUBBLE_RADIUS,
                                      2*BUBBLE_RADIUS,2*BUBBLE_RADIUS));
        
        g2d.setPaint(group.viewColor);
        g2d.draw(new Ellipse2D.Double(x-BUBBLE_RADIUS,y-BUBBLE_RADIUS,
                                      2*BUBBLE_RADIUS,2*BUBBLE_RADIUS));
        
        if (event == highlightedEvent)
        {
            int radius = 15; float dist[] = {0.0f,0.95f};
            Paint p = new RadialGradientPaint(new Point2D.Double((double)x,(double)y), //center point
                                              radius,dist, 
                                              new Color[] {new Color(0xFFFFFF00, true), new Color(0x30FFFF00,true)});
            
            g2d.setPaint(p);
            g2d.fill(new Ellipse2D.Double(x-3*BUBBLE_RADIUS,y-3*BUBBLE_RADIUS,
                                      6*BUBBLE_RADIUS,6*BUBBLE_RADIUS));
            
            
        }
        
        
        
        g2d.setPaint(Color.black);
        g2d.drawString(event.time.toString(), x,y + 2 * g2d.getFontMetrics().getHeight());
        g2d.drawString(event.title, x, y + g2d.getFontMetrics().getHeight());
    }
    
    private void paintPlayerPosition(Graphics2D g2d) {
        System.out.println("Painting player position " + this.currentPosition );
        if(currentPosition < this.viewStart)
            return;
        
        double tOffset = currentPosition - viewStart;
        double fullView = viewEnd - viewStart;
        double ratio = tOffset / fullView;
        int fullX = getWidth() - getX();
        int x = (int)(ratio * fullX);
        
        g2d.setPaint(Color.black);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawLine(x, 0, x, this.getHeight());
        
        if (playState == Constant.PlayState.PLAY)
            g2d.setPaint(playingColor);
        if (playState == Constant.PlayState.PAUSE)
            g2d.setPaint(pausedColor);
        g2d.fillRect(0, 0, x, getHeight());
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 995, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 490, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

 
    private void initPanel() {
        this.setPreferredSize(new Dimension(800,80));
    }
        
       

    //======================================================================
    //getters and setters
    //======================================================================
    
    public void setTimelineColor(Color timelineColor) {
        this.group.viewColor = timelineColor;
    }
    public Color getTimelineColor() {
        return group.viewColor;
    }

    @Override
    public void updateViewer(int currentPosition, int sequenceLength, Constant.PlayState p) {
        this.currentPosition = currentPosition;
        this.sequenceLength = sequenceLength;
        playState = p;
        repaint();
    }

    @Override
    public Constant.PlayState requestedPlayState() {
        //this component is a viewer, not a controller.
        return Constant.PlayState.NO_CHANGE_REQUESTED;
        
    }

  


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
