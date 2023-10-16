import Player.Player;
import Dice.Dice;

import javax.swing.*;
import java.awt.*;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

public class Game extends JFrame {
    int currentPlayer = 1;
    int seconds=60;
    boolean turnEnded = true;
    Dice dice = new Dice();
    private final int diceRoll = dice.roll();
    JFrame frame = new JFrame();
    JButton startGameButton = new JButton();
    JLabel titleLabel = new JLabel();
    JTextArea textfield = new JTextArea();
    private final java.util.List<Player> players = new ArrayList<>();
    private final java.util.List<JButton> playerButtons = new ArrayList<>();
    private final java.util.List<JLabel> playerScoreLabels = new ArrayList<>();
    JButton actionsButton = new JButton();
    JButton dice_button = new JButton();
    JLabel time_label = new JLabel();
    JLabel seconds_left = new JLabel();
    HashSet<String> usedNames = new HashSet<>();
    JButton endTurnButton = new JButton();
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
            textArea.setCaretPosition(textArea.getDocument().getLength()); // Scroll to the bottom
        }
    }

    public Game() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(650, 650);
        frame.getContentPane().setBackground(new Color(50, 50, 50));
        frame.setLayout(null);
        frame.setResizable(false);

        titleLabel.setBounds(210, 10, 650, 50);
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 40));
        titleLabel.setForeground(new Color(255, 255, 255)); // Set color to white
        titleLabel.setText("Game Title");

        textfield.setBounds(100, 100, 620, 400);
        textfield.setBackground(new Color(25, 25, 25));
        textfield.setForeground(new Color(255, 255, 255));
        textfield.setFont(new Font("Times New Roman", Font.BOLD, 20));
        textfield.setBorder(BorderFactory.createBevelBorder(1));
        textfield.setEditable(false);
        textfield.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(textfield);
        scrollPane.setBounds(100, 100, 540, 400);

        PrintStream printStream = new PrintStream(new TextAreaOutputStream(textfield, 5000));
        System.setOut(printStream);
        System.setErr(printStream);

        int numPlayers;
        while (true) {
            String numPlayersStr = JOptionPane.showInputDialog("Enter the number of players (1-4) or type 'exit' to quit:");
            if ("exit".equalsIgnoreCase(numPlayersStr)) {
                System.exit(0); // Exit the program if the user types 'exit'
            }
            try {
                numPlayers = Integer.parseInt(numPlayersStr);
                if (numPlayers >= 1 && numPlayers <= 4) {
                    break; // Exit the loop if the number is valid
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid number of players. Please enter a number between 1 and 4.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a number.");
            }
        }
        for (int i = 0; i < numPlayers; i++) {
            final int finalI = i; // Create a final copy of i
            String playerName = "Player " + (i + 1);
            players.add(new Player(playerName)); // Use add() method to add a new player
            JButton playerButton = new JButton();
            playerButton.setBounds(0, 100 + (i * 100), 98, 50);
            playerButton.setFont(new Font("Times New Roman", Font.BOLD, 15));
            playerButton.setFocusable(false);
            playerButton.setText(playerName);
            JLabel scoreLabel = new JLabel("Score: 0: ");
            playerScoreLabels.add(scoreLabel);
            playerButton.addActionListener(e -> {
                playerScoreLabels.add(scoreLabel);
                JLabel resourceLabel = new JLabel("Resources: " + players.get(finalI).getResources());
                playerScoreLabels.add(resourceLabel);
                // Create a new JFrame to display the player's score, resources and tiles owned
                JFrame scoreFrame = new JFrame("Score, Resources and Tiles Owned");
                scoreFrame.setSize(400, 200);
                scoreFrame.setLayout(new BoxLayout(scoreFrame.getContentPane(), BoxLayout.Y_AXIS)); // Set layout to BoxLayout to arrange labels vertically
                scoreFrame.add(scoreLabel);
                scoreFrame.add(resourceLabel);
                scoreFrame.setVisible(true);
            });
            playerButtons.add(playerButton); // Use add() method to add the button to the list
            frame.add(playerButton);
        }

        seconds_left.setBounds(535,510,100,100);
        seconds_left.setBackground(new Color(25,25,25));
        seconds_left.setForeground(new Color(255,0,0));
        seconds_left.setFont(new Font("Times New Roman",Font.BOLD,60));
        seconds_left.setBorder(BorderFactory.createBevelBorder(1));
        seconds_left.setOpaque(true);
        seconds_left.setHorizontalAlignment(JTextField.CENTER);
        seconds_left.setText(String.valueOf(seconds));

        time_label.setBounds(535,510,100,25);
        time_label.setBackground(new Color(50,50,50));
        time_label.setForeground(new Color(255,0,0));
        time_label.setFont(new Font("Times New Roman",Font.PLAIN,16));
        time_label.setHorizontalAlignment(JTextField.CENTER);
        time_label.setText("timer >:D");

        dice_button.setBounds(335,525,200,100);
        dice_button.setFont(new Font("Times New Roman",Font.BOLD,35));
        dice_button.setFocusable(false);
        dice_button.setEnabled(false);
        dice_button.setText("Roll Dice");
        dice_button.addActionListener(e -> {
            if (turnEnded) {
                // Roll the dice and perform actions for the current player
                int diceRoll = rollDice();
                System.out.println(players.get(currentPlayer - 1).getName() + " rolled a " + diceRoll);

                // Get the current player's position on the game board
                int currentPosition = players.get(currentPlayer- 1).getPosition();

                // Update the player's position by adding the dice roll
                int newPosition = currentPosition + diceRoll;

                // Check if the new position exceeds the maximum position on the game board

                players.get(currentPlayer - 1).setPosition(newPosition);

                dice_button.setEnabled(false);

                turnEnded = false; // Set turnEnded to false to indicate that the current player's turn has not ended
            } else {
                System.out.println("End your turn before the next player can roll the dice.");
            }
        });
        actionsButton.setBounds(125, 525, 200, 100); // Adjust these values as needed
        actionsButton.setFont(new Font("Times New Roman", Font.BOLD, 35));
        actionsButton.setFocusable(false);
        actionsButton.setEnabled(false);
        actionsButton.setText("Actions");
        actionsButton.addActionListener(a -> {
            String[] options = {"Option 1", "Option 2", "Option 3", "Option 4", "Option 5"};
            String selectedOption = (String) JOptionPane.showInputDialog(
                    frame,
                    playerButtons.get(currentPlayer - 1).getText() + ", choose an action:",
                    "Action Selection - " + currentPlayer,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );
            // Handle the selected option
            if (selectedOption != null) {
                System.out.println(playerButtons.get(currentPlayer - 1).getText() + " You selected: " + selectedOption);
            }
        });

        Timer timer = new Timer(1000, e -> {
            seconds--;
            seconds_left.setText(String.valueOf(seconds));
            if (seconds <= 0) {
                ((Timer)e.getSource()).stop();
                System.out.print("Game over\n");
            }

        });

        startGameButton.setBounds(0,500,100,110);// Adjust these values as needed
        startGameButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
        startGameButton.setFocusable(false);
        startGameButton.setText("Start");
        startGameButton.addActionListener(e -> {
            dice_button.setEnabled(true);
            actionsButton.setEnabled(true);
            seconds = 60; // Reset the seconds variable
            System.out.println("game has started");
            seconds_left.setText(String.valueOf(seconds)); // Update the seconds_left label
            timer.start(); // Start the timer
            frame.remove(startGameButton);
            frame.add(endTurnButton);
        });

        endTurnButton.setBounds(0, 500, 100, 110); // Adjust these values as needed
        endTurnButton.setFont(new Font("Times New Roman", Font.BOLD, 15));
        endTurnButton.setFocusable(false);
        endTurnButton.setText("<html>End<br>Turn</html>");
        endTurnButton.addActionListener(e -> {
            seconds = 60;
            seconds_left.setText(String.valueOf(seconds)); // Update the seconds_left label
            timer.start(); // Start the timer
            System.out.println(playerButtons.get(currentPlayer - 1).getText() + " has ended their turn");
            turnEnded = true; // Set turnEnded to true to indicate that the current player's turn has ended
            currentPlayer = (currentPlayer % players.size()) + 1; // Switch to the next player
            dice_button.setEnabled(true);
        });

        promptPlayerNames();
        frame.add(startGameButton);
        frame.add(titleLabel);
        frame.add(scrollPane);
        frame.add(time_label);
        frame.add(seconds_left);
        frame.add(dice_button);
        frame.add(actionsButton);
        frame.setVisible(true);
    }
    private String Uniquename(String playerLabel) {
        String name;
        do {
            name = JOptionPane.showInputDialog(frame, "Enter new name for " + playerLabel);
            if (name != null && name.trim().isEmpty()) {
                // Name is empty, show a message
                JOptionPane.showMessageDialog(frame, "Name cannot be empty. Please enter a different name.");
            } else if (name != null && name.length() > 8) {
                // Name is more than 8 characters long, show a message
                JOptionPane.showMessageDialog(frame, "Name must be up to 8 characters long. Please enter a different name.");
            } else if (usedNames.contains(name)) {
                // Name is already used, show a message
                JOptionPane.showMessageDialog(frame, "Name '" + name + "' is already in use. Please enter a different name.");
            }
        } while (name != null && (name.trim().isEmpty() || name.length() > 8 || usedNames.contains(name)));
        if (name != null) {
            usedNames.add(name);
        }
        return name;
    }

    private void promptPlayerNames() {
        for (int i = 0; i < playerButtons.size(); i++) {
            String newName = Uniquename(playerButtons.get(i).getText());
            if (newName != null) {
                players.get(i).setName(newName);
                playerButtons.get(i).setText(newName);
                System.out.print("Player " + (i + 1) + " has changed their name to " + newName + "\n");
            }
        }
    }
    public static void main(String[] args) {
        new Game();
    }
    private int rollDice() {
        // Generate random numbers between 1 and 6 for two dice
        int dice1 = (int) (Math.random() * 6) + 1;
        int dice2 = (int) (Math.random() * 6) + 1;
        // Calculate the sum of the two dice rolls
        int sum = dice1 + dice2;

        return sum;
    }
}
