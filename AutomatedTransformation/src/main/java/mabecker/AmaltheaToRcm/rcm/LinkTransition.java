package mabecker.amaltheatorcm.rcm;

import org.jdom2.Element;

public class LinkTransition extends BaseElement{

	/**
	 * Properties
	 */
	private int indexLink;
	
	/**
	 * Elements
	 */
	private CrossRef refIn;
	private CrossRef refOut;
	
	public LinkTransition(String _name) {
		
		super(_name);

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
	
	@Override
	public Element toXml() {
		Element trigStartMode = new Element("LinkTransition");
		
		trigStartMode.setAttribute("IndexLink", Integer.toString(indexLink));
		trigStartMode.setAttribute("Name", super.getName());
		trigStartMode.setAttribute("UUID", super.getId().toString());
		
		trigStartMode.addContent(super.getNoteElement());
		
		trigStartMode.addContent(refOut.toXml());
		trigStartMode.addContent(refIn.toXml());
		return trigStartMode;
	}
}
