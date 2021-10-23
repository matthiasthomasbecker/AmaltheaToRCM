/*
 * Copyright (C) Matthias Becker Royal Institute of Technology, Sweden, Alessio Bucaioni Malardalen University, Sweden.
 * All rights reserved.
 * See LICENSE file for copyright and license details.
 * 
 * This file is part of the artifact of the publication https://doi.org/10.1016/j.jss.2021.111106
 */
package mabecker.amaltheatorcm.rcm;

import org.jdom2.Element;

public class AgeDataStart extends BaseElement{

	private static int 	count = 1;
	private String 		nameUsage;
	private PortDataIn	in;
	private PortDataOut out;
	
	public AgeDataStart(String _name) {
		super("AgeStart=_" + count);
		count++;
		
		nameUsage = "AgeConstraint_" + _name;
		
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
		Element ageStart = new Element("AgeDataStart");
		
		ageStart.setAttribute("Name", super.getName());
		ageStart.setAttribute("NameUsage", nameUsage);
		ageStart.setAttribute("UUID", super.getId().toString());
		
		ageStart.addContent(super.getNoteElement());
		
		ageStart.addContent(in.toXml());
		ageStart.addContent(out.toXml());
		
		return ageStart;
	}
}
