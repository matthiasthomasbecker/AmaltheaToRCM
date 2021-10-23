/*
 * Copyright (C) Matthias Becker Royal Institute of Technology, Sweden, Alessio Bucaioni Malardalen University, Sweden.
 * All rights reserved.
 * See LICENSE file for copyright and license details.
 * 
 * This file is part of the artifact of the publication https://doi.org/10.1016/j.jss.2021.111106
 */
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
