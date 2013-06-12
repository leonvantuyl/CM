/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chessmanager;

import java.util.ArrayList;

/**
 *
 * @author Leon van Tuijl
 */
public class BoardContainer extends java.util.Observable {

    private ArrayList<String> mapArray;
    private BoardModel currentBoard;
    private BoardModel start;
    private int totalnumber;

    public BoardContainer() {
        start = new BoardModel();
        currentBoard = start;
        totalnumber = 0;
    }

    /*
     * !!!Hidden getters and setters
     */
    // <editor-fold>
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

    void fillContainer(ArrayList<String> maparray) {

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
            setChanged();
            notifyObservers();
        }
    }
}
