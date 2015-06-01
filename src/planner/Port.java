package planner;

/**
 * Port - each port has an id, ports' ids must be unique inside the components
 * they belong to
 */
public abstract class Port extends AppElement {
	protected String cId; // id of the component it is in
	protected String vmId; // id of the VM the component is in

	/**
	 * @param pId
	 * @param cId
	 * @param vMId
	 */
	public Port(String vMId, String cId, String pId) {
		super(pId);
		this.id = pId;
		this.cId = cId;
		this.vmId = vMId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Port other = (Port) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
