package mabecker.AmaltheaToRcm.RCM;

import java.util.UUID;

import org.jdom2.Element;

/**
 * Behaviour RCM element
 * @author mabecker
 *
 */
public class Behaviour {

	private String name;
	private UUID id;
	private Notes notes;
	private String entry;
	private Runtime runtime;
	
	public Behaviour(String _name) {
		name = _name;
		id = UUID.randomUUID();
		notes = new Notes("");
		entry = name;
	}
	
	public void setRuntime(Runtime runtime) {
		this.runtime = runtime;
	}
	
	public Runtime getRuntime() {
		return runtime;
	}

	public void setEntry(String entry) {
		this.entry = entry;
	}
	
	public String getEntry() {
		return entry;
	}
	
	public void setNotes(String notes) {
		this.notes.setText(notes);
	}
	
	public Notes getNotes() {
		return notes;
	}
	
	public String getName() {
		return name;
	}
	
	public Element toXlm() {
		
		Element behaviour = new Element("Behaviour");
		
		behaviour.setAttribute("Entry", entry);
		behaviour.setAttribute("Name", name);
		behaviour.setAttribute("UUID", id.toString());
		
		behaviour.addContent(notes.toXml());
		
		behaviour.addContent(runtime.toXml());
		
		return behaviour;
	}
}
