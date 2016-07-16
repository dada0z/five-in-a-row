package fiveInaRow;

import java.awt.Color;

public class Piece {
	public static final int DIAMETER = 30;
	private int x;
	private int y;
	private Color color = null;

	public Piece(int x, int y, Color color) {
		this.x = x;
		this.y = y;
		this.color = color;
	}

	public Color getColor() {
		return color;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
}
