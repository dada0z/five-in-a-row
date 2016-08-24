package fiveInaRow;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import utils.SaveUtil;
import utils.ShowMesssage;

public class ChessFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	public static final String MENU_ITEM_START = "start";
	public static final String MENU_ITEM_SAVE = "save";
	public static final String MENU_ITEM_EXIT = "exit";
	public static final String MENU_ITEM_UNDO = "undo";
	public static final String MENU_ITEM_MAN_MAN_MODE = "man-man";
	public static final String MENU_ITEM_MAN_MACHINE_MODE = "man-machine";
	public static final String MENU_ITEM_MAN_FIRST = "man-first";
	public static final String MENU_ITEM_MACHINE_FIRST = "machine-first";
	public static final String MENU_ITEM_RULES = "rules";
	public static final String MENU_ITEM_ABOUT = "about";
	public static final String[] MENU_ITEMS = { MENU_ITEM_START, MENU_ITEM_SAVE, MENU_ITEM_EXIT, MENU_ITEM_UNDO,
			MENU_ITEM_MAN_MAN_MODE, MENU_ITEM_MAN_MACHINE_MODE, MENU_ITEM_MAN_FIRST, MENU_ITEM_MACHINE_FIRST,
			MENU_ITEM_RULES, MENU_ITEM_ABOUT };
	private ChessBoard chessBoard = ChessBoard.getInstance();

	private JMenuBar menuBar;
	private JMenu sysMenu, editMenu, modeMenu, whoFirstMenu, helpMenu;
	private JMenuItem startMenuItem, saveMenuItem, exitMenuItem, undoMenuItem;
	private JMenuItem manToManMenuItem, manToMachineMenuItem;
	private JMenuItem manFirstMenuItem, machineFirstMenuItem;
	private JMenuItem ruleMenuItem, aboutMenuItem;
	private List<JMenuItem> menuItems = new ArrayList<>();

	public String msg;

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
		startMenuItem = new JMenuItem(MENU_ITEM_START);
		menuItems.add(startMenuItem);

		saveMenuItem = new JMenuItem(MENU_ITEM_SAVE);
		menuItems.add(saveMenuItem);

		exitMenuItem = new JMenuItem(MENU_ITEM_EXIT);
		menuItems.add(exitMenuItem);

		undoMenuItem = new JMenuItem(MENU_ITEM_UNDO);
		menuItems.add(undoMenuItem);

		manToManMenuItem = new JMenuItem(MENU_ITEM_MAN_MAN_MODE);
		menuItems.add(manToManMenuItem);

		manToMachineMenuItem = new JMenuItem(MENU_ITEM_MAN_MACHINE_MODE);
		menuItems.add(manToMachineMenuItem);

		manFirstMenuItem = new JMenuItem(MENU_ITEM_MAN_FIRST);
		menuItems.add(manFirstMenuItem);

		machineFirstMenuItem = new JMenuItem(MENU_ITEM_MACHINE_FIRST);
		menuItems.add(machineFirstMenuItem);

		ruleMenuItem = new JMenuItem(MENU_ITEM_RULES);
		menuItems.add(ruleMenuItem);

		aboutMenuItem = new JMenuItem(MENU_ITEM_ABOUT);
		menuItems.add(aboutMenuItem);

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
		startMenuItem.addActionListener(listener);
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
			switch (cmd) {
			case MENU_ITEM_START:
				chessBoard.setBlackFirst(true);
				chessBoard.restartGame();
				chessBoard.repaint();
				break;
			case MENU_ITEM_SAVE:
				SaveUtil.saveChessHistory();
				break;
			case MENU_ITEM_EXIT:
				msg = String.format("Are you sure to exit?");
				value = ShowMesssage.showWarning(msg);
				if (value == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
				if (value == JOptionPane.YES_NO_CANCEL_OPTION) {
					break;
				}
				break;
			case MENU_ITEM_UNDO:
				chessBoard.undo();
				chessBoard.repaint();
				break;
			case MENU_ITEM_MAN_MAN_MODE:
				chessBoard.setBlackFirst(true);
				chessBoard.setComputer(false);
				chessBoard.restartGame();
				break;
			case MENU_ITEM_MAN_MACHINE_MODE:
				chessBoard.setComputer(true);
				chessBoard.setBlackFirst(true);
				chessBoard.setManFirst(true);
				chessBoard.restartGame();
				break;
			case MENU_ITEM_MAN_FIRST:
				chessBoard.setBlackFirst(true);
				chessBoard.setManFirst(true);
				chessBoard.setCurRole("Man");
				chessBoard.setComputer(true);
				chessBoard.restartGame();
				break;
			case MENU_ITEM_MACHINE_FIRST:
				chessBoard.setBlackFirst(true);
				chessBoard.setManFirst(false);
				chessBoard.setCurRole("Machine");
				chessBoard.setComputer(true);
				chessBoard.restartGame();
				chessBoard.machineDo();
				chessBoard.setBlackFirst(false);
				chessBoard.setCurRole("Man");
				break;
			case MENU_ITEM_RULES:
				msg = String.format("Standard Gomoku");
				ShowMesssage.showMessage(msg);
				break;
			case MENU_ITEM_ABOUT:
				msg = String.format("Author：dada0z\nE-mail:dada0z@163.com");
				ShowMesssage.showMessage(msg);
				break;
			default:
				break;
			}
		}
	}

}
