/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chessmanager;

import java.awt.FileDialog;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Leon van Tuijl
 */
public class FileIO {

    private MainControl myControl;
    private FileWriter outputStream;
    private FileWriter inputStream;
    private String currentDir;

    public FileIO(MainControl Control) {
        myControl = Control;
        inputStream = null;
        outputStream = null;
        currentDir = System.getProperty("user.dir");
    }

    public void openFile() {
        ArrayList<String> mapArray = new ArrayList<String>();
        JFileChooser fc = new JFileChooser(currentDir);
        FileFilter filter = new FileNameExtensionFilter("Bestanden voor dit programma", "CMD");
        Scanner reader = null;


        fc.addChoosableFileFilter(filter);
        fc.setDialogTitle("Open a mapfile");
        fc.setAcceptAllFileFilterUsed(false);
        fc.setFileFilter(filter);

        try {
            if (fc.showOpenDialog(fc) == JFileChooser.APPROVE_OPTION) {
                //voor testen
                // System.out.println("getCurrentDirectory(): "  +  fc.getCurrentDirectory());
                // System.out.println("getSelectedFile() : " +  fc.getSelectedFile());

                currentDir = fc.getCurrentDirectory().toString();
                reader = new Scanner(fc.getSelectedFile());
            } else {
                System.out.println("No Selection ");
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Invalid File", "error", JOptionPane.ERROR_MESSAGE);
        }

        if (reader != null) {
            int i = 0;
            while (reader.hasNextLine()) {
                i++;
                mapArray.add(reader.nextLine());
            }
            System.out.println(i);
        }


        if (mapArray.size() > 0) {
            myControl.newContainer(mapArray);
        } else {
            System.out.println("mapArray is leeg");
        }

    }
}
