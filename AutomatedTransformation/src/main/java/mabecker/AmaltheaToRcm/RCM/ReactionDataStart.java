package mabecker.AmaltheaToRcm.RCM;

import java.util.UUID;

import org.jdom2.Element;

public class ReactionDataStart {
	private static int 	count = 1;
	private String 		name;
	private String 		nameUsage;
	private UUID 		id;
	private Notes 		notes;
	private PortDataIn	in;
	private PortDataOut out;
	
	public ReactionDataStart() {
		name = "Reaction_" + count;
		count++;
		
		nameUsage = "ReactionConstraint";
		id = UUID.randomUUID();
		
		notes = new Notes("");
		
		in = new PortDataIn("ID", null);
		in.setDimension(null);
		in.setDataPassingMethod(null);
		in.setValueInitial(null);
		in.setIndex(0);
		
		out = new PortDataOut("OD", null);
		out.setDimension(null);
		out.setDataPassingMethod(null);
		out.setValueInitial(null);
		out.setIndex(0);
	}
	
	public String getName() {
		return name;
	}
	
	public void setIn(PortDataIn in) {
		this.in = in;
	}
	
	public void setOut(PortDataOut out) {
		this.out = out;
	}
	
	public Element getXml() {
		Element ageStart = new Element("ReactionDataEnd");
		
		ageStart.setAttribute("Name", name);
		ageStart.setAttribute("NameUsage", nameUsage);
		ageStart.setAttribute("UUID", id.toString());
		
		ageStart.addContent(notes.toXml());
		
		ageStart.addContent(in.toXml());
		ageStart.addContent(out.toXml());
		
		return ageStart;
	}
}
