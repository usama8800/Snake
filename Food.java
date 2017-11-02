import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Food {

	protected int x, y, width, height, scorePlus, dx, dy;
	protected Clip eat;
	protected Random rand;
	protected URL eatUrl;

	public Food(Main main) {
		rand = new Random();
		width = 5;
		height = 5;
		x = 10 * (10 + rand.nextInt(Main.size.width / 10 - 20));
		y = 10 * (10 + rand.nextInt(Main.size.height / 10 - 20));
		eatUrl = this.getClass().getClassLoader().getResource("res/eat.au");
		setEat();
		scorePlus = 10;
		dx = 0;
		dy = 0;
	}

	private void setEat() {
		try {
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(eatUrl);
	        eat = AudioSystem.getClip();
	        eat.open(audioIn);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	public void update(Snake snake) {
		snakeFaceCollision(snake, true);
	}

	protected void move() {
		if (x + dx < 0 || x + dx > Main.size.width) dx = -dx;
		if (y + dy < 0 || y + dy > Main.size.height) dy = -dy;
		x += dx;
		y += dy;
	}

	protected void snakeFaceCollision(Snake snake, boolean round) {
		int blockRadius = snake.getBlockRadius();
		ArrayList<Point> body = snake.getBody();
		Point faceP = body.get(body.size() - 1);
		if (round) {
			int a = Math.abs(faceP.y - y);
			int b = Math.abs(faceP.x - x);
			double c = Math.sqrt(a * a + b * b);
			if (c < blockRadius / 2 + width) snakeFaceCollided(snake);
		} else {
			Rectangle thisR = new Rectangle(x, y, width, height);
			if (thisR.contains(faceP)) {
				snakeFaceCollided(snake);
			}
		}
	}

	protected void snakeFaceCollided(Snake snake) {
		eaten();
		snake.setSize(snake.getSize() + 1);
		snake.setScore(snake.getScore() + scorePlus);
		eat.start();
		eat.addLineListener(new LineListener() {
			public void update(LineEvent event) {
				if (event.getType().equals(LineEvent.Type.STOP)) {
					eat.close();
					setEat();
				}
			}
		});
	}

	protected int snakeBodyCollision(Snake snake, int d, boolean round) {
		if (Main.gameOver) return 0;
		boolean collision = false;
		int blockRadius = snake.getBlockRadius();
		ArrayList<Point> body = snake.getBody();
		Point face = body.get(body.size() - 1);
		Rectangle thisR = new Rectangle(x, y, width, height);
		Point segmentC = null;
		for (int i = 0; i < snake.getSize(); i++) {
			if (segmentC != null) break;
			try {
				Point segmentP = body.get(i);
				if (!segmentP.equals(face)) {
					if (round) {
						int a = Math.abs(segmentP.y - y);
						int b = Math.abs(segmentP.x - x);
						double c = Math.sqrt(a * a + b * b);
						if (c < blockRadius / 2 + width) {
							collision = true;
							segmentC = segmentP;
							break;
						}
					} else {
						if (thisR.contains(segmentP)) {
							collision = true;
							segmentC = segmentP;
							break;
						}
					}
				}
			} catch (IndexOutOfBoundsException e) {
			}
		}
		if (collision && segmentC != null) {
			d = -d;
		}
		return d;
	}

	protected void eaten() {
		x = 10 * (10 + rand.nextInt(Main.size.width / 10 - 20));
		y = 10 * (10 + rand.nextInt(Main.size.height / 10 - 20));
	}

	public void paint(Graphics2D g) {
		g.setColor(new Color(Integer.parseInt("188022", 16)));
		g.fillOval(x - width, y - width, width * 2, width * 2);
	}
}
