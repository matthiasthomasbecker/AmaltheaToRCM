package mabecker.AmaltheaToRcm.RCM;

import java.util.UUID;

import org.jdom2.Element;

public class Runtime {
	
	private String name;
	
	private UUID id;
	private String ctrlStack;
	private String timeBCET;
	private String timeWCET;

	private Notes notes;
	
	public Runtime(String _name) {
		name = _name;
		id = UUID.randomUUID();
		ctrlStack = "0";
		notes = new Notes("");
	}
	
	public void setTimeBCET(String timeBCET) {
		this.timeBCET = timeBCET;
	}
	
	public void setTimeWCET(String timeWCET) {
		this.timeWCET = timeWCET;
	}
	
	public void setCtrlStack(String ctrlStack) {
		this.ctrlStack = ctrlStack;
	}

	public Element toXml() {
		
		Element runtime = new Element("Runtime");

		runtime.setAttribute("CtrlStack", ctrlStack);
		runtime.setAttribute("Name", name);
		runtime.setAttribute("TimeBCET", timeBCET);
		runtime.setAttribute("TimeWCET", timeWCET);
		runtime.setAttribute("UUID", id.toString());
		
		runtime.addContent(notes.toXml());
		
		return runtime;
	}
}
