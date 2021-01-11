package mabecker.AmaltheaToRcm.RCM;

import java.util.UUID;

import org.jdom2.Element;

public class ReferenceInst {

	private String 	name;//="Reference"
	private String	path;//="RCM_Datatypes.rubusModel"
	private String	reference;//="int16_t"
	private UUID 	id;
	
	public ReferenceInst(String _name) {
		name = _name;
		path = "";
		reference = "";
		id = UUID.randomUUID();
	}
	
	public UUID getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPath() {
		return path;
	}
	
	public String getReference() {
		return reference;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public void setReference(String reference) {
		this.reference = reference;
	}
	
	public Element toXml() {
		
		Element referenceInstance = new Element("ReferenceInst");
		
		referenceInstance.setAttribute("Name", name);
		referenceInstance.setAttribute("Path", path);
		referenceInstance.setAttribute("Reference", reference);
		referenceInstance.setAttribute("UUID", id.toString());
		
		return referenceInstance;
	}
}
