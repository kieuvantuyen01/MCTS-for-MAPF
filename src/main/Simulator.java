package main;

import java.util.ArrayList;

public class Simulator {

    public static State simulateX(State state, Action action) {
        Action act = action;
        State st = state;
        State res = null;
        if (st.table[act.y][act.x] == 0)
            res = new State(st, act);
        return res;
    }

    public static ArrayList<Action> getActionsxt(State state) {
        State st = (State) state;
        ArrayList<Action> acs = new ArrayList<Action>();
        for (int i = -1; i < 2; ++i)
            for (int j = (i == 0 ? -1 : 0); j < (i == 0 ? 2 : 1); ++j)
                if (st.lastMove[st.nextColor].first + i >= 0 && st.lastMove[st.nextColor].first + i < st.rows
                        && st.lastMove[st.nextColor].second + j >= 0 && st.lastMove[st.nextColor].second + j < st.cols
                        && st.table[st.lastMove[st.nextColor].first + i][st.lastMove[st.nextColor].second + j] == 0)
                    acs.add(new Action(st.lastMove[st.nextColor].second + j, st.lastMove[st.nextColor].first + i,
                            st.nextColor));
        return acs;
    }

    public ArrayList<Action> getActionst(State state) {
        State st = (State) state;
        ArrayList<Action> acs = new ArrayList<Action>();
        for (int i = -1; i < 2; ++i)
            for (int j = (i == 0 ? -1 : 0); j < (i == 0 ? 2 : 1); ++j)
                if (st.lastMove[st.nextColor].first + i >= 0 && st.lastMove[st.nextColor].first + i < st.rows
                        && st.lastMove[st.nextColor].second + j >= 0 && st.lastMove[st.nextColor].second + j < st.cols
                        && st.table[st.lastMove[st.nextColor].first + i][st.lastMove[st.nextColor].second + j] == 0)
                    acs.add(new Action(st.lastMove[st.nextColor].second + j, st.lastMove[st.nextColor].first + i,
                            st.nextColor));
        return acs;
    }
}
