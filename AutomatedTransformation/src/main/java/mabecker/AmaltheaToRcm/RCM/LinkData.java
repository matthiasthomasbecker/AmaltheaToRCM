package mabecker.AmaltheaToRcm.RCM;

import java.util.ArrayList;
import java.util.UUID;

import org.eclipse.app4mc.amalthea.model.Label;
import org.jdom2.Element;

public class LinkData {
private static int count = 0;	//count the number of created links
	
	private String name;
	private int indexLink;
	private UUID id;
	private Notes notes;
	private ArrayList<CrossRef> crossrefs;
	private Label label;
	
	public LinkData(Label _label) {
		name = "link_data_" + count;
		indexLink = count;
		count++;
		id = UUID.randomUUID();
		notes = new Notes("");
		
		crossrefs = new ArrayList<CrossRef>();
		
		label = _label;
	}
	
	public Label getLabel() {
		return label;
	}
	
	public void setIndexLink(int indexLink) {
		this.indexLink = indexLink;
	}
	
	public void addCrossRef(CrossRef _crossRef) {
		crossrefs.add(_crossRef);
	}

	@Override
	public String toString() {
		String retval = "RCM LinkData: ";
		
		for (int i = 0; i < crossrefs.size(); i++) {
			if (i == 0) retval = retval + crossrefs.get(i).getReference();
			else {
				retval = retval + "\n              " + crossrefs.get(i).getReference();
			}
		}
		return retval;
	}
	
	public Element getXml() {
		
		Element link = new Element("LinkData");
		
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
