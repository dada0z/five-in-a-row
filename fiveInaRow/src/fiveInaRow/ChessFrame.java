package fiveInaRow;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import utils.SaveUtil;
import utils.ShowMesssage;

public class ChessFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private ChessBoard chessBoard = ChessBoard.getInstance();
	
	private JMenuBar menuBar;
	private JMenuItem startMenuItem, saveMenuItem, exitMenuItem, undoMenuItem;
	private JMenuItem manToManMenuItem, manToMachineMenuItem;
	private JMenuItem manFirstMenuItem, machineFirstMenuItem;
	private JMenuItem ruleMenuItem, aboutMenuItem;
	private JMenu sysMenu, editMenu, modeMenu, whoFirstMenu, helpMenu;

	public String msg;

	enum Menu {
		start("Start", 1), exit("Exit", 2), undo("Undo", 3), man_man("Man-Man", 4), man_machine("Man-Machine", 5), rule(
				"Rules", 6), about("About",
						7), manFirst("ManFirst", 8), machineFirst("MachineFirst", 9), save("Save", 10);
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

		setTitle("Five In a Row");
		Container contentPane = getContentPane();
		contentPane.add(chessBoard);
		chessBoard.setOpaque(true);

		// Add Menu
		menuBar = new JMenuBar();
		sysMenu = new JMenu("File");
		editMenu = new JMenu("Edit");
		modeMenu = new JMenu("Mode");
		whoFirstMenu = new JMenu("First");
		helpMenu = new JMenu("Help");
		// Init MenuItem
		startMenuItem = new JMenuItem("Start");
		saveMenuItem = new JMenuItem("Save");
		exitMenuItem = new JMenuItem("Exit");
		undoMenuItem = new JMenuItem("Undo");
		manToManMenuItem = new JMenuItem("Man-Man");
		manToMachineMenuItem = new JMenuItem("Man-Machine");
		manFirstMenuItem = new JMenuItem("ManFirst");
		machineFirstMenuItem = new JMenuItem("MachineFirst");
		ruleMenuItem = new JMenuItem("Rules");
		aboutMenuItem = new JMenuItem("About");

		// Add MenuItem to Menu
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
		// Init chessBoard listener
		ChessBoardListener listener = new ChessBoardListener();
		// listen to the menu items
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
		menuBar.add(sysMenu);
		menuBar.add(editMenu);
		menuBar.add(modeMenu);
		menuBar.add(whoFirstMenu);
		menuBar.add(helpMenu);
		setJMenuBar(menuBar);
		add(chessBoard);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();

	}

	private class ChessBoardListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			int value;
			switch (Menu.getIndex(cmd)) {
			case 1:
				chessBoard.setBlackFirst(true);
				chessBoard.restartGame();
				chessBoard.repaint();
				break;
			case 2:
				msg = String.format("Are you sure to exit?");
				value = ShowMesssage.showWarning(msg);
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
				chessBoard.setBlackFirst(true);
				chessBoard.setComputer(false);
				chessBoard.restartGame();
				break;
			case 5:
				chessBoard.setComputer(true);
				chessBoard.setBlackFirst(true);
				chessBoard.setManFirst(true);
				chessBoard.restartGame();
				break;
			case 6:
				msg = String.format("Standard Gomoku");
				ShowMesssage.showMessage(msg);
				break;
			case 7:
				msg = String.format("Author：dada0z\nE-mail:dada0z@163.com");
				ShowMesssage.showMessage(msg);
				break;
			case 8:
				chessBoard.setBlackFirst(true);
				chessBoard.setManFirst(true);
				chessBoard.setCurRole("Man");
				chessBoard.setComputer(true);
				chessBoard.restartGame();
				break;
			case 9:
				chessBoard.setBlackFirst(true);
				chessBoard.setManFirst(false);
				chessBoard.setCurRole("Machine");
				chessBoard.setComputer(true);
				chessBoard.restartGame();
				chessBoard.machineDo();
				chessBoard.setBlackFirst(false);
				chessBoard.setCurRole("Man");
				break;
			case 10:
				SaveUtil.saveChessHistory();
				break;
			default:
				break;
			}
		}
	}

}
