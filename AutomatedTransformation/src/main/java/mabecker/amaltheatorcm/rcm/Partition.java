package mabecker.amaltheatorcm.rcm;

import org.jdom2.Element;

public class Partition extends BaseElement{
	
	/**
	 * Elements
	 */
	private Application application;
	
	public Partition(String _name) {
		
		super(_name);
		
		application = new Application("Application");
	}
	
	public void setApplication(Application application) {
		this.application = application;
	}
	
	public Application getApplication() {
		return application;
	}
	
	@Override
	public Element toXml() {
		Element partition = new Element("Partition");
		
		partition.setAttribute("Name", super.getName());
		partition.setAttribute("UUID", super.getId().toString());
		
		partition.addContent(super.getNoteElement());
		
		partition.addContent(application.toXml());
		
		return partition;
	}
}
