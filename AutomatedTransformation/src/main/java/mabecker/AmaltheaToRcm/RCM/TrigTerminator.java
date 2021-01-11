package mabecker.AmaltheaToRcm.RCM;

import java.util.UUID;

import org.jdom2.Element;

public class TrigTerminator {

	private static int count = 0;
	
	private String name;
	private UUID id;
	private Notes notes;
	private PortTrigIn portTrigIn;
	
	public TrigTerminator() {
		name = "object_" + count;
		count++;
		id = UUID.randomUUID();
		notes = new Notes("");
	}
	
	public void setPortTrigIn(PortTrigIn portTrigIn) {
		this.portTrigIn = portTrigIn;
	}
	
	public String getName() {
		return name;
	}

	public Element getXml() {
		
		Element terminator = new Element("TrigTerminator");
		
		terminator.setAttribute("Name", name);
		terminator.setAttribute("UUID", id.toString());
		
		terminator.addContent(notes.toXml());
		
		terminator.addContent(portTrigIn.toXml());
		
		return terminator;
	}
}
