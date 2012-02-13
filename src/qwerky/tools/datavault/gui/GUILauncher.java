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

import java.io.File;
import java.util.prefs.Preferences;

import javax.swing.JFileChooser;

public class GUILauncher {

  private static final String DATAVAULT_LAST_DIR = "datavault_last_dir";

  public static void main(String[] args) {
    if (args.length == 1) {
      if (isVaultFileDetected(args)) {
        open(args[0]);
      } else {
        createNew(args[0]);
      }
    } else {
      openFromChooser();
    }
  }

  private static boolean isVaultFileDetected(String[] args) {
    if (args == null) return false;
    if (args.length != 1) return false;
    
    File file = new File(args[0]);
    if (!file.exists()) return false;
    if (file.isDirectory()) return false;
    if (!file.getName().endsWith(".vault")) return false;
    
    return true;
  }

  private static void createNew(String vaultPath) {
    File vaultFile = new File(vaultPath);
    DataVaultGUI.createVault(vaultFile);
  }

  private static void open(String vaultPath) {
    File vaultFile = new File(vaultPath);
    System.out.println("Opening " + vaultFile);
    DataVaultGUI.openVault(vaultFile);
  }
  
  private static void openFromChooser() {
    JFileChooser chooser = new JFileChooser();
    Preferences prefs = Preferences.userRoot();
    String lastPath = prefs.get(DATAVAULT_LAST_DIR, "c:\\");
    chooser.setCurrentDirectory(new File(lastPath));
    int returnVal = chooser.showOpenDialog(null);
    
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      File vaultFile = chooser.getSelectedFile();
      prefs.put(DATAVAULT_LAST_DIR, vaultFile.getParent());
      
      if (vaultFile.exists()) {
        DataVaultGUI.openVault(vaultFile);
      } else {
        DataVaultGUI.createVault(vaultFile);
      }
    }
  }
}
