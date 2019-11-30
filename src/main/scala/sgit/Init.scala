package sgit

import java.io.File

object Init {

  /**
   * initialize the .sgit directory
   */
  def init() : Unit = {
    val path = System.getProperty("user.dir")
    val / = File.separator
    val pathsgit = path + / + ".sgit" + /
    if (!isInit(path)) {
      Funcs.createDir(pathsgit)
      Funcs.createDir(pathsgit + "Commits")
      Funcs.writeInFile(pathsgit + "Commits" + / + "lastCommit.txt", "1078facc00319debd5fe09847c69f7439f4ed4d3")
      Funcs.createDir(pathsgit + "LocalRepo")
      Funcs.createDir(pathsgit + "Stage")
      Funcs.writeInFile(pathsgit + "index.txt","")
      Funcs.writeInFile(pathsgit + "committed.txt","false")
      Funcs.writeInFile(pathsgit + "logs.txt","")
      Funcs.writeInFile(pathsgit + "logsp.txt","")
      println("\nWorking directory is initialized with sgit")
    } else {
      println("\nProject already initialized")
    }
  }

  /**
   * checks if a directory is already initialized with sgit
   * @param path
   * @return
   */
  def isInit(path: String) : Boolean = {
    val dir = new File(path)
    if (dir.isDirectory) {
      val listFiles = dir.listFiles.toList.map(_.toString)
      val listNames = listFiles.map(_.substring(path.length+1))
      listNames.contains(".sgit")
    } else { false }
  }

}
