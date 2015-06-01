package planner;

/**
 * Import - an import is only connected to one export. We assume all imports are
 * mandatory.
 */
public class Import extends Port {
	/**
	 * connection keeps the location of the port to which it is to be connected
	 * (null if not connected).
	 */
	private PLoc connection;

	/**
	 * @param vMId
	 * @param cId
	 * @param pId
	 * @param connection
	 * @param active
	 */
	public Import(String vMId, String cId, String pId, PLoc connection,
			boolean active) {
		super(vMId, cId, pId);
		this.connection = connection;
	}

	/**
	 * @param vMId
	 * @param cId
	 * @param pId
	 */
	public Import(String vMId, String cId, String pId) {
		super(vMId, cId, pId);
		this.connection = null;
	}

	public PLoc getConnection() {
		return connection;
	}

	public void setConnection(PLoc connection) {
		this.connection = connection;
	}

	@Override
	public String toString() {
		return "Import [" + vmId + ", " + cId + ", " + id + ", connection="
				+ connection + "]";
	}
}
