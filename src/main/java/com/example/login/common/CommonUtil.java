package com.example.login.common;

/**
 * <PRE>
 * Class    : CmUtil
 * Function : CmUtil class
 * Comment  : CmUtil.
 * </PRE>F
 * @version : 1.0
 * @author  : 장  우  승
 */

import java.util.*;
import java.text.*;
import java.net.*;
import java.io.*;
import java.sql.*;
import java.util.Date;


public class CommonUtil
{


  /**
   * 사용법
   * CHECKisdigit("12a","12a".length())
   *
   * @param string
   * @param piStrLen
   * @return
   */
  public static final boolean CHECKisdigit(String string,int piStrLen)
  {
    for(int index=0 ; index<piStrLen ; index++){
      if(string.charAt(index) < 48 || string.charAt(index) > 57){
        return(false);
      }
    }
    return true;
  }


  /**
   * request_name = "cashCard-listCashcard"
   *              = "system" + "-" + "method"
   *
   * catchSTRINGseq("cashCard-listCashcard",1,"-");
   * '-' 구분자를 가지면서 첫번째 문자열을 구하라
   *
   * @param request_name
   * @return
   */
  public static String catchSTRINGseq(String request_name,int seq,String tag)
  {
    if ( seq == 0 )
      seq = 500;
    if ( tag == null )
      return null;

    StringTokenizer tokenizer = new StringTokenizer(request_name, tag);
    String [] matrix = new String[seq];

    int i = 0;
    while( tokenizer.hasMoreTokens() ){
      matrix[i] = tokenizer.nextToken();
      //System.out.println( "Tokenized String : " + matrix[i] );
      i++;
      if( i==seq )
        break;
    }
    return matrix[seq-1];
  }


  /**
   * @PARAM str 바꾸려는 문자열을 가진 원본
   * @PARAM pattern 찾을 문자열
   * @PARAM replace 바꿔줄 문자열
   * str="aaaaa k k"
   * pattern="k k"
   * replace="w w"
   * return = "aaaaa w w"
   */
  static String replace(String str, String pattern, String replace) {

    int s = 0; // 찾기 시작할 위치
    int e = 0; // StringBuffer에 append 할 위치
    StringBuffer result = new StringBuffer(); // 잠시 문자열 담궈둘 놈

    while ((e = str.indexOf(pattern, s)) >= 0) {
      result.append(str.substring(s, e));
      result.append(replace);
      s = e+pattern.length();
    }
    result.append(str.substring(s));
    return result.toString();
  }


  public static final boolean runtime(String cmd){
    try{
      //System.out.println(cmd);
      Runtime runtime = Runtime.getRuntime();
      runtime.exec("/home/cosif/jtm/bin/jmboot " + cmd);
    }
    catch(IOException ex){
      //System.out.println("예외발생"+ex.getMessage());
      ex.printStackTrace();
      return false;
    }
    return true;
  }

  public int Interval_GetTimeSec( String psStartTimer, String psEndTimer )
  {
    String ss=null;
    String mm=null;
    String ee=null;
    int startsec=99999999;
    int	endsec=99999999;

    //System.out.println("-------start Interval_GetTimesec");
    /***********************************************************************/
    /* 시작타임                                                            */
    /***********************************************************************/

    ss=psStartTimer.substring(0,2);
    mm=psStartTimer.substring(2,4);
    ee=psStartTimer.substring(4,6);
    startsec = Str2Int(ss)*60*60 + Str2Int(mm)*60 + Str2Int(ee);

    /***********************************************************************/
    /* 종료타임                                                            */
    /***********************************************************************/
    ss=psEndTimer.substring(0,2);
    mm=psEndTimer.substring(2,4);
    ee=psEndTimer.substring(4,6);
    endsec = Str2Int(ss)*60*60 + Str2Int(mm)*60 + Str2Int(ee);

    //System.out.println("-------end Interval_GetTimesec " + new ConvUtil().Int2Str(endsec-startsec));

    return(endsec-startsec);

  }


  /**
   * System.out.println으로 화면에 보여준다
   *
   * @param     String str
   * @return    void
   */
  public static final void systemOut(String msg)
  {
    System.out.println(msg) ;
  }


  /**
   * len 보다 긴 문자열일 경우 자른다.
   *
   * @param     str 입력 문자열
   * @param     len 문자열 길이
   * @return    String
   */
  public static final String cutString(String str, int len)
  {
    if (str != null) {
      if( str.length() < len+1 ) {
        return str ;
      } else {
        return str.substring(0,len);
      }
    }
    return "" ;
  }

  public static final String cutString(String str, int startpo,int len)
  {
    if (str != null) {
      return str.substring(startpo,(len+1));
    }
    return "" ;
  }



  /**
   * KSC5601으로 변환한다.
   *
   * @param     String str
   * @return    String
   * @exception UnsupportedEncodingException
   */
  public static final String ascToksc(String str) throws UnsupportedEncodingException {

    if(str==null)
      return "";
    return new String(str.getBytes("8859_1"),"KSC5601");

  }


  /**
   * 8859_1로 변환한다.
   *
   * @param     String str
   * @return    String
   * @exception UnsupportedEncodingException
   */
  public static final String kscToasc(String str) throws UnsupportedEncodingException {

    if(str==null)
      return "";
    return new String(str.getBytes("KSC5601"),"8859_1");

  }


  /**
   * 입력된 String이 한글, 알파벳, 숫자인지를 체크한다.
   *
   * @param     String data
   * @return    int
   *            0인 경우 숫자,1인 경우 알파벳, 2인 경우 한글
   */
  public static final int checkHan(String data) {
    int code = (int)data.charAt(0);
    int result = 1;	// 알파벳인 경우
    if(code > 127){
      result = 2;	// 한글인 경우
    } else if(code >= 48 && code <= 57){
      result = 0;	// 숫자인 경우
    }
    return result;
  }


  /**
   * 주어진 스트링이 한글인지 판단 // 주어진 문자열안에서 하나의 한글이라도 있다면 true
   * @param  String data
   * @return boolean
   *
   */
  public static final boolean isHanGul(String str)
  {
    // 문자열의 길이와 문자열의 바이트배열의 길이를 비교해서 체크
    if(str.length() == str.getBytes().length){
      System.out.println(str + " is not Han-gul");
      return false;
    }
    else{
      System.out.println(str + " has Han-gul");
      return true;
    }
  }



  /**
   * 입력된 String이 null인지를 체크한다.
   *
   * @param     String str
   * @return    String
   *            null일 경우 return ""
   */
  public static final String nullCheckString(String str) {
    if (str != null)
      return str ;
    return "" ;

  }


  /**
   * 시간관련 util
   *
   */
  public static final String getTime(long time) {

    SimpleDateFormat sdf = new SimpleDateFormat("mm:ss:SS") ;
    return (String)sdf.format(new java.util.Date(time)) ;
  }


  public static final void getTime(long time, String msg) {

    SimpleDateFormat sdf = new SimpleDateFormat("mm:ss:SS") ;
    System.out.println(msg + " : " + (String)sdf.format(new java.util.Date(time))) ;

  }


  public  static String getTime() {

    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SS") ;
    return (String)sdf.format(new java.util.Date(System.currentTimeMillis())) ;
  }


  public  static String getTimeFreeFormat(String format) {

    SimpleDateFormat sdf = new SimpleDateFormat(format) ;
    return (String)sdf.format(new java.util.Date(System.currentTimeMillis())) ;
  }


  /**
   * 숫자변환
   *
   */

  public static final int STR2INT(String str)
  {
    int i=0;
    try{
      i=Integer.parseInt(str);
    }
    catch(NumberFormatException e){
      System.out.println("STR2INT ERROR" + e.toString());
    }
    finally{
      return(i);
    }
  }


  public static final int Str2Int(String str)
  {
    int i=Integer.valueOf(str).intValue();
    //System.out.println(i);
    return(i);
  }


  public static final int atoi(String str)
  {
    int i=Integer.valueOf(str).intValue();
    //System.out.println(i);
    return(i);
  }


  public static final long Str2Long(String str)
  {
    long l=Long.valueOf(str).longValue();
    //System.out.println(l);
    return(l);
  }


  public static final float Str2Float(String str)
  {
    float f=Float.valueOf(str).floatValue();
    //System.out.println(f);
    return(f);
  }


  public static final double Str2Double(String str)
  {
    double d=Double.valueOf(str).doubleValue();
    //System.out.println(d);
    return(d);
  }


  public static final String Int2Str(int n)
  {
    String s=new Integer(n).toString();
    return(s);
  }


  public static final String Float2Str(float n)
  {
    String s1=new Float(n).toString();
    return(s1);
  }


  public static final String Long2Str(long l)
  {
    String s = new Long(l).toString();
    return( s );
  }


  public static final String Double2Str(double d)
  {
    String s = new Double(d).toString();
    return(s);
  }


  /**
   * 문자관련 변환
   *
   */
  public static final String TOupper(String str)
  {
    String up=str.toUpperCase();
    return(up);

  }


  public static final String TOlower(String str)
  {
    String lo=str.toLowerCase();
    return(lo);
  }


  public static final String Char2Str(char ch)
  {
    String lo=new Character(ch).toString();
    return(lo);
  }



  /**
   * string 관련
   *
   */
  public static final boolean exist_str(String org,String tar){
    if (org.regionMatches(org.indexOf(tar) ,tar,0,tar.length() )){
      return true;
    }
    else{
      return false;
    }
  }

  // org : 1234567  tar : 345 -> org에서 tar 스트링이 포함되어 있는지 확인한다.
  public static final boolean check_contain_string(String org,String tar){
    if (org.regionMatches(org.indexOf(tar) ,tar,0,tar.length() )){
      return true;
    }
    else{
      return false;
    }
  }

  public static final boolean memcmp(String st1,String st2)
  {
    if(st1.equals(st2))
      return(true);
    else
      return(false);
  }

  public synchronized static String GetEnvValue(String envfilename,String tmp)
  {
    String ConfigPath=null;
    Properties p = new Properties();
    FileInputStream fis = null;
    String tmpv = null;
    try {
      fis = new FileInputStream(envfilename);
      p.load(fis);
      tmpv=p.getProperty(tmp);
    }
    catch (IOException e) {
      System.out.println("GetEnvValue() 에서 환경셋팅시12 예외발생" );
      e.printStackTrace();
    }
    finally{
      try{
        if( fis != null ){
          fis.close();
        }
      }
      catch (Exception ignore) {
      }
    }
    return tmpv;
  }


  public static final boolean memcmp(String st1,String st2,int len)
  {
    String s1 = null;
    String s2 = null;

    try {
      s1 = st1.substring(0,len);
      s2 = st2.substring(0,len);
    } catch (Exception e) {
      e.printStackTrace();
      //System.out.println(e.getMessage());
      return false;
    }

    if(s1.equals(s2))
      return(true);
    else
      return(false);
  }


  public static final String memcpy(String org,int len)
  {
    String tar = null;
    try {
      String s1 = org.substring(0,len);
      System.out.println("s1 "+ s1);
      tar	= s1;
    } catch (Exception e) {
      e.printStackTrace();
      //System.out.println(e.getMessage());
      return null;
    }
    return(tar);
  }


  /**
   * 파일관련
   *
   */

  // 디렉토리에 대한 메타정보를 보여준다.
  // filepath ex) C:\\MYTEST\\CHB\\src\\common
  public static final int FileInfo(String filepath)
  {
    if (filepath.length() == 0)
    {
      return -1;
    }

    File f1 = new File(filepath);
    String[] ls;

    int i;
    for (ls = f1.list(), i = 0; ls != null && i < ls.length; printOne(new File(f1, ls[i])), i++);
    return 0;
  }



  public static final void printOne(File f)
  {
    if (f.exists())
    {
      System.out.print(f.canRead() ? "r" : "-");
      System.out.print(f.canWrite() ? "w" : "-");
      System.out.print(f.isDirectory() ? "x" : "-");
      System.out.print('\t');

      System.out.print(f.length());
      System.out.print('\t');


      System.out.print(new java.util.Date(f.lastModified()));
      System.out.print('\t');
    }
    else
    {
      System.out.print("\t\t\t\t\t 파일이 존재하지 않습니다.");
    }

    System.out.println("파일명 : " + f.getName());
  }



  // -------------------------------------------------------------------
  // _cpfile2file : copy iname oname
  //                ex) iname == "./kk.txt" oname=="./kk.txt1"
  // rtn : 복사한 파일의 크기 , error : -1
  // -------------------------------------------------------------------

  public static final int cpFile2File(String iname,String oname)
  {
    int fsize=0;


    try
    {
      FileInputStream  ifp = new FileInputStream(iname);
      FileOutputStream ofp = new FileOutputStream(oname);
      int ch;

      while ((ch=ifp.read()) > -1)
      {
        ofp.write(ch);
        fsize++;
      }
      ifp.close();
      ofp.close();

    }  catch (IOException e){
      System.err.println("I/O Exception error." + e.getMessage());
      return -1;
    }

    return fsize;
  }

  // 특정 디렉토리의 내용을 모두 지우기
  // args : C:\windows
  public static final void deleteDIR(String args) throws Exception
  {

    File directory = new File( args );

    if(!directory.isDirectory())
    {
      System.err.println( args + " is not directory" );
      return;
    }

    deleteAll( directory );
  }

  public static final void deleteAll( File directory )
  {
    File[] files = directory.listFiles();

    for(int i=0; i < files.length;i++){
      if( files[i].isDirectory() )
        deleteAll( files[i] );
      else
        files[0].delete();
    }
    directory.delete();
  }



  // -------------------------------------------------------------------
  // appendFileWrite : write a file (mode append)
  //                ex) path2filename=="./kk.txt"
  //                    writebuff : 복사될 string
  //			-1 : error , 0 : success
  // -------------------------------------------------------------------

  public static final int appendFileWrite (String path2filename,String writebuff)
  {
  /*
  if ( writebuff.length() > 2024 ){
   String tmpbuff;
   tmpbuff=writebuff.substring(1,2024);

   try {
       FileOutputStream fos = new FileOutputStream(path2filename, true);
       PrintStream ps = new PrintStream(fos);
       ps.print(tmpbuff+'\n');
       ps.close();
     }catch(Exception e){
       Syste.out.println("appendFileWrite Exception");
       e.printStackTrace();
       return(-1);
     }
  }

  else{
   try {
       FileOutputStream fos = new FileOutputStream(path2filename, true);
       PrintStream ps = new PrintStream(fos);
       ps.print(writebuff+'\n');
       ps.close();
      }catch(Exception e){
       e.printStackTrace();
       Syste.out.println("appendFileWrite Exception");
      }
  }
  */

    try {
      FileOutputStream fos = new FileOutputStream(path2filename, true);
      PrintStream ps = new PrintStream(fos);
      ps.print(writebuff+'\n');
      ps.close();
    }catch(Exception e){
      e.printStackTrace();
      //System.out.println("appendFileWrite Exception");
      return -1;
    }
    return 0;
  }


  /**
   * 시스템관련 UTIL
   *
   */

  public static final String getLocalHostName()
  {
    InetAddress Address=null ;

    try{
      Address = InetAddress.getLocalHost();

    } catch (Exception e) {
      System.out.println(e.getMessage());
      return (null);
    }
    return( Address.getHostName() );
  }

  public static final String GetHostName()
  {
    InetAddress Address=null ;

    try{
      Address = InetAddress.getLocalHost();
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return (null);
    }
    return( Address.getHostName() );
  }


  public static final String GetSysTime()
  {
    SimpleDateFormat fmt = new SimpleDateFormat("HHmmssSSS");
    return fmt.format(new java.util.Date()).toString();
  }


  public static final String GetDate(Connection conn, String itvdate)
  {
    String 	seqtmp=null;
    int    	seq=0;
    Statement 	stmt=null;
    ResultSet 	rs=null;

    try {

      String tSQL = "SELECT TO_CHAR( SYSDATE - " + itvdate + ", 'YYYYMMDD' ) FROM DUAL";
      //System.out.println(tSQL);
      stmt = conn.createStatement( );
      rs = stmt.executeQuery(tSQL);
      rs.next();

      seq = rs.getInt(1);
      //System.out.println("bbbbbbbbbbbbbbbbbb" + seq);
      rs.close();
    } catch(SQLException e) {
      System.out.println(e.getMessage());
      return null;
    }
    if (seq==0) return null;
    //System.out.println("aaaaaaaaaaaa" + seq);
    return(Int2Str(seq));
  }


  public static final String GetSysDate()
  {
    SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
    return fmt.format(new Date()).toString();
  }

  /**
   * 이함수는 현재의 시간과 일자를 리턴한다.
   * @return String - Current Date, time
   */

  public static final String getCurrentDate() {
    String currentDate="";

    Calendar c=Calendar.getInstance();

    String yyyy=new Integer(c.get(Calendar.YEAR)).toString();
    String mm=new Integer(c.get(Calendar.MONTH) + 1).toString();
    String dd=new Integer(c.get(Calendar.DATE)).toString();
    String hr=new Integer(c.get(Calendar.HOUR)).toString();
    String min=new Integer(c.get(Calendar.MINUTE)).toString();
    String sec=new Integer(c.get(Calendar.SECOND)).toString();

    if (mm.trim().length()==1){
      mm="0" + mm;
    }

    if (dd.trim().length()==1){
      dd="0" + dd;
    }

    currentDate=mm + "/" + dd + "/" + yyyy + "-" + ZeroToStr(hr,2) + ":" + ZeroToStr(min,2) + ":" + ZeroToStr(sec,2);

    return currentDate;
  }


  /**
   * 시작시간과 종료시간을 롱값으로 환산해 Interval 차이를 계산한다.
   *  - 이것은 초단위로 혼산된다.
   */
  public static final int ItvSec( String psStartTimer, String psEndTimer )
  {

    String 	ss = null;
    String 	mm = null;
    String 	ee = null;
    int    	startsec = 99999999;
    int	 	endsec = 99999999;

    try {
      ss=psStartTimer.substring(0,2);
      mm=psStartTimer.substring(2,4);
      ee=psStartTimer.substring(4,6);
      startsec = atoi(ss)*60*60 + atoi(mm)*60 + atoi(ee);

      ss=psEndTimer.substring(0,2);
      mm=psEndTimer.substring(2,4);
      ee=psEndTimer.substring(4,6);
      endsec = atoi(ss)*60*60 + atoi(mm)*60 + atoi(ee);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return (-1);
    }
    if( endsec < startsec ) return -1;
    else
      return(endsec-startsec);
  }


  /**
   * 한글 / 영어 encoder
   *
   */
  public static final String en(String ko) {
    String new_str = null;
    try {
      new_str  = new String(ko.getBytes("KSC5601"), "8859_1");
    } catch(UnsupportedEncodingException ex) {
      System.out.println(ex.getMessage());
      return null;
    }
    return new_str;
  }


  public static final String ko(String en) {
    String new_str = null;
    try {
      new_str  = new String(en.getBytes("8859_1"), "KSC5601");
    } catch(UnsupportedEncodingException ex) {
      System.out.println(ex.getMessage());
      return null;
    }
    return new_str;
  }


  /**
   * RunTime Execution
   *
   */
  public static final int exec_com(String command)
  {
    String cmd=command;
    Process p = null;

    try
    {
      p = Runtime.getRuntime().exec(cmd);
    } catch(IOException ex){
      System.out.println(ex.getMessage());
      return -1;
    }
    //return(p.exitValue());
    return(0);
  }


  public static final int execcommand(String args)
  {
    String command = args;
    Process ps = null;
    OutputStream out = null;
    BufferedReader in = null;
    String res =null;

    String [] cmd = {"cmd.exe","/c","empty"} ;
    cmd[2] = command;


    try {
      ps = Runtime.getRuntime().exec( cmd );
    }catch(IOException e) {
      System.out.println(e);
      return -1;
    }

    in = new BufferedReader(new InputStreamReader(ps.getInputStream()));
    out = ps.getOutputStream();
    try {
      while((res = in.readLine()) != null){

        System.out.println(res);
      }
    }catch(IOException e) {
      System.out.println(e);
      return -1;
    }

    return 0;
  }


  /**
   * HOST SEQ 구한다.

     CREATE SEQUENCE SQ_FALOGGHT_HOSTSEQ
     INCREMENT BY 1
     START WITH   1
     MAXVALUE     99999999
     CYCLE
     ORDER
     *
     */
  public static final Connection getDBConnection() {
    Connection connTemp = null;
    try{
      Class.forName("weblogic.jdbc.pool.Driver");
    } catch (ClassNotFoundException e) {
      //System.out.println("ClassNotFoundException raised!!!!!");
      e.printStackTrace();
    }
    try{
      connTemp = DriverManager.getConnection("jdbc:weblogic:pool:oraclePool", null);
      //System.out.println("Drivermanager okay!!!!!");
    } catch (SQLException e){
      System.out.println("SQLException in getConnection()");
      e.printStackTrace();
    }
    return connTemp;
  }



  public static final String gethostseq(Connection conn)
  {
    String 		seqtmp=null;
    int    		seq=0;
    Statement 	stmt=null;
    ResultSet 	rs=null;

    try {
      String tSQL = "select SQ_FALOGGHT_HOSTSEQ.nextval from dual";
      stmt = conn.createStatement( );
      rs = stmt.executeQuery(tSQL);
      rs.next();
      seqtmp=Int2Str(rs.getInt(1));
      rs.close();
    } catch(SQLException e) {
      System.out.println("Error : " + e);
      e.printStackTrace();
      try{
        if( stmt != null)
          stmt.close();
        if(  rs != null )
          rs.close();
      }
      catch(Exception ex){
        ex.printStackTrace();
      }
      return null;
    }
   catch(Exception e) {
    System.out.println("Error : " + e);
    e.printStackTrace();
    try{
      if( stmt != null)
        stmt.close();
      if(  rs != null )
        rs.close();
    }
    catch(Exception ex){
      ex.printStackTrace();
    }
    return null;
  }

    try{
      if( stmt != null)
        stmt.close();
      if(  rs != null )
        rs.close();
    }
    catch(Exception ex){}
    return(CommonUtil.ZeroToStr8(seqtmp));
  }




  /**
   * 이 함수는 property file (C:\MYTEST\CHB\src\webserver\webserver.properties)로
   * 부터 다큐먼트루트디렉토리,포터,로그디렉토리의 정보를 읽어와 저장한다.
   * @return boolean - true, if the file exits and parameters are specified.
   *					false, otherwise.
   *-----------------------------------------------------------------------------
   * webserver.properties 파일의 내용
   *#HOME_DIRECTORY=c:\\test\\v3\\
  HOME_DIRECTORY=C:\\MYTEST\\CHB\\src\\webserver\\root_docu\\
  #LOG_FILE=c:\\test\\log.txt
  LOG_FILE=C:\\MYTEST\\CHB\\src\\webserver\\log\\log.txt
  PORT_NUMBER=90
  */

  public static final boolean getProperties() {
  /*
  private static final String PROPERTIES_FILE="WebServer.properties";
  private static final String PARAM_HOME_DIRECTORY="HOME_DIRECTORY";
  private static final String PARAM_LOG_FILE="LOG_FILE";
  private static final String PARAM_PORT_NUMBER="PORT_NUMBER";

  public static String HOME_DIRECTORY;
  public static String LOG_FILE;
  public static int PORT_NUMBER;
  */

    String PROPERTIES_FILE="WebServer.properties";
    String PARAM_HOME_DIRECTORY="HOME_DIRECTORY";
    String PARAM_LOG_FILE="LOG_FILE";
    String PARAM_PORT_NUMBER="PORT_NUMBER";

    String HOME_DIRECTORY;
    String LOG_FILE;
    int PORT_NUMBER;

    Properties p=new Properties();
    String value=null;

    try{
      p.load(new FileInputStream( new File(PROPERTIES_FILE)));
      if ((value=p.getProperty(PARAM_HOME_DIRECTORY))!=null){
        HOME_DIRECTORY=value;
      }
      if ((value=p.getProperty(PARAM_LOG_FILE))!=null){
        LOG_FILE=value;
      }
      if ((value=p.getProperty(PARAM_PORT_NUMBER))!=null){
        PORT_NUMBER=new Integer(value).intValue();
      }
    }catch(Exception e){
      e.printStackTrace();
      return false;
    }
    return true;
  }

 /* @ int [] array에 있는 내용을 소트하기
  *
  *
  */

  public static final int [] SortINTarray(int num[])
  {
    int tar [];
    tar = num;
    Arrays.sort(tar);
    return tar;
  }


  /**
   * 들어온 double수에 대한 소수 2째자리까지 끊어내기
   *
   * @param pattern : ######.##
   * @double        : 123456.789
   */

  public static final double cutDecimalFormat(String pattern,double num)
  {
    //String pattern = "######.##";

    DecimalFormat dformat = new DecimalFormat(pattern);

    //double num = 123456.789;

    System.out.println(dformat.format(num));

    return num;

  }

  /**
  십진수 숫자를 형식화

  ** 형식화 패터 스트링에 사용되는 특수 문자 **
  0 : 하나의 숫자
  # : 하나의 숫자이나 0 일 경우 공백 출력
  . : 소수점을 나타내는 문자
  , : 숫자를 comma 로 분리하는 문자
  ; : 패턴을 양수 패턴과 음수 패턴으로 나눔
  - : 음의 부호를 나타내는 문자
  % : 100 을 곱한 후 퍼센트로 나타남
  기타 문자 : 패턴의 앞뒤에 사용될 수 있다.
  ' : 패턴의 앞뒤에서 특수 문자를 보통 문자로 사용할 수 있게 함
  */

  public static final String ZeroToStr8(String str) {
    DecimalFormat fmt = new DecimalFormat("00000000");
    fmt = new DecimalFormat("00000000");
    return(fmt.format(Str2Int(str)));
  }

  public static final String ZeroToStr(String str,int size) {
    StringBuffer tt = new StringBuffer();
    for ( int i = 1; i <= size ; i ++ )
    {
      tt.append('0');
    }

    DecimalFormat fmt = new DecimalFormat(tt.toString());
    fmt = new DecimalFormat(tt.toString());
    return(fmt.format(Str2Int(str)));
  }

  public static final String LZeroToStr(String str,int size) {

    StringBuffer tt = new StringBuffer();
    for ( int i = 1; i <= size ; i ++ )
    {
      tt.append('0');
    }

    DecimalFormat fmt = new DecimalFormat(tt.toString());
    fmt = new DecimalFormat(tt.toString());
    return(fmt.format(Str2Long(str)));
  }


  public static final String BigZeroToStr(String str,int size) {
    if( str == null )
      str="0";

    StringBuffer tt = new StringBuffer();
    for ( int i = 1; i <= size-3 ; i ++ )
    {
      tt.append('0');
    }

    DecimalFormat fmt = new DecimalFormat(tt.toString()+".00");
    fmt = new DecimalFormat(tt.toString()+".00");
    return(fmt.format(CommonUtil.Str2Double(str)));
  }

  public static final String CutSpaceToStr(String str,int size) {
    if( str == null ){
      return null;
    }
    StringBuffer tt = new StringBuffer();
    for ( int i = 0; i < size ; i ++ )
    {
      if( str.charAt(i) == ' ' )
        break;
      tt.append(str.charAt(i));
    }
    return tt.toString();
  }


  public static final String SpaceToStr(String str,int size) {

    if( str == null ) str = " ";

    StringBuffer tt = new StringBuffer();
    for ( int i = 1; i <= size ; i ++ )
    {
      tt.append(' ');
    }

    for( int i=size-1,ii=str.length()-1 ; ii>=0 ; i--,ii--)
    {
      tt.setCharAt(i,str.charAt(ii));
    }
    //System.out.println("str char[" + tt.toString() +"]"+tt.toString().length() );
    return tt.toString();
  }

  public static final String StarToStr(String str,int size)
  {
    if( str == null ) str = "*";
    StringBuffer tt = new StringBuffer();
    for ( int i = 1; i <= size ; i ++ )
      tt.append('*');
    for( int i=size-1,ii=str.length()-1 ; ii>=0 ; i--,ii--)
      tt.setCharAt(i,str.charAt(ii));
    return tt.toString();
  }

  public static final String ConvertStrToStar(String str,int size)
  {
    if( str == null ) str = "*";
    StringBuffer tt = new StringBuffer();
    for ( int i = 0; i < size ; i ++ ){
      tt.setCharAt(i, '*');
    }
    return tt.toString();
  }

  public static final String PSpaceToStr(String str1,int size) {
    if( str1==null )
      str1 = " ";

    String str= null;
    if( str1.length() >= size ){
      str = str1.substring(0,size);
    }
    else
      str = str1;

    StringBuffer tt = new StringBuffer();
    for ( int i = 1; i <= size ; i ++ )
    {
      tt.append(' ');
    }
    for( int i=0 ; i<str.length() ; i++)
    {
      tt.setCharAt(i,str.charAt(i));
    }

    return tt.toString();
  }

  public static final String AtferSpaceToStr(String str1,int size) {
    if( str1==null )
      str1 = " ";

    String str= null;
    if( str1.length() >= size ){
      str = str1.substring(0,size);
    }
    else
      str = str1;

    StringBuffer tt = new StringBuffer();
    for ( int i = 1; i <= size ; i ++ )
    {
      tt.append(' ');
    }
    for( int i=0 ; i<str.length() ; i++)
    {
      tt.setCharAt(i,str.charAt(i));
    }

    return tt.toString();
  }

  public static final String FillSpaceToStr(String str1,int size) {
    if( str1 == null ){
      str1 = " ";
    }
    String str= null;
    if( str1.length() >= size ){
      str = str1.substring(0,size);
    }
    else
      str = str1;

    StringBuffer tt = new StringBuffer();
    for ( int i = 1; i <= size ; i ++ )
    {
      tt.append(' ');
    }
    for( int i=0 ; i<str.length() ; i++)
    {
      tt.setCharAt(i,str.charAt(i));
    }

    return tt.toString();

  }

  public static final String ZeroToStr8(int str) {

    DecimalFormat fmt = new DecimalFormat("00000000");
    fmt = new DecimalFormat("00000000");
    System.out.println("int");
    return(fmt.format(str));
  }

  public static final String ZeroToStr8(long str) {

    DecimalFormat fmt = new DecimalFormat("00000000");
    fmt = new DecimalFormat("00000000");
    System.out.println("long");
    return(fmt.format(str));
  }

  public static final int getRandomNum(int k)
  {

    Random ran = new Random(17);
    // 17은 일반적인 솟수(Prime Number)인데
    // Prime Number로 해야 난수가 골고루 분포합니다.
    // 물론 다른 수로해도 괜찮습니다. 솟수면 더 좋고요..

    int randomNumber = ran.nextInt(); // 실제로 난수하나를 만들어내는 작업입니다.
    if( randomNumber < 0 ) randomNumber*=(-1);

    return (randomNumber % k);
    // 이건 0~100 사이로 난수의 폭을 규정하려는
    // 것인데 실제로 난수와는 상관없는 루틴입니다.
  }

  /////////////////////////////////////////////////////////////////////////////
  // 하나의 파일을 읽어 라인단위별로 출력한다.
  /////////////////////////////////////////////////////////////////////////////
  static void GetLineStrFromFile(){

    String infile = ".\\test.in";
    String line = null;

    try {
      BufferedReader in = new BufferedReader(new FileReader(infile));

      // 파일을 라인단위로 끌고와서 파싱을 한다.
      for(;;)
      {
        if( (line = getFileLine(in)) != null ){
          System.out.println(line);
        }
        else{
          in.close();
          break;
        }
      }
    }
    catch(IOException ioe) {
      System.out.println("cmdFser IOException " + ioe.getMessage() );
      ioe.printStackTrace();
    }
    return ;
  }

  static String getFileLine(BufferedReader in){

    try {
      String line = null;
      if( (line=in.readLine()) != null )
        return line;
      else
        return null;
    }
    catch(IOException ioe) {
      System.out.println("cmdFser IOException " + ioe.getMessage() );
      ioe.printStackTrace();
    }
    return null;
  }

  ///////////////////////////////////////////////////////////////////////////// < 끝


  public void readlinefromfile() throws IOException
  {
    BufferedReader in = new BufferedReader( new FileReader("filename") );

    for( String line; (line = in.readLine()) != null ; )
      System.out.println(line);
  }


  public static final int isExistFile(String fname,String fsize)
  {
    String ConfigPath =
        //"C:\\MYTEST\\CHB\\src\\점포-본지점 Ver1.0\\Config.INI";
    "C:\\MYTEST\\INETCHB\\envConfig.INI";
    Properties p = new Properties();


    try {
      p.load(new FileInputStream(ConfigPath));
      String TargetPath = p.getProperty("FTPTARPATH");

      if ( fname == null || fsize == null || fname == "" || fsize == "") {
        System.out.println("fname is null,empty or fsize is null,empty ");
        return -1;
      }


      File f = new File(ascToksc(TargetPath)+fname);

      if (f.exists()) {
        System.out.println("파일의 크기 : " +f.length());
        System.out.println("파일"+f.getName()+"은 존재합니다");

        if( fsize.equals(CommonUtil.Int2Str((int)f.length()) ) )
          return 0;
        else
          return -1;
      }
      else {
        System.out.println("파일" + fname + "은 존재하지 않습니다");
        return -1;
      }
    }
    catch (Exception e) {
      return -1;
    }

  }




  public static  void main(String args[] )
  {
    float kk = 300/1000;
    cutDecimalFormat("######.##",kk);
/*
    String aa3 = "a.gz";
    int ii;


    if( aa3.charAt( (ii=(aa3.length()-1)) )=='z' &&
     aa3.charAt( (ii=(aa3.length()-2)) )=='g' &&
     aa3.charAt( (ii=(aa3.length()-3)) )=='.'

    ){
     System.out.println("-------------kkkkkk--------------");
    }


    //System.out.println(compreTo(".gz" ));
  System.out.println("--------------[" +CommonUtil.SpaceToStr(aa3,25)+"]--------------");
   aa3 = "1";
  System.out.println("--------------[" +CommonUtil.SpaceToStr(aa3,25)+"]--------------");
  aa3 = "12";
  System.out.println("--------------[" +CommonUtil.SpaceToStr(aa3,25)+"]--------------");
  aa3 = "134444";
  System.out.println("--------------[" +CommonUtil.SpaceToStr(aa3,25)+"]--------------");

  String aa4 = "가나다";
  System.out.println("--------------[" +CommonUtil.SpaceToStr(aa4,25)+"]--------------");

  execcommand("start dir");

  System.out.println(ZeroToStr8(99999999));
  System.out.println(GetHostSeq()+"---00000000000");
  System.out.println(GetSysDate()+"---00000000000");
  System.out.println(getTimeFreeFormat("hhmmssSS"));
  System.out.println(getTimeFreeFormat("hh:mm:ss:SS"));
  System.out.println(getTime());

  System.out.println(memcmp("string","string"));
  System.out.println(memcmp("string","staing",5));

  int [] tar = {9,1,2,3,4,5,6,7};
  SortINTarray(tar);
  for(int i=0;i<tar.length;i++)
   System.out.println(tar[i]);

  execcommand("start dir");

  try {
   deleteDIR("C:\\MYTEST\\CHB\\src\\common\\tmp");
  }
  catch(Exception e){}


  String str1 = "test string";
     String str2 = "한글 테스트";
     String str3 = "testing....ㅁ...";
     isHanGul(str1);
     isHanGul(str2);
     isHanGul(str3);

  cutDecimalFormat("######.##",12345678.4454489);


  FileInfo("C:\\MYTEST\\CHB\\src\\common");



  ENVINI ini = new ENVINI();
  ini.savingINI() ;
  ini.loadingINI();

  File info = new File("./CommonUtil.java");
  if (info.exists() ){
   System.out.println("aaaaaaaaaaaaaaaaaaaaa" + info.length());
   if( info.length() == (int) 25717 )
    System.out.println("rrrrrrrrrrrrrrrrrrrrrrrrrrrrr");
  }
  else
   System.out.println("ssssssssssssssss");

  String aa = null;
    //if( aa.equals("") ) System.out.println("null kkkkkkkkkkkkk");



    ////////////////////////////////////////////////////
    // Hashtable test
  Hashtable kk = new Hashtable();
  class FF {
   public String aa;
   public String bb;

   public FF(String aa,String bb)	{
    this.aa=aa;
    this.bb=bb;
   }

  }


  kk.put( "1", new FF("aa","bb") );
  kk.put( "2", new FF("cc","bb") );
  kk.put( "3", new FF("dd","bb") );

  FF f1 = (FF) kk.get("1");
  f1.aa = "DDDDDDDDDDDDDDDDDDDDDD";

  FF f2 = (FF) kk.get("1");
  System.out.println(f2.aa);

  appendFileWrite("./hhhhhh.txt","한글aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

  isExistFile("kkk.in","1");
  System.out.println("---------------------------");

  Connection conn = DBMSManager.dbConn("scott","tiger");
  System.out.println( GetDate(conn,"2") );

  System.out.println("---------------------------............");
  GetLineStrFromFile();
  System.out.println("---------------------------..........fffffffffffffff..");
    ///////////////////////////////////////////////////

  class Input {
   String type;
   String start;
   String end;
   public String toString(){
    return (type+start+end);
   }
   public Input getIn(String str){
    Input in = new Input();
    in.type = str.substring(0,1);
    in.start = str.substring(1,9);
    in.end = str.substring(9,17);
    System.out.println(in.type);
    System.out.println(in.start);
    System.out.println(in.end);
    return in;
   }
  }

  Input in = new Input();
  in.getIn("a1111111122222222");
*/
  }







}