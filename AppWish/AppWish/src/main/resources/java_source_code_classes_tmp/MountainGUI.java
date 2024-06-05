

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class MountainGUI {

    public static void main(String[] args) {
        JFrame frame = new JFrame("3 Biggest Mountains in the World");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2));

        List<String> mountains = new ArrayList<>();
        mountains.add("Mount Everest (Nepal/China)");
        mountains.add("K2 (Pakistan/China)");
        mountains.add("Kangchenjunga (India/Nepal)");

        for (int i = 0; i < mountains.size(); i++) {
            JLabel mountainLabel = new JLabel(mountains.get(i));
            panel.add(new JLabel("Rank: " + (i + 1)));
            panel.add(mountainLabel);
            panel.add(new JSeparator());
        }

        frame.getContentPane().add(panel);

        frame.pack();
        frame.setVisible(true);
    }
}
 