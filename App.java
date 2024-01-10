import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        int boardWidth = 600;
        int boardHeight = boardWidth;

        JFrame frame = new JFrame("Ular");
        frame.setVisible(true);
	frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GameUlar gameUlar = new GameUlar(boardWidth, boardHeight);
        frame.add(gameUlar);
        frame.pack();
        gameUlar.requestFocus();
    }
}