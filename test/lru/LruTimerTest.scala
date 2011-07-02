package lru

import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.{Test, Before}
import org.junit.Assert._
import org.hamcrest.CoreMatchers._
import java.util.Date

@RunWith(classOf[JUnit4])
class LruTimerTest {
    
    @Test
    def 定期的に削除メッセージを送信する() {
        val mock = new MockRemovable()
        val timer = new LruTimer(mock)
        
        timer.start();
        
        Thread.sleep(15000)
        
        assertTrue("呼び出されている", mock.called)
        
        
    }
    
    private class MockRemovable extends Removable {
           var called = false;
            def remove(time:Date) {
                called = true;
            }
    }
    
    

}