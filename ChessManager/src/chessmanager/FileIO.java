/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chessmanager;

import java.awt.FileDialog;
import java.io.BufferedWriter;
import java.io.File;
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
    private String LastSaved;

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
        String name = "";


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
                name = fc.getSelectedFile().getName().replaceAll(".cmd", "");
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
            myControl.newContainer(mapArray, name);
        } else {
            System.out.println("mapArray is leeg");
        }

    }

    public void save(ArrayList<String> allBoards, String containerName, String dir) {
        String savename = containerName;
        if (dir.isEmpty()) {
            savename = containerName + ".cmd";
            dir = currentDir;
        }

        try {
            // Create file 
            File toBeSaved = new File(dir, savename);
            FileWriter fstream = new FileWriter(toBeSaved);

            if (!(toBeSaved.exists()) || containerName.equals("Untitled")) {
                saveAs(allBoards, containerName);
            } else {
                BufferedWriter out = new BufferedWriter(fstream);
                String outString = processList(allBoards);
                out.write(outString);
                //Close the output stream
                out.close();
            }
        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }

    public Boolean saveAs(ArrayList<String> allBoards, String containerName) {
        String name = containerName;
        String dir = currentDir;
        JFileChooser c = new JFileChooser(currentDir);
        Boolean succes = false;
        c.setApproveButtonText("Save");

        try {
            if (c.showOpenDialog(c) == JFileChooser.APPROVE_OPTION) {
                name = c.getSelectedFile().getName();
                dir = c.getCurrentDirectory().toString();
                succes = true;
            } else {
                succes = false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid File", "error", JOptionPane.ERROR_MESSAGE);
        }
        if (succes) {
            currentDir = dir;
            save(allBoards, name, dir);
            LastSaved = name;
        }
        return succes;
    }

    private String processList(ArrayList<String> allBoards) {
        String out = "";
        //  System.getProperty("line.separator");

        for (int i = 0; i < allBoards.size(); i++) {
            String info = allBoards.get(i) + "\n";
            out += info;
        }
        return out;
    }
    
    public String getLastSaved(){
        return LastSaved;
    }
            
}
