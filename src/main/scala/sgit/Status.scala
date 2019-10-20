package sgit

import scala.io.Source

object Status {

  /**
   * main for status command
   */
  def status() : Unit = {
    val modifs = getModifiedFiles(getFilesTracked, getCurrentFiles)
    if (modifs.isEmpty)
      println("All files are in stage")
    else {
      println("Here are the modified files :")
      modifs.foreach(m => println(m.substring(Funcs.path.length)))
    }
  }

  /**
   * Returns all modified files (sha1 from index and current directory are not the same if a file is modified and not staged)
   * @param x
   * @param y
   * @return
   */
  def getModifiedFiles(x : List[(String, String)], y : List[(String, String)]): List[String] = {
    val x2 = x.map( s => {
      (Funcs.antiSlash(s._1), Funcs.antiSlash(s._2))
    })
    if(x.nonEmpty && y.nonEmpty) {
      val result = x2.foldLeft(List[String]()){(acc, e) =>
        if (isFileModified(e, y)) { acc :+  e._1 } else { acc }
      }
      result
    } else if (x.isEmpty) {
      val (y1,y2) = y.unzip
      y1
    } else { List() }
  }

  /**
   * Return true if file from index has been modified
   * @param tupleFile
   * @param currentFiles
   * @return
   */
  def isFileModified(tupleFile : (String, String), currentFiles : List[(String, String)]) : Boolean = {
    val tupleCurrent = currentFiles.filter(_._1 == tupleFile._1)
    if (tupleCurrent.nonEmpty) {
      !(tupleFile._2 == tupleCurrent.head._2)
    } else { false }

  }

  /**
   * Returns List(Name,Sha1(name)) from index.txt in order to compare it with working directory files
   * @return
   */
  def getFilesTracked : List[(String, String)] = {
    val trackFilePath = Source.fromFile(Funcs.pathsgit + "Index.txt")
    val filesTracked = trackFilePath.getLines.toList
    trackFilePath.close()
    filesTracked.map(file => {
      (file.substring(40,file.length),file.substring(0,40))
    })
  }

  /**
   * Returns List(Name,Sha1(name)) of working directory files in order to compare it with index files
   * @return
   */
  def getCurrentFiles : List[(String, String)] = {
    val sourcePath = Funcs.path
    val allFiles = Funcs.getAllFiles(sourcePath, List[String]()).filterNot(f => f.contains(".sgit"))
    val allContents = allFiles.map(Funcs.getFileContentStringed)
    val allContentsHashed = Funcs.contentHasher(allContents)
    val allNames = allFiles.map(_.toString).map(Funcs.antiSlash)
    allNames zip allContentsHashed
  }

}
