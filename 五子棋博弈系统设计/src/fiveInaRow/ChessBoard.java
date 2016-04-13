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
	public static final int MARGIN = 30;// 边距
	public static int GRID_SPAN = 35;// 网格间距
	public static final int ROWS = 15;
	public static final int COLS = 15;

	Piece[] pieces = new Piece[(ROWS) * (COLS)];
	boolean isBlack;// 是否黑棋先手，默认开始是黑棋先手
	boolean isComputer;// 是否当前角色为计算机
	boolean isFirst;// 玩家先手为真
	String curRole;// 当前角色
	boolean isGameOver = false;// 是否游戏结束，默认游戏未结束
	int pointCount = 0;// 当前棋盘棋子的个数
	int curPointHIndex, curPointVIndex;// 当前刚下棋子的索引

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
				// 将鼠标点击的坐标位置转成网格索引
				int x = (e.getX() - MARGIN + GRID_SPAN / 2) / GRID_SPAN;
				int y = (e.getY() - MARGIN + GRID_SPAN / 2) / GRID_SPAN;
				// 游戏已经结束不能下
				// 落在棋盘外不能下
				// x，y位置已经有棋子存在，不能下
				if (x < 1 || x > ROWS || y < 1 || y > COLS || isGameOver
						|| hasChess(x, y))
					setCursor(new Cursor(Cursor.DEFAULT_CURSOR));// 默认鼠标表示不能下子
				else
					setCursor(new Cursor(Cursor.HAND_CURSOR));// 手状鼠标表示可以下子
			}
		});
	}

	// 绘制
	public void paintComponent(Graphics g) {

		super.paintComponent(g);// 画棋盘
		g.drawImage(bgImage, 0, 0, width, height, this);
		for (int i = 1; i <= ROWS; i++) {// 画横线
			g.drawLine(MARGIN, MARGIN + (i - 1) * GRID_SPAN, MARGIN
					+ (COLS - 1) * GRID_SPAN, MARGIN + (i - 1) * GRID_SPAN);
		}
		for (int i = 1; i <= COLS; i++) {// 画竖线
			g.drawLine(MARGIN + (i - 1) * GRID_SPAN, MARGIN, MARGIN + (i - 1)
					* GRID_SPAN, MARGIN + (ROWS - 1) * GRID_SPAN);
		}

		// 画棋子
		for (int i = 0; i < pointCount; i++) {
			// 网格交叉点x，y坐标
			int xPos = pieces[i].getX() * GRID_SPAN + MARGIN;
			int yPos = pieces[i].getY() * GRID_SPAN + MARGIN;
			g.setColor(pieces[i].getColor());// 设置颜色
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
			// 标记最后一个棋子的红矩形框

			if (i == pointCount - 1) {// 如果是最后一个棋子
				g.setColor(Color.red);
				g.drawRect(xPos - Piece.DIAMETER / 2,
						yPos - Piece.DIAMETER / 2, 34, 35);
			}
		}
	}

	public void mousePressed(MouseEvent evt) {// 鼠标在组件上按下时调用

		getCurPointIndex(evt);
		handleMousePressed();
	}

	// 覆盖mouseListener的方法
	public void mouseClicked(MouseEvent evt) {
		// 鼠标按键在组件上单击时调用
	}

	public void mouseEntered(MouseEvent evt) {
		// 鼠标进入到组件上时调用
	}

	public void mouseExited(MouseEvent evt) {
		// 鼠标离开组件时调用
	}

	public void mouseReleased(MouseEvent evt) {
		// 鼠标按钮在组件上释放时调用
	}

	// 在棋子数组中查找是否有索引为x，y的棋子存在
	private boolean hasChess(int x, int y) {
		for (Piece c : pieces) {
			if (c != null && c.getX() == x && c.getY() == y)
				return true;
		}
		return false;
	}

	public void getCurPointIndex(MouseEvent evt) {
		// 将鼠标点击的坐标位置转换成网格索引
		curPointHIndex = (evt.getX() - MARGIN + GRID_SPAN / 2) / GRID_SPAN;
		curPointVIndex = (evt.getY() - MARGIN + GRID_SPAN / 2) / GRID_SPAN;
	}

	public void handleMousePressed() {
		if (!isComputer || (isComputer && curRole == "玩家")) {

			// 游戏结束时，不再能下
			// 如果x，y位置已经有棋子存在，不能下
			// 落在棋盘外不能下
			if (isGameOver || hasChess(curPointHIndex, curPointVIndex)
					|| curPointHIndex < 0 || curPointHIndex >= ROWS
					|| curPointVIndex < 0 || curPointVIndex >= COLS)
				return;

			placePiece(curPointHIndex, curPointVIndex);
			repaint();
			if (isWin()) {
				String msg = String.format("恭喜，%s赢了！", isBlack ? "黑棋" : "白棋");
				showMessage(msg);
				isGameOver = true;
			}
			// if (!isGameOver) {
			isBlack = !isBlack;
			// }
			curRole = "计算机";
		}
		if (!isGameOver && isComputer && curRole == "计算机") {
			machineDo();
			curRole = "玩家";

		}
	}

	public void machineDo() {
		int max_black, max_white, max_temp, max = 0;
		curPointHIndex = 7;
		curPointVIndex = 7;
		for (int i = 0; i < COLS; i++) {
			for (int j = 0; j < ROWS; j++) {
				if (!hasChess(i, j)) {// 算法判断是否下子
					max_white = searchMax(i, j, Color.white);// 判断白子的最大值
					max_black = searchMax(i, j, Color.black);// 判断黑子的最大值
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
			String msg = String.format("恭喜，%s赢了！", isBlack ? "黑棋" : "白棋");
			showMessage(msg);
			isGameOver = true;
		}
		repaint();
		// if (!isGameOver) {
		isBlack = !isBlack;
		// }

	}

	// 计算棋盘上某一方格上八个方向棋子的最大值，
	// 这八个方向分别是：东、西、南、北、东北、西南、东南、西北方向
	private int searchMax(int x, int y, Color color) {
		int num = 0, max_num, max_temp = 0;
		int x_temp = x, y_temp = y;
		int x_temp1 = x_temp, y_temp1 = y_temp;
		// 判断东方
		for (int i = 1; i < 5; i++) {
			x_temp1 += 1;
			if (x_temp1 >= COLS)
				break;
			if (getChess(x_temp1, y_temp1, color) != null)
				num++;
			else
				break;
		}
		// 判断西方
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

		// 判断北方
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
		// 判断南方
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

		// 判断西北
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
		// 判断东南
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

		// 判断东北
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
		// 判断西南
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
		// .println(piece.getColor().equals(Color.black) ? "黑方：" : "白方：");
		pieces[pointCount++] = piece;

		// System.out.println("(" + piece.getX() + "," + piece.getY() + ")");

	}

	private boolean isWin() {
		int continueCount = 1;// 连续棋子的个数

		// 横向向西寻找
		for (int x = curPointHIndex - 1; x >= 0; x--) {
			Color c = isBlack ? Color.black : Color.white;
			if (getChess(x, curPointVIndex, c) != null) {
				continueCount++;
			} else
				break;
		}
		// 横向向东寻找
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

		// 继续另一种搜索纵向
		// 向上搜索
		for (int y = curPointVIndex - 1; y >= 0; y--) {
			Color c = isBlack ? Color.black : Color.white;
			if (getChess(curPointHIndex, y, c) != null) {
				continueCount++;
			} else
				break;
		}
		// 纵向向下寻找
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

		// 继续另一种情况的搜索：斜向
		// 东北寻找
		for (int x = curPointHIndex + 1, y = curPointVIndex - 1; y >= 0
				&& x <= COLS; x++, y--) {
			Color c = isBlack ? Color.black : Color.white;
			if (getChess(x, y, c) != null) {
				continueCount++;
			} else
				break;
		}
		// 西南寻找
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

		// 继续另一种情况的搜索：斜向
		// 西北寻找
		for (int x = curPointHIndex - 1, y = curPointVIndex - 1; x >= 0
				&& y >= 0; x--, y--) {
			Color c = isBlack ? Color.black : Color.white;
			if (getChess(x, y, c) != null)
				continueCount++;
			else
				break;
		}
		// 东南寻找
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
		// 清除棋子
		for (int i = 0; i < pieces.length; i++) {
			pieces[i] = null;
		}
		// 恢复游戏相关的变量值
		isBlack = true;
		isGameOver = false; // 游戏是否结束
		pointCount = 0; // 当前棋盘棋子个数
		repaint();
	}

	// 悔棋
	public void undo() {
		if (pointCount == 0)
			return;
		if (isComputer) {
			if ((isFirst && isBlack) || (!isFirst && !isBlack)) {
				pieces[pointCount - 1] = null;
				pieces[pointCount - 2] = null;
				pointCount -= 2;
				curRole = "玩家";
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
		String[] options = { "确定", "取消" };
		int value = JOptionPane.showOptionDialog(null, msg, "警告",
				JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,
				null, options, null);
		return value;
	}

	public void saveChessManual() {// 保存棋谱

		long time = System.currentTimeMillis();
		Date now = new Date(time);
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy/MM/dd HH:mm:ss");// 可以方便地修改日期格式

		String hehe = dateFormat.format(now);

		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"文本文档(*.txt)", "txt");// 建立过滤器
		chooser.setFileFilter(filter);// 开始过滤
		int returnVal = chooser.showSaveDialog(chooser);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();

			try {
				if (file.exists()) {
					int copy = JOptionPane.showConfirmDialog(null,
							"文件已存在，是否要替换当前文件？", "保存",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE);
					if (copy == JOptionPane.YES_OPTION) {
						file.delete();
						BufferedWriter bw = new BufferedWriter(new FileWriter(
								file));
						bw.append("五子棋棋谱格式\r\n(序号) 身份( 棋子坐标)\r\n例如：(7)白棋(7,7)\r\n");
						for (int i = 0; i < pointCount; i++) {
							String SerialNumber = "(" + i + ")";
							String color = pieces[i].getColor().equals(
									Color.black) ? "黑棋" : "白棋";
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
					bw.append("五子棋棋谱格式\r\n(序号) 身份( 棋子坐标)\r\n例如：(7)白棋(7,7)\r\n");
					for (int i = 0; i < pointCount; i++) {
						String SerialNumber = "(" + i + ")";
						String color = pieces[i].getColor().equals(Color.black) ? "黑棋"
								: "白棋";
						String location = "(" + pieces[i].getX() + ","
								+ pieces[i].getY() + ")" + "\r\n";
						bw.append(SerialNumber + color + location);
					}
					bw.append("\r\n" + hehe);
					bw.close();
				}

			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}

		}
		if (returnVal == JFileChooser.CANCEL_OPTION) {

		}
	}
}
