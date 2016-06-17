package syslabcontrol;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.GridLayout;
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
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

/*
|* RXTX binary builds provided as a courtesy of Mfizz Inc. (http://mfizz.com/).
|* Please see http://mfizz.com/oss/rxtx-for-java for more information.
|*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author woodw
 */
public class SysLabControl extends javax.swing.JFrame {

    Terminal terminal;
    TimelinePlayer player;
    DefinitionsPanel defPanel;
    
    public static boolean DO_NOT_CONNECT_COM_PORT = true;
    
    /**
     * Creates new form MainWindow
     */
    
    ChannelGroup testGroup, testGroup2;
    ArrayList <ChannelGroup> groups;
    
    public SysLabControl() {
        //initVariables();
        groups = new ArrayList();
        initComponents();
        initializeComponents();
    }
    private void initializeComponents() {
        initJTabbedPane();
    }

    private void initJTabbedPane() {
        ArrayList paneList = new ArrayList();
        
        
        terminal = new Terminal();
        defPanel = new DefinitionsPanel(this);
        
        player = new TimelinePlayer(this);
        this.addComponentListener(player);
        
        
        paneList.add(player);
        paneList.add(defPanel);
        paneList.add(terminal);
        
        

        Iterator <JPanel> iter = paneList.iterator();
        while (iter.hasNext()){
            JPanel panel = iter.next();
            jTabbedPane1.addTab(panel.getName(), panel);
        }
    }
    private void initVariables() {
        groups = new ArrayList();
        
        
        
        this.testGroup = new ChannelGroup("A20 / A21");
        
        testGroup.add(new Channel("A20", "A08B00C20", "8bit Relay Controller"));
        testGroup.add(new Channel("A21", "A08B00C21", "8bit Relay Controller"));
       
        
       Event e;
       e = new Event(testGroup, "Relays OFF","Dff", "Turns Relays Off");
       e.time.setTime(0, 0, 0);
       testGroup.timeline.add(e);
       
       e = new Event(testGroup, "Relays ON","D00", "Turns All Relays ON");
       e.time.setTime(0, 0, 15);
       testGroup.timeline.add(e);
       
       e = new Event(testGroup, "Odd Relays ON","D55", "Turns Odd Relays ON");
       e.time.setTime(0, 0, 30);
       testGroup.timeline.add(e);
       
       e = new Event(testGroup, "Even Relays ON","Daa", "Turns Even Relays ON");
       e.time.setTime(0, 0, 45);
       testGroup.timeline.add(e);
       
       
       this.testGroup2 = new ChannelGroup("A22 / A23");
        
        testGroup2.add(new Channel("A22", "A08B00C22", "8bit Relay Controller"));
        testGroup2.add(new Channel("A23", "A08B00C23", "8bit Relay Controller"));
        
       
       e = new Event(testGroup2, "Relays OFF","Dff", "Turns Relays Off");
       e.time.setTime(0, 0, 5);
       testGroup2.timeline.add(e);
       
       e = new Event(testGroup2, "Relays ON","D00", "Turns All Relays ON");
       e.time.setTime(0, 0, 10);
       testGroup2.timeline.add(e);
       
       e = new Event(testGroup2, "Odd Relays ON","D55", "Turns Odd Relays ON");
       e.time.setTime(0, 0, 50);
       testGroup2.timeline.add(e);
       
       e = new Event(testGroup2, "Even Relays ON","Daa", "Turns Even Relays ON");
       e.time.setTime(0, 0, 65);
       testGroup2.timeline.add(e);
       
       groups = new <ChannelGroup> ArrayList();
       groups.add(testGroup);
       groups.add(testGroup2);
       
       
        
        
    }
    
    public void loadState()
    {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
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
            
            groups = new ArrayList();
            
            while (reader.peek() != JsonToken.END_DOCUMENT)
            {
                ChannelGroup g = gson.fromJson(reader, ChannelGroup.class);
                groups.add(g);
                g.associateEventsWithGroup();
            }
            
            updatePanelStates();
            
        } catch (FileNotFoundException ex) {
            System.out.println("FileNotFound Error.");
        } catch (IOException ex) {
            System.out.println("Error reading file.");
        }
    }
    public void saveState()
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        
        String jsonString = "";
        
        Iterator i = groups.iterator();
        while(i.hasNext())
        {
            jsonString = jsonString + gson.toJson((ChannelGroup)i.next());
        }
        
        
        System.out.print(jsonString);
        
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
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

 

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();

        jToolBar1.setRollover(true);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 23, Short.MAX_VALUE)
        );

        jTabbedPane1.setMinimumSize(new java.awt.Dimension(800, 600));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 675, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SysLabControl.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SysLabControl.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SysLabControl.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SysLabControl.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SysLabControl().setVisible(true);
                
            }

            
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables

    Terminal getTerminal() {
        return terminal;
    }

    //called on load or save... make sure everything's pointing at the same instance
    //of groups
    
    protected void updatePanelStates() {
        defPanel.buildPanel();
        player.buildPanel();
    }

    
}


