package mabecker.amaltheatorcm.rcm;

import org.jdom2.Element;

public class TrigTerminator extends BaseElement{

	private static int count = 0;
	
	/**
	 * Elements
	 */
	private PortTrigIn portTrigIn;
	
	public TrigTerminator() {
		super("object_" + count);
		count++;
	}
	
	public void setPortTrigIn(PortTrigIn portTrigIn) {
		this.portTrigIn = portTrigIn;
	}

	@Override
	public Element toXml() {
		
		Element terminator = new Element("TrigTerminator");
		
		terminator.setAttribute("Name", super.getName());
		terminator.setAttribute("UUID", super.getId().toString());
		
		terminator.addContent(super.getNoteElement());
		
		terminator.addContent(portTrigIn.toXml());
		
		return terminator;
	}
}
