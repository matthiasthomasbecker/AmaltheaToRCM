package mabecker.AmaltheaToRcm.RCM;

import java.util.UUID;

import org.jdom2.Element;

public class LinkTransition {

	private int indexLink;
	private String name;
	private UUID id;
	private Notes notes;
	private CrossRef refIn;
	private CrossRef refOut;
	
	public LinkTransition(String _name) {
		name = _name;
		id = UUID.randomUUID();
		notes = new Notes("");
		indexLink = 0;
	}
	
	public void setOutput(String _connection) {
		refOut = new CrossRef("ObjectDest");
		refOut.setReference(_connection);
	}
	
	public void setInput(String _connection) {
		refIn = new CrossRef("ObjectSource");
		refIn.setReference(_connection);
	}
	
	public Element toXml() {
		Element trigStartMode = new Element("LinkTransition");
		
		trigStartMode.setAttribute("IndexLink", Integer.toString(indexLink));
		trigStartMode.setAttribute("Name", name);
		trigStartMode.setAttribute("UUID", id.toString());
		
		trigStartMode.addContent(notes.toXml());
		
		trigStartMode.addContent(refOut.toXml());
		trigStartMode.addContent(refIn.toXml());
		return trigStartMode;
	}
}
