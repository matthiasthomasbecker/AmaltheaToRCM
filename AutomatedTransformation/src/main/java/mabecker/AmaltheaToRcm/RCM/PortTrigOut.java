package mabecker.AmaltheaToRcm.RCM;

import java.util.UUID;

import org.jdom2.Element;

public class PortTrigOut {

	private String name;
	private int index;
	private UUID id;
	private Notes notes;
	
	public PortTrigOut(String _name) {
		name = _name;
		id = UUID.randomUUID();
		notes = new Notes("");
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public Element toXml() {
		
		Element portTrigOut = new Element("PortTrigOut");
		
		portTrigOut.setAttribute("Index", Integer.toString(index));
		portTrigOut.setAttribute("Name", name);
		portTrigOut.setAttribute("UUID", id.toString());
		
		portTrigOut.addContent(notes.toXml());
		
		return portTrigOut;
	}
}
