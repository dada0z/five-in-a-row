package 五子棋博弈系统设计;

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
		start("开始游戏", 1),
		exit("退出", 2),
		undo("悔棋", 3), 
		man_man("人人对弈", 4), 
		man_machine("人机对弈", 5), 
		rule("规则", 6), 
		about("关于五子棋", 7), 
		manFirst("玩家先手",8), 
		machineFirst("计算机先手", 9), 
		save("保存棋谱", 10); // 成员变量
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

		setTitle("五子棋");// 设置标题
		chessBoard = new ChessBoard();
		Container contentPane = getContentPane();
		contentPane.add(chessBoard);
		
		//设置chessBoard不透明
		chessBoard.setOpaque(true);
		// 创建和添加菜单
		menuBar = new JMenuBar();
		sysMenu = new JMenu("文件");
		editMenu = new JMenu("编辑");
		modeMenu = new JMenu("模式");
		whoFirstMenu = new JMenu("先手");
		helpMenu = new JMenu("帮助");
		// 初始化菜单项
		startMenuItem = new JMenuItem("开始游戏");
		saveMenuItem = new JMenuItem("保存棋谱");
		exitMenuItem = new JMenuItem("退出");
		undoMenuItem = new JMenuItem("悔棋");
		manToManMenuItem = new JMenuItem("人人对弈");
		manToMachineMenuItem = new JMenuItem("人机对弈");
		manFirstMenuItem = new JMenuItem("玩家先手");
		machineFirstMenuItem = new JMenuItem("计算机先手");
		ruleMenuItem = new JMenuItem("规则");
		aboutMenuItem = new JMenuItem("关于五子棋");

		// 将菜单项添加到菜单上
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
		// 初始化按钮事件监听器内部类
		ChessBoardListener listener = new ChessBoardListener();
		// 将菜单项注册到事件监听器上
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
		menuBar.add(sysMenu); // 将系统菜单添加到菜单栏上
		menuBar.add(editMenu); // 将编辑菜单添加到菜单栏上
		menuBar.add(modeMenu); // 将模式菜单添加到菜单栏上
		menuBar.add(whoFirstMenu); // 将先手菜单添加到菜单栏上
		menuBar.add(helpMenu); // 将帮助菜单添加到菜单栏上
		setJMenuBar(menuBar); // 将menuBar设置为菜单栏
		add(chessBoard); // 将面板对象添加到窗体上
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 设置界面关闭事件
		pack();// 自适应大小

	}

	private class ChessBoardListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();// 获得事件源
			int value;
			switch (Menu.getIndex(cmd)) {
			case 1:
				msg = String.format("您已经选择\n重新开始游戏\n确定重新开始吗？");
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

				msg = String.format("您已经选择\n退出\n真的退出吗？");
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
				msg = String.format("您已经选择\n人人对弈模式\n是否重新开始！");
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
				msg = String.format("您已经选择\n人机对弈模式\n是否重新开始！");
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
						.format("无禁手\n默认黑方先手\n黑白双方依次落子\n任意一方在横向竖向斜向\n拥有五颗连续的棋子则获胜");
				chessBoard.showMessage(msg);
				break;
			case 7:
				msg = String.format("作   者：大 大\nE-mail:18345153671@163.com");
				chessBoard.showMessage(msg);
				break;
			case 8:
				msg = String.format("您已经选择\n玩家先手\n是否重新开始！");
				value = chessBoard.showWarning(msg);
				if (value == JOptionPane.YES_OPTION) {
					chessBoard.isBlack = true;
					chessBoard.isFirst = true;
					chessBoard.curRole = "玩家";
					chessBoard.isComputer = true;
					chessBoard.restartGame();
					;
				}
				if (value == JOptionPane.YES_NO_CANCEL_OPTION) {
					break;
				}
				break;
			case 9:
				msg = String.format("您已经选择\n计算机先手\n是否重新开始！");
				value = chessBoard.showWarning(msg);
				if (value == JOptionPane.YES_OPTION) {
					chessBoard.isBlack = true;
					chessBoard.isFirst = false;
					chessBoard.curRole = "计算机";
					chessBoard.isComputer = true;
					chessBoard.restartGame();
					chessBoard.machineDo();
					chessBoard.isBlack = false;
					chessBoard.curRole = "玩家";
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
