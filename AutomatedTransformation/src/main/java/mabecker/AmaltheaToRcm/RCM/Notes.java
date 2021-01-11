package mabecker.AmaltheaToRcm.RCM;

import org.jdom2.CDATA;
import org.jdom2.Element;

public class Notes {

	private String text;
	
	public Notes(String _text) {
		text = _text;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}

	public Element toXml() {
		Element note = new Element("Notes");
		note.addContent(new CDATA(text));
		return note;
	}
}
