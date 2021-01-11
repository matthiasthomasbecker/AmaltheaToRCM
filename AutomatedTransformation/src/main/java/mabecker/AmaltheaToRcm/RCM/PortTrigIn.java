package mabecker.AmaltheaToRcm.RCM;

import java.util.UUID;

import org.jdom2.Element;

public class PortTrigIn {

	private String name;
	private int index;
	private UUID id;
	private Notes notes;
	
	public PortTrigIn(String _name) {
		name = _name;
		id = UUID.randomUUID();
		notes = new Notes("");
	}
	
	public void setIndex(int index) {
		this.index = index;
	}

	public Element toXml() {
		
		Element portTrigIn = new Element("PortTrigIn");
		
		portTrigIn.setAttribute("Index", Integer.toString(index));
		portTrigIn.setAttribute("Name", name);
		portTrigIn.setAttribute("UUID", id.toString());
		
		portTrigIn.addContent(notes.toXml());
		
		return portTrigIn;
	}
}
