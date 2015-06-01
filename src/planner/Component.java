package planner;

import java.util.HashSet;
import java.util.Set;

public class Component extends AppElement {
	private String vmId; // id of the VM the component is in
	private Set<Import> imps; // import ports
	private Set<Export> exps; // export ports
	private boolean started; // indicates whether the component is started

	/**
	 * @param vMId
	 * @param cId
	 * @param imps
	 * @param exps
	 */
	public Component(String vmId, String cId, Set<String> imps, Set<String> exps) {
		super(cId);
		id = cId;
		this.vmId = vmId;
		this.imps = new HashSet<Import>();
		for (String pId : imps)
			this.imps.add(new Import(vmId, cId, pId));
		this.exps = new HashSet<Export>();
		for (String pId : exps)
			this.exps.add(new Export(vmId, cId, pId));
		started = false;
	}

	/**
	 * A component is "startable" if all its imports are satisfied, that is, if
	 * all components it depends on are active.
	 * 
	 * @param app
	 * @return
	 */
	public boolean isStartable(CloudApp app) {
		for (Import imp : imps) {
			VM vm = CloudApp.get(imp.getConnection().getVMId(), app.getVMs());
			if (vm == null)
				return false; // the VM to host the component is not ready
			Component c = CloudApp.get(imp.getConnection().getCId(),
					vm.getComponents());
			if (c == null || !c.started)
				return false;
		}
		return true;
	}

	/**
	 * A component is "stopable" if all the components that depend on it, that
	 * is, connected to one of its exports, are stopped.
	 * 
	 * @param app
	 * @return
	 */
	public boolean isStopable(CloudApp app) {
		for (Export exp : exps) {
			for (PLoc ploc : exp.getConnections()) {
				VM vm = CloudApp.get(ploc.getVMId(), app.getVMs());
				if (vm != null) {
					Component c = CloudApp.get(ploc.getCId(),
							vm.getComponents());
					if (c != null && c.started)
						return false;
				}
			}
		}
		return true;
	}

	/**
	 * getDependencies() returns the CLocs of the components that depend on it
	 * (exports)
	 * 
	 * @return
	 */
	public Set<CLoc> getDependencies() {
		Set<CLoc> deps = new HashSet<CLoc>();
//		for (Import imp : imps) {
//			if (imp.getConnection() != null) {
//				deps.add(new CLoc(imp.getConnection().getVMId(), imp
//						.getConnection().getCId()));
//			}
//		}
		for (Export exp : exps) {
			for (PLoc ploc : exp.getConnections()) {
				deps.add(new CLoc(ploc.getVMId(), ploc.getCId()));
			}
		}
		return deps;
	}

	/**
	 * getRelated() returns the CLocs of the components it is related with
	 * (imports and exports)
	 * 
	 * @return
	 */
	public Set<CLoc> getRelated() {
		Set<CLoc> deps = new HashSet<CLoc>();
		for (Import imp : imps) {
			if (imp.getConnection() != null) {
				deps.add(new CLoc(imp.getConnection().getVMId(), imp
						.getConnection().getCId()));
			}
		}
		for (Export exp : exps) {
			for (PLoc ploc : exp.getConnections()) {
				deps.add(new CLoc(ploc.getVMId(), ploc.getCId()));
			}
		}
		return deps;
	}

	public String getVMId() {
		return vmId;
	}

	public Set<Import> getImps() {
		return imps;
	}

	public Set<Export> getExps() {
		return exps;
	}

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Component other = (Component) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (vmId == null) {
			if (other.vmId != null)
				return false;
		} else if (!vmId.equals(other.vmId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Component [vmId=" + vmId + ", cid=" + id + ", imps=" + imps
				+ ", exps=" + exps + ", started=" + started + "]";
	}
}
