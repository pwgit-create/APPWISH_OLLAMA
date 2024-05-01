package pn.cg.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StringUtilTest {

    private final String JAVA_CODE_WITH_EXTRA_DELIMITERS = "@START_HERE \\n" + //
            "\"import java.awt.*;\n" + //
            "import java.awt.event.*;\n" + //
            "import java.util.ArrayList;\n" + //
            "import java.util.List;\n" + //
            "\n" + //
            "@START_HERE\n" + //
            "public class PingPongGame {\n" + //
            "\n" + //
            "    private JFrame frame;\n" + //
            "    private JPanel contentPane;\n" + //
            "    private JButton button1, button2, button3, button4, button5;\n" + //
            "    private JTextField textField1, textField2;\n" + //
            "    private List<String> lines = new ArrayList<>();\n" + //
            "    private ImageIcon imageIcon;\n" + //
            "\n" + //
            "    public static void main(String[] args) {\n" + //
            "        @START_HERE\n" + //
            "        PingPongGame pingPongGame = new PingPongGame();\n" + //
            "        pingPongGame.initUI();\n" + //
            "        pingPongGame.runGame();\n" + //
            "        @END_HERE\n" + //
            "    }\n" + //
            "\n" + //
            "    public void initUI() {\n" + //
            "        frame = new JFrame();\n" + //
            "        contentPane = new JPanel();\n" + //
            "\n" + //
            "        // Set up the buttons and their actions\n" + //
            "        button1 = new JButton(\"Start\");\n" + //
            "        button2 = new JButton(\"Stop\");\n" + //
            "        button3 = new JButton(\"Refresh\");\n" + //
            "        button4 = new JButton(\"Quit\");\n" + //
            "        button5 = new JButton(\"Next Level\");\n" + //
            "\n" + //
            "        // Create buttons listener\n" + //
            "        ActionListener actionListener = new ActionListener() {\n" + //
            "            @Override\n" + //
            "            public void actionPerformed(ActionEvent e) {\n" + //
            "                String command = (String) e.getSource().toString();\n" + //
            "                if (\"Start\".equalsIgnoreCase(command)) {\n" + //
            "                    startGame();\n" + //
            "                } else if (\"Stop\".equalsIgnoreCase(command)) {\n" + //
            "                    stopGame();\n" + //
            "                } else if (\"Refresh\".equalsIgnoreCase(command)) {\n" + //
            "                    refreshGame();\n" + //
            "                } else if (\"Next Level\".equalsIgnoreCase(command)) {\n" + //
            "                    nextLevel();\n" + //
            "                } else if (\"Quit\".equalsIgnoreCase(command)) {\n" + //
            "                    System.exit(0);\n" + //
            "                }\n" + //
            "            }\n" + //
            "        };\n" + //
            "\n" + //
            "        // Add buttons listener to the buttons\n" + //
            "        button1.addActionListener(actionListener);\n" + //
            "        button2.addActionListener(actionListener);\n" + //
            "        button3.addActionListener(actionListener);\n" + //
            "        button4.addActionListener(actionListener);\n" + //
            "        button5.addActionListener(actionListener);\n" + //
            "\n" + //
            "        // Set up the text fields and their actions\n" + //
            "        textField1 = new JTextField(\"Level 1\");\n" + //
            "        textField2 = new JTextField();\n" + //
            "\n" + //
            "        // Add buttons to contentPane\n" + //
            "        contentPane.setLayout(new GridLayout(3, 2));\n" + //
            "        contentPane.add(button1);\n" + //
            "        contentPane.add(button2);\n" + //
            "        contentPane.add(button3);\n" + //
            "        contentPane.add(textField1);\n" + //
            "        contentPane.add(textField2);\n" + //
            "        contentPane.add(button4);\n" + //
            "        contentPane.add(button5);\n" + //
            "\n" + //
            "        // Set the imageIcon for button graphics\n" + //
            "        imageIcon = new ImageIcon(\"resources/ball-paddle-white.png\");\n" + //
            "        button1.setIcon(imageIcon);\n" + //
            "        button2.setIcon(imageIcon);\n" + //
            "        button3.setIcon(imageIcon);\n" + //
            "        button4.setIcon(imageIcon);\n" + //
            "        button5.setIcon(imageIcon);\n" + //
            "    }\n" + //
            "\n" + //
            "    public void runGame() {\n" + //
            "        frame.add(contentPane, BorderLayout.CENTER);\n" + //
            "        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);\n" + //
            "        frame.setSize(600, 400);\n" + //
            "        frame.setLocationRelativeTo(null);\n" + //
            "        frame.setVisible(true);\n" + //
            "    }\n" + //
            "\n" + //
            "    public void startGame() {\n" + //
            "        textField1.setText(\"Level \" + (lines.size() + 1) + \" Started\");\n" + //
            "        lines.add(\"Level \" + (lines.size() + 1));\n" + //
            "        runGame();\n" + //
            "    }\n" + //
            "\n" + //
            "    public void stopGame() {\n" + //
            "        if (!lines.isEmpty()) {\n" + //
            "            lines.remove(lines.size() - 1);\n" + //
            "            textField1.setText(\"Level \" + lines.size() + \" Stopped\");\n" + //
            "        }\n" + //
            "        frame.setVisible(false);\n" + //
            "    }\n" + //
            "\n" + //
            "    public void refreshGame() {\n" + //
            "        if (!lines.isEmpty()) {\n" + //
            "            lines.remove(lines.size() - 1);\n" + //
            "            textField1.setText(\"Level \" + lines.size() + \" Refreshed\");\n" + //
            "        } else {\n" + //
            "            startGame();\n" + //
            "        }\n" + //
            "    }\n" + //
            "\n" + //
            "    public void nextLevel() {\n" + //
            "        if (!lines.isEmpty()) {\n" + //
            "            lines.remove(lines.size() - 1);\n" + //
            "            textField1.setText(\"Level \" + (lines.size() + 1) + \" Next Level\");\n" + //
            "            lines.add(\"Next Level\");\n" + //
            "        } else {\n" + //
            "            startGame();\n" + //
            "        }\n" + //
            "    }\n" + //
            "}\n" + //
            "@END_HERE\n" + //
            "";

    @Test
    public void RemoveExtraStartDelimitersInResponseTest() {

        final String expected = "@START_HERE \\n" + //
                "\"import java.awt.*;\n" + //
                "import java.awt.event.*;\n" + //
                "import java.util.ArrayList;\n" + //
                "import java.util.List;\n" + //
                "\n" + //
                "\n" + //
                "public class PingPongGame {\n" + //
                "\n" + //
                "    private JFrame frame;\n" + //
                "    private JPanel contentPane;\n" + //
                "    private JButton button1, button2, button3, button4, button5;\n" + //
                "    private JTextField textField1, textField2;\n" + //
                "    private List<String> lines = new ArrayList<>();\n" + //
                "    private ImageIcon imageIcon;\n" + //
                "\n" + //
                "    public static void main(String[] args) {\n" + //
                "        \n" + //
                "        PingPongGame pingPongGame = new PingPongGame();\n" + //
                "        pingPongGame.initUI();\n" + //
                "        pingPongGame.runGame();\n" + //
                "        @END_HERE\n" + //
                "    }\n" + //
                "\n" + //
                "    public void initUI() {\n" + //
                "        frame = new JFrame();\n" + //
                "        contentPane = new JPanel();\n" + //
                "\n" + //
                "        // Set up the buttons and their actions\n" + //
                "        button1 = new JButton(\"Start\");\n" + //
                "        button2 = new JButton(\"Stop\");\n" + //
                "        button3 = new JButton(\"Refresh\");\n" + //
                "        button4 = new JButton(\"Quit\");\n" + //
                "        button5 = new JButton(\"Next Level\");\n" + //
                "\n" + //
                "        // Create buttons listener\n" + //
                "        ActionListener actionListener = new ActionListener() {\n" + //
                "            @Override\n" + //
                "            public void actionPerformed(ActionEvent e) {\n" + //
                "                String command = (String) e.getSource().toString();\n" + //
                "                if (\"Start\".equalsIgnoreCase(command)) {\n" + //
                "                    startGame();\n" + //
                "                } else if (\"Stop\".equalsIgnoreCase(command)) {\n" + //
                "                    stopGame();\n" + //
                "                } else if (\"Refresh\".equalsIgnoreCase(command)) {\n" + //
                "                    refreshGame();\n" + //
                "                } else if (\"Next Level\".equalsIgnoreCase(command)) {\n" + //
                "                    nextLevel();\n" + //
                "                } else if (\"Quit\".equalsIgnoreCase(command)) {\n" + //
                "                    System.exit(0);\n" + //
                "                }\n" + //
                "            }\n" + //
                "        };\n" + //
                "\n" + //
                "        // Add buttons listener to the buttons\n" + //
                "        button1.addActionListener(actionListener);\n" + //
                "        button2.addActionListener(actionListener);\n" + //
                "        button3.addActionListener(actionListener);\n" + //
                "        button4.addActionListener(actionListener);\n" + //
                "        button5.addActionListener(actionListener);\n" + //
                "\n" + //
                "        // Set up the text fields and their actions\n" + //
                "        textField1 = new JTextField(\"Level 1\");\n" + //
                "        textField2 = new JTextField();\n" + //
                "\n" + //
                "        // Add buttons to contentPane\n" + //
                "        contentPane.setLayout(new GridLayout(3, 2));\n" + //
                "        contentPane.add(button1);\n" + //
                "        contentPane.add(button2);\n" + //
                "        contentPane.add(button3);\n" + //
                "        contentPane.add(textField1);\n" + //
                "        contentPane.add(textField2);\n" + //
                "        contentPane.add(button4);\n" + //
                "        contentPane.add(button5);\n" + //
                "\n" + //
                "        // Set the imageIcon for button graphics\n" + //
                "        imageIcon = new ImageIcon(\"resources/ball-paddle-white.png\");\n" + //
                "        button1.setIcon(imageIcon);\n" + //
                "        button2.setIcon(imageIcon);\n" + //
                "        button3.setIcon(imageIcon);\n" + //
                "        button4.setIcon(imageIcon);\n" + //
                "        button5.setIcon(imageIcon);\n" + //
                "    }\n" + //
                "\n" + //
                "    public void runGame() {\n" + //
                "        frame.add(contentPane, BorderLayout.CENTER);\n" + //
                "        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);\n" + //
                "        frame.setSize(600, 400);\n" + //
                "        frame.setLocationRelativeTo(null);\n" + //
                "        frame.setVisible(true);\n" + //
                "    }\n" + //
                "\n" + //
                "    public void startGame() {\n" + //
                "        textField1.setText(\"Level \" + (lines.size() + 1) + \" Started\");\n" + //
                "        lines.add(\"Level \" + (lines.size() + 1));\n" + //
                "        runGame();\n" + //
                "    }\n" + //
                "\n" + //
                "    public void stopGame() {\n" + //
                "        if (!lines.isEmpty()) {\n" + //
                "            lines.remove(lines.size() - 1);\n" + //
                "            textField1.setText(\"Level \" + lines.size() + \" Stopped\");\n" + //
                "        }\n" + //
                "        frame.setVisible(false);\n" + //
                "    }\n" + //
                "\n" + //
                "    public void refreshGame() {\n" + //
                "        if (!lines.isEmpty()) {\n" + //
                "            lines.remove(lines.size() - 1);\n" + //
                "            textField1.setText(\"Level \" + lines.size() + \" Refreshed\");\n" + //
                "        } else {\n" + //
                "            startGame();\n" + //
                "        }\n" + //
                "    }\n" + //
                "\n" + //
                "    public void nextLevel() {\n" + //
                "        if (!lines.isEmpty()) {\n" + //
                "            lines.remove(lines.size() - 1);\n" + //
                "            textField1.setText(\"Level \" + (lines.size() + 1) + \" Next Level\");\n" + //
                "            lines.add(\"Next Level\");\n" + //
                "        } else {\n" + //
                "            startGame();\n" + //
                "        }\n" + //
                "    }\n" + //
                "}\n" + //
                "@END_HERE\n" + //
                "";


        final String acutal = StringUtil.RemoveExtraStartDelimitersInResponse(JAVA_CODE_WITH_EXTRA_DELIMITERS);


        Assertions.assertEquals(expected, acutal);


    }

    @Test
    public void removeExtraEndDelimitersInResponseTest() {

        final String TEST_TEXT = "@START_HERE javacode1.. more java code2... @END_HERE more javacode3.... @END_HERE";

        final String expected = "@START_HERE javacode1.. more java code2...  more javacode3.... @END_HERE";

        final String actual = StringUtil.RemoveExtraEndDelimitersInResponse(TEST_TEXT);

        Assertions.assertEquals(expected, actual);

    }


    @Test
    public void getUnbalancedBraceBracketsFromStringTest() {

        final int expected = 1;

        final String text = "public class PN{ Public static void main(String[] args) {}  for(int i=0;i++;i<10){ }";

        final int actual = StringUtil.GetUnbalancedBraceBracketsFromString(text);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void RemoveCommonAdditionStringsFromAiModelsTest() {


        final String text = "```java\npublic class PN{ Public static void main(String[] args) {}  for(int i=0;i++;i<10){ }}\n```";

        final String expected = "\npublic class PN{ Public static void main(String[] args) {}  for(int i=0;i++;i<10){ }}\n";

        final String actual = StringUtil.RemoveCommonAdditionStringsFromAiModels(text);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void AppendWithCorrectNumberOfBraceBucketsAtEndOfTheString() {


        final String text = "public class PN{ Public static void main(String[] args) {}  for(int i=0;i++;i<10){ }";

        final String expected = "public class PN{ Public static void main(String[] args) {}  for(int i=0;i++;i<10){ }}";

        final String actual = StringUtil.AppendBraceBucketsAtEndofTheString(text, StringUtil.GetUnbalancedBraceBracketsFromString(text));

        Assertions.assertEquals(expected, actual);
    }

}