import org.scalatest.FunSuite
import sgit.{Add, Funcs}

class AddTests extends FunSuite {

  val path: String = Funcs.antiSlash(System.getProperty("user.dir")) + "/src/test/scala/TestFiles/"

  test("Add.addFileToTracked") {
    assert(Add.addFileToTracked(path + "addFileToTracked.txt") ==
      Funcs.stringHasher(Funcs.getFileContentStringed(path + "addFileToTracked.txt")) + path + "addFileToTracked.txt"+"\r\n")
  }

}
