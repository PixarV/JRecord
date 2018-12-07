package net.sf.JRecord.cbl2json;

import net.sf.cb2xml.util.XmlUtils;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URL;


/**
 * Common Code to compare Xml Documents
 * @author Bruce Martin
 *
 */
public class Cbl2JsonCode {

	public static final boolean IS_MAC, IS_NIX, IS_WINDOWS;
	
	static {
		boolean isNix = false, isMac=false, isWin = true;
		try {
			String s = System.getProperty("os.name").toLowerCase();
			if (s != null) {
				isNix = (s.indexOf("nix") >= 0 || s.indexOf("nux") >= 0);
				isMac = s.indexOf("mac") >= 0;
				isWin = s.indexOf("win") >= 0;
			}
		} catch (Exception e) {
		}

		IS_MAC = isMac;
		IS_NIX = isNix;
		IS_WINDOWS = isWin;
	}

	public static String toString(String[] lines) {
		StringBuilder b = new StringBuilder();
		String sep = "";
		
		for (String s : lines) {
			b.append(sep).append(s);
			sep = "\n";
		}
		
		return b.toString();
	}
	
	public static InputStream toStream(String[] lines) {
		return toStream(toString(lines));
	}
	
	public static InputStream toStream(String s) {
		return new ByteArrayInputStream(s.getBytes());
	}
	
	public static boolean compare(byte[] b1, byte[] b2) {
		int num = Math.min(b1.length, b2.length);
		
		for (int i = 0;i < num; i++) {
			if (b1[i] != b2[i]) {
				return false;
			}
		}
		return true;
	}

	public static void compare(String id, String fileName, Document doc) throws IOException, SAXException, ParserConfigurationException {
//		compare(id, fileToDom(fileName), doc);
	}
	
	public static boolean compareDocs(String id, Document doc1, Document doc2) {	
		System.out.println("---------------- " + id);
		System.out.println(XmlUtils.domToString(doc2).toString());
		System.out.println();
		return compare(doc1.getDocumentElement(), doc2.getDocumentElement());
	}
	
	
	private static boolean compare(Element element1, Element element2) {
        NodeList nodeList1 = element1.getChildNodes();
        NodeList nodeList2 = element2.getChildNodes();
        boolean ret = true;
       //level += 1;

        if (nodeList1.getLength() == nodeList2.getLength()) {
	        for (int i = 0; ret && i < nodeList1.getLength(); i++) {
	        	ret = checkNode(nodeList1.item(i), nodeList2.item(i));
	        }
        } else {
        	System.out.println(":: Node Length >>" + element1.getNodeName() + " " + nodeList1.getLength()  + " " + nodeList2.getLength());
        	return false;
        }
        

        return ret;
	}
	
	private static boolean checkNode(Node node1, Node node2) {
        if (node1.getNodeType() == Node.ELEMENT_NODE
        &&  node2.getNodeType() == Node.ELEMENT_NODE) {
            Element childElement1 = (Element) node1;
            Element childElement2 = (Element) node2;
            NamedNodeMap attributes = childElement1.getAttributes();
            //System.out.println();

            for (int i = 0; i < attributes.getLength(); i++) {
            	Node item = attributes.item(i);

            	if (item instanceof Attr) {
            		Attr attr = (Attr) item;
					String value1 = attr.getValue();
					if (childElement2.hasAttribute(attr.getName())){
	            		String value2 = childElement2.getAttribute(attr.getName());
	            		System.out.print("\t" +attr.getName() + " ~ " + value1 + "==" + value2);
						if (value1 == null) {
							if (value2 != null) {
								System.out.println();
					        	System.out.println(":: Attribute >>" + attr.getName() + " ~ " + value1 + "==" + value2);
								return false;
							}
	            		} else if (! value1.equals(value2)) {
							System.out.println();
				        	System.out.println(":: Attribute >>" + attr.getName() + " ~ " + value1 + "==" + value2);
				        	return false;
	            		}
					}
            	}
            }

            return compare(childElement1, childElement2);
        } else if (node1.getNodeType() == Node.TEXT_NODE
        	   &&  node2.getNodeType()  == Node.TEXT_NODE) {
        	String s1 = node1.getNodeValue();
        	String s2 = node2.getNodeValue();
        	if ((s1 == null && s2 != null)
        	|| ! s1.trim().equals(s2.trim())) {
        		return false;
        	}
        } else if (node1.getNodeType() != node2.getNodeType()) {
        	return false;
        }
        return true;
	}
	
    public static Document fileToDom(String fileName)
	throws IOException, SAXException, ParserConfigurationException {

        DocumentBuilderFactory factory
           		= DocumentBuilderFactory.newInstance();
        return factory.newDocumentBuilder().parse(new File(fileName));
    }
    
	public static String loadFile(String fileName, boolean addToEnd) throws IOException {
		String recordSep = "\r\n";
		if (IS_NIX) {
			recordSep = "\n";
		}
		
		return loadFile(fileName, recordSep, addToEnd);
		
    }
    
	public static String loadFile(String fileName, String recordSep, boolean addToEnd) throws IOException {
		StringBuilder b = new StringBuilder();
		String sep = "";
		BufferedReader r = new BufferedReader(new FileReader(fileName));
		String s;
		while ((s = r.readLine()) != null) {
			b.append(sep).append(s);
			sep = recordSep;
		}
		r.close();
		
		if (addToEnd) {
			b.append(sep);
		}
		return b.toString();
	}


    public static String getFullName(String filename) {
    	URL resource = Cbl2JsonCode.class.getResource(filename);
    	if (resource == null) {
    		System.out.println(" --> Can not find: " + filename);
    	}
		return resource.getFile();
    }
}