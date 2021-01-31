package mabecker.AmaltheaToRcm.RCM;

import java.util.ArrayList;
import java.util.UUID;

import org.jdom2.Element;

public class LinkTrig {

	private static int count = 0;	//count the number of created links
	
	private String name;
	private int indexLink;
	private UUID id;
	private Notes notes;
	private ArrayList<CrossRef> crossrefs;
	
	public LinkTrig() {
		name = "link_trig_" + count;
		count++;
		id = UUID.randomUUID();
		notes = new Notes("");
		
		crossrefs = new ArrayList<CrossRef>();
	}
	
	public void setIndexLink(int indexLink) {
		this.indexLink = indexLink;
	}
	
	public void addCrossRef(CrossRef _crossRef) {
		crossrefs.add(_crossRef);
	}

	public Element getXml() {
		
		Element link = new Element("LinkTrig");
		
		link.setAttribute("IndexLink", Integer.toString(indexLink));
		link.setAttribute("Name", name);
		link.setAttribute("UUID", id.toString());
		
		link.addContent(notes.toXml());
		
		for (CrossRef crossRef : crossrefs) {
			link.addContent(crossRef.toXml());
		}
		return link;
	}
}
