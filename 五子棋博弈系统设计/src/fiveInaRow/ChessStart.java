package 五子棋博弈系统设计;

import javax.swing.JFrame;

public class ChessStart extends JFrame {
    public static final int HEIGHT = 600, WIDTH = 560;// 主窗口大小

    public static void main(String[] args) {

	ChessFrame cf = new ChessFrame();// 创建主框架
	cf.setBounds(0, 0, WIDTH, HEIGHT);
	cf.setVisible(true);// 显示主框架

	cf.setResizable(false);

    }

}
