import javax.swing.*;
import java.awt.*;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashSet;

public class Game extends JFrame {
    int seconds=10;
    JFrame frame = new JFrame();
    JLabel titleLabel = new JLabel();
    JTextArea textfield = new JTextArea();
    JButton player1 = new JButton();
    JButton player2 = new JButton();
    JButton player3 = new JButton();
    JButton player4 = new JButton();
    JButton actionsButton = new JButton();
    JButton dice = new JButton();
    JLabel time_label = new JLabel();
    JLabel seconds_left = new JLabel();
    JTextField number_right = new JTextField();
    HashSet<String> usedNames = new HashSet<>();
    // Custom OutputStream that appends text to the textfield
    private static class TextAreaOutputStream extends OutputStream {
        private final JTextArea textArea;
        private final int maxTextLength;

        public TextAreaOutputStream(JTextArea textArea, int maxTextLength) {
            this.textArea = textArea;
            this.maxTextLength = maxTextLength;
        }

        @Override
        public void write(int b) {
            textArea.append(String.valueOf((char) b));
            if (textArea.getText().length() > maxTextLength) {
                textArea.setText(""); // Clear text if it exceeds the limit
            }
        }
    }

    public Game() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(650, 650);
        frame.getContentPane().setBackground(new Color(50, 50, 50));
        frame.setLayout(null);
        frame.setResizable(false);

        titleLabel.setBounds(210, 10, 650, 50);
        titleLabel.setFont(new Font("MV Boli", Font.BOLD, 40));
        titleLabel.setForeground(new Color(255, 255, 255)); // Set color to white
        titleLabel.setText("Game Title");


        textfield.setBounds(100, 100, 620, 400);
        textfield.setBackground(new Color(25, 25, 25));
        textfield.setForeground(new Color(25, 255, 0));
        textfield.setFont(new Font("Ink Free", Font.BOLD, 20));
        textfield.setBorder(BorderFactory.createBevelBorder(1));
        textfield.setEditable(false);
        textfield.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(textfield);
        scrollPane.setBounds(100, 100, 540, 400);

        PrintStream printStream = new PrintStream(new TextAreaOutputStream(textfield, 5000));
        System.setOut(printStream);
        System.setErr(printStream);

        player1.setBounds(0, 100, 100, 100);
        player1.setFont(new Font("MV Boli", Font.BOLD, 20));
        player1.setFocusable(false);
        player1.setText("p1");
        player1.addActionListener(e -> {
            String newName = Uniquename("Player 1");
            if (newName != null) {
                player1.setText(newName);
                System.out.print("Player 1 has changed their name to " + newName + "\n");
            }
        });

        player2.setBounds(0, 200, 100, 100);
        player2.setFont(new Font("MV Boli", Font.BOLD, 20));
        player2.setFocusable(false);
        player2.setText("p2");
        player2.addActionListener(e -> {
            String newName = Uniquename("Player 2");
            if (newName != null) {
                player2.setText(newName);
                System.out.print("Player 2 has changed their name to " + newName + "\n");
            }
        });

        player3.setBounds(0,300,100,100);
        player3.setFont(new Font("MV Boli",Font.BOLD,20));
        player3.setFocusable(false);
        player3.setText("p3");
        player3.addActionListener(e -> {
            String newName = Uniquename("Player 3");
            if (newName != null) {
                player3.setText(newName);
                System.out.print("Player 1 has changed their name to " + newName + "\n");
            }
        });
        player4.setBounds(0,400,100,100);
        player4.setFont(new Font("MV Boli",Font.BOLD,20));
        player4.setFocusable(false);
        player4.setText("p4");
        player4.addActionListener(e -> {
            String newName = Uniquename("Player 4");
            if (newName != null) {
                player4.setText(newName);
                System.out.print("Player 1 has changed their name to " + newName + "\n");
            }
        });

        seconds_left.setBounds(535,510,100,100);
        seconds_left.setBackground(new Color(25,25,25));
        seconds_left.setForeground(new Color(255,0,0));
        seconds_left.setFont(new Font("Ink Free",Font.BOLD,60));
        seconds_left.setBorder(BorderFactory.createBevelBorder(1));
        seconds_left.setOpaque(true);
        seconds_left.setHorizontalAlignment(JTextField.CENTER);
        seconds_left.setText(String.valueOf(seconds));

        time_label.setBounds(535,510,100,25);
        time_label.setBackground(new Color(50,50,50));
        time_label.setForeground(new Color(255,0,0));
        time_label.setFont(new Font("MV Boli",Font.PLAIN,16));
        time_label.setHorizontalAlignment(JTextField.CENTER);
        time_label.setText("timer >:D");

        dice.setBounds(335,525,200,100);
        dice.setFont(new Font("MV Boli",Font.BOLD,35));
        dice.setFocusable(false);
        dice.setText("Roll Dice");
        dice.addActionListener(e -> {
            int diceRoll1 = (int)(Math.random() * 6) + 1;
            int diceRoll2 = (int)(Math.random() * 6) + 1;
            int total = diceRoll1 + diceRoll2;
            System.out.print("You rolled a " + total + "\n");

        });
        actionsButton.setBounds(100, 525, 200, 100); // Adjust these values as needed
        actionsButton.setFont(new Font("MV Boli", Font.BOLD, 35));
        actionsButton.setFocusable(false);
        actionsButton.setText("Actions");
        actionsButton.addActionListener(a -> {
            String[] options = {"Option 1", "Option 2", "Option 3", "Option 4", "Option 5"};
            String selectedOption = (String) JOptionPane.showInputDialog(
                    frame,
                    "Choose an action:",
                    "Action Selection",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );
            // Handle the selected option
            if (selectedOption != null) {
                System.out.println("You selected: " + selectedOption);
            }
        });

        number_right.setBounds(225,225,100,100);
        number_right.setBackground(new Color(25,25,25));
        number_right.setForeground(new Color(25,255,0));
        number_right.setFont(new Font("Ink Free",Font.BOLD,50));
        number_right.setBorder(BorderFactory.createBevelBorder(1));
        number_right.setHorizontalAlignment(JTextField.CENTER);
        number_right.setEditable(false);

        Timer timer = new Timer(1000, e -> {
            seconds--;
            seconds_left.setText(String.valueOf(seconds));
            if (seconds <= 0) {
                ((Timer)e.getSource()).stop();
                System.out.print("you suck\n");
            }
        });

        timer.start();
        frame.add(titleLabel);
        frame.add(scrollPane);
        frame.add(time_label);
        frame.add(seconds_left);
        frame.add(dice);
        frame.add(actionsButton);
        frame.add(player1);
        frame.add(player2);
        frame.add(player3);
        frame.add(player4);
        frame.setVisible(true);
    }
    private String Uniquename(String playerLabel) {
        String name;
        do {
            name = JOptionPane.showInputDialog(frame, "Enter new name for " + playerLabel);
            if (name != null && name.trim().isEmpty()) {
                // Name is empty, show a message
                JOptionPane.showMessageDialog(frame, "Name cannot be empty. Please enter a different name.");
            } else if (name != null && name.length() > 6) {
                // Name is more than 6 characters long, show a message
                JOptionPane.showMessageDialog(frame, "Name must be up to 6 characters long. Please enter a different name.");
            } else if (usedNames.contains(name)) {
                // Name is already used, show a message
                JOptionPane.showMessageDialog(frame, "Name '" + name + "' is already in use. Please enter a different name.");
            }
        } while (name != null && (name.trim().isEmpty() || name.length() > 6 || usedNames.contains(name)));
        if (name != null) {
            usedNames.add(name);
        }
        return name;
    }
    public static void main(String[] args) {
        new Game();
    }

}
