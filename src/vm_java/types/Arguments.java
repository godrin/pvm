package vm_java.types;

import java.util.ArrayList;
import java.util.List;

import vm_java.context.BasicObject;

public class Arguments {
	
	private List<BasicObject> arguments;

	public Arguments(Arguments args) {
		arguments=new ArrayList<BasicObject>();
		arguments.addAll(args.arguments);
	}
	public Arguments() {
		arguments=new ArrayList<BasicObject>();
	}

	public void pushFront(BasicObject basicObject) {
		arguments.add(0,basicObject);
	}

}
