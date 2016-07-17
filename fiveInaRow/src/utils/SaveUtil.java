package utils;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import fiveInaRow.ChessBoard;
import fiveInaRow.Piece;

public class SaveUtil {
	private static ChessBoard chessBoard = ChessBoard.getInstance();
	
	public static void saveChessHistory() {
		
		
		Piece[] pieces = chessBoard.getPieces();
		int pieceCount = chessBoard.getPieceCount();
		
		long time = System.currentTimeMillis();
		Date now = new Date(time);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		String hehe = dateFormat.format(now);

		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Plain Text(*.txt)", "txt");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showSaveDialog(chooser);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();

			try {
				if (file.exists()) {
					int copy = JOptionPane.showConfirmDialog(null, "File existed,replace it now?", "Save",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (copy == JOptionPane.YES_OPTION) {
						file.delete();
						BufferedWriter bw = new BufferedWriter(new FileWriter(file));
						bw.append("Five In a row History Format\r\n(Index) Role( Index of piece)\r\neg.(7)white(7,7)\r\n");
						for (int i = 0; i < pieceCount; i++) {
							String SerialNumber = "(" + i + ")";
							String color = pieces[i].getColor().equals(Color.black) ? "black" : "white";
							String location = "(" + pieces[i].getX() + "," + pieces[i].getY() + ")" + "\r\n";
							bw.append(SerialNumber + color + location);
						}
						bw.append(hehe);
						bw.close();

					}
				} else {
					BufferedWriter bw = new BufferedWriter(new FileWriter(file + ".txt"));
					bw.append("Five In a row History Format\r\n(Index) Role( Index of piece)\r\neg.(7)white(7,7)\r\n");
					for (int i = 0; i < pieceCount; i++) {
						String SerialNumber = "(" + i + ")";
						String color = pieces[i].getColor().equals(Color.black) ? "black" : "white";
						String location = "(" + pieces[i].getX() + "," + pieces[i].getY() + ")" + "\r\n";
						bw.append(SerialNumber + color + location);
					}
					bw.append("\r\n" + hehe);
					bw.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		if (returnVal == JFileChooser.CANCEL_OPTION) {

		}
	}
}
