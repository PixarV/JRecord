package xml2cbl.occursDepending.common;

import net.sf.JRecord.Details.AbstractLine;

public class PCblUpdateLine extends PCblBase {
	public final AbstractLine line;
	
	public PCblUpdateLine( AbstractLine line) {
		this.line = line;
	}

	@Override
	public void intField(String name, int value, ArrayIndex indexs) {
		line.getFieldValue(name + indexs.toString()).set(value);
	}


	/* (non-Javadoc)
	 * @see xml2cbl.occursDepending.common.IProcessCobolTree#getCount(xml2cbl.occursDepending.common.CblItem, xml2cbl.occursDepending.common.ArrayIndex, xml2cbl.occursDepending.common.ArrayIndex)
	 */
	@Override
	public int getCount(CblItem c, ArrayIndex indexs, ArrayIndex countIndexs) {
		
		return c.getCount(indexs, countIndexs);
		
	}

}
