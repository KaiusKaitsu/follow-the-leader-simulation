package project

import project.*
import project.Simulant
import project.FileReader
import java.io.File

import org.scalatest._
import org.scalatest.matchers.should._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.funsuite.AnyFunSuite

/** test for the class Vector2D */
class Vector2DSpec extends AnyFlatSpec with Matchers:

  "Vector2D" should "correctly add two vectors" in {
    val v1 = Vector2D(1, 2)
    val v2 = Vector2D(3, 4)
    (v1 + v2) should be (Vector2D(4, 6))
  }

  it should "correctly subtract two vectors" in {
    val v1 = Vector2D(1, 2)
    val v2 = Vector2D(3, 4)
    (v2 - v1) should be (Vector2D(2, 2))
  }

  it should "correctly multiply a vector by a scalar" in {
    val v = Vector2D(1, 2)
    (v * 2) should be (Vector2D(2, 4))
  }

  it should "correctly divide a vector by a scalar" in {
    val v = Vector2D(2, 4)
    (v / 2) should be (Vector2D(1, 2))
  }

  it should "correctly calculate the magnitude of a vector" in {
    val v = Vector2D(3, 4)
    v.magnitude should be (5)
  }

  it should "correctly normalize a vector" in {
    val v = Vector2D(3, 4)
    v.normalized.magnitude should be (1)
  }

  it should "correctly calculate the dot product of two vectors" in {
    val v1 = Vector2D(1, 2)
    val v2 = Vector2D(3, 4)
    (v1 dot v2) should be (11)
  }

end Vector2DSpec

/** test for the object FileReader */
class FileReaderSpec extends AnyFlatSpec with Matchers {

  "FileReader" should "return 'successfully loaded' for existing file 'Please write here'" in {
    val result = FileReader.read("Please write here")
    result should be ("succesfully loaded")
  }

  it should "return 'Corrupted file' for non-existing file 'rikki2'" in {
    val result = FileReader.read("rikki2")
    result should be ("Corrupted file")
  }

  it should "return 'File not found' for non-existing file 'rikki4'" in {
    val result = FileReader.read("rikki4")
    result should be ("File not found")
  }

  it should "return 'success' for writing a new file with a random name" in {
    val filename = scala.util.Random.alphanumeric.take(10).mkString
    val result = FileReader.write(filename)
    result should be ("success")
    new File(s"Saved config files/$filename").delete()
  }
  it should "return 'Corrupted file' for wrong format file rikki5'" in {
    val result = FileReader.read("rikki5")
    result should be("Corrupted file")
  }
}

/** test for the class configGeneric */
class ConfigGenericTest extends AnyFunSuite {

  test("ConfigGeneric instance should be initialized with valid values") {
    val config = new configGeneric(50, 30, 20, 10, 5, (500, 500), 10)

    assert(config.readLeaderSpeed == 50)
    assert(config.readFollowerSpeed == 30)
    assert(config.readLeaderMass == 20)
    assert(config.readFollowerMass == 10)
    assert(config.readFollowerNum == 5)
    assert(config.readMapSize == (500, 500))
    assert(config.readSimSpeed == 10)
  }

  test("changeLeaderSpeed method should update the leaderSpeed value") {
    val config = new configGeneric(50, 30, 20, 10, 5, (500, 500), 10)

    config.changeLeaderSpeed(80)
    assert(config.readLeaderSpeed == 80)

    config.changeLeaderSpeed(-10)
    assert(config.readLeaderSpeed == 1)

    config.changeLeaderSpeed(120)
    assert(config.readLeaderSpeed == 100)
  }

  // Repeat for the other methods (changeFollowerSpeed, changeLeaderMass, etc.)

  test("fromSpecific method should copy values from another ConfigGeneric instance") {
    val config1 = new configGeneric(50, 30, 20, 10, 5, (500, 500), 10)
    val config2 = new configGeneric(80, 60, 30, 15, 7, (800, 600), 20)

    config1.fromSpecific(config2)

    assert(config1.readLeaderSpeed == 80)
    assert(config1.readFollowerSpeed == 60)
    assert(config1.readLeaderMass == 30)
    assert(config1.readFollowerMass == 15)
    assert(config1.readFollowerNum == 7)
    assert(config1.readSimSpeed == 20)
  }

}