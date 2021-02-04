package mabecker.amaltheatorcm.rcm;

import org.jdom2.Element;

public class Runtime extends BaseElement{
	
	/**
	 * Properties
	 */
	private String ctrlStack;
	private String timeBCET;
	private String timeWCET;
	
	public Runtime(String _name) {

		super(_name);
		ctrlStack = "0";
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

	@Override
	public Element toXml() {
		
		Element runtime = new Element("Runtime");

		runtime.setAttribute("CtrlStack", ctrlStack);
		runtime.setAttribute("Name", super.getName());
		runtime.setAttribute("TimeBCET", timeBCET);
		runtime.setAttribute("TimeWCET", timeWCET);
		runtime.setAttribute("UUID", super.getId().toString());
		
		runtime.addContent(super.getNoteElement());
		
		return runtime;
	}
}
