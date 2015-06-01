import java.util.HashSet;
import java.util.Set;

import planner.CloudApp;

public class Main {

	public static void main(String[] args) {
//		test1();
//		test2();
		test3();
	}

	/**
	 * Start from scratch and destroy everything
	 * 
	 * app a:
	 * 
	 * VMs:
	 * 
	 * VM1 [Apache[iApache]]
	 * 
	 * VM2 [Tomcat[eTomcat,i1Tomcat,i2Tomcat], Cache[eCache]]
	 * 
	 * VM3 [MySQL[eMySQL]]
	 * 
	 * Bindings: (i1Tomcat, eMySQL) (i2Tomcat, eCache) (iApache, eTomcat)
	 * 
	 * app b:
	 * 
	 * VMs: none
	 */
	public static void test1() {
		// cloud app a
		CloudApp a = new CloudApp();

		a.addVM("VM1");
		Set<String> eApache = new HashSet<String>();
		Set<String> iApache = new HashSet<String>();
		iApache.add("iApache");
		a.addComponent("VM1", "Apache", iApache, eApache);

		a.addVM("VM2");
		Set<String> eTomcat = new HashSet<String>();
		Set<String> iTomcat = new HashSet<String>();
		eTomcat.add("eTomcat");
		iTomcat.add("i1Tomcat");
		iTomcat.add("i2Tomcat");
		a.addComponent("VM2", "Tomcat", iTomcat, eTomcat);
		Set<String> eCache = new HashSet<String>();
		Set<String> iCache = new HashSet<String>();
		eCache.add("eCache");
		a.addComponent("VM2", "Cache", iCache, eCache);

		a.addVM("VM3");
		Set<String> eMySQL = new HashSet<String>();
		Set<String> iMySQL = new HashSet<String>();
		eMySQL.add("eMySQL");
		a.addComponent("VM3", "MySQL", iMySQL, eMySQL);

		a.addBinding("VM2", "Tomcat", "i1Tomcat", "VM3", "MySQL", "eMySQL");
		a.addBinding("VM2", "Tomcat", "i2Tomcat", "VM2", "Cache", "eCache");
		a.addBinding("VM1", "Apache", "iApache", "VM2", "Tomcat", "eTomcat");

		CloudApp.get("Apache", CloudApp.get("VM1", a.getVMs()).getComponents())
				.setStarted(true);
		CloudApp.get("Tomcat", CloudApp.get("VM2", a.getVMs()).getComponents())
				.setStarted(true);
		CloudApp.get("Cache", CloudApp.get("VM2", a.getVMs()).getComponents())
				.setStarted(true);
		CloudApp.get("MySQL", CloudApp.get("VM3", a.getVMs()).getComponents())
				.setStarted(true);

		// cloud app b
		CloudApp b = new CloudApp();

		// a - b
		System.out.println("Source: " + a);
		System.out.println("Target: " + b);
//		System.out.println("Diff: " + CloudApp.diff(a, b));
		System.out.println("Acts: " + CloudApp.actions(a, b));
		System.out.println("Plan: ");
		for (String st : CloudApp.genPlan(a, b))
			System.out.println(st);
	}

	/**
	 * Go from scratch to everything
	 * 
	 * app a:
	 * 
	 * VMs: none
	 * 
	 * app b:
	 * 
	 * VMs:
	 * 
	 * VM1 [Apache[iApache]]
	 * 
	 * VM2 [Tomcat[eTomcat,i1Tomcat,i2Tomcat], Cache[eCache]]
	 * 
	 * VM3 [MySQL[eMySQL]]
	 * 
	 * Bindings: (i1Tomcat, eMySQL) (i2Tomcat, eCache) (iApache, eTomcat)
	 */
	public static void test2() {
		// cloud app a
		CloudApp a = new CloudApp();

		a.addVM("VM1");
		Set<String> eApache = new HashSet<String>();
		Set<String> iApache = new HashSet<String>();
		iApache.add("iApache");
		a.addComponent("VM1", "Apache", iApache, eApache);

		a.addVM("VM2");
		Set<String> eTomcat = new HashSet<String>();
		Set<String> iTomcat = new HashSet<String>();
		eTomcat.add("eTomcat");
		iTomcat.add("i1Tomcat");
		iTomcat.add("i2Tomcat");
		a.addComponent("VM2", "Tomcat", iTomcat, eTomcat);
		Set<String> eCache = new HashSet<String>();
		Set<String> iCache = new HashSet<String>();
		eCache.add("eCache");
		a.addComponent("VM2", "Cache", iCache, eCache);

		a.addVM("VM3");
		Set<String> eMySQL = new HashSet<String>();
		Set<String> iMySQL = new HashSet<String>();
		eMySQL.add("eMySQL");
		a.addComponent("VM3", "MySQL", iMySQL, eMySQL);

		a.addBinding("VM2", "Tomcat", "i1Tomcat", "VM3", "MySQL", "eMySQL");
		a.addBinding("VM2", "Tomcat", "i2Tomcat", "VM2", "Cache", "eCache");
		a.addBinding("VM1", "Apache", "iApache", "VM2", "Tomcat", "eTomcat");

		CloudApp.get("Apache", CloudApp.get("VM1", a.getVMs()).getComponents())
				.setStarted(true);
		CloudApp.get("Tomcat", CloudApp.get("VM2", a.getVMs()).getComponents())
				.setStarted(true);
		CloudApp.get("Cache", CloudApp.get("VM2", a.getVMs()).getComponents())
				.setStarted(true);
		CloudApp.get("MySQL", CloudApp.get("VM3", a.getVMs()).getComponents())
				.setStarted(true);

		// cloud app b
		CloudApp b = new CloudApp();

		// b - a
		System.out.println("Source: " + b);
		System.out.println("Target: " + a);
//		System.out.println("Diff: " + CloudApp.diff(b, a));
		System.out.println("Acts: " + CloudApp.actions(b, a));
		System.out.println("Plan: ");
		for (String st : CloudApp.genPlan(b, a))
			System.out.println(st);
	}

	/**
	 * Change the MySQL component from VM3 to a new VM4
	 * 
	 * app a:
	 * 
	 * VMs:
	 * 
	 * VM1 [Apache[iApache]]
	 * 
	 * VM2 [Tomcat[eTomcat,i1Tomcat,i2Tomcat], Cache[eCache]]
	 * 
	 * VM3 [MySQL[eMySQL]]
	 * 
	 * Bindings: (i1Tomcat, eMySQL) (i2Tomcat, eCache) (iApache, eTomcat)
	 * 
	 * app b:
	 * 
	 * VMs:
	 * 
	 * VM1 [Apache[iApache]]
	 * 
	 * VM2 [Tomcat[eTomcat,i1Tomcat,i2Tomcat], Cache[eCache]]
	 * 
	 * VM4 [MySQL[eMySQL]]
	 * 
	 * Bindings: (i1Tomcat, eMySQL) (i2Tomcat, eCache) (iApache, eTomcat)
	 */
	public static void test3() {
		// cloud app a
		CloudApp a = new CloudApp();

		a.addVM("VM1");
		Set<String> eApache = new HashSet<String>();
		Set<String> iApache = new HashSet<String>();
		iApache.add("iApache");
		a.addComponent("VM1", "Apache", iApache, eApache);

		a.addVM("VM2");
		Set<String> eTomcat = new HashSet<String>();
		Set<String> iTomcat = new HashSet<String>();
		eTomcat.add("eTomcat");
		iTomcat.add("i1Tomcat");
		iTomcat.add("i2Tomcat");
		a.addComponent("VM2", "Tomcat", iTomcat, eTomcat);
		Set<String> eCache = new HashSet<String>();
		Set<String> iCache = new HashSet<String>();
		eCache.add("eCache");
		a.addComponent("VM2", "Cache", iCache, eCache);

		a.addVM("VM3");
		Set<String> eMySQL = new HashSet<String>();
		Set<String> iMySQL = new HashSet<String>();
		eMySQL.add("eMySQL");
		a.addComponent("VM3", "MySQL", iMySQL, eMySQL);

		a.addBinding("VM2", "Tomcat", "i1Tomcat", "VM3", "MySQL", "eMySQL");
		a.addBinding("VM2", "Tomcat", "i2Tomcat", "VM2", "Cache", "eCache");
		a.addBinding("VM1", "Apache", "iApache", "VM2", "Tomcat", "eTomcat");

		CloudApp.get("Apache", CloudApp.get("VM1", a.getVMs()).getComponents())
				.setStarted(true);
		CloudApp.get("Tomcat", CloudApp.get("VM2", a.getVMs()).getComponents())
				.setStarted(true);
		CloudApp.get("Cache", CloudApp.get("VM2", a.getVMs()).getComponents())
				.setStarted(true);
		CloudApp.get("MySQL", CloudApp.get("VM3", a.getVMs()).getComponents())
				.setStarted(true);

		// cloud app b
		CloudApp b = new CloudApp();

		b.addVM("VM1");
		eApache = new HashSet<String>();
		iApache = new HashSet<String>();
		iApache.add("iApache");
		b.addComponent("VM1", "Apache", iApache, eApache);

		b.addVM("VM2");
		eTomcat = new HashSet<String>();
		iTomcat = new HashSet<String>();
		eTomcat.add("eTomcat");
		iTomcat.add("i1Tomcat");
		iTomcat.add("i2Tomcat");
		b.addComponent("VM2", "Tomcat", iTomcat, eTomcat);
		eCache = new HashSet<String>();
		iCache = new HashSet<String>();
		eCache.add("eCache");
		b.addComponent("VM2", "Cache", iCache, eCache);

		b.addVM("VM4");
		eMySQL = new HashSet<String>();
		iMySQL = new HashSet<String>();
		eMySQL.add("eMySQL");
		b.addComponent("VM4", "MySQL", iMySQL, eMySQL);

		b.addBinding("VM2", "Tomcat", "i1Tomcat", "VM4", "MySQL", "eMySQL");
		b.addBinding("VM2", "Tomcat", "i2Tomcat", "VM2", "Cache", "eCache");
		b.addBinding("VM1", "Apache", "iApache", "VM2", "Tomcat", "eTomcat");

		CloudApp.get("Apache", CloudApp.get("VM1", b.getVMs()).getComponents())
				.setStarted(true);
		CloudApp.get("Tomcat", CloudApp.get("VM2", b.getVMs()).getComponents())
				.setStarted(true);
		CloudApp.get("Cache", CloudApp.get("VM2", b.getVMs()).getComponents())
				.setStarted(true);
		CloudApp.get("MySQL", CloudApp.get("VM4", b.getVMs()).getComponents())
				.setStarted(true);

		// a - b
		System.out.println("Source: " + a);
		System.out.println("Target: " + b);
//		System.out.println("Diff: " + CloudApp.diff(a, b));
		System.out.println("Plan: ");
		for (String st : CloudApp.genPlan(a, b))
			System.out.println(st);
	}
}
