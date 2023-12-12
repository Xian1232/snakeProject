package com.gamecodeschool.snakegame;

// Interface for command
interface Command {
    void execute();
}

// Concrete command for starting the timer
class StartCommand implements Command {
    private Timer timer;

    public StartCommand(Timer timer) {
        this.timer = timer;
    }

    @Override
    public void execute() {
        timer.start();
    }
}

// Concrete command for stopping the timer
class StopCommand implements Command {
    private Timer timer;

    public StopCommand(Timer timer) {
        this.timer = timer;
    }

    @Override
    public void execute() {
        timer.stop();
    }
}

// Concrete command for pausing the timer
class PauseCommand implements Command {
    private Timer timer;

    public PauseCommand(Timer timer) {
        this.timer = timer;
    }

    @Override
    public void execute() {
        timer.pause();
    }
}

// Concrete command for resuming the timer
class ResumeCommand implements Command {
    private Timer timer;

    public ResumeCommand(Timer timer) {
        this.timer = timer;
    }

    @Override
    public void execute() {
        timer.resume();
    }
}

// Concrete command for killing the timer
class DieCommand implements Command {
    private Timer timer;

    public DieCommand(Timer timer) {
        this.timer = timer;
    }

    @Override
    public void execute() {
        timer.die();
    }
}

// Concrete command for resetting the timer
class ResetCommand implements Command {
    private Timer timer;

    public ResetCommand(Timer timer) {
        this.timer = timer;
    }

    @Override
    public void execute() {
        timer.reset();
    }
}
