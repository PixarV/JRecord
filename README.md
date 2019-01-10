# JRecord library fork 
from BRUCE A. MARTIN repository [repository](https://github.com/svn2github/jrecords)

## CURRENT VERSION: SNAPSHOT '0.85.1.2'

### 2018.12.21. SNAPSHOT '0.85.1.3' Issue: "Task-548 stream closing from cobol2json method in Cobol2JsonImpl.class"
    Method cobol2json(InputStream input, OutputStream output) closes output stream and we need reopen it each time.

### 2018.12.21. SNAPSHOT '0.85.1.2' Issue: "Task-379 'Value too big for field length' (numeric vs alpha numeric redefines issue)"
    If you have numeric field redefines with alpha-numeric, and alpha-numeric value is two spaces (4040 in EBCDIC) 
    you will have Exception "Value is to big for field {0} > {1} {2} ~ {3} {4}" and the program will immediately stopped.

### 2018.12.19. SNAPSHOT '0.85.1.1' Issue: "Task-368 RDW Non zero bytes"

    For a mainframe file with variable length records each record is preceded by a four byte Record Descriptor Word (RDW) 
    and a possible four byte Block Descriptor Word (BDW). The first two bytes of the RDW (and possible BDW) contains 
    a binary value that is the length of the record (or possible block). The second two bytes of the RDW 
    (and possible BDW) usually contain binary zeroes (or x'00').
    The problem starts, when second two bytes contain not binary zeroes. In this case we have IOException in VbByteReader.read(). 
    
## VERSION: RELEASE '0.85.1' - stable build by BRUCE A. MARTIN 