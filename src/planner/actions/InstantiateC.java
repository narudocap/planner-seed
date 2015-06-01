package planner.actions;

import java.util.List;
import java.util.Set;

import planner.CloudApp;
import planner.VM;
import planner.Component;

/**
 * InstantiateC - instantiates a new component, with CId as given, in the given
 * VM.
 */
public class InstantiateC extends Action {
	private String vmId;
	private String cId;
	private Set<String> imps, exps;

	/**
	 * @param vmId
	 * @param cId
	 * @param imps
	 * @param exps
	 */
	public InstantiateC(String vmId, String cId, Set<String> imps,
			Set<String> exps) {
		this.vmId = vmId;
		this.cId = cId;
		this.imps = imps;
		this.exps = exps;
	}

	@Override
	public boolean perform(CloudApp app, Set<Action> pool, List<String> log) {
		VM vm = CloudApp.get(vmId, app.getVMs());
		if (vm == null)
			return false; // the VM to host the component is not ready
		Component c = CloudApp.get(cId, vm.getComponents());
		if (c == null) {
			vm.addComponent(vmId, cId, imps, exps);
			log.add("Instantiate component " + vmId + "." + cId);
			return true;
		}
		pool.add(new StartC(vmId, cId));
		return false;
	}

	@Override
	public String toString() {
		return "InstantiateC[" + vmId + ", " + cId + ", " + imps + ", " + exps
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cId == null) ? 0 : cId.hashCode());
		result = prime * result + ((exps == null) ? 0 : exps.hashCode());
		result = prime * result + ((imps == null) ? 0 : imps.hashCode());
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
		InstantiateC other = (InstantiateC) obj;
		if (cId == null) {
			if (other.cId != null)
				return false;
		} else if (!cId.equals(other.cId))
			return false;
		if (exps == null) {
			if (other.exps != null)
				return false;
		} else if (!exps.equals(other.exps))
			return false;
		if (imps == null) {
			if (other.imps != null)
				return false;
		} else if (!imps.equals(other.imps))
			return false;
		if (vmId == null) {
			if (other.vmId != null)
				return false;
		} else if (!vmId.equals(other.vmId))
			return false;
		return true;
	}
}
