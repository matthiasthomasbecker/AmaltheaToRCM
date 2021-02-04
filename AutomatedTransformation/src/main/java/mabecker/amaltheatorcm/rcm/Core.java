package mabecker.amaltheatorcm.rcm;

import org.jdom2.Element;

public class Core extends BaseElement{
	
	/**
	 * Elements
	 */
	private Partition partition;
	
	public Core(String _name) {
		
		super(_name);
		
		partition = new Partition("Partition");
	}

	public Partition getPartition() {
		return partition;
	}

	@Override
	public Element toXml() {
		
		Element core = new Element("Core");
		
		core.setAttribute("Name", super.getName());
		core.setAttribute("UUID", super.getId().toString());
		
		core.addContent(super.getNoteElement());
		
		core.addContent(partition.toXml());

		return core;
	}
}
