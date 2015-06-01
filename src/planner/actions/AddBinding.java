package planner.actions;

import java.util.List;
import java.util.Set;

import planner.*;

public class AddBinding extends Action {
	private PLoc imp;
	private PLoc exp;

	/**
	 * @param imp
	 * @param exp
	 */
	public AddBinding(PLoc imp, PLoc exp) {
		this.imp = imp;
		this.exp = exp;
	}

	@Override
	public boolean perform(CloudApp app, Set<Action> pool, List<String> log) {
		VM ivm = CloudApp.get(imp.getVMId(), app.getVMs());
		if (ivm == null)
			return false;
		Component ic = CloudApp.get(imp.getCId(), ivm.getComponents());
		if (ic == null)
			return false;
		Import i = CloudApp.get(imp.getPId(), ic.getImps());
		if (i == null)
			throw new RuntimeException("Import missing");
		else if (i.getConnection() != null) {
			if (!i.getConnection().equals(exp))
				throw new RuntimeException(
						"Import connected to a different port");
		} else
			i.setConnection(exp);
		VM evm = CloudApp.get(exp.getVMId(), app.getVMs());
		if (evm == null)
			return false;
		Component ec = CloudApp.get(exp.getCId(), evm.getComponents());
		if (ec == null)
			return false;
		Export e = CloudApp.get(exp.getPId(), ec.getExps());
		if (e == null)
			throw new RuntimeException("Export missing");
		if (!e.getConnections().contains(imp))
			e.getConnections().add(imp);
		log.add("Connect " + imp + " and " + exp);
		return true;
	}

	@Override
	public String toString() {
		return "AddBinding[" + imp + ", " + exp + "]";
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
		AddBinding other = (AddBinding) obj;
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
