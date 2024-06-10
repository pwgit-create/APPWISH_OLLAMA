

import javax.swing.*;
import java.awt.*;

public class WishForAStar {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Wish for a Star");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel label = new JLabel("Why should you wish for a star?");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel panel = new JPanel();
        panel.add(label);

        frame.getContentPane().add(panel);
        frame.pack();

        JOptionPane.showMessageDialog(frame, "To make your dreams come true and to bring happiness in your life.");

        frame.setVisible(true);
    }
}
 