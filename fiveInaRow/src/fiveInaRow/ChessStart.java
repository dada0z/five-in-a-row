package fiveInaRow;

import javax.swing.JFrame;

public class ChessStart extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int HEIGHT = 600, WIDTH = 560;

	public static void main(String[] args) {

		ChessFrame chessFrame = new ChessFrame();

		chessFrame.setBounds(0, 0, WIDTH, HEIGHT);
		chessFrame.setVisible(true);
		chessFrame.setResizable(false);
	}
}
