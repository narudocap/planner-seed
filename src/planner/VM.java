package planner;

import java.util.HashSet;
import java.util.Set;

public class VM extends AppElement {
	private Set<Component> components;

	/**
	 * @param vMId
	 */
	public VM(String vmId) {
		super(vmId);
		this.components = new HashSet<Component>();
	}

	/**
	 * @param vMId
	 * @param components
	 */
	public VM(String vmId, Set<Component> components) {
		super(vmId);
		this.components = components;
	}

	public void addComponent(String vmId, String cId, Set<String> imps,
			Set<String> exps) {
		components.add(new Component(vmId, cId, imps, exps));
	}

	public boolean allStarted() {
		for (Component c : components) {
			if (!c.isStarted()) {
				return false;
			}
		}
		return true;
	}

	public boolean allStopped() {
		for (Component c : components) {
			if (c.isStarted()) {
				return false;
			}
		}
		return true;
	}

	public Set<Component> getComponents() {
		return components;
	}

	public void setComponents(Set<Component> components) {
		this.components = components;
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
		VM other = (VM) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "VM [id=" + id + ", components=" + components + "]";
	}
}
