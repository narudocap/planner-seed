package planner.actions;

import java.util.List;
import java.util.Set;

import planner.CloudApp;
import planner.VM;

/**
 * InstantiateVM - VM instantiation. We assume a VM is instantiated empty, but
 * it could contain pre-established components.
 */
public class InstantiateVM extends Action {
	private String vmId; // Id of the VM to be instantiated

	/**
	 * @param vMId
	 */
	public InstantiateVM(String vmId) {
		this.vmId = vmId;
	}

	@Override
	public boolean perform(CloudApp app, Set<Action> pool, List<String> log) {
		app.addVM(vmId);
		log.add("InstantiateVM(" + vmId + ")");
		return true;
	}

	@Override
	public String toString() {
		return "InstantiateVM[" + vmId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		InstantiateVM other = (InstantiateVM) obj;
		if (vmId == null) {
			if (other.vmId != null)
				return false;
		} else if (!vmId.equals(other.vmId))
			return false;
		return true;
	}
}
