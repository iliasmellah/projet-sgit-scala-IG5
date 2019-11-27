package sgit

object Main extends App {

  if(args.length > 0)
    {
      if(args(0) == "init")
        {
          Init.init() // TODO : check init from directory (sgit called from elsewhere)
        }
      else if(args(0) == "add")
        {
          if(args.length > 1)
            {
              if(args(1) == ".")
                {
                  Add.add() // TODO : add all
                }
              else
                {
                  Add.add() // TODO : add specific file
                }
            }
          else
          {
            println("Missing parameter after 'add'")
          }
        }
      else if (args(0) == "status")
        {
          Status.status()
        }
      else if (args(0) == "commit")
        {
          if(args.length > 1 && args(1) == "-m") {
            if (args.length > 2) {
              Commit.commit(Option(args(2))) // TODO : add method for specific message description
            }
            else {
              println("No commit message given. Please input a commit message after using '-m'")
            }
          }
          else
          {
            Commit.commit() // TODO : commit with default description
          }
        }
      else if (args(0) == "diff")
        {
          // TODO : diff method
        }
      else if (args(0) == "log")
        {
          if(args.length < 2)
            {
              Logs.logStat() // TODO : log
            }
          else if(args(1) == "-p")
            {
              Logs.logP() // TODO : log -p
            }
          else
            {
              println("Bad input after log. No method recognized")
            }
        }
      else if (args(0) == "help")
        {
          println("Help : here are available commands")
        }
    }
  else
    {
      println("Missing argumennt : init add commit log")
    }

/*
  loop
  @scala.annotation.tailrec
  def loop : Unit = {
    println("++++" + args(0))
    println("\r\nWELCOME TO SGIT 2!")
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
*/







}
