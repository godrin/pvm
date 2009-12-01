package vm_java.pruby;

import java.util.ArrayList;
import java.util.List;

public class Authorizations {
	List<Authorization> auths = new ArrayList<Authorization>();
	boolean committed = false;

	public boolean contains(String moduleName) {
		for (Authorization a : auths) {
			if (a.getModuleName().equals(moduleName))
				;
			return true;
		}
		return false;
	}

	public static Authorizations all() {
		Authorizations as = new Authorizations();
		as.add("VMIO");
		as.commit();
		return as;
	}

	void commit() {
		committed = true;
	}

	private void add(String pModuleName) {
		add(new Authorization(pModuleName));
	}

	public void add(Authorization authorization) {
		if (!committed) {
			auths.add(authorization);
		}
	}

}
