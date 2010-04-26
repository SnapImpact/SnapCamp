/*package org.snapimpact.geocode

import _root_.org.apache.commons.httpclient.methods.GetMethod
import _root_.java.io._
import _root_.net.liftweb.util._
import _root_.org.apache.commons.httpclient._, methods._, params._
import cookie.CookiePolicy

trait HttpClient {
  val hc = new org.apache.commons.httpclient.HttpClient

  protected def getStringFromUrl(url: String): Option[String] = {
    val gm = new GetMethod(url)
    execute(gm)
  }

  /**
   * @param method HttpMethodBase Takes the raw HTTP commons Method and executes it
   */
  private def execute(method: HttpMethodBase): Option[String] = {

    // Provide custom retry handler is necessary
    method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
      new DefaultHttpMethodRetryHandler(3, false))
    method.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY)

    try {
      // Execute the method.
      var statusCode = hc.executeMethod(method)

      if (statusCode != HttpStatus.SC_OK) {
        Thread.sleep(5)
        statusCode = hc.executeMethod(method)
      }

      if (statusCode == HttpStatus.SC_SERVICE_UNAVAILABLE) {
        Thread.sleep(15)
        statusCode = hc.executeMethod(method)
      }

      if (statusCode == HttpStatus.SC_OK) {
        Some(StreamResponseProcessor(method))
      } else {
        None
      }

    } catch {
      case ex: HttpException => {
        None
      }
      case ex: IOException => {
        None
      }
      case ex: Exception => {
        None
      }
    } finally {
      // Release the connection.
      method.releaseConnection();

    }
  }

  /**
   * As InputStream is a mutable I/O, we need to use a singleton to access
   * it / process it and return us a immutable result we can work with. If
   * we didnt do this then we get into a whole world of hurt and null pointers.
   */
  private object StreamResponseProcessor {
    /**
     * @param method HttpMethodBase Takes the raw HTTP commons Method and processes its stream response
     */
    def apply(method: HttpMethodBase): String = {
      val stream: InputStream = method.getResponseBodyAsStream()
      val reader: BufferedReader = new BufferedReader(new InputStreamReader(stream))
      val ret: StringBuffer = new StringBuffer

      try {
        def doRead {
          reader.readLine() match {
            case null => ()
            case line =>
              ret.append(" " + line)
              doRead
          }
        }

        doRead
        ret.toString
      } catch {
        case _ => null
      } finally {
        stream.close
        reader.close
      }
    }
  }
}*/
