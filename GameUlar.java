import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class GameUlar extends JPanel implements ActionListener, KeyListener {
    private class Tile {
        int x;
        int y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }  

    int boardWidth;
    int boardHeight;
    int tileSize = 25;
    
    //ular
    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    //makanan
    Tile makanan;
    Random random;

    //logika di dalam game
    int kecepatanX;
    int kecepatanY;
    Timer gameLoop;

    boolean gameOver = false;

    GameUlar(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();

        makanan = new Tile(10, 10);
        random = new Random();
        tempatMakanan();

        kecepatanX = 1;
        kecepatanY = 0;
        
		//waktu kecepatan gerak ular
		gameLoop = new Timer(100, this);//berapa lama waktu yang dibutuhkan untuk memulai pengatur waktu dalam game, milidetik antar frame
        gameLoop.start();
	}	
    
    public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	public void draw(Graphics g) {
       //Garis Kisi
        for(int i = 0; i < boardWidth/tileSize; i++) {
            //(x1, y1, x2, y2)
            g.drawLine(i*tileSize, 0, i*tileSize, boardHeight);
            g.drawLine(0, i*tileSize, boardWidth, i*tileSize); 
        }

        //makanan
        g.setColor(Color.red);
        // g.fillRect(food.x*tileSize, food.y*tileSize, tileSize, tileSize);
        g.fill3DRect(makanan.x*tileSize, makanan.y*tileSize, tileSize, tileSize, true);

        //kepala ular
        g.setColor(Color.green);
        // g.fillRect(snakeHead.x, snakeHead.y, tileSize, tileSize);
        // g.fillRect(snakeHead.x*tileSize, snakeHead.y*tileSize, tileSize, tileSize);
        g.fill3DRect(snakeHead.x*tileSize, snakeHead.y*tileSize, tileSize, tileSize, true);
        
        //tubuh ular
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            // g.fillRect(snakePart.x*tileSize, snakePart.y*tileSize, tileSize, tileSize);
            g.fill3DRect(snakePart.x*tileSize, snakePart.y*tileSize, tileSize, tileSize, true);
		}

        //Score
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if (gameOver) {
            g.setColor(Color.red);
            g.drawString("Game Over: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        }
        else {
            g.drawString("Score: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        }
	}

    public void tempatMakanan(){
        makanan.x = random.nextInt(boardWidth/tileSize);
		makanan.y = random.nextInt(boardHeight/tileSize);
	}

    public void move() {
        //ketika ular berpindah memakan makanan 
        if (collision(snakeHead, makanan)) {
            snakeBody.add(new Tile(makanan.x, makanan.y));
            tempatMakanan();
        }

        //posisi ular bergerak berpindah
        for (int i = snakeBody.size()-1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if (i == 0) { //pengkondisian tepat sebelum kepala ular
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }
            else {
                Tile prevSnakePart = snakeBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }
        //kepala ular ketika bergerak pindah
        snakeHead.x += kecepatanX;
        snakeHead.y += kecepatanY;

        //pengkondisian ketika game over
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);

            //bertabrakan dengan kepala ular
            if (collision(snakeHead, snakePart)) {
                gameOver = true;
            }
        }

        if (snakeHead.x*tileSize < 0 || snakeHead.x*tileSize > boardWidth || //melewati batas kiri atau batas kanan
            snakeHead.y*tileSize < 0 || snakeHead.y*tileSize > boardHeight ) { //melewati batas atas atau batas bawah
            gameOver = true;
        }
    }

    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) { //dipanggil setiap x milidetik dengan pengatur waktu gameLoop
        move();
        repaint();
        if (gameOver) {
            gameLoop.stop();
        }
    }  

    @Override
    public void keyPressed(KeyEvent e) {
        // System.out.println("KeyEvent: " + e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_UP && kecepatanY != 1) {
            kecepatanX = 0;
            kecepatanY = -1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN && kecepatanY != -1) {
            kecepatanX = 0;
            kecepatanY = 1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT && kecepatanX != 1) {
            kecepatanX = -1;
            kecepatanY = 0;
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT && kecepatanX != -1) {
            kecepatanX = 1;
            kecepatanY = 0;
        }
    }

    //not needed
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}