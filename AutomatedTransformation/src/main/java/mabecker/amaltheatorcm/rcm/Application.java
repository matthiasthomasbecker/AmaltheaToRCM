package mabecker.amaltheatorcm.rcm;

import org.jdom2.Element;

public class Application extends BaseElement{
	
	/**
	 * Properties of this element
	 */
	private String criticality;
	private String nameUsage;
	
	/**
	 * Elements
	 */
	private Mode mode;
	private TrigStartMode trigStartMode;
	private LinkTransition linkTransitionStart;
	private TrigEndMode trigEndMode;
	private LinkTransition linkTransitionEnd;
	
	public Application(String _name) {
		
		super(_name);

		nameUsage = _name;
		criticality = "NONE";
		
		mode = new Mode("Mode");
		trigStartMode = new TrigStartMode("StartMode");
		linkTransitionStart = new LinkTransition("linkModeIn");
		
		linkTransitionStart.setOutput(mode.getModeInputRef());
		linkTransitionStart.setInput(trigStartMode.getTrigRef());
		
		trigEndMode = new TrigEndMode("EndMode");
		linkTransitionEnd = new LinkTransition("linkModeOut");
		
		linkTransitionEnd.setOutput(trigEndMode.getTrigRef());
		linkTransitionEnd.setInput(mode.getModeOutputRef());
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
	
	public String getNameUsage() {
		return nameUsage;
	}
	
	@Override
	public Element toXml() {
		Element partition = new Element("Application");
		
		partition.setAttribute("Criticallity", criticality);
		partition.setAttribute("Name", super.getName());
		partition.setAttribute("NameUsage", nameUsage);
		partition.setAttribute("UUID", super.getId().toString());
		
		partition.addContent(super.getNoteElement());
		
		partition.addContent(mode.toXml());
		partition.addContent(trigStartMode.toXml());
		partition.addContent(linkTransitionStart.toXml());
		partition.addContent(trigEndMode.toXml());
		partition.addContent(linkTransitionEnd.toXml());
		return partition;
	}
}
