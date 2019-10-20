package sgit

object Compare {

  /**
   * gets differences between 2 files
   * a and b contains content of each lines and their number (location in the file)
   * rem and add are used to keep removed and added lines
   *
   * @param a
   * @param b
   * @param rem
   * @param add
   * @return
   */
  @scala.annotation.tailrec
  def diff(a : List[(String, Int)], b : List[(String, Int)], rem : List[(String, Int)], add : List[(String, Int)]) : (List[(String, Int)], List[(String, Int)]) = {
    if (a.isEmpty) { (rem, add ::: b) }
    else if (b.isEmpty) { (rem ::: a, add) }
    else {
      if (a.head._1 == b.head._1) {
        if (a.tail.isEmpty) { (rem, add ::: b.tail) }
        else if (b.tail.isEmpty) { (rem ::: a.tail, add)}
        else { diff(a.tail, b.tail, rem, add) }
      } else {
        val a1 = a.map(_._1)
        val b1 = b.map(_._1)
        if (a1.contains(b1.head)) {
          val rem2 = rem ::: a.slice(0, a.indexOf(b.head))
          diff(a.slice(a.indexOf(b.head) + 1, a.length), b.tail, rem2, add)
        } else {
          val add2 = add :+ b.head
          diff(a, b.tail, rem, add2)
        }
      }
    }
  }

  /* récupérer la liste des lignes du début de ls a ls(s) non incluse
  @scala.annotation.tailrec
  def getUntilLine(ls : List[String], s : String, r : List[String]) : List[String] = {
    if (ls.isEmpty) { r :+ ls.head } else {
      if (ls.head == s) { r } else {
        val r2 = r :+ ls.head
        if (ls.tail.isEmpty) { r2 } else {
          getUntilLine(ls.tail, s, r2)
        }
      }
    }
  }
  */

  @scala.annotation.tailrec
  def diff2(a : List[String], b : List[String], add : List[String], rem : List[String]) : (List[String], List[String]) = {
    if (a.isEmpty) { (rem, add ::: b) }
    else if (b.isEmpty) { (rem ::: a, add) }
    else {
      if (a.head == b.head) {
        if (a.tail.isEmpty) { (rem, add ::: b.tail) }
        else if (b.tail.isEmpty) { (rem ::: a.tail, add)}
        else { diff2(a.tail, b.tail, add, rem) }
      } else {
        if (a.contains(b.head)) {
          val rem2 = rem ::: a.slice(0, a.indexOf(b.head))
          diff2(a.slice(a.indexOf(b.head) + 1, a.length), b.tail, add, rem2)
        } else {
          val add2 = add :+ b.head
          diff2(a, b.tail, add2, rem)
        }
      }
    }
  }

  /**
   * is used to get differences between files with same name but not same path
   * useful to gets differences for example between a same named file from /Stage and /LocalRepo
   *
   * @param fileName
   * @return
   */
  def getModificationsOneFile(fileName : String) : (String, List[(String, Int)], List[(String, Int)]) = {
    val add, rem = List()
    val a = Funcs.getFileContentStringed(Funcs.pathsgit + "LocalRepo/" + fileName).split("\r\n").toList.zipWithIndex
    val b = Funcs.getFileContentStringed(Funcs.pathsgit + "Stage/" + fileName).split("\r\n").toList.zipWithIndex
    val diff = Compare.diff(a,b,add,rem)
    val removed = diff._1
    val added = diff._2
    (fileName, removed, added)
  }

}
