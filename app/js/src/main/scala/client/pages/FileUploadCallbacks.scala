package client.pages

import org.scalajs.dom.ErrorEvent
import org.scalajs.dom.ProgressEvent

import scala.scalajs.js

/**
  * Bundles functions that are used as callbacks for file upload events in [[org.scalajs.dom.XMLHttpRequestEventTarget]].
  * <p>
  * See also: https://developer.mozilla.org/en-US/docs/Web/API/XMLHttpRequest/upload
  * <p>
  * Created by Matthias Braun on 8/27/17.
  */
case class FileUploadCallbacks(
                                onLoadStart: js.Any => _ = (_: js.Any) => {},
                                onProgress: ProgressEvent => _ = (_: ProgressEvent) => {},
                                onAbort: js.Any => _ = (_: js.Any) => {},
                                onLoadEnd: ProgressEvent => _ = (_: ProgressEvent) => {},
                                onError: ErrorEvent => _ = (_: ErrorEvent) => {},
                                onLoad: js.Any => _ = (_: js.Any) => {},
                                onTimeOut: js.Any => _ = (_: js.Any) => {}
                              )

