package sgit

import java.io.File

import scala.collection.mutable.ListBuffer

object logsFulfiller {

  val path = System.getProperty("user.dir")
  val / = File.separator
  val pathsgit = path + / + ".sgit" + /

  def fulfillLogsp(commitSha : String, author : String, date : String, description : String) : Unit = {
    val pathS = pathsgit + "Stage" + /
    val filesLR = Funcs.getAllFiles(pathS, List())
    val fileName = filesLR(0).substring(pathS.length)

    val allFilesLR = Funcs.getAllFiles(pathsgit + "LocalRepo" + /, List())
    val logspContent = Funcs.getFileContentStringed(pathsgit + "logsp.txt")

    if (!allFilesLR.isEmpty && allFilesLR(0).contains(fileName)) {
      val diff = getModificationsOneFile(fileName)

      val removedLines = new ListBuffer[String]()
      diff._2.foreach(x => {
        removedLines += x._1
      })

      val addedLines = new ListBuffer[String]()
      diff._3.foreach(x => {
        addedLines += x._1
      })

      if (removedLines.isEmpty) {
        val logspReversed =
          "\nNot deleted lines\n" +
          addedLines.reverse.toList.mkString("\n") +
          "\nAdded lines : \n\n" +
          "\n *** File : " + fileName + " ***\n\n" +
          "\nDescription : " + description +
          "\nDate : " + date +
          "\nAuthor : " + author +
          "\nCommit : " + commitSha +"\n"
        val logspRight = logspReversed.split("\n").toList.reverse.mkString("\n")

        Funcs.writeInFile(pathsgit + "logsp.txt", logspRight + "\n\n------------------------\n\n" + logspContent)
      } else {
        val logspReversed = removedLines.reverse.toList.mkString("\n") +
          "\nDeleted lines : \n\n" +
          addedLines.reverse.toList.mkString("\n") +
          "\nAdded lines : \n\n" +
          "\n *** File : " + fileName + " ***\n\n" +
          "\nDescription : " + description +
          "\nDate : " + date +
          "\nAuthor : " + author +
          "\nCommit : " + commitSha +"\n"
        val logspRight = logspReversed.split("\n").toList.reverse.mkString("\n")

        Funcs.writeInFile(pathsgit + "logsp.txt", logspRight + "\n\n------------------------\n\n" + logspContent)
      }
    } else {
      val fileContent = Funcs.getFileContentStringed(pathsgit + "Stage" + / + fileName)
      val logspRight =
          "\nCommit : " + commitSha +
          "\nAuthor : " + author +
          "\nDate : " + date +
          "\nDescription : " + description + "\n" +
          "\n *** File : " + fileName + " (new file) ***\n\n" +
          "\nAdded lines : \n" +
          fileContent + "\n"

      Funcs.writeInFile(pathsgit + "logsp.txt", logspRight + "\n\n------------------------\n\n" + logspContent)
    }
  }

  def fulfillLogs(commitSha : String, author : String, date : String, description : String) : Unit = {
    val contentToAdd = "\nCommit : " + commitSha +
      "\nAuthor : " + author +
      "\nDate : " + date +
      "\nDescription : " + description

    val logsContent = Funcs.getFileContentStringed(pathsgit + "logs.txt")

    Funcs.writeInFile(pathsgit + "logs.txt", contentToAdd + "\n\n------------------------------\n" + logsContent)
  }

  def getModificationsOneFile(fileName : String) : (String, List[(String, Int)], List[(String, Int)]) = {
    val add, rem = List()
    val a = Funcs.getFileContentStringed(pathsgit + "LocalRepo" + / + fileName).split("\r\n").toList.zipWithIndex
    val b = Funcs.getFileContentStringed(pathsgit + "Stage" + / + fileName).split("\r\n").toList.zipWithIndex
    val diff = Compare.diff(a,b,add,rem)
    val removed = diff._1
    val added = diff._2
    (fileName, removed, added)
  }


}
