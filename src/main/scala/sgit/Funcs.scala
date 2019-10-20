package sgit

import java.nio.charset.{CharsetDecoder, CodingErrorAction}
import java.util.regex.Pattern
import java.io.{BufferedWriter, File, FileWriter}
import java.util.Calendar

import scala.io.{Codec, Source}

object Funcs {

  /**
   * is used to hash a list of strings
   * @param ls
   * @return
   */
  def contentHasher(ls : Seq[String]) : Seq[String] = {
    if (ls.nonEmpty) { ls.map(stringHasher) }
    else { Seq() }
  }

  /**
   * hashes a string with sha1
   * @param s
   * @return
   */
  def stringHasher(s : String) : String = {
    if (s.nonEmpty)
    { val sha1 = java.security.MessageDigest.getInstance("SHA-1")
      sha1.digest(s.getBytes("UTF-8")).map("%02x".format(_)).mkString
    } else { "String to hash is empty" }
  }

  val decoder: CharsetDecoder = Codec.UTF8.decoder.onMalformedInput(CodingErrorAction.IGNORE)
  val pathInit = System.getProperty("user.dir").replaceAll(Pattern.quote("\\"),"/") + "/src/main/scala/sgit/Path.txt"
  val path = getFileContentStringed(System.getProperty("user.dir").replaceAll(Pattern.quote("\\"),"/") + "/src/main/scala/sgit/Path.txt")
  val pathsgit = path + ".sgit/"


  def antiSlash(s : String) : String = {
      s.replaceAll(Pattern.quote("\\"),"/")
  }

  def slash(s : String) : String = {
    s.replaceAll(Pattern.quote("/"),"\\")
  }

  /**
   * gets the content of a file given its path
   * @param f
   * @return
   */
  def getFileContentStringed(f : String) : String = {
    if (f.nonEmpty) { Source.fromFile(f)(decoder).mkString }
    else { "File not found" }
  }

  /**
   * given a file, it will replace the so (string original) line by sf (string final) line
   * @param so
   * @param sf
   * @param fn
   */
  def replaceLine(so : String, sf : String, fn : String) : Unit = {
    if (so.nonEmpty && sf.nonEmpty && fn.nonEmpty) {
      val content = Funcs.getFileContentStringed(fn).split("\r\n").toList
      val newContent = content.foldLeft(List[String]()){(acc, e) =>
        if (e == so) {
          acc :+ sf
        }else {
          acc :+ e
        }
      }.mkString("\r\n")
      val file = new File(fn)
      val bw = new BufferedWriter(new FileWriter(file))
      bw.write(newContent)
      bw.close()
    }
  }

  /**
   * gets date with wanted format : YYYY:MM:DD:HH:mm:ss:ms-ms-ms (used for info file of each commit)
   * @return
   */
  def getCurrentTime : String = {
      val calendar = Calendar.getInstance()
      val time = (
        String.format("%04d",calendar.get(Calendar.YEAR)) + ":"
          + String.format("%02d",calendar.get(Calendar.MONTH)+1) + ":"
          + String.format("%02d",calendar.get(Calendar.DAY_OF_MONTH)) + ":"
          + String.format("%02d",calendar.get(Calendar.HOUR_OF_DAY)) + ":"
          + String.format("%02d",calendar.get(Calendar.MINUTE)) + ":"
          + String.format("%02d",calendar.get(Calendar.SECOND)) + ":"
          + String.format("%03d",calendar.get(Calendar.MILLISECOND))
      )
      time
  }

  /**
   * given a destination, it will write the content in it
   * @param dest
   * @param content
   */
  def writeInFile(dest : String, content : String) : Unit = {
      val file = new File(dest)
      val bw = new BufferedWriter(new FileWriter(file))
      bw.write(content)
      bw.close()
  }

  /**
   * copy a file from a source to a destination path
   * @param src
   * @param dest
   */
  def copyFile(src : String, dest : String) : Unit = {
      val c = Funcs.getFileContentStringed(src)
      writeInFile(dest, c)
  }

  /**
   * copy a whole directory, subdirectories and files in it from a given source to a destination
   * @param src
   * @param dest
   */
  def copyAllDir(src : String, dest: String) : Unit = {
      val lf = new File(src).listFiles.toList.filterNot( f => f.toString.contains(".sgit"))
      lf.foreach( f => {
          if (f.isDirectory) {
              val dir = new File(dest + f.getName)
              dir.mkdir()
              copyAllDir(src + f.getName + "/", dest + f.getName + "/")
          } else {
              copyFile(src + f.getName, dest + f.getName)
          }
      })
  }

  /**
   * delete removed files from a directory
   * option 6 between working dir - stage & option 10 between stage and local repo
   * @param src
   * @param dest
   * @param option
   */
  def deleteRemovedFiles(src : String, dest : String, option : Int) : Unit = {
    val als = getAllFiles(src, List()).filterNot( f => f.toString.contains(".sgit")).map(_.substring(Funcs.path.length))
    val ald = getAllFiles(dest, List()).map(new File(_))
    ald.foreach( d => {
      if (!als.contains(d.toString.substring(Funcs.pathsgit.length + option))) {
        d.delete()
      }
    })
  }

  /**
   * gets all files from a given directory path
   * @param path
   * @param result
   * @return
   */
  def getAllFiles(path : String, result : List[String]) : List[String] = {
    if (path.nonEmpty) {
      val dir = new File(path)
      if (dir.exists()) {
        val listFiles = dir.listFiles.toList
        return tailRecFileLoop(listFiles,result)
      }
      List()
    }
    List()
  }

  def tailRecFileLoop(listFiles : List[File], result :  List[String]) :  List[String]   = {
    if(listFiles.nonEmpty) {
      val file = listFiles.head
      val filePath = file.toString
      if(file.isFile) {
        return tailRecFileLoop(listFiles.tail, result ::: List(filePath))
      } else {
        val dirResult = getAllFiles(filePath,result)
        return tailRecFileLoop(listFiles.tail, result) ::: dirResult
      }
    }
    result
  }

  /**
   * creates a directory from a given path
   * @param dirPath
   */
  def createDir(dirPath : String) : Unit = {
    val dir = new File(dirPath)
    dir.mkdir()
  }

}