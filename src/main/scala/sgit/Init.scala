package sgit

import java.io.File

object Init {

  /**
   * ask user for his working directory path in order to initalize .sgit directory
   * @return
   */
  def askForPath() : String = {
    println("\r\nPlease give us the path of your working directory (only asked once to initialize)")
    println("It must look like this : C:/Users/pierre/Desktop/Project/")
    println("!!! Don't forget the last / in your path !!!")
    val pathGiven : String = scala.io.StdIn.readLine()
    val lastChar = pathGiven.substring(pathGiven.length-1)
    if (lastChar == "/" || lastChar == "\\") {
      Funcs.writeInFile(Funcs.pathInit, pathGiven)
      pathGiven
    } else {
      println("Do not forget the last / in your path")
      "wrong"
    }
  }

  /**
   * initialize the .sgit directory
   */
  def init() : Unit = {
    val path = Funcs.antiSlash(askForPath())
    if (path == "wrong") {
      init
    } else {
      val pathsgit = path + ".sgit/"
      if (!isInit(path)) {
        Funcs.createDir(pathsgit)
        Funcs.createDir(pathsgit + "Commits")
        Funcs.writeInFile(pathsgit + "Commits/lastCommit.txt", "1078facc00319debd5fe09847c69f7439f4ed4d3")
        Funcs.createDir(pathsgit + "LocalRepo")
        Funcs.createDir(pathsgit + "Stage")
        Funcs.writeInFile(pathsgit + "index.txt","")
        println("Working directory is initialized with sgit\r\n")
        println("Please relaunch sgit to get started")
        // really not nice ...
        System.exit(0)
      } else {
        println("Project already initialized")
      }
    }
  }

  /**
   * checks if a directory is already initialized with sgit
   * @param path
   * @return
   */
  def isInit(path: String) : Boolean = {
    val dir = new File(path)
    if (dir.exists()) {
      val listFiles = dir.listFiles.toList.map(_.toString)
      val listNames = listFiles.map(_.substring(path.length))
      listNames.contains(".sgit")
    } else { false }
  }

}
