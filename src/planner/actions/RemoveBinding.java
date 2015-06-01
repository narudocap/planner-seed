package planner.actions;

import java.util.List;
import java.util.Set;

import planner.CloudApp;
import planner.Component;
import planner.Export;
import planner.Import;
import planner.PLoc;
import planner.VM;

public class RemoveBinding extends Action {
	private PLoc imp;
	private PLoc exp;

	/**
	 * @param imp
	 * @param exp
	 */
	public RemoveBinding(PLoc imp, PLoc exp) {
		this.imp = imp;
		this.exp = exp;
	}

	@Override
	public boolean perform(CloudApp app, Set<Action> pool, List<String> log) {
		// import side
		VM ivm = CloudApp.get(imp.getVMId(), app.getVMs());
		if (ivm == null)
			throw new RuntimeException("Missing VM");
		Component ic = CloudApp.get(imp.getCId(), ivm.getComponents());
		if (ic == null)
			throw new RuntimeException("Missing component");
		Import i = CloudApp.get(imp.getPId(), ic.getImps());
		if (i == null)
			throw new RuntimeException("Missing import");
		// export side
		VM evm = CloudApp.get(exp.getVMId(), app.getVMs());
		if (evm == null)
			throw new RuntimeException("Missing VM");
		Component ec = CloudApp.get(exp.getCId(), evm.getComponents());
		if (ec == null)
			throw new RuntimeException("Missing component");
		// if the export-side component is active we need to stop it before
		// removing the link
		if (ic.isStarted()) {
			pool.add(new StopC(imp.getVMId(), imp.getCId()));
		}
		if (ec.isStarted()) {
			pool.add(new StopC(exp.getVMId(), exp.getCId()));
		}
		if (ic.isStarted() || ec.isStarted()) {
			return false;
		}
		i.setConnection(null);
		Export e = CloudApp.get(exp.getPId(), ec.getExps());
		if (e == null)
			throw new RuntimeException("Missing export");
		e.getConnections().remove(imp);
		log.add("Remove connection " + imp + " and " + exp);
		return true;
	}

	@Override
	public String toString() {
		return "RemoveBinding[" + imp + ", " + exp + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((exp == null) ? 0 : exp.hashCode());
		result = prime * result + ((imp == null) ? 0 : imp.hashCode());
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
		RemoveBinding other = (RemoveBinding) obj;
		if (exp == null) {
			if (other.exp != null)
				return false;
		} else if (!exp.equals(other.exp))
			return false;
		if (imp == null) {
			if (other.imp != null)
				return false;
		} else if (!imp.equals(other.imp))
			return false;
		return true;
	}
}
