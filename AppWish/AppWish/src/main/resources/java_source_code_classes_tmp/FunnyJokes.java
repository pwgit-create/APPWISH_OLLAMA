
import javax.swing.*;
import java.awt.*;

public class FunnyJokes {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Funny Jokes");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        JPanel panel = new JPanel();
        frame.add(panel);

        JLabel joke1Label = new JLabel("<html><p>Why don't eggs tell jokes?</p></html>");
        joke1Label.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(joke1Label);

        JButton joke1Button = new JButton("Tell me!");
        joke1Button.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Because they'd crack each other up!", "Joke", JOptionPane.INFORMATION_MESSAGE);
        });
        panel.add(joke1Button);

        JLabel joke2Label = new JLabel("<html><p>Why did the scarecrow win an award?</p></html>");
        joke2Label.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(joke2Label);

        JButton joke2Button = new JButton("Tell me!");
        joke2Button.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Because he was outstanding in his field!", "Joke", JOptionPane.INFORMATION_MESSAGE);
        });
        panel.add(joke2Button);

        JLabel joke3Label = new JLabel("<html><p>What do you call a fake noodle?</p></html>");
        joke3Label.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(joke3Label);

        JButton joke3Button = new JButton("Tell me!");
        joke3Button.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "An impasta!", "Joke", JOptionPane.INFORMATION_MESSAGE);
        });
        panel.add(joke3Button);

        JLabel joke4Label = new JLabel("<html><p>Why did the bicycle fall over?</p></html>");
        joke4Label.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(joke4Label);

        JButton joke4Button = new JButton("Tell me!");
        joke4Button.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Because it was two-tired!", "Joke", JOptionPane.INFORMATION_MESSAGE);
        });
        panel.add(joke4Button);

        JLabel joke5Label = new JLabel("<html><p>What do you call a can opener that doesn't work?</p></html>");
        joke5Label.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(joke5Label);

        JButton joke5Button = new JButton("Tell me!");
        joke5Button.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "A can't opener!", "Joke", JOptionPane.INFORMATION_MESSAGE);
        });
        panel.add(joke5Button);

        JLabel joke6Label = new JLabel("<html><p>Why did the chicken cross the playground?</p></html>");
        joke6Label.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(joke6Label);

        JButton joke6Button = new JButton("Tell me!");
        joke6Button.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "To get to the other slide!", "Joke", JOptionPane.INFORMATION_MESSAGE);
        });
        panel.add(joke6Button);

        JLabel joke7Label = new JLabel("<html><p>What do you call a bear with no socks on?</p></html>");
        joke7Label.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(joke7Label);

        JButton joke7Button = new JButton("Tell me!");
        joke7Button.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Barefoot!", "Joke", JOptionPane.INFORMATION_MESSAGE);
        });
        panel.add(joke7Button);

        JLabel joke8Label = new JLabel("<html><p>Why did the computer go to the doctor?</p></html>");
        joke8Label.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(joke8Label);

        JButton joke8Button = new JButton("Tell me!");
        joke8Button.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "It had a virus!", "Joke", JOptionPane.INFORMATION_MESSAGE);
        });
        panel.add(joke8Button);

        JLabel joke9Label = new JLabel("<html><p>What do you call a group of cows playing instruments?</p></html>");
        joke9Label.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(joke9Label);

        JButton joke9Button = new JButton("Tell me!");
        joke9Button.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "A moo-sical band!", "Joke", JOptionPane.INFORMATION_MESSAGE);
        });
        panel.add(joke9Button);

        JLabel joke10Label = new JLabel("<html><p>Why did the banana go to the doctor?</p></html>");
        joke10Label.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(joke10Label);

        JButton joke10Button = new JButton("Tell me!");
        joke10Button.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "He wasn't peeling well!", "Joke", JOptionPane.INFORMATION_MESSAGE);
        });
        panel.add(joke10Button);

        frame.setVisible(true);
    }
}
 