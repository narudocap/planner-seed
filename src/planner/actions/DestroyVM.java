package planner.actions;

import java.util.List;
import java.util.Set;

import planner.CloudApp;
import planner.Component;
import planner.VM;

public class DestroyVM extends Action {
	private String vmId;

	/**
	 * @param vM
	 */
	public DestroyVM(String vmId) {
		this.vmId = vmId;
	}

	@Override
	public boolean perform(CloudApp app, Set<Action> pool, List<String> log) {
		VM vm = CloudApp.get(vmId, app.getVMs());
		if (vm == null)
			return true;
		if (!vm.getComponents().isEmpty()) // the vm cannot be destroyed
			return false;
		CloudApp.remove(vmId, app.getVMs());
		log.add("Destroy VM " + vmId);
		return true;
	}

	@Override
	public String toString() {
		return "DestroyVM[" + vmId + "]";
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
		DestroyVM other = (DestroyVM) obj;
		if (vmId == null) {
			if (other.vmId != null)
				return false;
		} else if (!vmId.equals(other.vmId))
			return false;
		return true;
	}
}
