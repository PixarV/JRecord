package net.sf.JRecord.zExamples.iob.recordEditorXml.readWrite;

import net.sf.JRecord.JRecordInterface1;
import net.sf.JRecord.Details.AbstractLine;
import net.sf.JRecord.IO.AbstractLineReader;
import net.sf.JRecord.IO.AbstractLineWriter;
import net.sf.JRecord.def.IO.builders.IIOBuilder;
import net.sf.JRecord.zTest.Common.TstConstants;

/**
 * Reading / writing files using a RecordEditor-XML copybook
 * 
 * @author Bruce Martin
 *
 */
public final class XmplLineIO7 {


	    /**
	     * Example of
	     * 1) Loading an XML copybook (RecordEditor-Xml)
	     * 2) LineReader / LineWrite classes
	     */
	    private XmplLineIO7() {
	        super();

		    double GST_CONVERSION = 1.1;
	
		    String installDir     = TstConstants.SAMPLE_DIRECTORY;
		    String salesFile      = installDir + "DTAR020.bin";
		    String salesFileOut   = installDir + "DTAR020out.bin";
		    String copybookName   = TstConstants.RE_XML_DIRECTORY
	    					+ "DTAR020.Xml";
	        int lineNum = 0;
	        double gstExclusive;
	        AbstractLine saleRecord;

	        try {
	        	IIOBuilder ioBldr = JRecordInterface1.SCHEMA_XML.newIOBuilder(copybookName);
		            
	            AbstractLineReader reader  = ioBldr.newReader(salesFile);
	            AbstractLineWriter writer  = ioBldr.newWriter(salesFileOut);

	            while ((saleRecord = reader.read()) != null) {
	                lineNum += 1;

	                System.out.print(saleRecord.getFieldValue("KEYCODE-NO").asString()
	                        + " " + saleRecord.getFieldValue("QTY-SOLD").asString()
	                        + " " + saleRecord.getFieldValue("SALE-PRICE").asString());

	                gstExclusive = saleRecord.getFieldValue("SALE-PRICE").asDouble() / GST_CONVERSION;
	                saleRecord.getFieldValue("SALE-PRICE").set(gstExclusive);
	                writer.write(saleRecord);

	                System.out.println(" " + saleRecord.getFieldValue("SALE-PRICE").asString());
	            }

	            reader.close();
	            writer.close();
	        } catch (Exception e) {
	            System.out.println("~~> " + lineNum + " " + e.getMessage());
	            System.out.println();

	            e.printStackTrace();
	        }
	    }

	    public static void main(String[] args) {
	    	new XmplLineIO7();
	    }
}
