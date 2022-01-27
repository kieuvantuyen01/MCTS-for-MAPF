package main;

import java.io.*;
import java.util.*;

public class State {
    public boolean isInTree = false;
    public State parent = null;
    public Value value = null;
    private ArrayList<State> childs = null;
    protected int depth = 0;
    public int table[][];
    public int rows, cols;
    public int nextColor;
    public int lastColor;
    public int playerNumber = 0;

    public State.PII[] lastMove;
    public State.PII[] target;

    class PII {
        public Integer first;
        public Integer second;

        public PII(int i, int j) {
            first = i;
            second = j;
        }
        @Override
        public String toString() {
            return "{" + first + ", " + second + "}";
        }
    }
//    public State(String str) {
//        File file = new File("testcase\\" + str);
//
//        try {
//            Scanner sc;
//            sc = new Scanner(file);
//            LineNumberReader lnr = new LineNumberReader(new FileReader(file));
////			Kich thuoc ma tran
//            while (lnr.readLine() != null) {
//                row++;
//            }
//            size = sc.nextInt();
////			So cap
//            playerNumber = sc.nextInt();
//            table = new int[size][size];
////			Vị trí cuối cùng của các cặp số -- ví dụ số 3 cuối cùng ở vị trí i,j...
//            lastMove = new State.PII[playerNumber + 1];
////			Vị trí mục tiêu -> kết thúc trò chơi
//            target = new State.PII[playerNumber + 1];
//            for (int i = 1; i <= playerNumber; ++i) {
//                lastMove[i] = null;
//                target[i] = null;
//            }
//            for (int i = 0; i < size; ++i)
//                for (int j = 0; j < size; ++j) {
//                    table[i][j] = sc.nextInt();
//                    if (table[i][j] != 0)
//                        if (lastMove[table[i][j]] == null)
//                            lastMove[table[i][j]] = new State.PII(i, j);
//                        else
//                            target[table[i][j]] = new State.PII(i, j);
//                }
//            lastColor = playerNumber;
//            setNextColor();
//            parent = null;
//            lastColor = -1;
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }

    public State(File file) throws FileNotFoundException{
//        File file = new File(str);
            Scanner sc = new Scanner(file);
            List<List<String>> matrix = new ArrayList<>();
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
//                System.out.println(line);
                List<String> arr = Arrays.asList(line.split(" "));
                matrix.add(arr);
            }
            sc.close();

            rows = matrix.size();
//            System.out.println(rows);
            cols = matrix.get(0).size();
            table = new int[rows+1][cols+1];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    table[i][j] = Integer.parseInt(matrix.get(i).get(j));
                }
            }
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (playerNumber < table[i][j]) {
                        playerNumber = table[i][j];
                    }
                }
            }
            //Vị trí cuối cùng của các cặp số -- ví dụ số 3 cuối cùng ở vị trí i,j...
            lastMove = new State.PII[playerNumber + 1];
            //Vị trí mục tiêu -> kết thúc trò chơi
            target = new State.PII[playerNumber + 1];
            for (int i = 1; i <= playerNumber; ++i) {
                lastMove[i] = null;
                target[i] = null;
            }
            for (int i = 0; i < rows; ++i)
                for (int j = 0; j < cols; ++j) {
                    if (table[i][j] != 0)
                        if (lastMove[table[i][j]] == null)
                            lastMove[table[i][j]] = new State.PII(i, j);
                        else
                            target[table[i][j]] = new State.PII(i, j);
                }
            lastColor = playerNumber;
            setNextColor();
            parent = null;
            lastColor = -1;
    }
    public State(State st, Action act) {
        rows = st.rows;
        cols = st.cols;
        playerNumber = st.playerNumber;
        table = new int[rows][cols];
        lastMove = new State.PII[playerNumber + 1];
        target = new State.PII[playerNumber + 1];
        for (int i = 1; i <= playerNumber; ++i) {
            lastMove[i] = st.lastMove[i];
            target[i] = st.target[i];
        }
        for (int i = 0; i < rows; ++i)
            for (int j = 0; j < cols; ++j)
                table[i][j] = st.table[i][j];
        table[act.y][act.x] = act.color;
        lastMove[act.color] = new State.PII(act.y, act.x);
        parent = st;
        lastColor = st.nextColor;
        setNextColor();
        depth = st.depth + 1;
    }
    // Chọn ô màu thích hợp - chạy hết tất cả các vị trí có số, chọn vị trí tốt nhất
    private void setNextColor() {
        double best = 1000000;
        int bestColor = lastColor % playerNumber + 1;
        for (int i = 1; i <= playerNumber; ++i) {
            nextColor = (lastColor + i - 1) % playerNumber + 1;

            int cn = childNumber();
            int cnt = childNumberTarget();
            if (isNear(nextColor) || cn == 0 || cnt == 0)
                continue;
            if (cn == 1) {
                bestColor = nextColor;
                break;
            }
            if (cnt == 1) {
                bestColor = nextColor;
                State.PII temp = lastMove[nextColor];
                lastMove[nextColor] = target[nextColor];
                target[nextColor] = temp;
                break;
            }
            // double gn = cn - dis(nextColor) / size * 2;
            if (best > cn) {
                best = cn;
                bestColor = nextColor;
            } else if (best == cn) {
                // if (dis(nextColor) > dis(bestColor))
                // bestColor = nextColor;
            }
        }
        nextColor = bestColor;
    }

    // kiểm tra 2 ô cùng màu  mở cạnh nhau
    private boolean isNear(int color) {
        return Math.abs(lastMove[color].first - target[color].first)
                + Math.abs(lastMove[color].second - target[color].second) == 1;
    }

    public boolean win (){
        int res = 0;
        for (int i = 1; i <= playerNumber; ++i)
            res += isNear(i) ? 1 : 0;
        if (res == playerNumber) {
            System.out.println(res);
            return true;
        }
        System.out.println(res);
        return false;
    }
    public boolean isNotTerminal() {
        int res = 0;
        for (int i = 1; i <= playerNumber; ++i)
            res += isNear(i) ? 1 : 0;
        if (res == playerNumber) {
//			System.out.println("Target and source are neighbor - too easy!");
            return false;
        }
        if (!hasChild()) {
//			System.out.println("No children to expand!!");
            return false;
        }
        return true;
    }

//	Giá trị của 1 State được tính bằng khoảng cách các cặp nối với nhau/số cặp
    public Value getValue() {
        if (isNotTerminal()) {
            System.out.println("getValue");
            return null;
        }
        double res = 0;
        boolean m[] = new boolean[playerNumber + 1];
        for (int i = 1; i <= playerNumber; ++i) {
            res += isNear(i) ? 1 : 0;
            // res -= isNear(i) ? (double) depth / (2 * size * size) : 0;
            m[i] = isNear(i);
        }

        return new Value(-1, res / playerNumber, m);
    }

    public ArrayList<State> refreshChilds() {
        ArrayList<State> childss = new ArrayList<State>();
        for (int i = -1; i < 2; ++i)
            for (int j = (i == 0 ? -1 : 0); j < (i == 0 ? 2 : 1); ++j)
                if (lastMove[nextColor].first + i >= 0 && lastMove[nextColor].first + i < rows
                        && lastMove[nextColor].second + j >= 0 && lastMove[nextColor].second + j < cols
                        && table[lastMove[nextColor].first + i][lastMove[nextColor].second + j] == 0){
                    childss.add(Simulator.simulateX(this,
                            new Action(lastMove[nextColor].second + j, lastMove[nextColor].first + i, nextColor)));}
        return childss;
    }

    private boolean hasChild() {
        for (int i = -1; i < 2; ++i)
            for (int j = (i == 0 ? -1 : 0); j < (i == 0 ? 2 : 1); ++j)
                if (lastMove[nextColor].first + i >= 0 && lastMove[nextColor].first + i < rows
                        && lastMove[nextColor].second + j >= 0 && lastMove[nextColor].second + j < cols
                        && table[lastMove[nextColor].first + i][lastMove[nextColor].second + j] == 0)
                    return true;
        return false;
    }
//    public boolean hasChildren() {
//        for (int i = -1; i < 2; ++i)
//            for (int j = (i == 0 ? -1 : 0); j < (i == 0 ? 2 : 1); ++j)
//                if (lastMove[nextColor].first + i >= 0 && lastMove[nextColor].first + i < size
//                        && lastMove[nextColor].second + j >= 0 && lastMove[nextColor].second + j < size
//                        && table[lastMove[nextColor].first + i][lastMove[nextColor].second + j] == 0)
//                    return true;
//        return false;
//    }
    //	Trả về số con có thể là số ô xung quanh có value=0
    private int childNumber() {
        int ans = 0;

        for (int i = -1; i < 2; ++i)
            for (int j = (i == 0 ? -1 : 0); j < (i == 0 ? 2 : 1); ++j)
//				(i=-1,j=0 :U), (i=0, j=-1: L);(i=0, j=0); (i=0, j=1); (i=1, j=0);

                if (lastMove[nextColor].first + i >= 0 && lastMove[nextColor].first + i < rows
                        && lastMove[nextColor].second + j >= 0 && lastMove[nextColor].second + j < cols
                        && table[lastMove[nextColor].first + i][lastMove[nextColor].second + j] == 0)
                    ++ans;
        return ans;
    }
    // Tính số ô con xq ô target
    private int childNumberTarget() {
        State.PII temp = lastMove[nextColor];
        lastMove[nextColor] = target[nextColor];
        target[nextColor] = temp;
        int res = childNumber();
        temp = lastMove[nextColor];
        lastMove[nextColor] = target[nextColor];
        target[nextColor] = temp;
        return res;
    }

    public ArrayList<State> getChilds() {// what if change color ? TODO
        if (isInTree) {
            if (childs == null)
                childs = refreshChilds();
            return childs;
        }
        return refreshChilds();
    }

    public State getRandomChild() {
        ArrayList<State> childss = getChilds();
        Random random = new Random();
        int v = random.nextInt(childss.size());
        if (childss.isEmpty())
            return null;
        return childss.get(v);
    }

    public void reset() {
        isInTree = false;
        parent = null;
        value = null;
        childs = null;
        lastColor = -1;
    }

    @Override
    public String toString() {
        String s = "{";
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j)
                if(table[i][j]<10)
                    s += " "+ table[i][j] + ", ";
                else
                    s += table[i][j] + ", ";
            s += (i != rows - 1 ? "\n " : "");
        }
        return s + "}";
    }
    boolean hashed;
    int hash;

    @Override
    public int hashCode() {
        return hash = (hashed ? hash
                : 31 * Objects.hash(parent, nextColor) + (table == null ? 0 : Arrays.deepHashCode(table)));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj.getClass() != State.class)
            return false;
        State st = (State) obj;

        boolean res = nextColor == st.nextColor & (parent == null ? st.parent == null : parent.equals(st.parent));
        for (int i = 0; i < rows; ++i)
            for (int j = 0; j < cols; ++j)
                if (table[i][j] != st.table[i][j])
                    res = false;
        return res;
    }
}
