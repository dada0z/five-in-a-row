package �����岩��ϵͳ���;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class ChessFrame extends JFrame {
	private ChessBoard chessBoard;
	private JMenuBar menuBar;

	private JMenuItem startMenuItem, saveMenuItem, exitMenuItem, undoMenuItem;
	
	private JMenuItem manToManMenuItem, manToMachineMenuItem;
	private JMenuItem manFirstMenuItem,machineFirstMenuItem;
	private JMenuItem ruleMenuItem, aboutMenuItem;
	private JMenu sysMenu, editMenu, modeMenu, whoFirstMenu, helpMenu;

	public String msg;

	enum Menu {
		start("��ʼ��Ϸ", 1),
		exit("�˳�", 2),
		undo("����", 3), 
		man_man("���˶���", 4), 
		man_machine("�˻�����", 5), 
		rule("����", 6), 
		about("����������", 7), 
		manFirst("�������",8), 
		machineFirst("���������", 9), 
		save("��������", 10); // ��Ա����
		public static int getIndex(String name) {
			for (Menu menu : Menu.values()) {
				if (menu.name == name)
					return menu.index;
			}
			return 0;
		}

		private int index;

		private String name;

		private Menu() {

		}

		private Menu(String name, int index) {
			this.name = name;
			this.index = index;
		}
	}

	public ChessFrame() {

		setTitle("������");// ���ñ���
		chessBoard = new ChessBoard();
		Container contentPane = getContentPane();
		contentPane.add(chessBoard);
		
		//����chessBoard��͸��
		chessBoard.setOpaque(true);
		// ��������Ӳ˵�
		menuBar = new JMenuBar();
		sysMenu = new JMenu("�ļ�");
		editMenu = new JMenu("�༭");
		modeMenu = new JMenu("ģʽ");
		whoFirstMenu = new JMenu("����");
		helpMenu = new JMenu("����");
		// ��ʼ���˵���
		startMenuItem = new JMenuItem("��ʼ��Ϸ");
		saveMenuItem = new JMenuItem("��������");
		exitMenuItem = new JMenuItem("�˳�");
		undoMenuItem = new JMenuItem("����");
		manToManMenuItem = new JMenuItem("���˶���");
		manToMachineMenuItem = new JMenuItem("�˻�����");
		manFirstMenuItem = new JMenuItem("�������");
		machineFirstMenuItem = new JMenuItem("���������");
		ruleMenuItem = new JMenuItem("����");
		aboutMenuItem = new JMenuItem("����������");

		// ���˵�����ӵ��˵���
		sysMenu.add(startMenuItem);
		sysMenu.add(saveMenuItem);
		sysMenu.add(exitMenuItem);
		editMenu.add(undoMenuItem);
		modeMenu.add(manToManMenuItem);
		modeMenu.add(manToMachineMenuItem);
		whoFirstMenu.add(manFirstMenuItem);
		whoFirstMenu.add(machineFirstMenuItem);
		helpMenu.add(ruleMenuItem);
		helpMenu.add(aboutMenuItem);
		// ��ʼ����ť�¼��������ڲ���
		ChessBoardListener listener = new ChessBoardListener();
		// ���˵���ע�ᵽ�¼���������
		this.startMenuItem.addActionListener(listener);
		saveMenuItem.addActionListener(listener);
		exitMenuItem.addActionListener(listener);
		undoMenuItem.addActionListener(listener);
		manToManMenuItem.addActionListener(listener);
		manToMachineMenuItem.addActionListener(listener);
		manFirstMenuItem.addActionListener(listener);
		machineFirstMenuItem.addActionListener(listener);
		ruleMenuItem.addActionListener(listener);
		aboutMenuItem.addActionListener(listener);
		menuBar.add(sysMenu); // ��ϵͳ�˵���ӵ��˵�����
		menuBar.add(editMenu); // ���༭�˵���ӵ��˵�����
		menuBar.add(modeMenu); // ��ģʽ�˵���ӵ��˵�����
		menuBar.add(whoFirstMenu); // �����ֲ˵���ӵ��˵�����
		menuBar.add(helpMenu); // �������˵���ӵ��˵�����
		setJMenuBar(menuBar); // ��menuBar����Ϊ�˵���
		add(chessBoard); // ����������ӵ�������
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// ���ý���ر��¼�
		pack();// ����Ӧ��С

	}

	private class ChessBoardListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();// ����¼�Դ
			int value;
			switch (Menu.getIndex(cmd)) {
			case 1:
				msg = String.format("���Ѿ�ѡ��\n���¿�ʼ��Ϸ\nȷ�����¿�ʼ��");
				value = chessBoard.showWarning(msg);
				if (value == JOptionPane.YES_OPTION) {
					chessBoard.isBlack = true;
					chessBoard.restartGame();
					chessBoard.repaint();
				}
				if (value == JOptionPane.YES_NO_CANCEL_OPTION) {
					break;
				}
				break;
			case 2:

				msg = String.format("���Ѿ�ѡ��\n�˳�\n����˳���");
				value = chessBoard.showWarning(msg);
				if (value == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
				if (value == JOptionPane.YES_NO_CANCEL_OPTION) {
					break;
				}
				break;
			case 3:
				chessBoard.undo();
				chessBoard.repaint();
				break;
			case 4:
				msg = String.format("���Ѿ�ѡ��\n���˶���ģʽ\n�Ƿ����¿�ʼ��");
				value = chessBoard.showWarning(msg);
				if (value == JOptionPane.YES_OPTION) {
					chessBoard.isBlack = true;
					chessBoard.isComputer = false;
					chessBoard.restartGame();
				}
				if (value == JOptionPane.YES_NO_CANCEL_OPTION) {
					break;
				}
				break;
			case 5:
				msg = String.format("���Ѿ�ѡ��\n�˻�����ģʽ\n�Ƿ����¿�ʼ��");
				value = chessBoard.showWarning(msg);
				if (value == JOptionPane.YES_OPTION) {
					chessBoard.isComputer = true;
					chessBoard.isBlack = true;
					chessBoard.isFirst = true;
					chessBoard.restartGame();
				}
				if (value == JOptionPane.YES_NO_CANCEL_OPTION) {
					break;
				}
				break;
			case 6:
				msg = String
						.format("�޽���\nĬ�Ϻڷ�����\n�ڰ�˫����������\n����һ���ں�������б��\nӵ������������������ʤ");
				chessBoard.showMessage(msg);
				break;
			case 7:
				msg = String.format("��   �ߣ��� ��\nE-mail:18345153671@163.com");
				chessBoard.showMessage(msg);
				break;
			case 8:
				msg = String.format("���Ѿ�ѡ��\n�������\n�Ƿ����¿�ʼ��");
				value = chessBoard.showWarning(msg);
				if (value == JOptionPane.YES_OPTION) {
					chessBoard.isBlack = true;
					chessBoard.isFirst = true;
					chessBoard.curRole = "���";
					chessBoard.isComputer = true;
					chessBoard.restartGame();
					;
				}
				if (value == JOptionPane.YES_NO_CANCEL_OPTION) {
					break;
				}
				break;
			case 9:
				msg = String.format("���Ѿ�ѡ��\n���������\n�Ƿ����¿�ʼ��");
				value = chessBoard.showWarning(msg);
				if (value == JOptionPane.YES_OPTION) {
					chessBoard.isBlack = true;
					chessBoard.isFirst = false;
					chessBoard.curRole = "�����";
					chessBoard.isComputer = true;
					chessBoard.restartGame();
					chessBoard.machineDo();
					chessBoard.isBlack = false;
					chessBoard.curRole = "���";
				}

				if (value == JOptionPane.YES_NO_CANCEL_OPTION) {
					break;
				}
				break;
			case 10:
				chessBoard.saveChessManual();
				break;
			default:
				break;
			}
		}
	}

}
