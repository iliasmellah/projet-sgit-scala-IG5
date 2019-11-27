package sgit

import java.io.File

object Add {

  val path = System.getProperty("user.dir")
  val / = File.separator
  val pathsgit = path + / + ".sgit" + /

  /**
   * main of add command - adds files from directory to stage and to index
   */
  def add(): Unit = {
    val sourcePath = path
    val allFiles = Funcs.getAllFiles(sourcePath, List[String]()).filterNot(f => f.contains(".sgit"))
    addFilesToTracked(allFiles)
    copyDataToStage()
    println("All files added to stage")
  }

  /**
   * given a file name, adds it to index.txt file
   * @param files
   */
  def addFilesToTracked(files : List[String]) : Unit = {
    val tw = files.map( f => {
      addFileToTracked(f)
    }).mkString
    Funcs.writeInFile(pathsgit + "index.txt", tw)
  }

  /**
   * returns sha1(fileName) + name + "\r\n" in order to fulfill index.txt
   * @param name
   * @return concatenation of sha1(name) and name
   */
  def addFileToTracked(name : String) : String = {
    val content = Funcs.getFileContentStringed(name)
    if (content.isEmpty) {
      val sha1 = Funcs.stringHasher(name)
      sha1 + name + "\r\n"
    } else {
      val sha1 = Funcs.stringHasher(content)
      sha1 + name + "\r\n"
    }
  }

  /**
   * copy all working directory to stage
   */
  def copyDataToStage() : Unit = {
    val src = path + /
    val des = pathsgit + "Stage" + /
    Funcs.deleteRemovedFiles(src, des, 6)
    Funcs.copyAllDir(src, des)
  }

  /**
   * updates the index with modified files
   * @param fileName
   * @param currentFiles
   */
  def updateTrackedFile(fileName : String, currentFiles : List[(String, String)]) : Unit = {
    val listTupleCurrent = currentFiles.filter(t => t._1 == fileName)
    val content = Funcs.getFileContentStringed(pathsgit + "index.txt").split("\r\n").toList
    content.foldLeft(List[String]()) { (acc, e) =>
      if (e.substring(40) == fileName) {
        Funcs.replaceLine(e, listTupleCurrent.head._2 + listTupleCurrent.head._1, pathsgit + "index.txt")
      }
      acc
    }
  }

}
