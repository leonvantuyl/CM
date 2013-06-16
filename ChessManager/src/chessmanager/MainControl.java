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
import javax.swing.JOptionPane;

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
        view.setVisible(true);
        emptyContainer();
    }

    public void emptyContainer() {
        model = new BoardContainer();
        connect();
        model.emptyContainer("Untitled");
    }

    public void newContainer(ArrayList<String> maparray, String name) {
        model = new BoardContainer();
        connect();
        model.fillContainer(maparray, name);
    }

    public void connect() {
        view.setModel(model);
        model.addObserver(view);
        view.addscrollListener(new ScrollListener());
        view.addBeginsetup(new normalSetupListener());
        view.addEmptyfieldlistener(new EmptyListener());
        view.addCodeListener(new codeListener());
        view.addNameListener(new nameListener());
        view.addDeleteDiagramListener(new deleteDiagramListener());
        view.addNewDiagramListener(new addDiagramListener());
        view.addNewContainerListener(new newContainerListener());
        view.addRevertListener(new revertListener());
        view.addSaveAsListener(new saveAsListener());
        view.addSaveListener(new saveListener());
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

    class EmptyListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            model.changeCode("8/8/8/8/8/8/8/8");
        }
    }

    class normalSetupListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            model.changeCode("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
        }
    }

    class codeListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            model.changeCode(view.getBoardCode());
        }
    }

    class nameListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            model.changeName(view.getBoardName());
        }
    }

    class revertListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            model.revert();
        }
    }

    class saveListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            IO.save(model.getAllBoards(), model.getContainerName(), "");
            model.setChangedContainer(false);
        }
    }

    class saveAsListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            Boolean saved = IO.saveAs(model.getAllBoards(), model.getContainerName());
            if (saved) {
                model.setContainerName(IO.getLastSaved());
                model.setChangedContainer(false);
            }
        }
    }

    class addDiagramListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            model.insertBoard(false);
        }
    }

    class deleteDiagramListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            model.removeBoard();
        }
    }

    class newContainerListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (model.isChanged()) {
                int dialogButton = JOptionPane.YES_NO_OPTION;
                int dialogResult = JOptionPane.showConfirmDialog(null, "There are unsaved changes, are you sure?", "New file", dialogButton);
                if (dialogResult == 0) {
                    emptyContainer();
                } else {
                    System.out.println("Cancel");
                }
            } else {
                emptyContainer();
            }
        }
    }
}
