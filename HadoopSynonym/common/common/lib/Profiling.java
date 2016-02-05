package common.lib;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

//import java.util.HashMap;

public class Profiling {

	class Cost {
		public String name;
		public long mills;

		public Cost(String name, long mills) {
			this.name = name;
			this.mills = mills;
		}

		@NotNull
		@Override
		public String toString() {
			return "Cost [name=" + name + ", mills=" + mills + "]";
		}
	}

	@NotNull
	public static ThreadLocal<ArrayList<Cost>> profilings = new ThreadLocal<ArrayList<Cost>>();

	static {
		profilings.set(new ArrayList<Cost>());
	}

	public static void clear() {
		if (profilings.get() == null) {
			profilings.set(new ArrayList<Cost>());
		}
		profilings.get().clear();
	}

	@NotNull
	public static String getString() {
		if (profilings.get() == null) {
			profilings.set(new ArrayList<Cost>());
		}
		String ret = "[ ";
		ArrayList<Cost> costs = profilings.get();
		for (Cost cost : costs) {
			String l = cost.name + ":" + cost.mills + ",";
			ret += l;
		}
		ret += " ]";
		return ret;
	}

	public static void addCost(String name, long mills) {
		if (profilings.get() == null) {
			profilings.set(new ArrayList<Cost>());
		}
		ArrayList<Cost> costs = profilings.get();
		Profiling profiling = new Profiling();
		Cost cost = profiling.new Cost(name, mills);
		costs.add(cost);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
