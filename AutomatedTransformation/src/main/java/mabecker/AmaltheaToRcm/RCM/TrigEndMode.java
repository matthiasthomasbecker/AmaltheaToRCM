package mabecker.AmaltheaToRcm.RCM;

import java.util.UUID;

import org.jdom2.Element;

public class TrigEndMode {
	private String name;
	private UUID id;
	private String entry;
	private Notes notes;
	private PortTrigIn trigIn;
	
	public TrigEndMode(String _name) {
		name = _name;
		id = UUID.randomUUID();
		notes = new Notes("");
		entry = "";
		
		trigIn = new PortTrigIn("TI1");
		trigIn.setIndex(0);
	}
	
	public Element toXml() {
		Element trigStartMode = new Element("TrigEndMode");
		
		trigStartMode.setAttribute("Entry", entry);
		trigStartMode.setAttribute("Name", name);
		trigStartMode.setAttribute("UUID", id.toString());
		
		trigStartMode.addContent(notes.toXml());
		
		trigStartMode.addContent(trigIn.toXml());
		return trigStartMode;
	}

	public String getTrigRef() {
		return name + "\\" + trigIn.getName();
	}
}
