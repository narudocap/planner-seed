package planner.actions;

import java.util.List;
import java.util.Set;

import planner.CloudApp;

public class Up extends Action {
	@Override
	public String toString() {
		return "Up";
	}

	@Override
	public boolean perform(CloudApp app, Set<Action> pool, List<String> log) {
		throw new RuntimeException("Wrong!");
	}
	
	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		return true;
	}
}
