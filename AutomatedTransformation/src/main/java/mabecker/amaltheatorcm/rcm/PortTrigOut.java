package mabecker.amaltheatorcm.rcm;

import org.jdom2.Element;

public class PortTrigOut extends BaseElement{

	/**
	 * Properties
	 */
	private int index;
	
	public PortTrigOut(String _name) {
		super(_name);
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	@Override
	public Element toXml() {
		
		Element portTrigOut = new Element("PortTrigOut");
		
		portTrigOut.setAttribute("Index", Integer.toString(index));
		portTrigOut.setAttribute("Name", super.getName());
		portTrigOut.setAttribute("UUID", super.getId().toString());
		
		portTrigOut.addContent(super.getNoteElement());
		
		return portTrigOut;
	}
}
