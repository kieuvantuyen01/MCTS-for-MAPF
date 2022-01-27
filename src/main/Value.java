package main;


public class Value {
	public static int modelNumber = 1;

	protected int num;
	protected double value;
	protected double bestValue;
	protected boolean mark[];

	public Value(int num, double value) {
		this.num = num;
		this.value = value;
		bestValue = value;
	}

	public Value(int i, double d, boolean[] m) {
		this.num = i;
		this.value = d;
		bestValue = value;
		this.mark = m;
	}


	public int compareTo(Value vv) {
		if (value < vv.value)
			return -1;
		else
			return 1;
	}
	public int compareTo_UCT(Value vv, int total_number) {
		double u1 = value + Math.sqrt(2 * Math.log(total_number) / num);
		double u2 = vv.value + Math.sqrt(2 * Math.log(total_number) / vv.num);
		if (u1 < u2)
			return -1;
		else
			return 1;
	}

	public Value update(State state, Value simulationResult) {
		State st = state;
		Value simulation_result = simulationResult;
		++num;
		value = (value * (num - 1) + simulation_result.value - (st.lastColor != -1
						? simulation_result.mark[st.lastColor]
						? (double) (1.5) * (1 / st.playerNumber) : 0 : 0)) / num;
		return this;
	}

	@Override
	public String toString() {
		return "{num: " + num + ", value: " + value + ", maxValue: " + bestValue + "}";
	}
}
