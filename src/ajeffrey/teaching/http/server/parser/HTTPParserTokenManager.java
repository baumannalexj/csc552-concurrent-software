/* Generated By:JavaCC: Do not edit this line. HTTPParserTokenManager.java */
package ajeffrey.teaching.http.server.parser;
import java.net.URL;
import java.net.MalformedURLException;

public class HTTPParserTokenManager implements HTTPParserConstants
{
private final int jjStopStringLiteralDfa_0(int pos, long active0)
{
   switch (pos)
   {
      default :
         return -1;
   }
}
private final int jjStartNfa_0(int pos, long active0)
{
   return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
}
private final int jjStopAtPos(int pos, int kind)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   return pos + 1;
}
private final int jjStartNfaWithStates_0(int pos, int kind, int state)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) { return pos + 1; }
   return jjMoveNfa_0(state, pos + 1);
}
private final int jjMoveStringLiteralDfa0_0()
{
   switch(curChar)
   {
      case 32:
         return jjStopAtPos(0, 3);
      case 45:
         return jjStartNfaWithStates_0(0, 8, 21);
      case 46:
         return jjStartNfaWithStates_0(0, 5, 21);
      case 47:
         return jjStopAtPos(0, 6);
      case 58:
         return jjStopAtPos(0, 4);
      case 95:
         return jjStartNfaWithStates_0(0, 7, 21);
      default :
         return jjMoveNfa_0(2, 0);
   }
}
private final void jjCheckNAdd(int state)
{
   if (jjrounds[state] != jjround)
   {
      jjstateSet[jjnewStateCnt++] = state;
      jjrounds[state] = jjround;
   }
}
private final void jjAddStates(int start, int end)
{
   do {
      jjstateSet[jjnewStateCnt++] = jjnextStates[start];
   } while (start++ != end);
}
private final void jjCheckNAddTwoStates(int state1, int state2)
{
   jjCheckNAdd(state1);
   jjCheckNAdd(state2);
}
private final void jjCheckNAddStates(int start, int end)
{
   do {
      jjCheckNAdd(jjnextStates[start]);
   } while (start++ != end);
}
private final void jjCheckNAddStates(int start)
{
   jjCheckNAdd(jjnextStates[start]);
   jjCheckNAdd(jjnextStates[start + 1]);
}
private final int jjMoveNfa_0(int startState, int curPos)
{
   int[] nextStates;
   int startsAt = 0;
   jjnewStateCnt = 22;
   int i = 1;
   jjstateSet[0] = startState;
   int j, kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 2:
                  if ((0x3ff600000000000L & l) != 0L)
                  {
                     if (kind > 12)
                        kind = 12;
                     jjCheckNAdd(21);
                  }
                  else if ((0x2400L & l) != 0L)
                  {
                     if (kind > 9)
                        kind = 9;
                  }
                  if ((0x3ff000000000000L & l) != 0L)
                  {
                     if (kind > 10)
                        kind = 10;
                     jjCheckNAdd(19);
                  }
                  else if (curChar == 13)
                     jjstateSet[jjnewStateCnt++] = 15;
                  break;
               case 14:
                  if ((0x2400L & l) != 0L && kind > 9)
                     kind = 9;
                  break;
               case 15:
                  if (curChar == 10 && kind > 9)
                     kind = 9;
                  break;
               case 16:
                  if (curChar == 13)
                     jjstateSet[jjnewStateCnt++] = 15;
                  break;
               case 18:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 10)
                     kind = 10;
                  jjCheckNAdd(19);
                  break;
               case 19:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 13)
                     kind = 13;
                  jjCheckNAdd(19);
                  break;
               case 20:
                  if ((0x3ff600000000000L & l) == 0L)
                     break;
                  if (kind > 12)
                     kind = 12;
                  jjCheckNAdd(21);
                  break;
               case 21:
                  if ((0x3ff600000000000L & l) == 0L)
                     break;
                  if (kind > 14)
                     kind = 14;
                  jjCheckNAdd(21);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 2:
                  if ((0x7fffffe87fffffeL & l) != 0L)
                  {
                     if (kind > 12)
                        kind = 12;
                     jjCheckNAdd(21);
                  }
                  if ((0x7fffffe07fffffeL & l) != 0L)
                  {
                     if (kind > 11)
                        kind = 11;
                  }
                  if (curChar == 104)
                     jjstateSet[jjnewStateCnt++] = 12;
                  else if (curChar == 72)
                     jjstateSet[jjnewStateCnt++] = 8;
                  else if (curChar == 103)
                     jjstateSet[jjnewStateCnt++] = 4;
                  else if (curChar == 71)
                     jjstateSet[jjnewStateCnt++] = 1;
                  break;
               case 0:
                  if (curChar == 84 && kind > 1)
                     kind = 1;
                  break;
               case 1:
                  if (curChar == 69)
                     jjstateSet[jjnewStateCnt++] = 0;
                  break;
               case 3:
                  if (curChar == 116 && kind > 1)
                     kind = 1;
                  break;
               case 4:
                  if (curChar == 101)
                     jjstateSet[jjnewStateCnt++] = 3;
                  break;
               case 5:
                  if (curChar == 103)
                     jjstateSet[jjnewStateCnt++] = 4;
                  break;
               case 6:
                  if (curChar == 80 && kind > 2)
                     kind = 2;
                  break;
               case 7:
                  if (curChar == 84)
                     jjstateSet[jjnewStateCnt++] = 6;
                  break;
               case 8:
                  if (curChar == 84)
                     jjstateSet[jjnewStateCnt++] = 7;
                  break;
               case 9:
                  if (curChar == 72)
                     jjstateSet[jjnewStateCnt++] = 8;
                  break;
               case 10:
                  if (curChar == 112 && kind > 2)
                     kind = 2;
                  break;
               case 11:
                  if (curChar == 116)
                     jjstateSet[jjnewStateCnt++] = 10;
                  break;
               case 12:
                  if (curChar == 116)
                     jjstateSet[jjnewStateCnt++] = 11;
                  break;
               case 13:
                  if (curChar == 104)
                     jjstateSet[jjnewStateCnt++] = 12;
                  break;
               case 17:
                  if ((0x7fffffe07fffffeL & l) != 0L && kind > 11)
                     kind = 11;
                  break;
               case 20:
                  if ((0x7fffffe87fffffeL & l) == 0L)
                     break;
                  if (kind > 12)
                     kind = 12;
                  jjCheckNAdd(21);
                  break;
               case 21:
                  if ((0x7fffffe87fffffeL & l) == 0L)
                     break;
                  if (kind > 14)
                     kind = 14;
                  jjCheckNAdd(21);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               default : break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if ((i = jjnewStateCnt) == (startsAt = 22 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
static final int[] jjnextStates = {
};
public static final String[] jjstrLiteralImages = {
"", null, null, "\40", "\72", "\56", "\57", "\137", "\55", null, null, null, 
null, null, null, };
public static final String[] lexStateNames = {
   "DEFAULT", 
};
private ASCII_CharStream input_stream;
private final int[] jjrounds = new int[22];
private final int[] jjstateSet = new int[44];
protected char curChar;
public HTTPParserTokenManager(ASCII_CharStream stream)
{
   if (ASCII_CharStream.staticFlag)
      throw new Error("ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");
   input_stream = stream;
}
public HTTPParserTokenManager(ASCII_CharStream stream, int lexState)
{
   this(stream);
   SwitchTo(lexState);
}
public void ReInit(ASCII_CharStream stream)
{
   jjmatchedPos = jjnewStateCnt = 0;
   curLexState = defaultLexState;
   input_stream = stream;
   ReInitRounds();
}
private final void ReInitRounds()
{
   int i;
   jjround = 0x80000001;
   for (i = 22; i-- > 0;)
      jjrounds[i] = 0x80000000;
}
public void ReInit(ASCII_CharStream stream, int lexState)
{
   ReInit(stream);
   SwitchTo(lexState);
}
public void SwitchTo(int lexState)
{
   if (lexState >= 1 || lexState < 0)
      throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE);
   else
      curLexState = lexState;
}

private final Token jjFillToken()
{
   Token t = Token.newToken(jjmatchedKind);
   t.kind = jjmatchedKind;
   String im = jjstrLiteralImages[jjmatchedKind];
   t.image = (im == null) ? input_stream.GetImage() : im;
   t.beginLine = input_stream.getBeginLine();
   t.beginColumn = input_stream.getBeginColumn();
   t.endLine = input_stream.getEndLine();
   t.endColumn = input_stream.getEndColumn();
   return t;
}

int curLexState = 0;
int defaultLexState = 0;
int jjnewStateCnt;
int jjround;
int jjmatchedPos;
int jjmatchedKind;

public final Token getNextToken() 
{
  int kind;
  Token specialToken = null;
  Token matchedToken;
  int curPos = 0;

  EOFLoop :
  for (;;)
  {   
   try   
   {     
      curChar = input_stream.BeginToken();
   }     
   catch(java.io.IOException e)
   {        
      jjmatchedKind = 0;
      matchedToken = jjFillToken();
      return matchedToken;
   }

   jjmatchedKind = 0x7fffffff;
   jjmatchedPos = 0;
   curPos = jjMoveStringLiteralDfa0_0();
   if (jjmatchedKind != 0x7fffffff)
   {
      if (jjmatchedPos + 1 < curPos)
         input_stream.backup(curPos - jjmatchedPos - 1);
         matchedToken = jjFillToken();
         return matchedToken;
   }
   int error_line = input_stream.getEndLine();
   int error_column = input_stream.getEndColumn();
   String error_after = null;
   boolean EOFSeen = false;
   try { input_stream.readChar(); input_stream.backup(1); }
   catch (java.io.IOException e1) {
      EOFSeen = true;
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
      if (curChar == '\n' || curChar == '\r') {
         error_line++;
         error_column = 0;
      }
      else
         error_column++;
   }
   if (!EOFSeen) {
      input_stream.backup(1);
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
   }
   throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, TokenMgrError.LEXICAL_ERROR);
  }
}

}
