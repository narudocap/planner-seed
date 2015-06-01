package planner.actions;

import java.util.List;
import java.util.Set;

import planner.CLoc;
import planner.CloudApp;
import planner.Component;
import planner.VM;

public class DestroyC extends Action {
	private String vmId;
	private String cId;

	/**
	 * @param vM
	 * @param component
	 */
	public DestroyC(String vmId, String cId) {
		this.vmId = vmId;
		this.cId = cId;
	}

	@Override
	public boolean perform(CloudApp app, Set<Action> pool, List<String> log) {
		VM vm = CloudApp.get(vmId, app.getVMs());
		if (vm == null)
			throw new RuntimeException("Missing VM");
		Component c = CloudApp.get(cId, vm.getComponents());
		if (c == null) {
			throw new RuntimeException("Missing component");
		}
		Set<CLoc> deps = c.getRelated();
		if (!deps.isEmpty()) {
//			for (CLoc cloc : deps)
//				pool.add(new StopC(cloc.getVMId(), cloc.getCId()));
//			if (c.isStarted())
//				pool.add(new StopC(vmId, cId));
			return false;
		}
		if (c.isStarted()) {
			pool.add(new StopC(vmId, cId));
			return false;
		}
		CloudApp.remove(cId, vm.getComponents());
		log.add("Destroy component " + vmId + "." + cId);
		return true;
	}

	@Override
	public String toString() {
		return "DestroyC[" + vmId + ", " + cId + "]";
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
		DestroyC other = (DestroyC) obj;
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
