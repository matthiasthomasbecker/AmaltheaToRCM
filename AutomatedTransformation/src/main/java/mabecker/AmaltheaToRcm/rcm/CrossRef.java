package mabecker.amaltheatorcm.rcm;

import org.jdom2.Element;

public class CrossRef extends BaseElement{

	/**
	 * Properties
	 */
	private String reference;
	
	public CrossRef(String _name) {
		
		super(_name);
	}
	
	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getReference() {
		return reference;
	}
	
	@Override
	public Element toXml() {
		
		Element crossref = new Element("CrossRef");
		
		crossref.setAttribute("Name", super.getName());
		crossref.setAttribute("Reference", reference);
		crossref.setAttribute("UUID", super.getId().toString());
		
		crossref.addContent(super.getNoteElement());
		
		return crossref;
	}
}
