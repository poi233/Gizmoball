package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Observable;

import javax.swing.Timer;

import model.base.Board;
import model.giamoPhysics.IPhysicsEngine;
import model.fileSystem.Loader;
import model.fileSystem.Saver;
import model.giamoPhysics.PhysicsEngine;
import model.base.BadFileException;

public class RunMode extends Observable implements ActionListener {

    private static final int FRAMES_PER_SEC = 60;
    private static final int DEFAULT_BOARD_WIDTH = 20,
            DEFAULT_BOARD_HEIGHT = 20;
    private Board board;
    private Timer timer;
    private IPhysicsEngine engine;
    private TriggerHandler triggerhandler;
    private boolean isPaused = false;

    //界面刷新情况判断
    public enum UpdateReason {

        RunStateChanged, BoardChanged, SelectedToolChanged, StatusChanged
    }

    public RunMode() {
        timer = new Timer(1000 / FRAMES_PER_SEC, this);
        board = new Board(DEFAULT_BOARD_WIDTH, DEFAULT_BOARD_HEIGHT);
        engine = new PhysicsEngine();
        triggerhandler = new TriggerHandler();
    }

    //初始化界面
    public void newGame() {

        board.getBalls().clear();
        board.getGizmos().clear();
        triggerhandler.clear();
        this.setChanged();
        this.notifyObservers(UpdateReason.BoardChanged);
    }

    //从文件中读取界面
    public void loadGame(String path) throws FileNotFoundException,
            IOException, BadFileException {
        newGame();

        Loader loader = new Loader(path, board);
        loader.load(engine);
        triggerhandler.addLinks(loader.getKeyUpTriggers(),
                loader.getKeyDownTriggers());
        this.setChanged();
        this.notifyObservers(UpdateReason.BoardChanged);
    }

    //保存界面至文件
    public void saveGame(String path) throws IOException {
        Saver saver = new Saver(path);
        saver.save(engine, board, triggerhandler);
    }

    //切换运行状态
    public void toggleRunState() {
        if (this.getIsRunning()) {
            stopRunning();
        } else if (this.getIsRunning() ==false && getIsPaused() == false) {
            startRunning();
        } else if (this.getIsRunning() ==false && getIsPaused() == true){

            stopRunning();
        }
    }

    //开始运行
    public void startRunning() {
            engine.initialise(board);
            timer.start();
            isPaused = false;
            this.setChanged();
            this.notifyObservers(UpdateReason.RunStateChanged);
    }

    //停止运行
    public void stopRunning() {
            timer.stop();
            isPaused = false;
            this.setChanged();
            this.notifyObservers(UpdateReason.RunStateChanged);
    }

    //暂停运行
    public void pauseRunning() {
        if (this.getIsRunning()) {
            timer.stop();
            isPaused = true;
            this.setChanged();
            this.notifyObservers(UpdateReason.RunStateChanged);
        }
    }

    //恢复运行
    public void resumeRunning() {
        if (!this.getIsRunning()) {
            timer.start();
            isPaused = false;
            this.setChanged();
            this.notifyObservers(UpdateReason.RunStateChanged);
        }
    }

    //重新运行
    public void restartRunning() {
        if (!this.getIsRunning()) {
            engine.initialise(board);
            timer.start();
            isPaused = false;
            this.setChanged();
            this.notifyObservers(UpdateReason.RunStateChanged);
        }
    }

    //获取运行状态
    public boolean getIsRunning() {
        return timer.isRunning();
    }

    //获取暂停状态
    public boolean getIsPaused() {
        return isPaused;
    }

    //timer反馈函数
    @Override
    public void actionPerformed(ActionEvent e) {

        engine.calculateState((double) 1 / FRAMES_PER_SEC);
        this.setChanged();
        this.notifyObservers(UpdateReason.BoardChanged);
    }

    public Board getBoard() {
        return board;
    }

    public TriggerHandler getTriggerHandler() {
        return triggerhandler;
    }
}
