import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;

public class Rat extends Food {

	private long startTime;
	private Image rat;
	private int frames;
	private double frame, animSpeed;

	public Rat(Main main) {
		super(main);
		scorePlus = 100;
		width = 10;
		height = 20;
		dy = 3;
		rat = new ImageIcon("res/spider.png").getImage();
		startTime = System.currentTimeMillis();
		frames = 2;
		frame = 0;
		animSpeed = 0.1;
	}

	public void update(Snake snake) {
		snakeFaceCollision(snake, false);
		move();
		if (System.currentTimeMillis() - startTime >= 10000) {
			eaten();
		}
		dy = snakeBodyCollision(snake, dy, false);
		frame = frame + animSpeed < frames ? frame+animSpeed:0;
	}

	protected void eaten() {
		super.eaten();
		dy = 0;
		x = -100;
		y = -100;
	}

	public void paint(Graphics2D g) {
		if (dy > 0) g.drawImage(rat, x - 3, y, x - 3 + width, y + height, width * (int) frame, 0, width + width * (int) frame, height, null);
		else g.drawImage(rat, x - 3, y, x - 3 + width, y + height, width * (int) frame, height, width + width * (int) frame, height * 2, null);
	}
}
