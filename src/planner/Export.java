package planner;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Export - an export can be connected to several imports.
 */
public class Export extends Port {
	/**
	 * For each import port connected to an export port, we keep its PLoc
	 * (location) and whether its component is active
	 */
	private Set<PLoc> connections;

	/**
	 * @param vMId
	 * @param cId
	 * @param pId
	 */
	public Export(String vmId, String cId, String pId) {
		super(vmId, cId, pId);
		this.connections = new HashSet<PLoc>();
	}

	public Set<PLoc> getConnections() {
		return connections;
	}

	@Override
	public String toString() {
		return "Export [" + vmId + ", " + cId + ", " + id + "), connections="
				+ connections + "]";
	}

}
