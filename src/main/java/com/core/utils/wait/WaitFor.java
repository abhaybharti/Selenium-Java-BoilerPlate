package main.java.com.core.utils.wait;

/**
 * The WaitFor class contains constants for various timeouts and retry counts used in an application.
 */
public class WaitFor {
  public static final int REST_API_CALL_TIMEOUT = 600000;
  public static final int HIGH_IMPLICIT_TIME_SEC = 60;
  public static final int MED_IMPLICIT_TIME_SEC = 30;
  public static final int PRIORITY_MED_IMPLICIT_TIME_SEC = 20;
  public static final int PRIORITY_LOW_IMPLICIT_TIME_SEC = 5;
  public static final int LOW_IMPLICIT_TIME_SEC = 5;
  public static final int MAX_IMPLICIT_TIME_SEC = 120;

  public static final int EXPLICIT_WAIT = 20;

  public static final int implicitWait = 1;
  public static final int PAGE_LOAD_TIMEOUT = 30;
  public static final int SCRIPT_TIMEOUT = 10;

  public static final int APPLICATION_RETRY_COUNT = 5;

  public static final int APPLICATION_RETRY_MIN_COUNT=2;
  public static final int APPLICATION_RETRY_MID_COUNT = 10;
  public static final int APPLICATION_RETRY_MAX_COUNT=30;
  public static final int POOLING_TIME_SEC = 20;
}
