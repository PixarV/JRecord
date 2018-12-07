package net.sf.JRecord.zExamples.cobol.iobuilder;

import net.sf.JRecord.Common.Constants;
import net.sf.JRecord.Details.AbstractLine;
import net.sf.JRecord.IO.AbstractLineReader;
import net.sf.JRecord.IO.AbstractLineWriter;
import net.sf.JRecord.IO.CobolIoProvider;
import net.sf.JRecord.def.IO.builders.ICobolIOBuilder;
import net.sf.JRecord.common.TstConstants;

/**
 * Read / Write Mainframe Cobol file using a 
 * Cobol Copybook - <b>CobolIOBuilder</b> version.
 * 
 * <p><b>Note:</b> The input and output file formats are exactly the same. 
 * 
 * 
 * @author Bruce Martin
 *
 */
public final class XmplReadWrite01 {

    private static final double GST_CONVERSION = 1.1;

    private String installDir     = TstConstants.SAMPLE_DIRECTORY;
    private String salesFile      = installDir + "DTAR020.bin";
    private String salesFileOut   = installDir + "DTAR020out.bin";
    private String copybookName   = TstConstants.COBOL_DIRECTORY + "DTAR020.cbl";

    /**
     * Example of LineReader / LineWrite classes
     */
    private XmplReadWrite01() {
        super();

        int lineNum = 0;
        double gstExclusive;
        AbstractLine saleRecord;

        try {
        	ICobolIOBuilder iob = CobolIoProvider.getInstance()
        								.newIOBuilder(copybookName)
        									.setFont("cp037")                                   // US EBCDIC
        									.setFileOrganization(Constants.IO_FIXED_LENGTH_RECORDS);  
            AbstractLineReader reader = iob.newReader(salesFile);
            AbstractLineWriter writer = iob.newWriter(salesFileOut);

            while ((saleRecord = reader.read()) != null) {
                lineNum += 1;

                System.out.print(saleRecord.getFieldValue("DTAR020-KEYCODE-NO").asString()
                        + " " + saleRecord.getFieldValue("DTAR020-QTY-SOLD").asString()
                        + " " + saleRecord.getFieldValue("DTAR020-SALE-PRICE").asString());

                gstExclusive = saleRecord.getFieldValue("DTAR020-SALE-PRICE").asDouble() / GST_CONVERSION;
                saleRecord.getFieldValue("DTAR020-SALE-PRICE").set(gstExclusive);
                writer.write(saleRecord);

                System.out.println(" " + saleRecord.getFieldValue("DTAR020-SALE-PRICE").asString());
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
    	new XmplReadWrite01();
    }
}