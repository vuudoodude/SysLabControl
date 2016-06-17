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
public interface SequenceViewerInterface {
    //allow running test sequence to communicate it's state to the viewer.
    public void updateViewer(int currentPosition, int sequenceLength, 
    Constant.PlayState playstate);
    
    public Constant.PlayState requestedPlayState();
            
}
