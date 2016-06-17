/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package syslabcontrol;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.OutputStream;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;

/**
 *
 * @author woodw
 */
public class Terminal extends javax.swing.JPanel {
    
    
    OutputStream textOut; // from serial device to terminal display;
    ArrayList commandQueue;
    int commandQueueCounter;
    
    boolean reply;
    String replyString;
    
    TerminalComm terminalComm;
    List bursts;
    
    

    /**
     * Creates new form Terminal
     */
    public Terminal() {
        initComponents();
        initializePromptBehaviour();
        
        
        
        byte[] inBuffer= new byte[2048];     
        textOut = new terminalOutputStream(terminalTextArea);
        terminalComm = new TerminalComm(textOut);
        try {
            if (!SysLabControl.DO_NOT_CONNECT_COM_PORT)
                terminalComm.Connect("COM3");
        } catch (Exception ex) {
            Logger.getLogger(Terminal.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        reply = false;
        replyString = "";
        
        bursts = Collections.synchronizedList(new ArrayList<String>());
        
        new Thread(){
            public void run()
            {
                
                while(true)
                {
                    
                    try {
                        Thread.sleep(250);
                        
                        Iterator i = bursts.iterator();
                     
                        if(i.hasNext()){
                            String b = (String)i.next();
                            sendBurst(b);
                            bursts.remove(b);
                            Thread.sleep(1000);
                        }
                        
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Terminal.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
            }    
            
        }.start();
        
        
    }
    
    
    private void initializePromptBehaviour() {
        commandQueue = new ArrayList();
        commandQueueCounter = 0;
    }
    
    
    public class terminalOutputStream extends OutputStream
    {
        private final JTextArea terminal;
        
        public terminalOutputStream(JTextArea terminal)
        {
            this.terminal = terminal;            
        }      

        @Override
        public void write(int b) throws IOException 
        {
            
            if(reply)
            {
                terminal.append("\t");
                reply = false;
                
                replyString = "";
            }
            
            replyString = replyString + (char)b;
            
            terminal.append("" + (char)b);
            terminal.setCaretPosition(terminal.getDocument().getLength());
        }
    }
    public void sendCommand(String s)
    {
         splitAndAddToBurstQueue(s);
        
    }
    private void splitAndAddToBurstQueue(String s)
    {
        
        while(s.length() > 64)
        {
            //find the end of the last instruction in the burst -- don't split the instruction
            int lastIndexOfBurst = 63;
            for(int i = 60; i<64; i++)
            {
                if (s.charAt(i) == 'A' || 
                    s.charAt(i) == 'B' ||
                    s.charAt(i) == 'C' ||
                    s.charAt(i) == 'D' )
                {
                    lastIndexOfBurst = i;
                    break;
                }
            }
            bursts.add(s.substring(0, lastIndexOfBurst));
            s = s.substring(lastIndexOfBurst, s.length());
        }
        bursts.add(s);
        
    }
       
        
    
    private void sendBurst(String burst)
    {
        terminalComm.Write(burst.getBytes());
        terminalTextArea.append("\n" + burst + "\n");
        reply = true;
        
    }
            
            
    public String getLastReply()
    {
        return replyString;
    }
    
    public boolean isConnected()
    {
        return terminalComm.isConnected();
    }
   public void connect()
   {
       try {
            terminalComm.Connect("COM3");
        } catch (Exception ex) {
            Logger.getLogger(Terminal.class.getName()).log(Level.SEVERE, null, ex);
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

        promptButton = new javax.swing.JButton();
        terminalPrompt = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        terminalTextArea = new javax.swing.JTextArea();
        jCheckBox1 = new javax.swing.JCheckBox();
        connectButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setName("Terminal"); // NOI18N

        promptButton.setText("SEND");
        promptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                promptButtonActionPerformed(evt);
            }
        });

        terminalPrompt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                terminalPromptActionPerformed(evt);
            }
        });
        terminalPrompt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                terminalPromptKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                terminalPromptKeyTyped(evt);
            }
        });

        terminalTextArea.setColumns(20);
        terminalTextArea.setFont(new java.awt.Font("Miriam Fixed", 0, 13)); // NOI18N
        terminalTextArea.setRows(5);
        jScrollPane2.setViewportView(terminalTextArea);

        jCheckBox1.setSelected(true);
        jCheckBox1.setText("Autoscroll Terminal");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        connectButton.setText("Connect");
        connectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("Connect on COM3");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(terminalPrompt))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jCheckBox1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 552, Short.MAX_VALUE)
                        .addComponent(jLabel1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(promptButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(connectButton, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(connectButton)
                        .addComponent(jLabel1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(terminalPrompt, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(promptButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void promptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_promptButtonActionPerformed
        String s =  terminalPrompt.getText();
        commandQueue.add(s);         
        if(commandQueue.size() > 20)
        {
            commandQueue.remove(0);
        }   
        commandQueueCounter = commandQueue.size() - 1;
        s=s.trim();
        
        terminalPrompt.setText("");
        
        sendCommand(s);
    }//GEN-LAST:event_promptButtonActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        terminalTextArea.setAutoscrolls(jCheckBox1.isSelected());
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void terminalPromptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_terminalPromptActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_terminalPromptActionPerformed

    private void connectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectButtonActionPerformed
        connect();
        if(terminalComm.isConnected())
        {
            this.jLabel1.setText("CONNECTED on COM3");
        }
        
    }//GEN-LAST:event_connectButtonActionPerformed

    private void terminalPromptKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_terminalPromptKeyTyped
        int keyCode = evt.getKeyChar();
        switch (keyCode) {
            case KeyEvent.VK_ENTER:
                this.promptButtonActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
                break;
         
        }
        
       
        
    }//GEN-LAST:event_terminalPromptKeyTyped

    private void terminalPromptKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_terminalPromptKeyPressed
        int keyCode = evt.getKeyCode();
        switch (keyCode) {            
            case KeyEvent.VK_UP:
                this.terminalPrompt.setText((String) commandQueue.get(commandQueueCounter));
                commandQueueCounter --;
                if(commandQueueCounter < 0)
                    commandQueueCounter = 0;
                break;
            case KeyEvent.VK_DOWN:
                this.terminalPrompt.setText((String) commandQueue.get(commandQueueCounter));
                commandQueueCounter ++;
                if(commandQueueCounter >= commandQueue.size())
                    commandQueueCounter = commandQueue.size() - 1;
                break;
                
            default:
                break;
        }
    }//GEN-LAST:event_terminalPromptKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton connectButton;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton promptButton;
    private javax.swing.JTextField terminalPrompt;
    private javax.swing.JTextArea terminalTextArea;
    // End of variables declaration//GEN-END:variables
}
