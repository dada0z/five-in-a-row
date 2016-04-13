package 五子棋博弈系统设计;

import java.awt.Color;

public class Piece {
    private int x = -1;// 棋盘中的x索引
    private int y = -1;// 棋盘中的y索引
    private Color color = null;// 颜色
    public static final int DIAMETER = 30;// 直径

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
