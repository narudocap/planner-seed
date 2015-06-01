package planner.actions;

import java.util.List;
import java.util.Set;

import planner.CLoc;
import planner.CloudApp;
import planner.VM;
import planner.Component;

/**
 * InstantiateC - instantiates a new component, with CId as given, in the given
 * VM.
 */
public class StartC extends Action {
	private String vmId;
	private String cId;

	/**
	 * @param vmId
	 * @param cId
	 */
	public StartC(String vmId, String cId) {
		this.vmId = vmId;
		this.cId = cId;
	}

	@Override
	public boolean perform(CloudApp app, Set<Action> pool, List<String> log) {
		VM vm = CloudApp.get(vmId, app.getVMs());
		if (vm == null)
			return false;
		Component c = CloudApp.get(cId, vm.getComponents());
		if (c == null)
			return false;
		if (c.isStartable(app)) {
			c.setStarted(true);
			log.add("Start component " + vmId + "." + cId);
			return true;
		}
		for (CLoc cloc : c.getDependencies()) {
			pool.add(new StartC(cloc.getVMId(), cloc.getCId()));
		}
		return false;
	}

	@Override
	public String toString() {
		return "StartC [" + vmId + ", " + cId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cId == null) ? 0 : cId.hashCode());
		result = prime * result + ((vmId == null) ? 0 : vmId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StartC other = (StartC) obj;
		if (cId == null) {
			if (other.cId != null)
				return false;
		} else if (!cId.equals(other.cId))
			return false;
		if (vmId == null) {
			if (other.vmId != null)
				return false;
		} else if (!vmId.equals(other.vmId))
			return false;
		return true;
	}
}
