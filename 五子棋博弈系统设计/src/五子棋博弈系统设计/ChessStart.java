package �����岩��ϵͳ���;

import javax.swing.JFrame;

public class ChessStart extends JFrame {
    public static final int HEIGHT = 600, WIDTH = 560;// �����ڴ�С

    public static void main(String[] args) {

	ChessFrame cf = new ChessFrame();// ���������
	cf.setBounds(0, 0, WIDTH, HEIGHT);
	cf.setVisible(true);// ��ʾ�����

	cf.setResizable(false);

    }

}
