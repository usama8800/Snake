import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends JPanel implements Runnable {

	private Graphics2D g;
	private Image offScreen;
	public static Dimension size;
	private Snake snake;
	private Food food;
	private Thread main, specials;
	public static boolean gameOver;
	private int mx = 0, my = 0;
	private Rectangle playAgain;
	private Random rand;
	private Food special;
	public Main Game = this;
	private int score;
	private Font a50;

	public void start() {
		snake = new Snake(Game);
		food = new Food(Game);
		gameOver = false;
		special = null;
		if (!main.isAlive())
			main.start();
		specials = new Thread(new Runnable() {

			public void run() {
				while (true) {
					try {
						// Thread.sleep(15000 + rand.nextInt(30000));
						Thread.sleep(10000);
						// Thread.sleep(60);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (!gameOver) {
						int num = 0;
						if (score > 100) {
							num = 1;
						}
						if (score > 500) {
							num = 1 + rand.nextInt(4);
						}
						if (num == 1)
							special = new Spider(Game);
						if (num > 1 && num < 5)
							special = new Rat(Game);
					}
				}
			}
		});
		specials.start();
	}

	public void run() {
		while (true) {
			if (snake.getSpeed() == 0) {
				gameOver = true;
			}
			score = snake.getScore();
			if (special != null)
				special.update(snake);
			food.update(snake);
			snake.update();
			repaint();
			try {
				Thread.sleep(gameOver ? 400 : 17);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void paint(Graphics g1) {
		g = (Graphics2D) g1;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(new Color(Integer.parseInt("17bf22", 16)));
		g.fillRect(0, 0, getWidth(), getHeight());
		if (special != null)
			special.paint(g);
		food.paint(g);
		snake.paint(g);
		if (gameOver) {
			g.setColor(new Color(0, 0, 0, 100));
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setFont(a50);
			playAgain = drawString(g, "Play Again?", getHeight() / 2 + 100, playAgain);
			g.setFont(a50);
			g.setColor(new Color(Integer.parseInt("7a70ff", 16)));
			g.drawString("You score was " + String.valueOf(snake.getScore()), 210, 250);
			g.setFont(new Font("Arial", Font.PLAIN, 80));
			g.drawString("Game Over!", 200, 150);
		} else {
			g.setFont(new Font("Arial", Font.PLAIN, 20));
			g.setColor(new Color(Integer.parseInt("7a70ff", 16)));
			g.drawString("Score: " + String.valueOf(snake.getScore()), getWidth() - 110, 30);
		}
		g1.drawImage(offScreen, 0, 0, Game);
	}

	public Rectangle drawString(Graphics2D g, String text, int y, Rectangle rect) {
		Rectangle2D strlen = g.getFontMetrics().getStringBounds(text, g);
		if (rect == null) {
			rect = new Rectangle((int) (getWidth() / 2 - strlen.getWidth() / 2), y - (int) strlen.getHeight(),
					(int) strlen.getWidth(), (int) strlen.getHeight());
		}
		if (rect.contains(mx, my)) {
			g.setColor(new Color(220, 50, 50));
			g.setFont(new Font("Arial", Font.BOLD, 50));
			g.drawString(text, (int) (getWidth() / 2 - strlen.getWidth() / 2), y);
		} else {
			g.setColor(new Color(Integer.parseInt("38a9ff", 16)));
			g.drawString(text, (int) (getWidth() / 2 - strlen.getWidth() / 2), y);
		}
		return rect;
	}

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		JFrame frame = new JFrame("Snake");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new Main());
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public Main() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		size = new Dimension((int) (screenSize.width * .7), (int) (screenSize.height * .7));
		setPreferredSize(size);
		setFocusable(true);
		rand = new Random();
		MouseEvents me = new MouseEvents();
		addMouseListener(me);
		addMouseMotionListener(me);
		addKeyListener(new KeyEvents());
		a50 = new Font("Arial", Font.PLAIN, 50);
		main = new Thread(Game);
		start();
	}

	private class MouseEvents extends MouseAdapter {

		public void mouseMoved(MouseEvent e) {
			mx = e.getX();
			my = e.getY();
		}

		public void mouseClicked(MouseEvent e) {
			if (gameOver && playAgain.contains(mx, my))
				start();
		}
	}

	private class KeyEvents extends KeyAdapter {

		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			if (key == KeyEvent.VK_W)
				snake.setDirection(1);
			if (key == KeyEvent.VK_D)
				snake.setDirection(2);
			if (key == KeyEvent.VK_S)
				snake.setDirection(3);
			if (key == KeyEvent.VK_A)
				snake.setDirection(4);
		}
	}
}