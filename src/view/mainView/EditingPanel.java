package view.mainView;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import view.toolIcons.AbsorberIcon;
import view.toolIcons.BallIcon;
import view.toolIcons.CircleBumperIcon;
import view.toolIcons.DeleteIcon;
import view.toolIcons.RightFlipperIcon;
import view.toolIcons.LeftFlipperIcon;
import view.toolIcons.LinkGizmoIcon;
import view.toolIcons.LinkKeyDownIcon;
import view.toolIcons.LinkKeyUpIcon;
import view.toolIcons.MoveGizmoIcon;
import view.toolIcons.RotateGizmoIcon;
import view.toolIcons.SpinnerIcon;
import view.toolIcons.SquareBumperIcon;
import view.toolIcons.TriangleBumperIcon;
import controller.DesignMode;
import controller.DesignMode.DesignCommand;
import controller.RunMode;
import controller.RunMode.UpdateReason;

public class EditingPanel extends JPanel implements Observer, ActionListener {
    private RunMode runMode;
    private DesignMode designMode;

    private JButton modeButton;
    private JButton pauseButton;
    private JPanel designModePanel;
    private ButtonGroup designModeGroup;

    public EditingPanel(RunMode runMode, DesignMode designMode) {
        this.runMode = runMode;
        this.designMode = designMode;

        designMode.addObserver(this);
        initialiseComponents();
    }

    private void initialiseComponents() {
        super.setPreferredSize(new Dimension(100, 400));

        modeButton = new JButton("开始");
        modeButton.setPreferredSize(new Dimension(68, 30));
        modeButton.setToolTipText("切换游戏模式和编辑模式");
        modeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runMode.toggleRunState();
                designMode.setCurrentCommand(DesignCommand.None);
            }
        });

        pauseButton = new JButton("暂停");
        pauseButton.setPreferredSize(new Dimension(68, 30));
        pauseButton.setVisible(false);
        pauseButton.setToolTipText("暂停游戏");
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pauseButton.getText().toString().equals("暂停")) {
                    runMode.pauseRunning();
                    designMode.setCurrentCommand(DesignCommand.Pause);
                } else {
                    runMode.resumeRunning();
                    designMode.setCurrentCommand(DesignCommand.None);
                }
            }
        });


        super.add(modeButton);
        super.add(pauseButton);

        designModeGroup = new ButtonGroup();
        designModePanel = new JPanel();
        designModePanel.setPreferredSize(new Dimension(100, 430));
        super.add(designModePanel);

        makeToolbarButton(new CircleBumperIcon(Color.green), 30, 30, "Circle bumper", DesignCommand.AddCircleBumper);
        makeToolbarButton(new SquareBumperIcon(Color.red), 30, 30, "Square bumper", DesignCommand.AddSquareBumper);
        makeToolbarButton(new TriangleBumperIcon(Color.blue), 30, 30, "Triangle bumper", DesignCommand.AddTriangleBumper);
        makeToolbarButton(new CircleBumperIcon(Color.cyan), 30, 30, "Accelerator gizmo", DesignCommand.AddAcceleratorGizmo);
        makeToolbarButton(new CircleBumperIcon(new Color(139, 0, 244)), 30, 30, "Portal gizmo", DesignCommand.AddPortalGizmo);
        makeToolbarButton(new CircleBumperIcon(Color.white), 30, 30, "Multiball gizmo", DesignCommand.AddMultiballGizmo);
        makeToolbarButton(new CircleBumperIcon(Color.gray), 30, 30, "Gate gizmo", DesignCommand.AddGateGizmo);
        makeToolbarButton(new SpinnerIcon(Color.gray), 30, 30, "Spinner gizmo", DesignCommand.AddSpinnerGizmo);

        makeToolbarButton(new LeftFlipperIcon(Color.gray), 30, 30, "Left flipper", DesignCommand.AddLeftFlipper);
        makeToolbarButton(new RightFlipperIcon(Color.gray), 30, 30, "Right flipper", DesignCommand.AddRightFlipper);
        makeToolbarButton(new BallIcon(Color.yellow), 30, 30, "Ball", DesignCommand.AddBall);

        makeToolbarButton(new AbsorberIcon(Color.magenta), 66, 30, "Absorber", DesignCommand.AddAbsorber);

        makeToolbarButton(new LinkKeyDownIcon(Color.gray), 68, 30, "Allows you to link a gizmo to a key down event.", DesignCommand.ConnectKeyDown);
        makeToolbarButton(new LinkKeyUpIcon(Color.gray), 68, 30, "Allows you to link a gizmo to a key up event.", DesignCommand.ConnectKeyUp);
        makeToolbarButton(new LinkGizmoIcon(Color.gray), 68, 30, "Allows you to link two gizmos together.", DesignCommand.ConnectGizmo);
        makeToolbarButton(new DeleteIcon(Color.gray), 30, 30, "Delete gizmo/ball", DesignCommand.DeleteGizmo);
        makeToolbarButton(new RotateGizmoIcon(Color.gray), 30, 30, "Rotate gizmo", DesignCommand.RotateGizmo);
        makeToolbarButton(new MoveGizmoIcon(Color.gray), 30, 30, "Move gizmo", DesignCommand.MoveGizmo);
    }


    private void makeToolbarButton(Icon icon, int width, int height, String tooltip, DesignCommand command) {
        CommandButton button = new CommandButton(icon, command);

        button.setPreferredSize(new Dimension(width, height));
        button.setToolTipText(tooltip);
        button.addActionListener(this);

        designModeGroup.add(button);
        designModePanel.add(button);
    }


    public void setRunMode(boolean runMode, boolean pauseMode) {
        if (runMode == true) {
            designModePanel.setVisible(false);
            pauseButton.setVisible(true);
            modeButton.setText("编辑");
            if (pauseMode == true) {
                pauseButton.setText("恢复");
            } else {
                pauseButton.setText("暂停");
            }
        } else if (runMode == false) {
            if (pauseMode == true) {
                pauseButton.setText("恢复");
            } else {
                designModePanel.setVisible(true);
                pauseButton.setVisible(false);
                modeButton.setText("开始");
                pauseButton.setText("暂停");
            }
        }
    }

    @Override
    public void update(Observable source, Object arg) {
        UpdateReason reason = (UpdateReason) arg;

        switch (reason) {
            case SelectedToolChanged:
                if (designMode.getCurrentCommand() == DesignCommand.None)
                    designModeGroup.clearSelection();

                break;
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        CommandButton source = (CommandButton) e.getSource();
        designMode.setCurrentCommand(source.getCommand());
    }


    private class CommandButton extends JToggleButton {
        private DesignCommand command;

        public CommandButton(Icon icon, DesignCommand command) {
            super(icon);
            this.command = command;
        }

        public DesignCommand getCommand() {
            return command;
        }
    }
}
