package sgit

object Main extends App {

  loop
  @scala.annotation.tailrec
  def loop : Unit = {
    println("\r\nWELCOME TO SGIT !")
    println("Let us know what you need ! Exit with \"exit\"\r\n")
    val command : String = scala.io.StdIn.readLine()
    command match {
      case "init" => {
        // We get the working directory path to initialize
        // We initialize the working directory with files and directories sgit needs
        Init.init
      }
      case "add" => {
        // We add all files from working directory to stage directory & adds all these files with their sha1 key to the index.txt
        Add.add()
      }
      case "commit" => {
        // Commit all files in stage in its own directory commit with all needed information in it
        Commit.commit()

      }
      case "status" => {
        // We check if there are modified files that are not added in stage - Functionnality in progress -
        Status.status()
      }
      case "log" => {
        // Show all statistics between each commit
        Logs.logStat()
      }
      case "info" => {
        // Show all statistics between each commit
        println("Available commands : init, add, commit, status, log")
      }
      case "exit" => System.exit(0)
      case _ => println("This is not a proper sgit command ! Please try again")
    }
    loop
  }








}
