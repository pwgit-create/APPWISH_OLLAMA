
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class KeyboardController {

 JFrame frame;
 JPanel panel;
 int x = 50, y = 50; // position of the figure

 public static void main(String[] args) {
 KeyboardController controller = new KeyboardController();
 controller.init();
 }

 public void init() {
 frame = new JFrame("Keyboard Control");
 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 frame.setSize(500, 500);

 panel = new JPanel() {
 @Override
 public void paintComponent(Graphics g) {
 super.paintComponent(g);
 g.setColor(Color.BLACK);
 g.fillOval(x, y, 10, 10);
 }
 };
 frame.add(panel);
 frame.addKeyListener(new KeyAdapter() {
 @Override
 public void keyPressed(KeyEvent e) {
 switch (e.getKeyCode()) {
 case KeyEvent.VK_LEFT:
 x -= 5;
 break;
 case KeyEvent.VK_RIGHT:
 x += 5;
 break;
 case KeyEvent.VK_UP:
 y -= 5;
 break;
 case KeyEvent.VK_DOWN:
 y += 5;
 break;
 }
 panel.repaint();
 }
 });
 frame.setVisible(true);
 }
} 