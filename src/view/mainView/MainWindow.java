package view.mainView;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controller.DesignMode;
import controller.RunMode;
import controller.RunMode.UpdateReason;
import model.base.BadFileException;

/**
 * Main application mainView.
 */
@SuppressWarnings("serial")
public class MainWindow extends JFrame implements Observer {
	public static final int L = 20;

	private RunMode runMode;
	private DesignMode designMode;

	private JMenuItem newMenuItem, openMenuItem, saveMenuItem;
	private RunningPanel boardView;
	private EditingPanel toolbar;
	private JPanel contentPane;
	private JLabel statusBar;

	/**
	 * Creates a new mainView for gizmoball.
	 */
	public MainWindow(RunMode runMode, DesignMode designMode) {
		super("Gizmoball");
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.runMode = runMode;
		this.runMode.addObserver(this);

		this.designMode = designMode;
		this.designMode.addObserver(this);

		initialiseComponents();
		initialiseActionListeners();
	}

	/**
	 * Initialise all the mainView components.
	 */
	private void initialiseComponents() {
		toolbar = new EditingPanel(runMode, designMode);
		boardView = new RunningPanel(runMode, designMode);

		JMenuBar menubar = new JMenuBar();
		super.setJMenuBar(menubar);

		JMenu fileMenu = new JMenu("File");
		menubar.add(fileMenu);

		newMenuItem = new JMenuItem("New");
		fileMenu.add(newMenuItem);

		openMenuItem = new JMenuItem("Open");
		fileMenu.add(openMenuItem);

		saveMenuItem = new JMenuItem("Save");
		fileMenu.add(saveMenuItem);

		statusBar = new JLabel();
		statusBar.setBorder(new EmptyBorder(5, 5, 5, 5));

		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		contentPane.setPreferredSize(new Dimension(530, 410));
		contentPane.add(boardView, BorderLayout.CENTER);
		contentPane.add(toolbar, BorderLayout.EAST);
		contentPane.add(statusBar, BorderLayout.SOUTH);

		super.setContentPane(contentPane);
		super.setMinimumSize(new Dimension(650, 580));

		super.pack();
		super.requestFocus();
	}

	/**
	 * Sets action listeners. 
	 */
	private void initialiseActionListeners() {
		final JFrame parent = this;

		newMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				runMode.newGame();
			}
		});

		openMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(System
						.getProperty("user.dir"));

				if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();

					try {
						runMode.loadGame(file.getAbsolutePath());
					} catch (FileNotFoundException ex) {
						JOptionPane.showMessageDialog(parent,
								"The file cannot be found.", "Load error",
								JOptionPane.ERROR_MESSAGE);
					} catch (IOException ex) {
						JOptionPane.showMessageDialog(parent,
								"Error reading file: " + ex.getMessage(),
								"Load error", JOptionPane.ERROR_MESSAGE);
					} catch (BadFileException ex) {
						JOptionPane.showMessageDialog(
								parent,
								"The file format is incorrect: "
										+ ex.getMessage(), "Load error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		saveMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(System
						.getProperty("user.dir"));

				if (chooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();

					try {
						runMode.saveGame(file.getAbsolutePath());
					} catch (IOException ex) {
						JOptionPane.showMessageDialog(parent,
								"Error writing file: " + ex.getMessage(),
								"Save error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
	}

	@Override
	public void update(Observable source, Object arg) {
		UpdateReason reason = (UpdateReason) arg;

		switch (reason) {
		case RunStateChanged:
			toolbar.setRunMode(runMode.getIsRunning());

			if (runMode.getIsRunning())
				contentPane.remove(statusBar);
			else
				contentPane.add(statusBar, BorderLayout.SOUTH);

			break;

		case StatusChanged:
			statusBar.setText(designMode.getStatusMessage());
			break;
		}
	}
}
