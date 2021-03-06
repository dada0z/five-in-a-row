﻿package fiveInaRow;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.UIManager;

import utils.ShowMesssage;

public class ChessBoard extends JPanel implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;
	private static final int MARGIN = 30;
	private static int GRID_SPAN = 35;
	private static final int ROWS = 15;
	private static final int COLS = 15;
	private Piece[] pieces = new Piece[(ROWS) * (COLS)];
	private int pieceCount;

	private boolean isBlackFirst;
	private boolean isComputer;
	private boolean isManFirst;
	private String curRole;
	private boolean isGameOver;
	private int curPieceXIndex, curPieceYIndex;
	private Coordinate mCoordinate = new Coordinate();// MousePosition
	private Coordinate pCoordinate = new Coordinate();// PiecePosition

	private static ChessBoard INSTANCE = new ChessBoard();
	private int chessBoardWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
	private int chessBoardHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
	private BufferedImage bgImage;

	public Piece[] getPieces() {
		return pieces;
	}

	public int getPieceCount() {
		return pieceCount;
	}

	public static ChessBoard getInstance() {
		return INSTANCE;
	}

	public void setBlackFirst(boolean isBlackFirst) {
		this.isBlackFirst = isBlackFirst;
	}

	public void setComputer(boolean isComputer) {
		this.isComputer = isComputer;
	}

	public void setManFirst(boolean isManFirst) {
		this.isManFirst = isManFirst;
	}

	public void setCurRole(String curRole) {
		this.curRole = curRole;
	}

	private ChessBoard() {

		try {
			// set the chess board style
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			bgImage = ImageIO.read(new File("background.jpg"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		addMouseListener(this);
		addMouseMotionListener(this);
	}

	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		drawBgImage(g);
		drawChessBoard(g);

		drawChessPieces(g);
	}

	/**
	 * @param g
	 */
	private void drawChessPieces(Graphics g) {
		for (int i = 0; i < pieceCount; i++) {

			int xPos = pieces[i].getX() * GRID_SPAN + MARGIN;
			int yPos = pieces[i].getY() * GRID_SPAN + MARGIN;
			g.setColor(pieces[i].getColor());
			if (pieces[i].getColor() == Color.black) {
				drawBlackPiece(g, xPos, yPos);
			} else {
				drawWhitePiece(g, xPos, yPos);
			}

			Ellipse2D e = new Ellipse2D.Float(xPos - Piece.DIAMETER / 2, yPos - Piece.DIAMETER / 2, 34, 35);
			((Graphics2D) g).fill(e);

			if (i == pieceCount - 1) {
				g.setColor(Color.red);
				g.drawRect(xPos - Piece.DIAMETER / 2, yPos - Piece.DIAMETER / 2, 34, 35);
			}
		}
	}

	/**
	 * @param g
	 * @param xPos
	 * @param yPos
	 */
	private void drawWhitePiece(Graphics g, int xPos, int yPos) {
		RadialGradientPaint paint = new RadialGradientPaint(xPos - Piece.DIAMETER / 2 + 25,
				yPos - Piece.DIAMETER / 2 + 10, 70, new float[] { 0f, 1f }, new Color[] { Color.WHITE, Color.BLACK });
		((Graphics2D) g).setPaint(paint);
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
				RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);
	}

	/**
	 * @param g
	 * @param xPos
	 * @param yPos
	 */
	private void drawBlackPiece(Graphics g, int xPos, int yPos) {
		RadialGradientPaint paint = new RadialGradientPaint(xPos - Piece.DIAMETER / 2 + 25,
				yPos - Piece.DIAMETER / 2 + 10, 20, new float[] { 0f, 1f }, new Color[] { Color.WHITE, Color.BLACK });
		((Graphics2D) g).setPaint(paint);
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
				RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);
	}

	/**
	 * @param g
	 */
	private void drawChessBoard(Graphics g) {
		for (int i = 1; i <= ROWS; i++) {
			g.drawLine(MARGIN, MARGIN + (i - 1) * GRID_SPAN, MARGIN + (COLS - 1) * GRID_SPAN,
					MARGIN + (i - 1) * GRID_SPAN);
		}
		for (int i = 1; i <= COLS; i++) {
			g.drawLine(MARGIN + (i - 1) * GRID_SPAN, MARGIN, MARGIN + (i - 1) * GRID_SPAN,
					MARGIN + (ROWS - 1) * GRID_SPAN);
		}
	}

	/**
	 * @param g
	 */
	private void drawBgImage(Graphics g) {
		g.drawImage(bgImage, 0, 0, chessBoardWidth, chessBoardHeight, this);
	}

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mCoordinate.setXY(e.getX(), e.getY());

		// translate the MousePosition to PiecePosition
		pCoordinate = m2p(mCoordinate);
		// game over
		// outside the board
		// existed
		if (isOutsideBoard(pCoordinate) || isGameOver || hasChess(pCoordinate))
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		else
			setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	@Override
	public void mousePressed(MouseEvent evt) {
		getCurPointIndex(evt);
		handleMousePressed();
	}

	@Override
	public void mouseClicked(MouseEvent evt) {
	}

	@Override
	public void mouseEntered(MouseEvent evt) {
	}

	@Override
	public void mouseExited(MouseEvent evt) {
	}

	@Override
	public void mouseReleased(MouseEvent evt) {
	}

	private Coordinate m2p(Coordinate coordinate) {
		int x = (coordinate.getX() - MARGIN + GRID_SPAN / 2) / GRID_SPAN;
		int y = (coordinate.getY() - MARGIN + GRID_SPAN / 2) / GRID_SPAN;

		coordinate.setXY(x, y);
		return coordinate;
	}

	private boolean isOutsideBoard(Coordinate coordinate) {
		if (coordinate.getX() < 1 || coordinate.getX() > COLS) {
			return true;
		}
		if (coordinate.getY() < 1 || coordinate.getY() > ROWS) {
			return true;
		}

		return false;
	}

	// judge piece if exist at somewhere
	private boolean hasChess(int x, int y) {
		for (Piece c : pieces) {
			if (c != null && c.getX() == x && c.getY() == y)
				return true;
		}
		return false;
	}

	private boolean hasChess(Coordinate coordinate) {
		for (Piece c : pieces) {
			if (c != null && c.getX() == coordinate.getX() && c.getY() == coordinate.getY())
				return true;
		}
		return false;
	}

	public void getCurPointIndex(MouseEvent evt) {
		// translate the MousePosition to PiecePosition
		curPieceXIndex = (evt.getX() - MARGIN + GRID_SPAN / 2) / GRID_SPAN;
		curPieceYIndex = (evt.getY() - MARGIN + GRID_SPAN / 2) / GRID_SPAN;
	}

	public void handleMousePressed() {
		if (!isComputer || (isComputer && curRole == "Man")) {

			// gameover
			// exist
			// outside the board
			if (isGameOver || hasChess(curPieceXIndex, curPieceYIndex) || curPieceXIndex < 0 || curPieceXIndex >= ROWS
					|| curPieceYIndex < 0 || curPieceYIndex >= COLS)
				return;

			placePiece(curPieceXIndex, curPieceYIndex);
			repaint();
			if (isWin()) {
				String msg = String.format("Congratulatin,%s win!", isBlackFirst ? "black" : "white");
				ShowMesssage.showMessage(msg);
				isGameOver = true;
			}
			isBlackFirst = !isBlackFirst;
			curRole = "Machine";
		}
		if (!isGameOver && isComputer && curRole == "Machine") {
			machineDo();
			curRole = "Man";

		}
	}

	public void machineDo() {
		int max_black, max_white, max_temp, max = 0;
		curPieceXIndex = 7;
		curPieceYIndex = 7;
		for (int i = 0; i < COLS; i++) {
			for (int j = 0; j < ROWS; j++) {
				if (!hasChess(i, j)) {
					max_white = searchMax(i, j, Color.white);
					max_black = searchMax(i, j, Color.black);
					max_temp = Math.max(max_white, max_black);
					if (max_temp > max) {
						max = max_temp;
						curPieceXIndex = i;
						curPieceYIndex = j;
					}
				}
			}
		}
		placePiece(curPieceXIndex, curPieceYIndex);
		repaint();
		if (isWin()) {
			String msg = String.format("Congratulation,%s win!", isBlackFirst ? "black" : "white");
			ShowMesssage.showMessage(msg);
			isGameOver = true;
		}
		repaint();
		isBlackFirst = !isBlackFirst;
	}

	private int searchMax(int x, int y, Color color) {
		int num = 0, max_num, max_temp = 0;
		int x_temp = x, y_temp = y;
		int x_temp1 = x_temp, y_temp1 = y_temp;

		for (int i = 1; i < 5; i++) {
			x_temp1 += 1;
			if (x_temp1 >= COLS)
				break;
			if (getChess(x_temp1, y_temp1, color) != null)
				num++;
			else
				break;
		}

		x_temp1 = x_temp;
		for (int i = 1; i < 5; i++) {
			x_temp1 -= 1;
			if (x_temp1 < 0)
				break;
			if (getChess(x_temp1, y_temp1, color) != null)
				num++;
			else
				break;
		}
		if (num < 5)
			max_temp = num;

		x_temp1 = x_temp;
		y_temp1 = y_temp;
		num = 0;
		for (int i = 1; i < 5; i++) {
			y_temp1 -= 1;
			if (y_temp1 < 0)
				break;
			if (getChess(x_temp1, y_temp1, color) != null)
				num++;
			else
				break;
		}

		y_temp1 = y_temp;
		for (int i = 1; i < 5; i++) {
			y_temp1 += 1;
			if (y_temp1 >= ROWS)
				break;
			if (getChess(x_temp1, y_temp1, color) != null)
				num++;
			else
				break;
		}
		if (num > max_temp && num < 5)
			max_temp = num;

		x_temp1 = x_temp;
		y_temp1 = y_temp;
		num = 0;
		for (int i = 1; i < 5; i++) {
			x_temp1 -= 1;
			y_temp1 -= 1;
			if (y_temp1 < 0 || x_temp1 < 0)
				break;
			if (getChess(x_temp1, y_temp1, color) != null)
				num++;
			else
				break;
		}

		x_temp1 = x_temp;
		y_temp1 = y_temp;
		for (int i = 1; i < 5; i++) {
			x_temp1 += 1;
			y_temp1 += 1;
			if (y_temp1 >= ROWS || x_temp1 >= COLS)
				break;
			if (getChess(x_temp1, y_temp1, color) != null)
				num++;
			else
				break;
		}
		if (num > max_temp && num < 5)
			max_temp = num;

		x_temp1 = x_temp;
		y_temp1 = y_temp;
		num = 0;
		for (int i = 1; i < 5; i++) {
			x_temp1 += 1;
			y_temp1 -= 1;
			if (y_temp1 < 0 || x_temp1 >= COLS)
				break;
			if (getChess(x_temp1, y_temp1, color) != null)
				num++;
			else
				break;
		}

		x_temp1 = x_temp;
		y_temp1 = y_temp;
		for (int i = 1; i < 5; i++) {
			x_temp1 -= 1;
			y_temp1 += 1;
			if (y_temp1 >= ROWS || x_temp1 < 0)
				break;
			if (getChess(x_temp1, y_temp1, color) != null)
				num++;
			else
				break;
		}
		if (num > max_temp && num < 5)
			max_temp = num;
		max_num = max_temp;
		return max_num;
	}

	private void placePiece(int x, int y) {
		Color color = isBlackFirst ? Color.black : Color.white;
		Piece piece = new Piece(x, y, color);
		piece.setX(x);
		piece.setY(y);
		pieces[pieceCount++] = piece;
	}

	private boolean isWin() {
		int continueCount = 1;

		for (int x = curPieceXIndex - 1; x >= 0; x--) {
			Color c = isBlackFirst ? Color.black : Color.white;
			if (getChess(x, curPieceYIndex, c) != null) {
				continueCount++;
			} else
				break;
		}

		for (int x = curPieceXIndex + 1; x <= COLS; x++) {
			Color c = isBlackFirst ? Color.black : Color.white;
			if (getChess(x, curPieceYIndex, c) != null) {
				continueCount++;
			} else
				break;
		}
		if (continueCount >= 5) {
			return true;
		}
		continueCount = 1;

		for (int y = curPieceYIndex - 1; y >= 0; y--) {
			Color c = isBlackFirst ? Color.black : Color.white;
			if (getChess(curPieceXIndex, y, c) != null) {
				continueCount++;
			} else
				break;
		}

		for (int y = curPieceYIndex + 1; y <= ROWS; y++) {
			Color c = isBlackFirst ? Color.black : Color.white;
			if (getChess(curPieceXIndex, y, c) != null)
				continueCount++;
			else
				break;

		}
		if (continueCount >= 5)
			return true;
		continueCount = 1;

		for (int x = curPieceXIndex + 1, y = curPieceYIndex - 1; y >= 0 && x <= COLS; x++, y--) {
			Color c = isBlackFirst ? Color.black : Color.white;
			if (getChess(x, y, c) != null) {
				continueCount++;
			} else
				break;
		}

		for (int x = curPieceXIndex - 1, y = curPieceYIndex + 1; x >= 0 && y <= ROWS; x--, y++) {
			Color c = isBlackFirst ? Color.black : Color.white;
			if (getChess(x, y, c) != null) {
				continueCount++;
			} else
				break;
		}
		if (continueCount >= 5)
			return true;
		continueCount = 1;

		for (int x = curPieceXIndex - 1, y = curPieceYIndex - 1; x >= 0 && y >= 0; x--, y--) {
			Color c = isBlackFirst ? Color.black : Color.white;
			if (getChess(x, y, c) != null)
				continueCount++;
			else
				break;
		}

		for (int x = curPieceXIndex + 1, y = curPieceYIndex + 1; x <= COLS && y <= ROWS; x++, y++) {
			Color c = isBlackFirst ? Color.black : Color.white;
			if (getChess(x, y, c) != null)
				continueCount++;
			else
				break;
		}
		if (continueCount >= 5)
			return true;
		continueCount = 1;

		return false;
	}

	private Piece getChess(int xIndex, int yIndex, Color color) {
		for (Piece p : pieces) {
			if (p != null && p.getX() == xIndex && p.getY() == yIndex && p.getColor() == color)
				return p;
		}
		return null;
	}

	public void restartGame() {

		for (int i = 0; i < pieces.length; i++) {
			pieces[i] = null;
		}

		isBlackFirst = true;
		isGameOver = false;
		pieceCount = 0;
		repaint();
	}

	public void undo() {
		if (pieceCount == 0)
			return;
		if (isComputer) {
			if ((isManFirst && isBlackFirst) || (!isManFirst && !isBlackFirst)) {
				pieces[pieceCount - 1] = null;
				pieces[pieceCount - 2] = null;
				pieceCount -= 2;
				curRole = "Man";
			} else {
				pieces[pieceCount - 1] = null;
				pieceCount--;
				isBlackFirst = !isBlackFirst;
			}
		} else {
			pieces[pieceCount - 1] = null;
			pieceCount--;
			isBlackFirst = !isBlackFirst;

		}
		if (pieceCount > 0) {
			curPieceXIndex = pieces[pieceCount - 1].getX();
			curPieceYIndex = pieces[pieceCount - 1].getY();
		}
		isGameOver = false;
		repaint();
	}
}
