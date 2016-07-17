package utils;

import javax.swing.JOptionPane;

import fiveInaRow.ChessBoard;

public class ShowMesssage {
	private static ChessBoard chessBoard = ChessBoard.getInstance();
	public static void showMessage(String msg) {
		JOptionPane.showMessageDialog(chessBoard, msg);
	};

	public static int showWarning(String msg) {
		String[] options = { "OK", "Cancel" };
		int value = JOptionPane.showOptionDialog(null, msg, "Warning", JOptionPane.YES_NO_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null, options, null);
		return value;
	}
}
