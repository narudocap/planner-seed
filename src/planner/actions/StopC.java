package planner.actions;

import java.util.List;
import java.util.Set;

import planner.CLoc;
import planner.CloudApp;
import planner.VM;
import planner.Component;

/**
 * StopC - stops a component
 */
public class StopC extends Action {
	private String vmId;
	private String cId;

	/**
	 * @param vmId
	 * @param cId
	 */
	public StopC(String vmId, String cId) {
		this.vmId = vmId;
		this.cId = cId;
	}

	@Override
	public boolean perform(CloudApp app, Set<Action> pool, List<String> log) {
		VM vm = CloudApp.get(vmId, app.getVMs());
		if (vm != null) {
			Component c = CloudApp.get(cId, vm.getComponents());
			if (c != null) {
				if (c.isStopable(app)) {
					c.setStarted(false);
					log.add("Stop component " + vmId + "." + cId);
					return true;
				}
				for (CLoc cloc : c.getDependencies()) {
					pool.add(new StopC(cloc.getVMId(), cloc.getCId()));
				}
			} else
				return true;
		} else
			return true;
		return false;
	}

	@Override
	public String toString() {
		return "StopC[" + vmId + ", " + cId + "]";
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
		StopC other = (StopC) obj;
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
