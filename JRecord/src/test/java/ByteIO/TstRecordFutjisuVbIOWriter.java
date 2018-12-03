/*
 * @Author Bruce Martin
 * Created on 28/08/2005
 *
 * Purpose:
 */
/*  -------------------------------------------------------------------------
 *
 *                Project: JRecord
 *    
 *    Sub-Project purpose: Provide support for reading Cobol-Data files 
 *                        using a Cobol Copybook in Java.
 *                         Support for reading Fixed Width / Binary / Csv files
 *                        using a Xml schema.
 *                         General Fixed Width / Csv file processing in Java.
 *    
 *                 Author: Bruce Martin
 *    
 *                License: LGPL 2.1 or latter
 *                
 *    Copyright (c) 2016, Bruce Martin, All Rights Reserved.
 *   
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation; either
 *    version 2.1 of the License, or (at your option) any later version.
 *   
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *
 * ------------------------------------------------------------------------ */

package ByteIO;

import junit.framework.TestCase;
import net.sf.JRecord.ByteIO.AbstractByteReader;
import net.sf.JRecord.ByteIO.FujitsuVbByteReader;
import net.sf.JRecord.ByteIO.FujitsuVbByteWriter;
import net.sf.JRecord.common.IO;
import net.sf.JRecord.common.TstConstants;
import net.sf.JRecord.common.TstData;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testCategories.SlowTest;

import java.io.IOException;
import java.util.Arrays;

/**
 *
 *
 * @author Bruce Martin
 *
 */
public class TstRecordFutjisuVbIOWriter {


	private static final String TMP_DIRECTORY = TstConstants.TEMP_DIRECTORY;
 
    private final String fileName = TMP_DIRECTORY + "VbTestFile.tmp";
	private final byte[][] dtar020Lines = TstData.DTAR020_LINES;



    /**
     * @see TestCase#setUp()
     */
    @Before
    public void setUp() throws Exception {
    }


    /**
     * @see TestCase#tearDown()
     */
    @After
    public void tearDown() throws Exception {

    }


    @Test
    public void testBinWrite1() throws Exception {

        tst1file(dtar020Lines);
    }


    @Test
    public void testBinWrite2() throws Exception {

        tst1file(TstData.DTAR107_LINES);
    }


    @Test
    public void testBinWrite3() throws Exception {

        tst1file(TstData.FUTJISU_LINES);
    }

    @Test
    @Category(SlowTest.class)
    public void testBinWrite4() throws Exception {
        int l = TstData.FUTJISU_LINES.length
        	  + TstData.DTAR107_LINES.length
        	  + dtar020Lines.length;
        byte[][] lines = new byte[l][];
        int i, j;

        j = 0;
        for (i = 0; i < TstData.DTAR107_LINES.length; i++) {
            lines[j++] = TstData.DTAR107_LINES[i];
        }
        for (i = 0; i < dtar020Lines.length; i++) {
            lines[j++] = dtar020Lines[i];
        }
        for (i = 0; i < TstData.FUTJISU_LINES.length; i++) {
            lines[j++] = TstData.FUTJISU_LINES[i];
         }
        System.out.println("... " + j);


        tst1file(lines);
    }

    public void tst1file(byte[][] lines)
    throws IOException {

        int i, j;
        int copies = 5000;
        byte[][] largeFile = new byte[lines.length * copies][];

        for (i = 0; i < copies; i++) {
            for (j = 0; j < lines.length; j++) {
                largeFile[i * lines.length + j]
                          = lines[j];
            }
        }

        binReadCheck("Standard >> ", lines);
        binReadCheck("   Large >> ", largeFile);
        System.out.println(".. end ..");
    }

    private void binReadCheck(String id, byte[][] lines2Test)
    throws IOException {
        AbstractByteReader tReader = new FujitsuVbByteReader();
        byte[] line;
        int i = 0;
        boolean b;

        System.out.println(id + "Bin Read");
        writeAFile(fileName, lines2Test);
        tReader.open(fileName);

        while ((line = tReader.read()) != null) {
            b = Arrays.equals(lines2Test[i], line);
            if (!b) {
                System.out.println("");
                System.out.println(id + "Error Line " + i);
                System.out.println("  Expected: " + new String(lines2Test[i],  "CP037"));
                System.out.println("       Got: " + new String(line, "CP037"));
                System.out.println("");

                Assert.assertTrue(id + "Bin Line " + i + " is not correct ", b);
            }
            i += 1;
        }

        Assert.assertEquals(id + "Expected to read " + lines2Test.length + " got " + i, lines2Test.length, i);

        tReader.close();
    }


    /**
     * writes byte array to a file
     *
     * @param name major part of the file name
     * @param bytes data to write to the file
     * @param details file layout details
     *
     * @throws IOException any IO errors
     */
    private void writeAFile(String name, byte[][] bytes)
    throws IOException  {

        IO.writeAFile(new FujitsuVbByteWriter(), name, bytes);
    }

}
