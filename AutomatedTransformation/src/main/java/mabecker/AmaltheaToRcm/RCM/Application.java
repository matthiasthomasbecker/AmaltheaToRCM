package mabecker.AmaltheaToRcm.RCM;

import java.util.UUID;

import org.jdom2.Element;

public class Application {
	private String name;
	private String criticality;
	private String nameUsage;
	
	private UUID id;
	private Notes notes;
	
	private Mode mode;
	private TrigStartMode trigStartMode;
	private LinkTransition linkTransition;
	
	public Application(String _name) {
		name = _name;
		nameUsage = _name;
		criticality = "NONE";
		id = UUID.randomUUID();
		notes = new Notes("");
		
		mode = new Mode("Mode");
		trigStartMode = new TrigStartMode("StartMode");
		linkTransition = new LinkTransition("link");
		
		linkTransition.setOutput(mode.getModeInputRef());
		linkTransition.setInput(trigStartMode.getTrigRef());
	}
	
	public Mode getMode() {
		return mode;
	}
	
	public void setMode(Mode mode) {
		this.mode = mode;
	}
	
	public void setCriticality(String criticality) {
		this.criticality = criticality;
	}
	
	public String getCriticality() {
		return criticality;
	}
	
	public String getName() {
		return name;
	}
	
	public UUID getId() {
		return id;
	}
	
	public String getNameUsage() {
		return nameUsage;
	}
	
	public Element toXml() {
		Element partition = new Element("Application");
		
		partition.setAttribute("Criticallity", criticality);
		partition.setAttribute("Name", name);
		partition.setAttribute("NameUsage", nameUsage);
		partition.setAttribute("UUID", id.toString());
		
		partition.addContent(notes.toXml());
		
		partition.addContent(mode.toXml());
		partition.addContent(trigStartMode.toXml());
		partition.addContent(linkTransition.toXml());
		return partition;
	}
}
