package fiveInaRow;

import java.awt.Color;

public class Piece extends Coordinate {
	public static final int DIAMETER = 30;
	private Color color = null;

	public Piece(int x, int y, Color color) {
		this.setX(x);
		this.setY(y);
		this.color = color;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
