package mabecker.AmaltheaToRcm.RCM;

import java.util.UUID;

import org.jdom2.Element;

public class Partition {
	private String name;
	
	private UUID id;
	private Application application;
	private Notes notes;
	
	public Partition(String _name) {
		name = _name;
		id = UUID.randomUUID();
		notes = new Notes("");
		
		application = new Application("Application");
	}
	
	public void setApplication(Application application) {
		this.application = application;
	}
	
	public Application getApplication() {
		return application;
	}
	
	public UUID getId() {
		return id;
	}
	
	public Element toXml() {
		Element partition = new Element("Partition");
		
		partition.setAttribute("Name", name);
		partition.setAttribute("UUID", id.toString());
		
		partition.addContent(notes.toXml());
		
		partition.addContent(application.toXml());
		
		return partition;
	}
}
