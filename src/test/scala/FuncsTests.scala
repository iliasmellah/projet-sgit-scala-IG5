import org.scalatest.FunSuite
import sgit.Funcs

class FuncsTests extends FunSuite {

  val path: String = Funcs.antiSlash(System.getProperty("user.dir")) + "/src/test/scala/TestFiles/"

  test("Funcs.contentHasher") {
    assert(Funcs.contentHasher(Seq("un","deux")) === Seq("dd3bc42b2cbba792a371118cd1c87384c107bf6c","503c02f0c435f0b4da0fa867f7cd59ca411b6857"))
    assert(Funcs.contentHasher(Seq()) === Seq())
  }


  test("Funcs.stringHasher") {
    assert(Funcs.stringHasher("test") === "a94a8fe5ccb19ba61c4c0873d391e987982fbbd3")
    assert(Funcs.stringHasher("") === "String to hash is empty")
  }

  test("Funcs.antiSlash") {
    assert(Funcs.antiSlash("\\") === "/")
    assert(Funcs.antiSlash("a\\a") === "a/a")
    assert(Funcs.antiSlash("a\\a\\a") === "a/a/a")
    assert(Funcs.antiSlash("") === "")
  }

  test("Funcs.getFileContentStringed") {
    assert(Funcs.getFileContentStringed(path + "getFileContentStringed.txt") === "Here is a the getFileContentStringed test file !")
    assert(Funcs.getFileContentStringed("") === "File not found")
  }

  test("Funcs.replaceLine") {
    if(Funcs.getFileContentStringed(path + "replaceLine.txt") === "1"){
      Funcs.replaceLine("1", "2", path + "replaceLine.txt")
      assert(Funcs.getFileContentStringed(path + "replaceLine.txt") === "2")
    } else if (Funcs.getFileContentStringed(path + "replaceLine.txt") === "2") {
      Funcs.replaceLine("2", "1", path + "replaceLine.txt")
      assert(Funcs.getFileContentStringed(path + "replaceLine.txt") === "1")
    } else {
      Funcs.replaceLine("","","")
      assert(Funcs.getFileContentStringed(path + "replaceLine.txt") === ())
    }
  }

  test("Funcs.writeInFile") {
    Funcs.writeInFile(path + "writeInFile.txt", "")
    assert(Funcs.getFileContentStringed(path + "writeInFile.txt") === "")
    Funcs.writeInFile(path + "writeInFile.txt", "Testing write in file ...")
    assert(Funcs.getFileContentStringed(path + "writeInFile.txt") === "Testing write in file ...")
  }

  test("Funcs.getAllFiles") {
    assert(Funcs.getAllFiles("", List()) === List())
    assert(Funcs.getAllFiles("xzexzexze", List()) === List())
    assert(Funcs.getAllFiles(path, List()).map(Funcs.antiSlash) === List(path + "addFileToTracked.txt",
      path + "getFileContentStringed.txt", path +"replaceLine.txt", path + "writeInFile.txt"))
  }
}
