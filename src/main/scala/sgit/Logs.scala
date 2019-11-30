package sgit

import java.io.File

object Logs {

  val path = System.getProperty("user.dir")
  val / = File.separator
  val pathsgit = path + / + ".sgit" + /

  def printLogsp() = {
    println("\n" + Funcs.getFileContentStringed(pathsgit + "logsp.txt"))
  }

  def printLogs() = {
    println("\n" + Funcs.getFileContentStringed(pathsgit + "logs.txt"))
  }

  /**
   * get needed files to shows logs
   * option : "info" or "addedFiles" or "removedFiles" --> gets data from each one of these files for all commits
   * @param option
   * @return
   */
  def getLogNeededFiles(option : String) : List[String] = {
    val listDir = new File(pathsgit + "Commits" + /).listFiles.filter(_.isDirectory).toList
    listDir.map( d => d + / + option + ".txt")
  }

  /**
   * gets log files content
   * @param ls
   * @return
   */
  def getLogFilesContent(ls : List[String]) : List[List[String]] = {
    ls.map( l => {
      Funcs.getFileContentStringed(l).split("\r\n").toList
    })
  }

  /**
   * main for log command
   */
  def logStat() : Unit = {
    val infos = getLogNeededFiles("info")
    val listInfos = getLogFilesContent(infos)
    listInfos.foreach(l => {
      println("\r\n"
        + "Commit ID : " + l(0) + "\r\n"
        + "Author : " + l(1) + "\r\n"
        + "Date : " + l(2) + "\r\n"
        + "Description : " + l(3) + "\r\n"
        + "Parent commit ID : " + l(4) + "\r\n"
        + "Total Number of Added Lines : " + l(5) + "\r\n"
        + "Total Number of Removed Lines : " + l(6) + "\r\n"
        + "Total Number of Removed Files : " + l(7) + "\r\n"
        + "Total Number of Added Files : " + l(8) + "\r\n"
        + "\r\n****************************************")
    })
  }

  //useless at this point since logStat does better
  def logP() : Unit = {
    val infos = getLogNeededFiles("info")
    val fullCommitNames = getLogFilesContent(infos).map(_(0))
    val adds = getLogNeededFiles("addedFiles")
    val listAdds = getLogFilesContent(adds)
    val rems = getLogNeededFiles("removedFiles")
    val listRems = getLogFilesContent(rems)

    val allLogsData = fullCommitNames zip (listAdds zip listRems)

    allLogsData.foreach( ald => {
      println("Commit name : " + ald._1)
      println("Files Added : " + ald._2._1.mkString(" -- "))
      println("Files Removed : " + ald._2._2.mkString(" -- "))
      println("\r\n****************************************")
    })
  }


}
