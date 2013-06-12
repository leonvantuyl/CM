/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chessmanager;

import java.util.ArrayList;

/**
 *
 * @author Beheerder
 */
public class BoardModel extends java.util.Observable {

    private String code;
    private String newCode;
    private String message;
    private String name;
    private int number;
    private BoardModel next;
    private BoardModel previous;

    public BoardModel() {
        message = "Correct FEN-position";
    }

    /**
     * Hidden getters and setters
     */
    // <editor-fold>
    public String getNewCode() {
        return newCode;
    }

    public void setNewCode(String newCode) {
        this.newCode = newCode;
        try {
            validateCode(newCode);
        } catch (FENException ex) {
            setMessage(ex.getError());
        }

        setChanged();
        notifyObservers();
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
        setNewCode(code);
    }

    public BoardModel getNext() {
        return next;
    }

    public void setNext(BoardModel next) {
        this.next = next;
    }

    public BoardModel getPrevious() {
        return previous;
    }

    public void setPrevious(BoardModel previous) {
        this.previous = previous;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        System.out.println(message);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    // </editor-fold>

    private void validateCode(String newCode) throws FENException {
        String errorArrow = "^";
        String error = "";
        Boolean approved = true;

        int officialSize = 71;
        int currentRowSize = 0;
        int currentRow = 1;
        int currentLoc = 0;
        int arrowLoc = 0;

        char[] array = newCode.toCharArray();
        for (int i = 0; i < array.length; i++) {
            char curChar = array[i];

            arrowLoc++;
            switch (curChar) {
                case 'r':
                case 'n':
                case 'b':
                case 'q':
                case 'k':
                case 'p':
                case 'R':
                case 'N':
                case 'B':
                case 'Q':
                case 'K':
                case 'P':
                    currentRowSize++;
                    currentLoc++;
                    if (currentRowSize > 8) {
                        error = "Too many pieces on rank " + currentRow + "; '/' expected";
                        approved = false;
                    }


                    break;

                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                    currentRowSize += Character.getNumericValue(curChar);
                    currentLoc += Character.getNumericValue(curChar);
                    if (currentRowSize > 8) {
                        error = "Too many empty squares added to rank " + currentRow;
                        approved = false;
                    }
                    break;


                case '/':
                    if (currentRow < 8) {
                        if (currentRowSize == 8) {
                            currentRowSize = 0;
                            currentRow++;
                            currentLoc++;
                        } else {
                            error = "Not enough squares defined on rank " + currentRow;
                            approved = false;
                        }
                    } else {
                        error = "Too many ranks defined";
                        approved = false;
                    }
                    break;

                default:
                    error = "Illegal character found";
                    approved = false;
                    break;
            }

            if (!approved) // stop controle if false
            {
                i = array.length;
            }

        }
        //size check
        if (approved) {
            if (currentLoc == officialSize) {
                //System.out.println("Groot genoeg");
            } else {
                error = "Not enough sqaures defined";
                approved = false;
            }
        }

        //De laatste check anders throw.
        if (approved) {
            //System.out.println("OK");              
        } else {
            int p = 1;
            while (p < arrowLoc) // pijl setten
            {
                String space = " ";
                errorArrow = space + errorArrow;
                p++;
            }
            throw new FENException(newCode + "\n" + errorArrow + "\n" + error);
        }

    }
}
