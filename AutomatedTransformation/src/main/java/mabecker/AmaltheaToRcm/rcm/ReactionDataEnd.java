package mabecker.amaltheatorcm.rcm;

import org.jdom2.Element;

import mabecker.amaltheatorcm.common.Time;

public class ReactionDataEnd extends BaseElement{

	private static int 	count = 1;
	
	/**
	 * Properties
	 */
	private String 		nameUsage;
	private String		timeDeadline;
	
	/**
	 * Elements
	 */
	private PortDataIn	in;
	private PortDataOut out;
	
	public ReactionDataEnd(Time _deadline, String _name) {
		super("ReactionEnd_" + count);
		count++;
		
		nameUsage = "ReactionConstraint_" + _name;
		
		timeDeadline = _deadline.toStringShort();
				
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
	
	public void setIn(PortDataIn in) {
		this.in = in;
	}
	
	public void setOut(PortDataOut out) {
		this.out = out;
	}
	
	@Override
	public Element toXml() {
		Element ageStart = new Element("ReactionDataEnd");
		
		ageStart.setAttribute("Name", super.getName());
		ageStart.setAttribute("NameUsage", nameUsage);
		ageStart.setAttribute("TimeDeadline", timeDeadline);
		ageStart.setAttribute("UUID", super.getId().toString());
		
		ageStart.addContent(super.getNoteElement());
		
		ageStart.addContent(in.toXml());
		ageStart.addContent(out.toXml());
		
		return ageStart;
	}
}
