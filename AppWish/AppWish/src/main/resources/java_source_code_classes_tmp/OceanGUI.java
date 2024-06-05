

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class OceanGUI {

    public static void main(String[] args) {
        // Create a JFrame for the GUI
        JFrame frame = new JFrame("Largest Oceans");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a panel to hold the labels and text
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));

        // Add labels and text for each ocean
        JLabel pacificLabel = new JLabel("Pacific Ocean: " + 155.6e+6 + " km²");
        JLabel atlanticLabel = new JLabel("Atlantic Ocean: " + 85.1e+6 + " km²");
        JLabel indianLabel = new JLabel("Indian Ocean: " + 73.5e+6 + " km²");
        JLabel arcticLabel = new JLabel("Arctic Ocean: " + 14.0e+6 + " km²");

        panel.add(pacificLabel);
        panel.add(atlanticLabel);
        panel.add(indianLabel);
        panel.add(arcticLabel);

        // Add the panel to the frame
        frame.getContentPane().add(panel);

        // Set the size and show the frame
        frame.setSize(300, 200);
        frame.setVisible(true);
    }
}
 