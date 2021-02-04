package mabecker.amaltheatorcm.rcm;

import org.jdom2.Element;

public class TrigEndMode extends BaseElement{

	/**
	 * Properties
	 */
	private String entry;
	
	/**
	 * Elements
	 */
	private PortTrigIn trigIn;
	
	public TrigEndMode(String _name) {

		super(_name);
		entry = "";
		
		trigIn = new PortTrigIn("TI1");
		trigIn.setIndex(0);
	}
	
	@Override
	public Element toXml() {
		Element trigStartMode = new Element("TrigEndMode");
		
		trigStartMode.setAttribute("Entry", entry);
		trigStartMode.setAttribute("Name", super.getName());
		trigStartMode.setAttribute("UUID", super.getId().toString());
		
		trigStartMode.addContent(super.getNoteElement());
		
		trigStartMode.addContent(trigIn.toXml());
		return trigStartMode;
	}

	public String getTrigRef() {
		return super.getName() + "\\" + trigIn.getName();
	}
}
