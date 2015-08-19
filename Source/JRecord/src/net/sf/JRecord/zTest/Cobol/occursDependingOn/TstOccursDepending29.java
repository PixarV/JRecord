package net.sf.JRecord.zTest.Cobol.occursDependingOn;

import java.io.IOException;

import net.sf.JRecord.Common.AbstractFieldValue;
import net.sf.JRecord.Common.Constants;
import net.sf.JRecord.Common.IFieldDetail;
import net.sf.JRecord.Common.RecordException;
import net.sf.JRecord.Details.AbstractLine;
import net.sf.JRecord.Details.LayoutDetail;
import net.sf.JRecord.IO.CobolIoProvider;
import net.sf.JRecord.Numeric.ICopybookDialects;
import net.sf.JRecord.def.IO.builders.ICobolIOBuilder;
import junit.framework.TestCase;

public class TstOccursDepending29 extends TestCase {

	private static final String MONTHS = "months";
	private static final String WEEK_NO = "week-no";

	public void testPositionCalc1() throws Exception {
		try {
			tstPosition("OccursDependingOn29.cbl");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	

	
	private  void tstPosition(String copybookFile)  throws IOException, RecordException {
		String copybookFileName = WriteSampleFile.class.getResource(copybookFile).getFile();
		ICobolIOBuilder ioBuilder = CobolIoProvider.getInstance()
				.newIOBuilder(copybookFileName, ICopybookDialects.FMT_MAINFRAME)
					.setFileOrganization(Constants.IO_STANDARD_TEXT_FILE);


		boolean normal = copybookFile.endsWith("1.cbl");
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 7; j++) {
				System.out.println();
				System.out.print("** line: " + i + " " + j);
				for (int week = 1; week < 4; week++) {
					for (int day = 1; day < 4; day++) {
						for (int hour = 1; hour < 5; hour++) {
							tstLine(ioBuilder.newLine(), i, j, week, day, hour, normal);
						}
					}
				}
			}
		}

	}
	

	private void tstLine(AbstractLine line, int purchaseCount, int salesCount, int week, int day, int hour, boolean normalPos) throws RecordException {
		LayoutDetail layout = line.getLayout();
		IFieldDetail weekNoFld = layout.getFieldFromName(WEEK_NO);
		IFieldDetail monthFld = layout.getFieldFromName(MONTHS);
		IFieldDetail weekCountFld = layout.getFieldFromName("week-of-month");
		IFieldDetail dayFld = layout.getFieldFromName("days");
		IFieldDetail hourFld = layout.getFieldFromName("hours");
		//IFieldDetail purchCountFld = layout.getFieldFromName("total-purchase-count");
		
		/** Setting the Occurs Depending fields !!! **/
		line.getFieldValue(monthFld).set(salesCount);
		line.getFieldValue(weekCountFld).set(week);
		line.getFieldValue(dayFld).set(day);
		line.getFieldValue(hourFld).set(hour);
		@SuppressWarnings("deprecation")
		int pos = hourFld.getEnd() + 1;
		line.getFieldValue(weekNoFld).set(purchaseCount);
				
		check(line, layout.getFieldFromName("Location-Number"));
		check(line, layout.getFieldFromName("Location-Name"));

		for (int i = 0; i < salesCount; i++) {
			IFieldDetail countFld = layout.getFieldFromName("sales-count (" + i + ")");
			IFieldDetail valueFld = layout.getFieldFromName("sales-value (" + i + ")");
			if (salesCount == 7 && i == 5) {
				System.out.print('*');
			}
			for (int w = 0; w < week; w++) {
				pos = check(line, layout.getFieldFromName("week-sales (" + i + ", " + w +  ")"), pos);
				for (int d = 0; d < day; d++) {
					pos = check(line, layout.getFieldFromName("d-sales (" + i + ", " + w + ", " + d +  ")"), pos);
					for (int h = 0; h < hour; h++) {
						pos = check(line, layout.getFieldFromName("daily-sales (" + i + ", " + w + ", " + d + ", " + h + ")"), pos);
					}
				}
			}
			for (int w = 0; w < week; w++) {
				for (int d = 0; d < day; d++) {
					for (int h = 0; h < hour; h++) {
						pos = check(line, layout.getFieldFromName("daily-value (" + i + ", " + w + ", " + d + ", " + h + ")"), pos);
					}
					pos = check(line, layout.getFieldFromName("d-value (" + i + ", " + w + ", " + d  + ")"), pos);
				}
				pos = check(line, layout.getFieldFromName("week-value (" + i + ", " + w + ")"), pos);
			}
			pos = check(line, countFld, pos);
			pos = check(line, valueFld, pos);
		}

		pos = check(line, layout.getFieldFromName("total-sales"), pos);
		pos = check(line, layout.getFieldFromName(WEEK_NO), pos);		
	

		for (int i = 0; i < purchaseCount; i++) {
			for (int w = 0; w < week; w++) {
				pos = check(line, layout.getFieldFromName("week-purch (" + i + ", " + w + ")"), pos);
				for (int d = 0; d < day; d++) {
					pos = check(line, layout.getFieldFromName("d-purch (" + i + ", " + w + ", " + d + ")"), pos);
					for (int h = 0; h < hour; h++) {
						pos = check(line, layout.getFieldFromName("daily-purch (" + i + ", " + w + ", " + d + ", " + h + ")"), pos);
						pos = check(line, layout.getFieldFromName("daily-purch-val (" + i + ", " + w + ", " + d + ", " + h + ")"), pos);
					}
					pos = check(line, layout.getFieldFromName("d-purch-val (" + i + ", " + w + ", " + d + ")"), pos);
				}
				pos = check(line, layout.getFieldFromName("week-purch-val (" + i + ", " + w + ")"), pos);
			}
			pos = check(line, layout.getFieldFromName("purchase-count (" + i + ")"), pos);
			pos = check(line, layout.getFieldFromName("purchase-value (" + i + ")"), pos);
		}

		pos = check(line, layout.getFieldFromName("total-purchase-count"), pos);
		pos = check(line, layout.getFieldFromName("total-purchase-value"), pos);

		
		//System.out.println("** line: " + purchaseCount + " " + salesCount + " length=" + line.getData().length);
	}

	private void check(AbstractLine line, IFieldDetail fld) throws RecordException {
		check(line, fld, fld.getPos());
	}
	
	private int check(AbstractLine line, IFieldDetail fld, int pos) throws RecordException {
		String id = fld.getName();
		assertEquals(id, pos, fld.calculateActualPosition(line));
		int end = pos + fld.getLen() - 1;
		assertEquals(id, end, fld.calculateActualEnd(line));
		
		if (WEEK_NO.equalsIgnoreCase(fld.getName()) || MONTHS.equalsIgnoreCase(fld.getName())) {
			
		} else {
			for (int i = 0; i < 4; i++) {
				setAndCheck(line, fld, i);
			}
		}
		return end + 1;
	}
	
	private void setAndCheck(AbstractLine line, IFieldDetail fld, int value) throws RecordException {
		
		AbstractFieldValue fieldValue = line.getFieldValue(fld);
		fieldValue.set(value);
		if (fieldValue.isNumeric()) {
			if (fld.getDecimal() == 0) {
				assertEquals(value, fieldValue.asInt());
			} else {
				assertEquals(Integer.toString(value) + ".00", fieldValue.asString());
			}
		} else {
			assertEquals(Integer.toString(value), fieldValue.asString());
		}
	}

}
