package textengine

import java.io.{BufferedInputStream, File, FileInputStream}

import textengine.messengers.Messenger

import scala.concurrent.Future
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global


class ContentUtils(cluster: Cluster, messenger: Messenger, textUtils: TextUtils) {

  def getContent(path: String) = {
    cluster.info.text = messenger.loadMessenger.duringMessage
    if (new File(path).exists()) {
      val in = new BufferedInputStream(new FileInputStream(path))
      val numBytes = in.available()
      val bytes = new Array[Byte](numBytes)
      if (bytes.length < cluster.vars.max_file_size) {
        in.read(bytes, 0, numBytes)
        Some(new String(bytes, "UTF-8"))
      }
      else None
    } else {
      None
    }

  }

  def loadContentFromPath(path : String) = {
    loadContentFromData(getContent(path))
  }

  def loadContentFromData(data: Option[String]): Unit = {
    if (data.isDefined) {
      val load = Future {
        cluster.editor.text = data.get
      }
      load.onComplete {
        case Success(s) =>
          cluster.top.title = cluster.windowName + cluster.pathKeeper
          cluster.line.text = textUtils.caretUpdate
          cluster.info.text = messenger.loadMessenger.readyMessage
        case Failure(f) => cluster.info.text = messenger.loadMessenger.failedMessage
      }
    }
    else {
      cluster.info.text = messenger.loadMessenger.fileNotFoundMessage
    }
  }

  /*
    Reloads the content of a file
  */
  def reloadContent() = {
    val data = cluster.editor.text
    val load = Future {
      cluster.editor.text = data
    }
    cluster.info.text = messenger.loadMessenger.duringMessage
    load.onComplete {
      case Success(s) =>
        cluster.top.title = cluster.windowName + cluster.pathKeeper
        cluster.line.text = textUtils.caretUpdate
        cluster.info.text = messenger.loadMessenger.readyMessage
      case Failure(f) => cluster.info.text = messenger.loadMessenger.failedMessage
    }
  }

  def sortContent(collection: Seq[String], compare:(String, String) => Boolean) = {
    val res = Future {
      cluster.editor.text = collection.sortWith(compare).mkString("\n")
    }
    cluster.info.text = messenger.sortMessenger.duringMessage
    res.onComplete {
      case Success(sorted_file) =>
        cluster.info.text = messenger.sortMessenger.successMessage
      case Failure(e) =>
        messenger.sortMessenger.failureMessage
    }
  }

}