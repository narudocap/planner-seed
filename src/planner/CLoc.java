package planner;

/**
 * CLoc - Component Locations A CLoc is a component address, given by a tuple
 * VMId . CId
 */
public class CLoc {
	private String vmId, cId;

	/**
	 * @param vmId
	 * @param cId
	 */
	public CLoc(String vmId, String cId) {
		this.vmId = vmId;
		this.cId = cId;
	}

	public String getVMId() {
		return vmId;
	}

	public void setVMId(String vmId) {
		this.vmId = vmId;
	}

	public String getCId() {
		return cId;
	}

	public void setCId(String cId) {
		this.cId = cId;
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
		CLoc other = (CLoc) obj;
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

	@Override
	public String toString() {
		return "PLoc [" + vmId + ", " + cId + "]";
	}
}
