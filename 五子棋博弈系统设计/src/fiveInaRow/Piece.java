package �����岩��ϵͳ���;

import java.awt.Color;

public class Piece {
    private int x = -1;// �����е�x����
    private int y = -1;// �����е�y����
    private Color color = null;// ��ɫ
    public static final int DIAMETER = 30;// ֱ��

    public void setColor(Color color) {
	this.color = color;
    }

    public void setX(int x) {
	this.x = x;
    }

    public void setY(int y) {
	this.y = y;
    }

    public Piece(int x, int y, Color color) {
	this.x = x;
	this.y = y;
	this.color = color;
    }

    public int getX() {
	return x;
    }

    public int getY() {
	return y;
    }

    public Color getColor() {
	return color;
    }
}
