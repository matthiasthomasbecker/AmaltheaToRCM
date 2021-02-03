package mabecker.AmaltheaToRcm.RCM;

import java.util.UUID;

import org.jdom2.Element;

public class TrigPriority {

	private String name;
	private UUID id;
	private Notes notes;
	private int ctrlPriority;
	private PortTrigIn in;
	private PortTrigOut out;
	
	public TrigPriority(String _name, int _priority) {
		name = _name;
		id = UUID.randomUUID();
		notes = new Notes("");
		ctrlPriority = _priority;
		
		in = new PortTrigIn("TI1");
		in.setIndex(0);
		
		out = new PortTrigOut("TO1");
		out.setIndex(0);
	}
	
	public String getName() {
		return name;
	}
	
	public int getCtrlPriority() {
		return ctrlPriority;
	}
	
	public UUID getId() {
		return id;
	}
	
	public Notes getNotes() {
		return notes;
	}
	
	public PortTrigIn getTrigInPort() {
		return in;
	}
	
	public PortTrigOut getGetOutPort() {
		return out;
	}
	
	public Element toXml() {
		Element prio = new Element("TrigPriority");

		prio.setAttribute("CtrlPriority", Integer.toString(ctrlPriority));
		prio.setAttribute("Name", name);
		prio.setAttribute("UUID", id.toString());
		
		prio.addContent(notes.toXml());
		
		prio.addContent(in.toXml());
		prio.addContent(out.toXml());
		
		return prio;
	}
}
