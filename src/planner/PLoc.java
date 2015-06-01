package planner;
/**
 * PLoc - Port Locations 
 * A PLoc is a port address, given by a tuple VMId . CId . PId
 */
public class PLoc {
	private String vmId, cId, pId;

	/**
	 * @param vmId
	 * @param cId
	 * @param pId
	 */
	public PLoc(String vmId, String cId, String pId) {
		super();
		this.vmId = vmId;
		this.cId = cId;
		this.pId = pId;
	}

	public String getVMId() {
		return vmId;
	}

	public void setVMId(String vMId) {
		vmId = vMId;
	}

	public String getCId() {
		return cId;
	}

	public void setCId(String cId) {
		this.cId = cId;
	}

	public String getPId() {
		return pId;
	}

	public void setPId(String pId) {
		this.pId = pId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cId == null) ? 0 : cId.hashCode());
		result = prime * result + ((pId == null) ? 0 : pId.hashCode());
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
		PLoc other = (PLoc) obj;
		if (cId == null) {
			if (other.cId != null)
				return false;
		} else if (!cId.equals(other.cId))
			return false;
		if (pId == null) {
			if (other.pId != null)
				return false;
		} else if (!pId.equals(other.pId))
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
		return "PLoc [" + vmId + ", " + cId + ", " + pId + "]";
	}
}
