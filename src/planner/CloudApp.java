package planner;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import planner.actions.*;

/**
 * A Cloud App is represented as a set of VMs, each of which may contain
 * components interconnected through their import and export ports. A
 * CloudManager can (1) calculate the differences between two cloud apps,
 * returning a set of app elements to be removed and a set of app elements to be
 * added.
 */
public class CloudApp {

	Set<VM> vms = new HashSet<VM>();
	List<Action> actions = new LinkedList<Action>(); // pending actions to be
														// formed

	public void addVM(String vmId) {
		VM vm = get(vmId, vms);
		if (vm != null)
			throw new RuntimeException("VM already exists");
		vms.add(new VM(vmId));
	}

	public void addComponent(String vmId, String cId, Set<String> imps,
			Set<String> exps) {
		VM vm = get(vmId, vms);
		if (vm == null)
			throw new RuntimeException("VM does not exist");
		Component c = get(cId, vm.getComponents());
		if (c != null)
			throw new RuntimeException("C already exists");
		vm.getComponents().add(new Component(vmId, cId, imps, exps));
	}

	public void addBinding(String vmId1, String cId1, String pId1,
			String vmId2, String cId2, String pId2) {
		VM vm1 = get(vmId1, vms);
		if (vm1 == null)
			throw new RuntimeException("Incorrect vm");
		Component c1 = get(cId1, vm1.getComponents());
		if (c1 == null)
			throw new RuntimeException("Incorrect component");
		Import imp = get(pId1, c1.getImps());
		if (imp == null)
			throw new RuntimeException("Incorrect port");
		imp.setConnection(new PLoc(vmId2, cId2, pId2));
		VM vm2 = get(vmId2, vms);
		if (vm2 == null)
			throw new RuntimeException("Incorrect vm");
		Component c2 = get(cId2, vm2.getComponents());
		if (c2 == null)
			throw new RuntimeException("Incorrect component");
		Export exp = get(pId2, c2.getExps());
		if (exp == null)
			throw new RuntimeException("Incorrect port");
		exp.getConnections().add(new PLoc(vmId1, cId1, pId1));
	}

	/**
	 * diff calculates the difference between two cloud apps. It returns a set
	 * of application elements in the first app that are not in the second app.
	 * Notice that components may have moved from one vm to another, this is
	 * considered a difference. The component is to be removed and later added.
	 * 
	 * @param first
	 *            first cloud app
	 * @param second
	 *            second cloud app
	 * @return set with the elements in the source not in the target
	 */
	public static Set<AppElement> diff(CloudApp first, CloudApp second) {
		Set<AppElement> diff = new HashSet<AppElement>();
		// for each vm in the source configuration...
		for (VM vm1 : first.vms) {
			// we try to get a vm with the same id in the target
			VM vm2 = get(vm1, second.vms);
			// if found
			if (vm2 != null) {
				// we similarly check every component in it
				for (Component c1 : vm1.getComponents()) {
					Component c2 = get(c1, vm2.getComponents());
					if (c2 != null) {
						for (Import imp1 : c1.getImps()) {
							Import imp2 = get(imp1, c2.getImps());
							// the port should be there
							if (imp2 == null)
								throw new RuntimeException(
										"Components with the same ids but different ports!");
							// it should be connected to the same component
							if (imp1.getConnection() != null
									&& !imp1.getConnection().equals(
											imp2.getConnection())) {
								diff.add(new Binding(new PLoc(vm1.getId(), c1
										.getId(), imp1.getId()), imp1
										.getConnection()));
							}
						}
					} else { // not found
						diff.add(c1); // need to clone?
					}
				}
			} else {
				// if there is no corresponding vm in the target we need to
				// remove it and all its components and bindings
				diff.add(vm1); // need to clone?
				for (Component c1 : vm1.getComponents()) {
					diff.add(c1); // need cloning?
					for (Import imp1 : c1.getImps()) {
						if (imp1.getConnection() != null) {
							diff.add(new Binding(new PLoc(vm1.getId(), c1
									.getId(), imp1.getId()), imp1
									.getConnection()));
						}
					}
				}
			}
		}
		return diff;
	}

	/**
	 * actions(source,target) gives a list of high-level actions to go from the
	 * source configuration to the target configuration. The actions are given
	 * in a list of actions which need further refinement. The sequence is
	 * ordered as VM and C instantiations / binding removal and VM and C
	 * destructions / binding addition. Splitting them in these three phases is
	 * important for the next stage, otherwise we may be trying to start things
	 * that need to be temporarily stopped before time.
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	public static List<Action> actions(CloudApp source, CloudApp target) {
		List<Action> actions = new LinkedList<Action>();
		List<Action> instantiateVMs = new LinkedList<Action>();
		List<Action> instantiateCs = new LinkedList<Action>();
		List<Action> addBindings = new LinkedList<Action>();
		List<Action> destroyVMs = new LinkedList<Action>();
		List<Action> destroyCs = new LinkedList<Action>();
		List<Action> removeBindings = new LinkedList<Action>();
		// elements to be added
		for (AppElement elt : diff(target, source)) {
			if (elt instanceof VM) {
				VM vm = (VM) elt;
				instantiateVMs.add(new InstantiateVM(vm.getId()));
			}
			if (elt instanceof Component) {
				Component c = (Component) elt;
				Set<String> imps = new HashSet<String>();
				for (Import imp : c.getImps()) {
					imps.add(imp.getId());
				}
				Set<String> exps = new HashSet<String>();
				for (Export exp : c.getExps()) {
					exps.add(exp.getId());
				}
				instantiateCs.add(new InstantiateC(c.getVMId(), c.getId(),
						imps, exps));
				if (c.isStarted())
					instantiateCs.add(new StartC(c.getVMId(), c.getId()));
			}
			if (elt instanceof Binding) {
				Binding b = (Binding) elt;
				addBindings.add(new AddBinding(b.getImp(), b.getExp()));
			}
		}
		// elements to be removed
		for (AppElement elt : diff(source, target)) {
			if (elt instanceof VM) {
				VM vm = (VM) elt;
				destroyVMs.add(new DestroyVM(vm.getId()));
			}
			if (elt instanceof Component) {
				Component c = (Component) elt;
				destroyCs.add(new DestroyC(c.getVMId(), c.getId()));
			}
			if (elt instanceof Binding) {
				Binding b = (Binding) elt;
				removeBindings.add(new RemoveBinding(b.getImp(), b.getExp()));
			}
		}
		actions.addAll(instantiateVMs);
		actions.addAll(instantiateCs);
		if ((!instantiateVMs.isEmpty() || !instantiateCs.isEmpty())
				&& (!removeBindings.isEmpty() || !destroyCs.isEmpty() || !destroyVMs
						.isEmpty()))
			actions.add(new Down());
		actions.addAll(removeBindings);
		actions.addAll(destroyCs);
		actions.addAll(destroyVMs);
		if ((!removeBindings.isEmpty() || !destroyCs.isEmpty() || !destroyVMs
				.isEmpty()) && !addBindings.isEmpty())
			actions.add(new Up());
		actions.addAll(addBindings);

		return actions;
	}

	/**
	 * genPlan(source,target) generates a sequence of low-level actions to go
	 * from source to target. It starts from the actions returned by the
	 * actions(source,target) function and from there it refines and sort the
	 * actions to be perform. The resulting sequence of actions is given as a
	 * list of strings. This sequence includes the start and stop of components,
	 * in an order in which dependencies between them are satisfied.
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	public static List<String> genPlan(CloudApp source, CloudApp target) {
		List<Action> actions = actions(source, target);
		Set<Action> pool = new HashSet<Action>();
		Set<Action> addons = new HashSet<Action>();
		List<String> log = new LinkedList<String>();
		Iterator<Action> itactions = actions.iterator();
		while (itactions.hasNext()) {
			do {
				Action current = itactions.next();
				if ((current instanceof Down) || (current instanceof Up)) {
					break;
				}
				pool.add(current);
			} while (itactions.hasNext());
			while (!pool.isEmpty()) {
				Iterator<Action> itpool = pool.iterator();
				while (itpool.hasNext()) {
					Action current = itpool.next();
					// System.out.println("Try: "+ current);
					if (current.perform(source, addons, log)) {
						// System.out.println("Success: "+ current);
						itpool.remove();
					}
					// System.out.println("Addons: "+addons);
				}
				pool.addAll(addons);
				addons.clear();
			}
		}
		return log;
	}

	/**
	 * get returns an element in es with identifier id. The getId() method of
	 * AppElement is used to get identifiers.
	 * 
	 * @param id
	 *            id of the element to seek
	 * @param es
	 *            set of elements where to seek
	 * @return
	 */
	public static <T extends AppElement> T get(String id, Set<T> es) {
		Iterator<T> it = es.iterator();
		while (it.hasNext()) {
			T e2 = it.next();
			if (id.equals(e2.getId())) {
				return e2;
			}
		}
		return null; // reaches the end without finding it
	}

	/**
	 * get returns an element in es which is equal to e1.
	 * 
	 * @param e1
	 *            element to seek
	 * @param es
	 *            set of elements where to seek
	 * @return
	 */
	protected static <T> T get(T e1, Set<T> es) {
		T e2;
		Iterator<T> it = es.iterator();
		while (it.hasNext()) {
			e2 = it.next();
			if (e2.equals(e1)) {
				return e2;
			}
		}
		return null; // reaches the end without finding it
	}

	/**
	 * remove(id,es) removes from es an element whose id is id. We assume that
	 * there are no two elements in es with the same identifier. The function
	 * returns a boolean value indicating whether the elmenet was removed.
	 * 
	 * @param id
	 *            identifier of the element to be removed
	 * @param es
	 *            set of elements where to seek
	 * @return boolean indicating whether an element has been removed
	 */
	public static <T extends AppElement> boolean remove(String id, Set<T> es) {
		Iterator<T> it = es.iterator();
		while (it.hasNext()) {
			T e2 = it.next();
			if (id.equals(e2.getId())) {
				it.remove();
				return true;
			}
		}
		return false;
	}

	public Set<VM> getVMs() {
		return vms;
	}

	@Override
	public String toString() {
		return "CloudApp [vms=" + vms + "]";
	}
}
