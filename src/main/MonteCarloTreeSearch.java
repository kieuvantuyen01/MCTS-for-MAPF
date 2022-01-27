package main;

import java.util.*;


public class MonteCarloTreeSearch {

	protected Simulator simulator;
	protected Game game;

	public MonteCarloTreeSearch(Game game, Simulator simulator) {
		this.game = game;
		this.simulator = simulator;
	}
// Chọn state có điểm cao nhất
	public State getBestNextState(State root) {
		root.reset();
//		500- 15,
		int time = 2000;
		while (time-- > 0) {
			State leaf = selection(root);
			State expandedLeaf = expansion(leaf);
			Value simulationResult = rollout(expandedLeaf);
			backpropagation(simulationResult, expandedLeaf);
		}
		return bestChild(root);
	}

	private State selection(State state) {// Done
		State st = state;
		while (st.isInTree && st.isNotTerminal()) {
//			System.out.println("selection");
			st = best_uct(st);
		}
		return st;
	}

	private State expansion(State state) {
		// expand just random child here ?
		// expand all child
		// expand some child
		State st = state;
		if (!st.isInTree) {
			st.isInTree = true;
			st.value = game.CreateZeroValue();
		} else if (state.isNotTerminal()) {
			System.out.println("---WTF---");
			// state = simulator.randomChild(state);
			// states.put(state, new Value(0, 0));
		}
		return st;
	}

	private State best_uct(State state) { // DONE
		// Value vx = states.get(state);
		Value vx = state.value;
		ArrayList<State> childs = state.getChilds();
		State ans = null;
		Value vbest = null;
		for (State st : childs) {
			if (!st.isInTree)
				return st;
			Value vv = st.value;
			if (vbest == null || vbest.compareTo_UCT(vv, vx.num) < 0) {
				vbest = vv;
				ans = st;
			}
		}
		return ans;
	}

	private Value rollout(State state) {
		while (state.isNotTerminal()) {
//			System.out.println("Rollout");
			state = state.getRandomChild();
		}
		return state.getValue();
	}

	private void backpropagation(Value simulation_result, State state) {
		while (state != null) {
			if (state.isInTree)
				state.value = state.value.update(state, simulation_result);
			state = state.parent;
		}
	}

	private State bestChild(State state) {
		ArrayList<State> childs = state.getChilds();
		State ans = null;
		Value vbest = null;
		for (State ch : childs) {
			Value vv = ch.value;
			if (vbest == null || vbest.compareTo(vv) < 0) {
				vbest = vv;
				ans = ch;
			}
		}
		return ans;
	}
}
