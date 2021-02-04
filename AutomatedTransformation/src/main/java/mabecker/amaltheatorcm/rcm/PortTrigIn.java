package mabecker.amaltheatorcm.rcm;

import org.jdom2.Element;

public class PortTrigIn extends BaseElement{

	/**
	 * Properties
	 */
	private int index;
	
	public PortTrigIn(String _name) {
		super(_name);
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	@Override
	public Element toXml() {
		
		Element portTrigIn = new Element("PortTrigIn");
		
		portTrigIn.setAttribute("Index", Integer.toString(index));
		portTrigIn.setAttribute("Name", super.getName());
		portTrigIn.setAttribute("UUID", super.getId().toString());
		
		portTrigIn.addContent(super.getNoteElement());
		
		return portTrigIn;
	}
}
