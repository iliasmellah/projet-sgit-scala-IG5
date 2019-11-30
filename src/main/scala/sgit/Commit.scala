package sgit

import java.io.File

object Commit {

  val path = System.getProperty("user.dir")
  val / = File.separator
  val pathsgit = path + / + ".sgit" + /

  /**
   * main for commit command
   */
  def commit(commitMessage: Option[String] = None) : Unit = {

    val date = Funcs.getCurrentTime
    // for the 1st commit : sha1(sha1(sha1("initial commit"))) to insure that no other commit will have the same name
    // so lastCommit : "1078facc00319debd5fe09847c69f7439f4ed4d3" for 1st commit
    val parentCommit = Funcs.getFileContentStringed(pathsgit + "Commits/lastCommit.txt")
    val commitSha = Funcs.stringHasher(date + parentCommit)

    // creates the commit directory and initiates it
    Commit.createCommitDir(commitSha)

    // gets files removed and added between Local Repo and Stage directories
    val diffFiles = diffFilesLocalData()
    Commit.fulfillRemovedAddedFiles(commitSha, diffFiles)

    // gets all non added files from stage (files that may have been modified from last commit)
    /*val allNonAddedStageFiles = Funcs.getAllFiles(pathsgit + "Stage", List()).map(_.substring((pathsgit + "Stage/").length)).filterNot(f => diffFiles._2.contains(f))

    allNonAddedStageFiles.foreach( f => {
      fullfillOneModifiedFile(commitSha, Compare.getModificationsOneFile(f))
    })*/

    // creates the info file of the commit
    val nbRemovedFiles = diffFilesLocalData()._1.length
    val nbAddedFiles = diffFilesLocalData()._2.length
    val nbAddedLines = Commit.getLinesNumberFiles(Commit.getAllLinesFromModifiedFiles(commitSha.substring(0,5), "addedLines"), 0)
    val nbRemovedLines = Commit.getLinesNumberFiles(Commit.getAllLinesFromModifiedFiles(commitSha.substring(0,5), "removedLines"), 0)

    val info = List(commitSha, date, commitMessage.getOrElse("No commit message"), parentCommit, nbAddedLines, nbRemovedLines, nbRemovedFiles, nbAddedFiles)
    createInfoCommitFile(commitSha, info)

    logsFulfiller.fulfillLogsp(commitSha, "Arnaud Castelltort", date, commitMessage.getOrElse("No commit message"))
    logsFulfiller.fulfillLogs(commitSha, "Arnaud Castelltort", date, commitMessage.getOrElse("No commit message"))

    val a = pathsgit + "Stage" + /
    val b = pathsgit + "LocalRepo" + /
    Funcs.deleteRemovedFiles(a,b,10)
    copyAllDirForCommit(a,b)

    Funcs.writeInFile(pathsgit + "committed.txt","true")


    println("\nStaged files committed")
  }


  /**
   * creates the commit file with its commit sha key substringed and fulfills it
   * @param commitSha
   */
  def createCommitDir(commitSha : String) : Unit = {
    val commitName = commitSha.substring(0,5)
    val dir = new File(pathsgit + "Commits/" + commitName)
    dir.mkdir()
    val dirRemovedFiles = new File(pathsgit + "Commits/" + commitName + "/removedFiles")
    val dirAddedFiles = new File(pathsgit + "Commits/" + commitName + "/addedFiles")
    /*Funcs.createDir(pathsgit + "Commits/" + commitName)
    Funcs.createDir(pathsgit + "Commits/" + commitName + "/removedFiles")
    Funcs.createDir(pathsgit + "Commits/" + commitName + "/addedFiles")*/
    Funcs.writeInFile(pathsgit + "Commits/" + commitName + "/removedFiles.txt", "")
    val dirModifiedFiles = new File(pathsgit + "Commits/" + commitName + "/modifiedFiles")
    dirModifiedFiles.mkdir()
    updateLastCommitFile(commitSha)
  }

  /**
   * creates the info file of a commit, containing all need information about the given commit
   * @param commitSha
   * @param info
   */
  def createInfoCommitFile(commitSha : String, info : List[Any]) : Unit = {
    val dest = pathsgit + "Commits/" + commitSha.substring(0,5) + "/info.txt"
    val stringedInfo = info.mkString("\r\n")
    Funcs.writeInFile(dest, stringedInfo)
  }

  /**
   * updates the lastcommit file with the last commit done
   * @param commitSha
   */
  def updateLastCommitFile(commitSha : String): Unit = {
    val lastCommitFile = pathsgit + "Commits/lastCommit.txt"
    Funcs.replaceLine(Funcs.getFileContentStringed(lastCommitFile), commitSha, lastCommitFile)
  }

  /**
   * get difference between files in localrepo (already committed) and files in stage
   */
  def diffFilesLocalData() : (List[String], List[String]) = {
    // substring is needed to access directly to wanted dir
    // toSet.toList because unresolved duplicates in the list
    val filesLR = Funcs.getAllFiles(pathsgit + "LocalRepo"+ /, List()).map(_.substring(pathsgit.length + 10 )).toSet.toList
    val filesD = Funcs.getAllFiles(pathsgit + "Stage" + /, List()).map(_.substring(pathsgit.length + 6 )).toSet.toList
    Compare.diff2(filesLR, filesD, List(), List())
  }

  /**
   * fulfills the removeFiles and addedFiles in the given commit
   * @param commitSha
   * @param diffFiles
   */
  def fulfillRemovedAddedFiles(commitSha : String, diffFiles : (List[String], List[String])): Unit = {
    Funcs.writeInFile(pathsgit + "Commits/" + commitSha.substring(0,5) + "/removedFiles.txt", diffFiles._1.mkString("\r\n"))
    Funcs.writeInFile(pathsgit + "Commits/" + commitSha.substring(0,5) + "/addedFiles.txt", diffFiles._2.mkString("\r\n"))
  }

  /**
   * for a given commit and a given file, writes removed and added content and their lines
   * @param commitSha1
   * @param modificationsFile
   */
  def fullfillOneModifiedFile(commitSha1 : String, modificationsFile : (String, List[(String, Int)], List[(String, Int)])) : Unit = {
    if (modificationsFile._2.nonEmpty || modificationsFile._3.nonEmpty) {
      val dirPath = pathsgit + "Commits/" + commitSha1.substring(0,5) + "/modifiedFiles/" + modificationsFile._1.substring(0,modificationsFile._1.length-4).replaceAll("""\\""", "-")
      val dir = new File(dirPath)
      dir.mkdir()
      Funcs.writeInFile(dirPath + "/removedContent.txt", modificationsFile._2.map(_._1).mkString("\r\n"))
      Funcs.writeInFile(dirPath + "/removedLines.txt", modificationsFile._2.map(_._2).mkString("\r\n"))
      Funcs.writeInFile(dirPath + "/addedContent.txt", modificationsFile._3.map(_._1).mkString("\r\n"))
      Funcs.writeInFile(dirPath + "/addedLines.txt", modificationsFile._3.map(_._2).mkString("\r\n"))
    }
  }

  /**
   * gets the number of lines for a given file
   * @param fileName
   * @return
   */
  def getLinesNumberOneFile(fileName : String): Int = {
    if (Funcs.getFileContentStringed(fileName).length == 0) { 0 }
    else { Funcs.getFileContentStringed(fileName).split("\r\n").length }
  }

  /**
   * gets the total number of lines for a list of files
   * @param fileNames
   * @param result
   * @return
   */
  @scala.annotation.tailrec
  def getLinesNumberFiles(fileNames : List[String], result : Int) : Int = {
    if (fileNames.isEmpty) { result }
    else { getLinesNumberFiles(fileNames.tail, result + getLinesNumberOneFile(fileNames.head)) }
  }

  /**
   * choose option "addedLines" if we want to get sum of added lines from commit, option "removedLines" for removed
   * @param commitSha
   * @param option
   * @return
   */
  def getAllLinesFromModifiedFiles(commitSha : String, option : String) : List[String] = {
    val listDir = new File(pathsgit + "Commits/" + commitSha.substring(0,5) + "/modifiedFiles").listFiles.toList
    listDir.map( d => d + "\\" + option + ".txt")
  }

  // unexplained error when i tried to reuse Funcs.copyAllDir
  def copyAllDirForCommit(src : String, dest: String) : Unit = {
    val lf = new File(src).listFiles.toList
    lf.foreach( f => {
      if (f.isDirectory) {
        val dir = new File(dest + f.getName)
        dir.mkdir()
        copyAllDirForCommit(src + f.getName + "/", dest + f.getName + "/")
      } else {
        Funcs.copyFile(src + f.getName, dest + f.getName)
      }
    })
  }

}
