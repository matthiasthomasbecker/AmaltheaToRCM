package mabecker.AmaltheaToRcm.RCM;

import java.util.UUID;

import org.jdom2.Element;

public class TrigClockTT {

	private String name;
	private String timePeriod;
	private UUID id;
	private Notes notes;
	private PortTrigOut portTrigOut;
	
	public TrigClockTT(String _name) {
		name = _name;
		id = UUID.randomUUID();
		notes = new Notes("");
	}
	
	public void setPortTrigOut(PortTrigOut portTrigOut) {
		this.portTrigOut = portTrigOut;
	}
	public void setTimePeriod(String timePeriod) {
		this.timePeriod = timePeriod;
	}
	
	public String getName() {
		return name;
	}

	public Element getXml() {
		
		Element trigClockTT = new Element("TrigClockTT");
		
		trigClockTT.setAttribute("Name", name);
		trigClockTT.setAttribute("Period", timePeriod);
		trigClockTT.setAttribute("UUID", id.toString());
		
		trigClockTT.addContent(notes.toXml());
		
		trigClockTT.addContent(portTrigOut.toXml());
		
		return trigClockTT;
	}
}
