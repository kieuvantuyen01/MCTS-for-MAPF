package main;

import java.io.File;
import java.io.FileNotFoundException;

public class Game {

    protected boolean Centralized;
    protected State myState;

    public void init(File fileEntry) throws FileNotFoundException {
        Centralized = true;
        myState = new State(fileEntry);
    }

    public Value CreateZeroValue(){
        return new Value(0, 0);
    };

    public boolean notEnded() {
        return myState.isNotTerminal();
    }

    public State getState() {
        return myState;
    }

    public void updateState(State newState) {
        myState = newState;
    }

    public boolean winGame() {
        return myState.win();
    }
}
