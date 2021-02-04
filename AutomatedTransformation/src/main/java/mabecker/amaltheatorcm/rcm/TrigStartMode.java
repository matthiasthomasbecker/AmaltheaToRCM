package mabecker.amaltheatorcm.rcm;

import org.jdom2.Element;

public class TrigStartMode extends BaseElement{
	
	/**
	 * Properties
	 */
	private String entry;

	/**
	 * Elements
	 */
	private PortTrigOut trigOut;
	
	public TrigStartMode(String _name) {
		
		super(_name);
		
		entry = "";
		
		trigOut = new PortTrigOut("TO1");
		trigOut.setIndex(0);
	}
	
	@Override
	public Element toXml() {
		Element trigStartMode = new Element("TrigStartMode");
		
		trigStartMode.setAttribute("Entry", entry);
		trigStartMode.setAttribute("Name", super.getName());
		trigStartMode.setAttribute("UUID", super.getId().toString());
		
		trigStartMode.addContent(super.getNoteElement());
		
		trigStartMode.addContent(trigOut.toXml());
		return trigStartMode;
	}

	public String getTrigRef() {
		return super.getName() + "\\" + trigOut.getName();
	}
}
