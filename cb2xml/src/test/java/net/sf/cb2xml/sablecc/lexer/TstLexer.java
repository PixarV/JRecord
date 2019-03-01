package net.sf.cb2xml.sablecc.lexer;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import net.sf.cb2xml.sablecc.node.TComment;
import net.sf.cb2xml.sablecc.node.Token;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TstLexer {

  private static Lexer lexer;
  private static InputStream stream;

  @BeforeClass
  public static void setup() {
    ClassLoader classLoader = TstLexer.class.getClassLoader();
    stream = classLoader.getResourceAsStream("TestParserWithIbmWords.cpy");
    PushbackReader reader = new PushbackReader(new InputStreamReader(stream));
    lexer = new Lexer(reader);
  }

  @Test
  public void testSkipEjectParsing() throws IOException, LexerException {
    for (int i = 0; i < 10; i++) {
      Token token = lexer.next();
      String strToken = token.getText();
      if (strToken.equals("SKIP1") || strToken.equals("SKIP2") || strToken.equals("SKIP3")
          || strToken.equals("EJECT")) {
        assertEquals(token.getClass(), TComment.class);
      }
    }
  }

  @AfterClass
  public static void tearDown() {
    try {
      stream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
