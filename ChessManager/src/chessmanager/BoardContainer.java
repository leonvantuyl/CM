/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chessmanager;

import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Leon van Tuijl
 */
public class BoardContainer extends java.util.Observable {

    private ArrayList<String> mapArray;
    private BoardModel currentBoard;
    private BoardModel start;
    private int totalnumber;
    private String containerName;
    private String fullcontName;
    private Boolean changed;

    
    public void setChangedContainer(Boolean changed) {
        this.changed = changed;
        setChanged();
        notifyObservers();
    }

    public BoardContainer() {
        start = new BoardModel();
        start.setNumber(0);
        currentBoard = start;
        totalnumber = 0;
        changed = false;
    }

    /*
     * !!!Hidden getters and setters
     */
    // <editor-fold>
    public String getFullcontName() {
        return fullcontName;
    }

    public void updateFullcontName() {
        this.fullcontName = containerName + ", Diagram " + currentBoard.getNumber() + " of " + totalnumber;
    }

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
        updateFullcontName();
    }

    public BoardModel getCurrentBoard() {
        return currentBoard;
    }

    public void setCurrentBoard(BoardModel currentBoard) {
        this.currentBoard = currentBoard;
    }

    public int getTotalnumber() {
        return totalnumber;
    }

    public void setTotalnumber(int totalnumber) {
        this.totalnumber = totalnumber;
    }

    public ArrayList<String> getMapArray() {
        return mapArray;
    }

    public void setMapArray(ArrayList<String> mapArray) {
        this.mapArray = mapArray;
    }

    private void nextMap() {
        if (currentBoard.getNext() != null) {
            currentBoard = currentBoard.getNext();
        }
    }

    private void previousMap() {
        if (currentBoard.getPrevious() != null) {
            currentBoard = currentBoard.getPrevious();
        }
    }
    // </editor-fold>

    public void fillContainer(ArrayList<String> maparray, String containerName) {

        mapArray = maparray;

        for (int i = 0; i < maparray.size(); i++) {
            BoardModel newboard = new BoardModel();
            //koppel
            newboard.setPrevious(currentBoard);
            currentBoard.setNext(newboard);
            currentBoard = newboard;

            //aantal borden optellen
            totalnumber++;
            //code en naam number setten
            String info = maparray.get(i);

            //set code en name, eerst standaard waarden daarna check of die substring (spatie) bevat.
            String code = info;
            String name = "";
            if (info.contains(" ")) {
                name = info.substring(info.indexOf(' ') + 1); // "name substring"
                code = info.substring(0, info.indexOf(' ')); // "code substring"
            }


            newboard.setName(name);
            newboard.setCode(code);
            newboard.setNumber(totalnumber);
            //  System.out.println(newboard.getCode() + " " + newboard.getName() + " " + newboard.getNumber() );
        }
        currentBoard = start; // reset op begin        
        nextMap(); //vat de eerste map
        setContainerName(containerName);
        setChanged();
        notifyObservers();
        // System.out.println(totalnumber);

    }

    public void getBoard(int scrollValue) {
        int current = (currentBoard.getNumber() - 1);
        int difference = 0;
        int direction = 0; //0 niks 1 next 2 previous
        Boolean changed = false;
        if (current > scrollValue) {
            difference = current - scrollValue;
            direction = 2;
            changed = true;
        } else if (current < scrollValue) {
            difference = scrollValue - current;
            direction = 1;
            changed = true;
        } else {
            System.out.println("geen scroll value verschil");
        }

        while (difference != 0) {
            if (direction == 1) {
                nextMap();
            }
            if (direction == 2) {
                previousMap();
            }
            difference--;
        }
        if (changed) {
            updateFullcontName();
            setChanged();
            notifyObservers();
        }
    }

    Boolean isChanged() {
        return changed;
    }

    void emptyContainer(String untitled) {
        containerName = untitled;
        fullcontName = untitled;
        
        insertBoard(true);
        
    }

    public ArrayList<String> getAllBoards() {
        BoardModel now = currentBoard;
        currentBoard = start;
        ArrayList<String> returnArray = new ArrayList<String>();
        while (currentBoard.getNext() != null) {
            currentBoard = currentBoard.getNext();
            String file = currentBoard.getNewCode() + " " + currentBoard.getName();
            returnArray.add(file);
        }
        currentBoard = now;
        
        return returnArray;
    }

    public void changeCode(String code) {
        currentBoard.setNewCode(code);
        setChangedContainer(true);
    }

    public void changeName(String name) {
        currentBoard.setName(name);
        setChangedContainer(true);;
    }

    public void revert() {
        currentBoard.revert();
        setChanged();
        notifyObservers();
    }

    public void insertBoard(Boolean first) {
        setTotalnumber(getTotalnumber() + 1);
        BoardModel newboard = new BoardModel();        
        if (currentBoard.getNext() != null) {
            currentBoard.getNext().setPrevious(newboard);
            newboard.setNumber(currentBoard.getNext().getNumber());
            correctNumbersPlus();
        } else {
            newboard.setNumber(totalnumber);
        }

        newboard.setPrevious(currentBoard);
        newboard.setNext(currentBoard.getNext());
        currentBoard.setNext(newboard);
        currentBoard = start.getNext();
        if(!first)
        {
            updateFullcontName();
            setChangedContainer(true);
        }
        else
        {
            setChanged();
        notifyObservers();
        }
            
    }

    public void removeBoard() {
        BoardModel nextOrPrevious = null;
        if (currentBoard.getPrevious() != start) {
            nextOrPrevious = currentBoard.getPrevious();
        } else if (currentBoard.getNext() != null) {
            nextOrPrevious = currentBoard.getNext();
        } else {
            JOptionPane.showMessageDialog(null, "You can't have less than 1 diagram");
        }
        if (nextOrPrevious != null) {
            currentBoard.getPrevious().setNext(currentBoard.getNext());
            if(currentBoard.getNext() != null)
            {
            currentBoard.getNext().setPrevious(currentBoard.getPrevious());
            correctNumbersMinus();
            }            
            currentBoard = start.getNext();
            setTotalnumber(getTotalnumber() - 1);
        }       
        updateFullcontName();
        setChangedContainer(true);
    }

    private void correctNumbersMinus() {
        BoardModel current = currentBoard;
        while (current.getNext() != null) {
            current = current.getNext();
            current.setNumber((current.getNumber() - 1));
        }
    }

    private void correctNumbersPlus() {
        BoardModel current = currentBoard;
        while (current.getNext() != null) {
            current = current.getNext();
            int number = current.getNumber();
            current.setNumber((number + 1));
        }
    }
}
