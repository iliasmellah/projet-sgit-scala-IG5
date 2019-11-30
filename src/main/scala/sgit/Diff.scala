package sgit

import java.io.File

import scala.collection.mutable.ListBuffer

object Diff {

  val path = System.getProperty("user.dir")
  val / = File.separator
  val pathsgit = path + / + ".sgit" + /

  def diff() = {
    val filesS = Funcs.getAllFiles(pathsgit + "Stage" + /, List())
    val filesWDSgit = Funcs.getAllFiles(path + /, List())
    val filesWD = filesWDSgit.filterNot(_.contains(".sgit"))

    if (!filesS.isEmpty) {
      val fileS = filesS(0).substring(pathsgit.length + 6)
      val fileWD = filesWD(0).substring(path.length + 1)
      val filename = fileS
      val diff = getModificationsOneFile(fileS)

      val removedLines = new ListBuffer[String]()
      diff._2.foreach(x => {
        removedLines += x._1
      })

      val addedLines = new ListBuffer[String]()
      diff._3.foreach(x => {
        addedLines += x._1
      })

      println("\nDiff file : " + filename + "\n")
      if(addedLines.isEmpty && removedLines.isEmpty) {
        println("No changes made yet : staging area already updated")
      } else {
        if (addedLines.isEmpty) {
          println("No added lines\n")
        } else {
          println("Added lines :")
          println(addedLines.toList.mkString("\n + "))
        }
        println("\nNo deleted lines")
      }
    } else {
      val fileName = filesWD(0).substring(path.length + 1)
      val contentFileWD = Funcs.getFileContentStringed(path + fileName)
      println("Diff file (new file) : " + fileName + "\n\n")
      println("Added lines :")
      println(contentFileWD.mkString("\n"))
    }
  }

  def getModificationsOneFile(fileName : String) : (String, List[(String, Int)], List[(String, Int)]) = {
    val add, rem = List()
    val b = Funcs.getFileContentStringed(path + / + fileName).split("\r\n").toList.zipWithIndex
    val a = Funcs.getFileContentStringed(pathsgit + "Stage" + / + fileName).split("\r\n").toList.zipWithIndex
    val diff = Compare.diff(a,b,add,rem)
    val removed = diff._1
    val added = diff._2
    (fileName, removed, added)
  }
}
