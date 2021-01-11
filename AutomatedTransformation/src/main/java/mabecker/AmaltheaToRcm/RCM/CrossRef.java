package mabecker.AmaltheaToRcm.RCM;

import java.util.UUID;

import org.jdom2.Element;

public class CrossRef {

	private String name;
	private String reference;
	private UUID id;
	private Notes notes;
	
	public CrossRef(String _name) {
		name = _name;
		id = UUID.randomUUID();
		notes = new Notes("");
	}
	
	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getReference() {
		return reference;
	}
	
	public Element toXml() {
		
		Element crossref = new Element("CrossRef");
		
		crossref.setAttribute("Name", name);
		crossref.setAttribute("Reference", reference);
		crossref.setAttribute("UUID", id.toString());
		
		crossref.addContent(notes.toXml());
		
		return crossref;
	}
}
