package fiveInaRow;

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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ChessBoard extends JPanel implements MouseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int MARGIN = 30;// �߾�
	public static int GRID_SPAN = 35;// ������
	public static final int ROWS = 15;
	public static final int COLS = 15;

	Piece[] pieces = new Piece[(ROWS) * (COLS)];
	boolean isBlack;// �Ƿ�������֣�Ĭ�Ͽ�ʼ�Ǻ�������
	boolean isComputer;// �Ƿ�ǰ��ɫΪ�����
	boolean isFirst;// �������Ϊ��
	String curRole;// ��ǰ��ɫ
	boolean isGameOver = false;// �Ƿ���Ϸ������Ĭ����Ϸδ����
	int pointCount = 0;// ��ǰ�������ӵĸ���
	int curPointHIndex, curPointVIndex;// ��ǰ�������ӵ�����

	Color colortemp;
	int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	int height = Toolkit.getDefaultToolkit().getScreenSize().height;
	BufferedImage bgImage = null;

	public ChessBoard() {

		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException e1) {
			// TODO
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			// TODO
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			// TODO
			e1.printStackTrace();
		}
		try {
			bgImage = ImageIO.read(new File("background.jpg"));
		} catch (IOException e1) {
			// TODO
			e1.printStackTrace();
		}
		addMouseListener(this);
		addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent e) {

			}

			public void mouseMoved(MouseEvent e) {
				// �������������λ��ת����������
				int x = (e.getX() - MARGIN + GRID_SPAN / 2) / GRID_SPAN;
				int y = (e.getY() - MARGIN + GRID_SPAN / 2) / GRID_SPAN;
				// ��Ϸ�Ѿ�����������
				// ���������ⲻ����
				// x��yλ���Ѿ������Ӵ��ڣ�������
				if (x < 1 || x > ROWS || y < 1 || y > COLS || isGameOver
						|| hasChess(x, y))
					setCursor(new Cursor(Cursor.DEFAULT_CURSOR));// Ĭ������ʾ��������
				else
					setCursor(new Cursor(Cursor.HAND_CURSOR));// ��״����ʾ��������
			}
		});
	}

	// ����
	public void paintComponent(Graphics g) {

		super.paintComponent(g);// ������
		g.drawImage(bgImage, 0, 0, width, height, this);
		for (int i = 1; i <= ROWS; i++) {// ������
			g.drawLine(MARGIN, MARGIN + (i - 1) * GRID_SPAN, MARGIN
					+ (COLS - 1) * GRID_SPAN, MARGIN + (i - 1) * GRID_SPAN);
		}
		for (int i = 1; i <= COLS; i++) {// ������
			g.drawLine(MARGIN + (i - 1) * GRID_SPAN, MARGIN, MARGIN + (i - 1)
					* GRID_SPAN, MARGIN + (ROWS - 1) * GRID_SPAN);
		}

		// ������
		for (int i = 0; i < pointCount; i++) {
			// ���񽻲��x��y����
			int xPos = pieces[i].getX() * GRID_SPAN + MARGIN;
			int yPos = pieces[i].getY() * GRID_SPAN + MARGIN;
			g.setColor(pieces[i].getColor());// ������ɫ
			colortemp = pieces[i].getColor();
			if (colortemp == Color.black) {
				RadialGradientPaint paint = new RadialGradientPaint(xPos
						- Piece.DIAMETER / 2 + 25, yPos - Piece.DIAMETER / 2
						+ 10, 20, new float[] { 0f, 1f }, new Color[] {
						Color.WHITE, Color.BLACK });
				((Graphics2D) g).setPaint(paint);
				((Graphics2D) g).setRenderingHint(
						RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
				((Graphics2D) g).setRenderingHint(
						RenderingHints.KEY_ALPHA_INTERPOLATION,
						RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);
			} else if (colortemp == Color.white) {
				RadialGradientPaint paint = new RadialGradientPaint(xPos
						- Piece.DIAMETER / 2 + 25, yPos - Piece.DIAMETER / 2
						+ 10, 70, new float[] { 0f, 1f }, new Color[] {
						Color.WHITE, Color.BLACK });
				((Graphics2D) g).setPaint(paint);
				((Graphics2D) g).setRenderingHint(
						RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
				((Graphics2D) g).setRenderingHint(
						RenderingHints.KEY_ALPHA_INTERPOLATION,
						RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);

			}

			Ellipse2D e = new Ellipse2D.Float(xPos - Piece.DIAMETER / 2, yPos
					- Piece.DIAMETER / 2, 34, 35);
			((Graphics2D) g).fill(e);
			// ������һ�����ӵĺ���ο�

			if (i == pointCount - 1) {// ��������һ������
				g.setColor(Color.red);
				g.drawRect(xPos - Piece.DIAMETER / 2,
						yPos - Piece.DIAMETER / 2, 34, 35);
			}
		}
	}

	public void mousePressed(MouseEvent evt) {// ���������ϰ���ʱ����

		getCurPointIndex(evt);
		handleMousePressed();
	}

	// ����mouseListener�ķ���
	public void mouseClicked(MouseEvent evt) {
		// ��갴��������ϵ���ʱ����
	}

	public void mouseEntered(MouseEvent evt) {
		// �����뵽�����ʱ����
	}

	public void mouseExited(MouseEvent evt) {
		// ����뿪���ʱ����
	}

	public void mouseReleased(MouseEvent evt) {
		// ��갴ť��������ͷ�ʱ����
	}

	// �����������в����Ƿ�������Ϊx��y�����Ӵ���
	private boolean hasChess(int x, int y) {
		for (Piece c : pieces) {
			if (c != null && c.getX() == x && c.getY() == y)
				return true;
		}
		return false;
	}

	public void getCurPointIndex(MouseEvent evt) {
		// �������������λ��ת������������
		curPointHIndex = (evt.getX() - MARGIN + GRID_SPAN / 2) / GRID_SPAN;
		curPointVIndex = (evt.getY() - MARGIN + GRID_SPAN / 2) / GRID_SPAN;
	}

	public void handleMousePressed() {
		if (!isComputer || (isComputer && curRole == "���")) {

			// ��Ϸ����ʱ����������
			// ���x��yλ���Ѿ������Ӵ��ڣ�������
			// ���������ⲻ����
			if (isGameOver || hasChess(curPointHIndex, curPointVIndex)
					|| curPointHIndex < 0 || curPointHIndex >= ROWS
					|| curPointVIndex < 0 || curPointVIndex >= COLS)
				return;

			placePiece(curPointHIndex, curPointVIndex);
			repaint();
			if (isWin()) {
				String msg = String.format("��ϲ��%sӮ�ˣ�", isBlack ? "����" : "����");
				showMessage(msg);
				isGameOver = true;
			}
			// if (!isGameOver) {
			isBlack = !isBlack;
			// }
			curRole = "�����";
		}
		if (!isGameOver && isComputer && curRole == "�����") {
			machineDo();
			curRole = "���";

		}
	}

	public void machineDo() {
		int max_black, max_white, max_temp, max = 0;
		curPointHIndex = 7;
		curPointVIndex = 7;
		for (int i = 0; i < COLS; i++) {
			for (int j = 0; j < ROWS; j++) {
				if (!hasChess(i, j)) {// �㷨�ж��Ƿ�����
					max_white = searchMax(i, j, Color.white);// �жϰ��ӵ����ֵ
					max_black = searchMax(i, j, Color.black);// �жϺ��ӵ����ֵ
					max_temp = Math.max(max_white, max_black);
					if (max_temp > max) {
						max = max_temp;
						curPointHIndex = i;
						curPointVIndex = j;
					}
				}
			}
		}
		placePiece(curPointHIndex, curPointVIndex);
		repaint();
		if (isWin()) {
			String msg = String.format("��ϲ��%sӮ�ˣ�", isBlack ? "����" : "����");
			showMessage(msg);
			isGameOver = true;
		}
		repaint();
		// if (!isGameOver) {
		isBlack = !isBlack;
		// }

	}

	// ����������ĳһ�����ϰ˸��������ӵ����ֵ��
	// ��˸�����ֱ��ǣ����������ϡ��������������ϡ����ϡ���������
	private int searchMax(int x, int y, Color color) {
		int num = 0, max_num, max_temp = 0;
		int x_temp = x, y_temp = y;
		int x_temp1 = x_temp, y_temp1 = y_temp;
		// �ж϶���
		for (int i = 1; i < 5; i++) {
			x_temp1 += 1;
			if (x_temp1 >= COLS)
				break;
			if (getChess(x_temp1, y_temp1, color) != null)
				num++;
			else
				break;
		}
		// �ж�����
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

		// �жϱ���
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
		// �ж��Ϸ�
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

		// �ж�����
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
		// �ж϶���
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

		// �ж϶���
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
		// �ж�����
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
		Color color = isBlack ? Color.black : Color.white;
		Piece piece = new Piece(x, y, color);
		piece.setX(x);
		piece.setY(y);
		// System.out
		// .println(piece.getColor().equals(Color.black) ? "�ڷ���" : "�׷���");
		pieces[pointCount++] = piece;

		// System.out.println("(" + piece.getX() + "," + piece.getY() + ")");

	}

	private boolean isWin() {
		int continueCount = 1;// �������ӵĸ���

		// ��������Ѱ��
		for (int x = curPointHIndex - 1; x >= 0; x--) {
			Color c = isBlack ? Color.black : Color.white;
			if (getChess(x, curPointVIndex, c) != null) {
				continueCount++;
			} else
				break;
		}
		// ������Ѱ��
		for (int x = curPointHIndex + 1; x <= COLS; x++) {
			Color c = isBlack ? Color.black : Color.white;
			if (getChess(x, curPointVIndex, c) != null) {
				continueCount++;
			} else
				break;
		}
		if (continueCount >= 5) {
			return true;
		}
		continueCount = 1;

		// ������һ����������
		// ��������
		for (int y = curPointVIndex - 1; y >= 0; y--) {
			Color c = isBlack ? Color.black : Color.white;
			if (getChess(curPointHIndex, y, c) != null) {
				continueCount++;
			} else
				break;
		}
		// ��������Ѱ��
		for (int y = curPointVIndex + 1; y <= ROWS; y++) {
			Color c = isBlack ? Color.black : Color.white;
			if (getChess(curPointHIndex, y, c) != null)
				continueCount++;
			else
				break;

		}
		if (continueCount >= 5)
			return true;
		continueCount = 1;

		// ������һ�������������б��
		// ����Ѱ��
		for (int x = curPointHIndex + 1, y = curPointVIndex - 1; y >= 0
				&& x <= COLS; x++, y--) {
			Color c = isBlack ? Color.black : Color.white;
			if (getChess(x, y, c) != null) {
				continueCount++;
			} else
				break;
		}
		// ����Ѱ��
		for (int x = curPointHIndex - 1, y = curPointVIndex + 1; x >= 0
				&& y <= ROWS; x--, y++) {
			Color c = isBlack ? Color.black : Color.white;
			if (getChess(x, y, c) != null) {
				continueCount++;
			} else
				break;
		}
		if (continueCount >= 5)
			return true;
		continueCount = 1;

		// ������һ�������������б��
		// ����Ѱ��
		for (int x = curPointHIndex - 1, y = curPointVIndex - 1; x >= 0
				&& y >= 0; x--, y--) {
			Color c = isBlack ? Color.black : Color.white;
			if (getChess(x, y, c) != null)
				continueCount++;
			else
				break;
		}
		// ����Ѱ��
		for (int x = curPointHIndex + 1, y = curPointVIndex + 1; x <= COLS
				&& y <= ROWS; x++, y++) {
			Color c = isBlack ? Color.black : Color.white;
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
			if (p != null && p.getX() == xIndex && p.getY() == yIndex
					&& p.getColor() == color)
				return p;
		}
		return null;
	}

	public void restartGame() {
		// �������
		for (int i = 0; i < pieces.length; i++) {
			pieces[i] = null;
		}
		// �ָ���Ϸ��صı���ֵ
		isBlack = true;
		isGameOver = false; // ��Ϸ�Ƿ����
		pointCount = 0; // ��ǰ�������Ӹ���
		repaint();
	}

	// ����
	public void undo() {
		if (pointCount == 0)
			return;
		if (isComputer) {
			if ((isFirst && isBlack) || (!isFirst && !isBlack)) {
				pieces[pointCount - 1] = null;
				pieces[pointCount - 2] = null;
				pointCount -= 2;
				curRole = "���";
			} else {
				pieces[pointCount - 1] = null;
				pointCount--;
				isBlack = !isBlack;
			}
		} else {
			pieces[pointCount - 1] = null;
			pointCount--;
			isBlack = !isBlack;

		}
		if (pointCount > 0) {
			curPointHIndex = pieces[pointCount - 1].getX();
			curPointVIndex = pieces[pointCount - 1].getY();
		}
		isGameOver = false;
		repaint();
	}

	public void showMessage(String msg) {
		JOptionPane.showMessageDialog(this, msg);
	};

	public int showWarning(String msg) {
		String[] options = { "ȷ��", "ȡ��" };
		int value = JOptionPane.showOptionDialog(null, msg, "����",
				JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,
				null, options, null);
		return value;
	}

	public void saveChessManual() {// ��������

		long time = System.currentTimeMillis();
		Date now = new Date(time);
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy/MM/dd HH:mm:ss");// ���Է�����޸����ڸ�ʽ

		String hehe = dateFormat.format(now);

		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"�ı��ĵ�(*.txt)", "txt");// ����������
		chooser.setFileFilter(filter);// ��ʼ����
		int returnVal = chooser.showSaveDialog(chooser);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();

			try {
				if (file.exists()) {
					int copy = JOptionPane.showConfirmDialog(null,
							"�ļ��Ѵ��ڣ��Ƿ�Ҫ�滻��ǰ�ļ���", "����",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE);
					if (copy == JOptionPane.YES_OPTION) {
						file.delete();
						BufferedWriter bw = new BufferedWriter(new FileWriter(
								file));
						bw.append("���������׸�ʽ\r\n(���) ���( ��������)\r\n���磺(7)����(7,7)\r\n");
						for (int i = 0; i < pointCount; i++) {
							String SerialNumber = "(" + i + ")";
							String color = pieces[i].getColor().equals(
									Color.black) ? "����" : "����";
							String location = "(" + pieces[i].getX() + ","
									+ pieces[i].getY() + ")" + "\r\n";
							bw.append(SerialNumber + color + location);
						}
						bw.append(hehe);
						bw.close();

					}
				} else {
					BufferedWriter bw = new BufferedWriter(new FileWriter(file
							+ ".txt"));
					bw.append("���������׸�ʽ\r\n(���) ���( ��������)\r\n���磺(7)����(7,7)\r\n");
					for (int i = 0; i < pointCount; i++) {
						String SerialNumber = "(" + i + ")";
						String color = pieces[i].getColor().equals(Color.black) ? "����"
								: "����";
						String location = "(" + pieces[i].getX() + ","
								+ pieces[i].getY() + ")" + "\r\n";
						bw.append(SerialNumber + color + location);
					}
					bw.append("\r\n" + hehe);
					bw.close();
				}

			} catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}

		}
		if (returnVal == JFileChooser.CANCEL_OPTION) {

		}
	}
}
