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

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPasswordField;

public class PasswordDialog extends JDialog implements ActionListener {

  private static final long serialVersionUID = -909948475667454381L;
  
  private JPasswordField passwordField;
  private JButton okButton;
  private JButton cancelButton;
  
  private boolean passwordEntered = false;
    
  public PasswordDialog(JFrame owner) {
    super(owner, "Enter password", true);
    setLayout(new FlowLayout());
    setSize(200, 100);
    createWidgets();
  }
  
  private void createWidgets() {
    passwordField = new JPasswordField(15);
    add(passwordField);
    passwordField.addActionListener(this);
    
    okButton = new JButton("OK");
    okButton.addActionListener(this);
    add(okButton);
    
    cancelButton = new JButton("Cancel");
    cancelButton.addActionListener(this);
    add(cancelButton);
  }

  @Override
  public void actionPerformed(ActionEvent evt) {
    if (evt.getSource() != cancelButton) {
      passwordEntered = true;
    }
    dispose();
  }
  
  public boolean wasPasswordEntered() {
    return passwordEntered;
  }
  
  public char[] getPassword() {
    return passwordField.getPassword();
  }
}
