/**
 * 
 */
package org.glycoinfo.rdf.glycan.service.search.wurcs;

/**
 * @author aoki
 *
 */
public class MotifException extends Exception {

  private static final long serialVersionUID = 1L;

  /**
   * @param message
   */
  public MotifException(String message) {
    super(message);
  }

  /**
   * @param cause
   */
  public MotifException(Throwable cause) {
    super(cause);
  }

  /**
   * @param message
   * @param cause
   */
  public MotifException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param message
   * @param cause
   * @param enableSuppression
   * @param writableStackTrace
   */
  public MotifException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
