package lru

import java.util.Date

class LruTimer(removable: Removable) extends Thread{
  override def run() = {
      while(true) {
          Thread.sleep(10000)
          removable.remove(new Date())
      }
  }
}