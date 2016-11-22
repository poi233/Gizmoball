import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import view.mainView.MainWindow;
import controller.DesignMode;
import controller.RunMode;


public class PlayGame
{
	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error setting look and feel.", "Error", JOptionPane.ERROR_MESSAGE);
		}

		final RunMode viewmodel = new RunMode();
		final DesignMode designmodeViewmodel = new DesignMode(viewmodel.getBoard(), viewmodel.getTriggerHandler());

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainWindow window = new MainWindow(viewmodel, designmodeViewmodel);
				window.setVisible(true);
				//Providing same controller/model to another view instance shows mvc
				//MainWindow window2 = new MainWindow(viewmodel, designmodeViewmodel);
				//window2.setVisible(true);
			}
		});
	}
}
