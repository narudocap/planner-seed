package planner;

public class Binding extends AppElement {
	private PLoc imp, exp;

	/**
	 * @param imp
	 * @param exp
	 */
	public Binding(PLoc imp, PLoc exp) {
		super(null); // this is not really an app element
		this.imp = imp;
		this.exp = exp;
	}

	public PLoc getImp() {
		return imp;
	}

	public void setImp(PLoc imp) {
		this.imp = imp;
	}

	public PLoc getExp() {
		return exp;
	}

	public void setExp(PLoc exp) {
		this.exp = exp;
	}

	@Override
	public String toString() {
		return "Binding [imp=" + imp + ", exp=" + exp + "]";
	}
}
