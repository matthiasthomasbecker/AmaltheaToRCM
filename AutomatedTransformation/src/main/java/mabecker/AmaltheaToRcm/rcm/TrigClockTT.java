package mabecker.amaltheatorcm.rcm;

import org.jdom2.Element;

public class TrigClockTT extends BaseElement{

	/**
	 * Properties
	 */
	private String timePeriod;
	
	/**
	 * elements
	 */
	private PortTrigOut portTrigOut;
	
	public TrigClockTT(String _name) {
		super(_name);
		
		portTrigOut = new PortTrigOut("TO1");
		portTrigOut.setIndex(0);
	}
	
	public void setTimePeriod(String timePeriod) {
		this.timePeriod = timePeriod;
	}

	@Override
	public Element toXml() {
		
		Element trigClockTT = new Element("TrigClockTT");
		
		trigClockTT.setAttribute("Name", super.getName());
		trigClockTT.setAttribute("TimePeriod", timePeriod);
		trigClockTT.setAttribute("UUID", super.getId().toString());
		
		trigClockTT.addContent(super.getNoteElement());
		
		trigClockTT.addContent(portTrigOut.toXml());
		
		return trigClockTT;
	}
}
