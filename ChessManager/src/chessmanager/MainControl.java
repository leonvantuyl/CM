/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chessmanager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.ArrayList;

/**
 *
 * @author Leon van Tuijl
 */
public class MainControl implements java.awt.event.ActionListener {
    
    private MainWindow view;
    private FileIO IO;
    private BoardContainer model;
    
    public MainControl() {
        IO = new FileIO(this);
        
        view = new MainWindow(IO);
        view.show();
        
    }
    
    public void newContainer(ArrayList<String> maparray) {
        model = new BoardContainer();
        view.setModel(model);
        model.addObserver(view);        
        model.fillContainer(maparray);
        view.addscrollListener(new ScrollListener());
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    class ScrollListener implements AdjustmentListener {

        @Override
        public void adjustmentValueChanged(AdjustmentEvent ae) {
            model.getBoard(view.getScrollValue());
        }
    }
}
