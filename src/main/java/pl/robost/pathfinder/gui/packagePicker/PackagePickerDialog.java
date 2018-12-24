package pl.robost.pathfinder.gui.packagePicker;

import javax.swing.*;

public class PackagePickerDialog {
    private JPanel content;
    private JTextField packageNameField;

    public PackagePickerDialog() {
        this.packageNameField.requestFocus();
    }

    public JPanel getContent() {
        return content;
    }

    public String getPackageName(){
        return packageNameField.getText();
    }
}
