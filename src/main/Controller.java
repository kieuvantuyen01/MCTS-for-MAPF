package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class Controller {
    public static Game game;
    public static String solve = "CAN'T SOLVE";
    static Simulator simulator;
    static MonteCarloTreeSearch mcts;
    static String time = "";
    private static String result;

    public Controller(File fileEntry) throws FileNotFoundException {
        this.game = new Game();
        game.init(fileEntry);
        this.simulator = new Simulator();
        this.mcts = new MonteCarloTreeSearch(game, simulator);
    }

    public static List<String> inFoList() {
        List<String> res = new ArrayList<>();
        res.add(String.valueOf(game.getState().rows));
        res.add(String.valueOf(game.getState().cols));
        res.add(String.valueOf(game.getState().playerNumber));
        res.add(time);
        res.add(solve);
        return res;
    }

    public static void outputToTxt(String result, File outFile) {
        try {
            FileWriter writer = new FileWriter(outFile, true);
            writer.write(result + "\n");
//            System.out.println("writing" + result);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() throws IOException, TimeoutException {
//        State khởi tạo bằng ma trận đầu vào
        long startTimes = System.currentTimeMillis();
        System.out.println(game.getState());
        while (game.notEnded()) {
            State state = game.getState();
//            Chọn qua các bước: selection - expansion - roullout - backprapogation
            State nextState = mcts.getBestNextState(state);
            game.updateState(nextState);
        }
        time = String.valueOf(System.currentTimeMillis() - startTimes);
        if (game.winGame()) {
            solve = "SOLVE";
            result = "FinalState: \n" + game.getState() + " Time: " + (System.currentTimeMillis() - startTimes)
                    + " Ratio: " + game.getState().getValue() + " Depth: " + game.getState().depth;
            System.out.println("FinalState: \n" + game.getState());
            System.out.println(" Time: " + (System.currentTimeMillis() - startTimes)
                    + " Ratio: " + game.getState().getValue() + " Depth: " + game.getState().depth);
            System.out.println(game.winGame());
        } else if (!game.winGame()) {
            solve = "CAN'T SOLVE";
            System.out.println(game.winGame());
        }
    }
}
