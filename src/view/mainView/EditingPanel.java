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
        designModePanel.setPreferredSize(new Dimension(100, 460));
        super.add(designModePanel);

        makeToolbarButton(new CircleBumperIcon(Color.lightGray), 30, 30, "圆形", DesignCommand.AddCircleBumper);
        makeToolbarButton(new SquareBumperIcon(Color.lightGray), 30, 30, "方形", DesignCommand.AddSquareBumper);
        makeToolbarButton(new TriangleBumperIcon(Color.lightGray), 30, 30, "三角形", DesignCommand.AddTriangleBumper);
        makeToolbarButton(new CircleBumperIcon(Color.blue), 30, 30, "加速器", DesignCommand.AddAcceleratorGizmo);
        makeToolbarButton(new CircleBumperIcon(new Color(139, 0, 244)), 30, 30, "传送门", DesignCommand.AddPortalGizmo);
        makeToolbarButton(new CircleBumperIcon(Color.white), 30, 30, "发射器", DesignCommand.AddMultiballGizmo);
        makeToolbarButton(new CircleBumperIcon(Color.gray), 30, 30, "可变门", DesignCommand.AddGateGizmo);
        makeToolbarButton(new SpinnerIcon(Color.pink), 30, 30, "旋转板", DesignCommand.AddSpinnerGizmo);

        makeToolbarButton(new LeftFlipperIcon(Color.pink), 30, 30, "左挡板", DesignCommand.AddLeftFlipper);
        makeToolbarButton(new RightFlipperIcon(Color.pink), 30, 30, "右挡板", DesignCommand.AddRightFlipper);
        makeToolbarButton(new BallIcon(Color.orange), 30, 30, "小球", DesignCommand.AddBall);

        makeToolbarButton(new AbsorberIcon(Color.cyan), 66, 30, "吸收器", DesignCommand.AddAbsorber);

        makeToolbarButton(new LinkKeyDownIcon(Color.black), 68, 30, "为组件添加按下按键事件", DesignCommand.ConnectKeyDown);
        makeToolbarButton(new LinkKeyUpIcon(Color.black), 68, 30, "为组件添加释放按键事件", DesignCommand.ConnectKeyUp);
        makeToolbarButton(new LinkGizmoIcon(Color.black), 68, 30, "为高级组件添加事件", DesignCommand.ConnectGizmo);
        makeToolbarButton(new DeleteIcon(Color.gray), 68, 30, "删除组件或小球", DesignCommand.DeleteGizmo);
        makeToolbarButton(new RotateGizmoIcon(Color.gray), 68, 30, "旋转组件", DesignCommand.RotateGizmo);
        makeToolbarButton(new MoveGizmoIcon(Color.gray), 68, 30, "移动组件", DesignCommand.MoveGizmo);
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
