package syslabcontrol;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author USB_Room
 */
public class ChannelPanel extends javax.swing.JPanel implements ActionListener {

    /**
     * Creates new form ChannelPanel
     */
    ChannelGroup group;
    Terminal terminal;
    
    JMenuItem addChannel = new JMenuItem("Add Channel");
    JMenuItem remove = new JMenuItem("Remove Selection");
    JMenuItem moveToDifferentGroup= new JMenuItem("Move to Group");
    
    public ChannelPanel(ChannelGroup group, Terminal terminal) {
        this.group = group;
        this.terminal = terminal;
        
        initComponents();
        initRelayControl();
        
        initTable();
    }
    private void initTable() {
        ChannelPanelTableModel model = new ChannelPanelTableModel(group);
        jTable1.setModel(model);
        
        JPopupMenu popup = new JPopupMenu();
        jTable1.setComponentPopupMenu(popup);
        
        addChannel.addActionListener(this);
        remove.addActionListener(this);
        moveToDifferentGroup.addActionListener(this);
        
        popup.add(addChannel);
        popup.add(remove);
        popup.add(moveToDifferentGroup);
        
    }
    
    public void initRelayControl()
    {
        Simple8bitRelayControlPanel relayControl = new Simple8bitRelayControlPanel();
        relayControl.setSize(relayControl.getPreferredSize());
        relayControlPanel.add(relayControl);
        relayControl.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent evt)
    {
        JMenuItem selection = (JMenuItem)evt.getSource();
        if (selection == addChannel)
        {
            addEmptyChannel();
        }
        if (selection == remove)
        {
            int rows[] = jTable1.getSelectedRows();
            Channel[] rem_Chan = new Channel[rows.length];
            for(int i = 0; i < rows.length; i++)
            {
                rem_Chan[i] = group.channels.get(rows[i]);
            }
            for(int i = 0; i < rem_Chan.length; i ++)
            {
                group.channels.remove(rem_Chan[i]);
            }
            
            syncTable();
        }
        
        if (selection == moveToDifferentGroup)
        {
            ArrayList a = ((DefinitionsPanel)getParent().getParent()).parentFrame.sequence.groups;
            String s = "";
            Iterator i = a.iterator();
            while (i.hasNext())
            {
                s = s + ((ChannelGroup)i.next()).groupName + "~";
            }
            String[] options = s.split("~");
            String returnedString = (String) JOptionPane.showInputDialog(this, "Which group to move to?", "Confirm Move...", JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            
            ChannelGroup target = null;
            
            i=a.iterator();
            while(i.hasNext())
            {
                ChannelGroup g = (ChannelGroup) i.next();
                if (g.groupName.equals(returnedString))
                    target = g;
            }
            if (target == null)
            {   
                System.out.println("Error in Move Method");
                return;
            }
            int rows[] = jTable1.getSelectedRows();
            Channel[] mov_Chan = new Channel[rows.length];
            for(int x = 0; x < rows.length; x++)
            {
                mov_Chan[x] = group.channels.get(rows[x]);
            }
            for(int x = 0; x < mov_Chan.length; x++)
            {
                target.add(mov_Chan[x]);
                group.channels.remove(mov_Chan[x]);
            }
            ((DefinitionsPanel)(this.getParent().getParent())).syncAllTables();
            
        }
    }
    public void addEmptyChannel()
    {
        group.add(new Channel("","",""));
        String[] s = {"","","",group.groupName};
        syncTable();
               
    }
     public void renameGroup(String s)
    {
        group.groupName = s;
        
        Iterator i = group.channels.iterator();
        while(i.hasNext())
        {
            ((Channel)i.next()).group = s;
        }
        syncTable();        
        
    }
     public void syncTable()
     {
         ((ChannelPanelTableModel)jTable1.getModel()).fireTableModelChanged();
     }
   

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        commandButton = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        relayControlPanel = new javax.swing.JPanel();

        commandButton.setText("Send Command");
        commandButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                commandButtonActionPerformed(evt);
            }
        });

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField1KeyTyped(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout relayControlPanelLayout = new javax.swing.GroupLayout(relayControlPanel);
        relayControlPanel.setLayout(relayControlPanelLayout);
        relayControlPanelLayout.setHorizontalGroup(
            relayControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        relayControlPanelLayout.setVerticalGroup(
            relayControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 55, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(relayControlPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(commandButton))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 761, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 508, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(commandButton)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 16, 16))
                    .addComponent(relayControlPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void commandButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_commandButtonActionPerformed
        
        int[] channels = jTable1.getSelectedRows();
        String command = jTextField1.getText().trim();
        if(command.isEmpty() || command.equalsIgnoreCase(""))
        {
            command = ((Simple8bitRelayControlPanel)relayControlPanel.getComponent(0)).getCommand();
        }
        
        /*String sb = new String("");
        
        for(int i = 0; i < channels.length; i++)
        {
            sb = sb + jTable1.getModel().getValueAt(channels[i], 1) + command;
        }*/
        terminal.sendCommand(group.buildCommandString(command));
        
        
    }//GEN-LAST:event_commandButtonActionPerformed

    private void jTextField1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyTyped
        int keyCode = evt.getKeyChar();
        switch (keyCode) {
            case KeyEvent.VK_ENTER:
                commandButtonActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
                jTextField1.setText("");
                break;
         
        }
    }//GEN-LAST:event_jTextField1KeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton commandButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JPanel relayControlPanel;
    // End of variables declaration//GEN-END:variables

    void removeGroup() {
        group = null;
    }
    
    private class ChannelPanelTableModel implements TableModel
    {

        String[] COLUMN_NAMES = { "Name", "Address", "Type", "Group"};
        boolean[] editable =    { true,    true,     true,    false  };
        ChannelGroup group;
        LinkedList<TableModelListener> listeners;

        public ChannelPanelTableModel(ChannelGroup group)
        {
            this.group = group;
            listeners = new LinkedList();
        }
        
        @Override
        public int getRowCount() {
            return group.channels.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMN_NAMES.length;
        }

        @Override
        public String getColumnName(int columnIndex) {
            return COLUMN_NAMES[columnIndex];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return editable[columnIndex];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if(columnIndex == 0) //Name
            {
                return ((Channel)group.channels.get(rowIndex)).channelName;
            }
            if(columnIndex == 1) //Address
            {
                return ((Channel)group.channels.get(rowIndex)).address;
            }
            if(columnIndex == 2) //Type
            {
                return ((Channel)group.channels.get(rowIndex)).type;
            }
            if(columnIndex == 3) //Group
            {
                return group.groupName;
            }
            return "ERROR";
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if(columnIndex == 0) //Name
            {
                ((Channel)group.channels.get(rowIndex)).setName((String)aValue);
            }
            if(columnIndex == 1) //Address
            {
                 ((Channel)group.channels.get(rowIndex)).setAddress((String)aValue);
            }
            if(columnIndex == 2) //Type
            {
                ((Channel)group.channels.get(rowIndex)).setType((String)aValue);
            }
            //group is not editable through this method.
        }

        @Override
        public void addTableModelListener(TableModelListener l) {
            listeners.add(l);   
        }

        @Override
        public void removeTableModelListener(TableModelListener l) {
            listeners.remove(l);
        }
        public void fireTableModelChanged()
        {
            Iterator i = listeners.iterator();
            while (i.hasNext())
            ((TableModelListener)i.next()).tableChanged(new TableModelEvent (this));
        }
    }
}

