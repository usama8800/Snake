import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;

public class Spider extends Food {

	private long startTime;
	private Image spider;

	public Spider(Main main) {
		super(main);
		scorePlus = 50;
		dx = 3;
		spider = new ImageIcon("res/spider.png").getImage();
		startTime = System.currentTimeMillis();
	}

	public void update(Snake snake) {
		super.update(snake);
		move();
		if (System.currentTimeMillis() - startTime >= 10000) {
			eaten();
		}
		dx = snakeBodyCollision(snake, dx, true);
	}

	public void paint(Graphics2D g) {
		if (dx > 0) g.drawImage(spider, x - width, y - height, x + width, y + height, 0, 0, width * 2, height * 2, null);
		else g.drawImage(spider, x - width, y - height, x + width, y + height, 0, height * 2, width * 2, height * 4, null);
	}

	protected void eaten() {
		super.eaten();
		dx = 0;
		x = -100;
		y = -100;
	}
}
