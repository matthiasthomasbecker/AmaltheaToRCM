package mabecker.AmaltheaToRcm.RCM;

import java.util.UUID;

import org.jdom2.Element;

public class TrigStartMode {
	private String name;
	private UUID id;
	private String entry;
	private Notes notes;
	private PortTrigOut trigOut;
	
	public TrigStartMode(String _name) {
		name = _name;
		id = UUID.randomUUID();
		notes = new Notes("");
		entry = "";
		
		trigOut = new PortTrigOut("TO1");
		trigOut.setIndex(0);
	}
	
	public Element toXml() {
		Element trigStartMode = new Element("TrigStartMode");
		
		trigStartMode.setAttribute("Entry", entry);
		trigStartMode.setAttribute("Name", name);
		trigStartMode.setAttribute("UUID", id.toString());
		
		trigStartMode.addContent(notes.toXml());
		
		trigStartMode.addContent(trigOut.toXml());
		return trigStartMode;
	}

	public String getTrigRef() {
		return name + "\\" + trigOut.getName();
	}
}
