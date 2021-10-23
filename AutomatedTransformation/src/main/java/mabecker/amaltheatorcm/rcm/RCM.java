/*
 * Copyright (C) Matthias Becker Royal Institute of Technology, Sweden, Alessio Bucaioni Malardalen University, Sweden.
 * All rights reserved.
 * See LICENSE file for copyright and license details.
 * 
 * This file is part of the artifact of the publication https://doi.org/10.1016/j.jss.2021.111106
 */
package mabecker.amaltheatorcm.rcm;

import java.util.UUID;

import org.jdom2.Element;

public class RCM {

	private String	model = "RCM";
	private String 	version = "2017-01-01";
	private UUID 	id;
	private RcmSystem 	system;
	
	public RCM(String _sysName) {
		system = new RcmSystem(_sysName);
		id = UUID.randomUUID();
	}
	
	public RcmSystem getSystem() {
		return system;
	}
	
	public void setSystem(RcmSystem system) {
		this.system = system;
	}
	
	public Element toXml() {
		
		Element rcm = new Element("document");
		
		rcm.setAttribute("Model", model);
		rcm.setAttribute("UUID", id.toString());
		rcm.setAttribute("Version", version);
		
		rcm.addContent(system.toXml());
		
		return rcm;
	}
}
