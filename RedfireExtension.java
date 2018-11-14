package connection;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jivesoftware.smack.packet.ExtensionElement;

public class RedfireExtension implements ExtensionElement {

    public static final String elementName = "redfire-invite";
    public static final String namespace = "http://redfire.4ng.net/xmlns/redfire-invite";


    public String getElementName() {
        return elementName;
    }

    public String getNamespace() {
        return namespace;
    }

    private Map<String, String> map;


    public String toXML() {
        StringBuffer buf = new StringBuffer();

        buf.append("<").append(elementName).append(" xmlns=\"").append(namespace).append("\">");

        for (Iterator i=getNames(); i.hasNext(); ) {
            String name = (String)i.next();
            String value = getValue(name);

            buf.append("<").append(name).append(">").append(value).append("</").append(name).append(">");
        }

        buf.append("</").append(elementName).append(">");
        return buf.toString();
    }

    public synchronized Iterator getNames() {

        if (map == null) {
            return Collections.EMPTY_LIST.iterator();
        }
        return Collections.unmodifiableMap(new HashMap<String, String>(map)).keySet().iterator();
    }


    public synchronized String getValue(String name) {

        if (map == null) {
            return null;
        }
        return map.get(name);
    }

    public synchronized void setValue(String name, String value) {

        if (map == null) {
            map = new HashMap <String, String>();
        }

        map.put(name, value);
    }
}
