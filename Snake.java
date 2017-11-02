import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

public class Snake {

	private int initialSize, maxSpeed, size, speed, x, y, blockRadius, xr, yr, direction, score;
	private float speedInc;
	private boolean newlyChanged, xOrY;
	private ArrayList<Point> body;

	public Snake(Main main) {
		initialSize = 5;
		newlyChanged = false;
		direction = 2;
		maxSpeed = 6;
		blockRadius = 10;
		x = 400;
		y = 200;
		size = initialSize;
		speedInc = 0;
		speed = 2;
		score = 0;
		xr = 0;
		yr = 0;
		xOrY = true;
		body = new ArrayList<Point>();
		for (int i = 0; i < size; i++) {
			body.add(new Point(x, y));
		}
	}

	public void update() {
		// MOVE
		xOrY = direction == 2 || direction == 4;
		y += !xOrY ? direction == 3 ? speed : speed * -1 : 0;
		x += xOrY ? direction == 2 ? speed : speed * -1 : 0;
		xr = x % blockRadius;
		yr = y % blockRadius;
		if (speed + (direction == 2 || direction == 4 ? xr : yr) >= blockRadius) {
			body.add(new Point(x - xr, y - yr));
			newlyChanged = false;
			speedInc += 0.001;
		}
		if (body.size() > size + (newlyChanged ? 1 : 0)) body.remove(0);
		// ADD SPEED GRADUALLY
		if (speedInc >= 1 && speed < maxSpeed) {
			speed++;
			speedInc = 0;
		}
		// GO THROUGH BORDER
		if (x > Main.size.width + blockRadius) x = 0;
		if (x < 0) x = Main.size.width + blockRadius;
		if (y > Main.size.height + blockRadius) y = 0;
		if (y < 0) y = Main.size.height + blockRadius;
		// DEATH
		try {
			for (int i = 1; i < size - 1; i++)
				if (body.get(i).equals(body.get(body.size() - 1)) && i < body.size() - 5 && size > initialSize) speed = 0;
		} catch (IndexOutOfBoundsException e) {
		}
	}

	public void paint(Graphics2D g) {
		g.setColor(new Color(Integer.parseInt("9eff49", 16)));
		for (int i = 0; i < size; i++) {
			try {
				Point segment = body.get(i);
				g.fillOval(segment.x - blockRadius / 2, segment.y - blockRadius / 2, blockRadius, blockRadius);
			} catch (IndexOutOfBoundsException e) {
			}
		}
	}

	public void grow() {
		size++;
		body.add(new Point(x, y));
	}

	public void setDirection(int d) {
		if (newlyChanged) return;
		if (direction == 2 && d == 4) return;
		if (direction == 4 && d == 2) return;
		if (direction == 1 && d == 3) return;
		if (direction == 3 && d == 1) return;
		direction = d;
		body.add(new Point(x - xr, y - yr));
		newlyChanged = true;
	}

	public int getBlockRadius() {
		return blockRadius;
	}

	public ArrayList<Point> getBody() {
		return body;
	}

	public int getSpeed() {
		return speed;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
}
