/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chessmanager;

/**
 *
 * @author Leon van Tuijl
 */
public class FENException extends Exception {

    private String FENerror;

    public FENException(String message) {
        super(message);
        FENerror = "Error in FEN-position \n" + message;
    }

    public FENException(String message, Throwable throwable) {
        super(message, throwable);
        FENerror = "Error in FEN-position \n" + message;
    }

    public String getError() {
        return FENerror;
    }
}
