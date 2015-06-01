package planner.actions;

import java.util.List;
import java.util.Set;

import planner.CloudApp;

/**
 * Action - each of the actions the cloud manager can perform on the system.
 * Subclasses: InstantiateVM, InstantiateC, AddBinding, DestroyVM, DestroyC,
 * RemoveBinding.
 */
public abstract class Action {
	public abstract boolean perform(CloudApp app, Set<Action> pool, List<String> log);
}
