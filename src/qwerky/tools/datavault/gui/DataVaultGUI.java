/**
 *  Copyright (C) 2012  Neil Taylor (https://github.com/qwerky/)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package qwerky.tools.datavault.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import qwerky.tools.datavault.DataVault;

public class DataVaultGUI implements ActionListener {

  private DataVault vault;
  private final JFrame frame;
  private JTextArea textArea;
  
  /**
   * Static instantiator.
   * @param vaultFile
   */
  public static void openVault(File vaultFile) {
    DataVaultGUI gui = new DataVaultGUI(vaultFile);
    
    try {
      gui.showVaultContents();
    } catch (Exception ex) {
      ex.printStackTrace();
      JOptionPane.showMessageDialog(null, "Exception occurred:\n" + ex, "Error opening vault.", JOptionPane.ERROR_MESSAGE);
    }
  }
  
  
  /**
   * Static instantiator.
   * @param vaultFile
   */
  public static void createVault(File vaultFile) {
    if (vaultFile.exists()) {
      openVault(vaultFile);
    } else {
      try {
        vaultFile.createNewFile();
      } catch (IOException ioe) {
        JOptionPane.showMessageDialog(null, "Error creating new vault file " + vaultFile.getAbsolutePath() + "\n" + ioe, "Error", JOptionPane.ERROR_MESSAGE);
      }
      DataVaultGUI gui = new DataVaultGUI(vaultFile);
      gui.showNewVault();
    }
  }


  /**
   * Constructor.
   * @param vaultFile
   */
  private DataVaultGUI(File vaultFile) {
    this.vault = new DataVault(vaultFile);
    this.frame = new JFrame("DataVault: " + vaultFile.getAbsolutePath());
    createWidgets();
  }
  
  private void createWidgets() {
    textArea = new JTextArea();
    JScrollPane scrollPane = new JScrollPane(textArea);
    frame.getContentPane().add(scrollPane);
    frame.setJMenuBar(createMenu());
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setSize(300, 400);
  }
  
  private void showVaultContents() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IOException {
    char[] password = getPassword();
    if (password == null) {
      frame.dispose();
      return;
    }
    Reader reader = null;
    try {
      reader = new InputStreamReader(vault.getInputStream(password));
      textArea.read(reader, vault);
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (Exception ignore){}
      }
    }
    frame.setVisible(true);
  }

  private void showNewVault() {
    frame.setVisible(true);
  }

  private char[] getPassword() {
    PasswordDialog dialog = new PasswordDialog(frame);
    dialog.setVisible(true);
    if (dialog.wasPasswordEntered()) {
      return dialog.getPassword();
    } else {
      return null;
    }
  }


  private void saveVaultContents() {
    char[] password = getPassword();
    PrintWriter writer = null;
    try {
      writer = new PrintWriter(vault.getOutputStream(password));
      writer.print(textArea.getText());
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(frame, "Error occurred:\n" + ex, "Ooops!", JOptionPane.ERROR_MESSAGE);
    } finally {
      if (writer != null) {
        try {
          writer.close();
        } catch (Exception ignore){}
      }
    }
  }

  private JMenuBar createMenu() {
    JMenuBar menuBar = new JMenuBar();
    JMenu fileMenu = new JMenu("File");
    
    JMenuItem openItem = new JMenuItem("Open");
    openItem.setActionCommand("open");
    openItem.addActionListener(this);
    
    JMenuItem saveItem = new JMenuItem("Save");
    saveItem.setActionCommand("save");
    saveItem.addActionListener(this);
    
    JMenuItem exitItem = new JMenuItem("Exit");
    exitItem.setActionCommand("exit");
    exitItem.addActionListener(this);
    
    fileMenu.add(openItem);
    fileMenu.add(saveItem);
    fileMenu.add(exitItem);
    
    menuBar.add(fileMenu);
    
    return menuBar;
  }
  
  
  @Override
  public void actionPerformed(ActionEvent evt) {
    if (evt.getActionCommand().equals("open")) {
      //TODO
    } else if (evt.getActionCommand().equals("save")) {
      saveVaultContents();
    } else if (evt.getActionCommand().equals("exit")) {
      frame.dispose();
    }
  }
  
}
