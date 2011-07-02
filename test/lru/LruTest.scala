package lru

import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.{Test, Before}
import org.junit.Assert._
import org.hamcrest.CoreMatchers._
import java.util.Date

@RunWith(classOf[JUnit4])
class LruTest {

  var lru: Lru = _
 
  @Before
  def setup = {
    lru = new Lru();
  }
  
  @Test
  def マップに１つ値を設定するとそれが取得できる() = {
      lru.put("KEY1", "VALUE1")
      assertEquals("VALUE1", lru.peek("KEY1").get)
  }
  
  @Test
  def マップに２つ値を設定するとそれが取得できる() = {
      lru.put("KEY1", "VALUE1")
      lru.put("KEY2", "VALUE2")
      assertEquals("VALUE1", lru.peek("KEY1").get)
      assertEquals("VALUE2", lru.peek("KEY2").get)
  }
  
  @Test
  def マップに3つ値を設定するとそれが最初がnull() = {
      lru.put("KEY1", "VALUE1")
      lru.put("KEY2", "VALUE2")
      lru.put("KEY3", "VALUE3")
      assertEquals(None, lru.peek("KEY1"))
  }

  @Test
  def マップに2つの値を繰り返しいれて取得できる() = {
      lru.put("KEY1", "VALUE1")
      lru.put("KEY2", "VALUE2")
      lru.put("KEY1", "VALUE1")
      lru.put("KEY1", "VALUE1")
      assertEquals("VALUE2", lru.peek("KEY2").get)
      assertEquals("VALUE1", lru.peek("KEY1").get)
  }
  
  @Test
  def 値を入れ直した場合に順序が更新される() = {
      lru.put("KEY1", "VALUE1")
      lru.put("KEY2", "VALUE2")
      lru.put("KEY1", "VALUE1")
      lru.put("KEY3", "VALUE3")
      
      assertEquals(Some("VALUE1"), lru.peek("KEY1"))
      assertEquals(None, lru.peek("KEY2"))
      assertEquals(Some("VALUE3"), lru.peek("KEY3"))
  }
  
  @Test
  def 値を取り出した場合に順序が更新される() = {
      lru.put("KEY1", "VALUE1")
      lru.put("KEY2", "VALUE2")
      lru.get("KEY1")
      lru.put("KEY3", "VALUE3")
      
      assertEquals(Some("VALUE1"), lru.peek("KEY1"))
      assertEquals(None, lru.peek("KEY2"))
      assertEquals(Some("VALUE3"), lru.peek("KEY3"))
  }

  @Test
  def キャッシュサイズを３に変更して３つの値を出し入れできる() = {
      lru = new Lru(3)
      
      lru.put("KEY1", "VALUE1")
      lru.put("KEY2", "VALUE2")
      lru.put("KEY3", "VALUE3")  
      
      assertEquals(Some("VALUE1"), lru.peek("KEY1"))
      assertEquals(Some("VALUE2"), lru.peek("KEY2"))
      assertEquals(Some("VALUE3"), lru.peek("KEY3"))
      
      lru.put("KEY4", "VALUE4")
      
      assertEquals(None, lru.peek("KEY1"))
      assertEquals(Some("VALUE4"), lru.peek("KEY4"))
  }
  
  @Test 
  def 値が入っていないものをGetしても状態は変わらない() {
      lru.put("KEY1", "VALUE1")
      lru.put("KEY2", "VALUE2")
      lru.get("KEY3")
      
      assertEquals(None, lru.peek("KEY3"))
      assertEquals(Some("VALUE1"), lru.peek("KEY1"))
      assertEquals(Some("VALUE2"), lru.peek("KEY2"))
  }
  
  @Test
  def キャッシュサイズを最初に3あとから2に変更して一番古いものが消えることを確認する() {
      lru = new Lru(3)
      
      lru.put("KEY1", "VALUE1")
      lru.put("KEY2", "VALUE2")
      lru.put("KEY3", "VALUE3")
      
      lru.changeSize(2)
      
      assertEquals(None, lru.peek("KEY1"))
  }
  
  @Test
  def キャッシュが満タンでなくてもサイズを小さくできることを確認する() {
      lru = new Lru(3)
      lru.put("KEY1", "VALUE1")
      lru.changeSize(2)
      assertEquals(Some("VALUE1"), lru.peek("KEY1"))
  }
  
  @Test
  def ある時点の前のキャッシュを削除する() {
      lru = new Lru(3)
      lru.put("KEY1", "VALUE1")
      lru.remove( new Date(System.currentTimeMillis() + 10000) )
      assertEquals(None, lru.peek("KEY1"))
  }
  
  
}