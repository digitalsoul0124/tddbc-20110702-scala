package lru

import java.util.Date
import scala.collection.mutable._

class Lru(var cacheSize:Int = 2) extends Removable {
    
    val map = new HashMap[String,String] 
    var keystack = new KeyStack()
    
    def put(key: String, value: String) = {
        keystack.touch(key)
        map += key -> value
    }
    
    def get(key: String): Option[String] = {
        if(!map.contains(key)) return None
        keystack.touch(key)
        map.get(key)
    }
    
    def peek(key: String): Option[String] = map.get(key)
    
    def changeSize(size: Int) = {
        keystack.changeSize(size)
    }
   def remove(time:Date) = {
       keystack.removeBefore(time)
   }
   
    
    class KeyStack {
        
        var stack = ListBuffer[Tuple2[String,Date]]()

        def touch(key: String) = {
            stack = stack.filter( _._1 != key ) + ( key -> new Date )
            shrinkIfNeeded()
        }
        
        def changeSize(size: Int) = {
            cacheSize = size
            shrinkIfNeeded()
        }
        
        private def shrinkIfNeeded() = {
            if (stack.size > cacheSize) {
                for(i <- stack.dropRight(cacheSize)){
                    map.remove(i._1)           
                }
                stack = stack.takeRight(cacheSize)          
            }
        }
        
        def removeBefore(time:Date) = {
            for( i <- stack.filter( ! _._2.after(time) ) ){
                map.remove(i._1)
            }
            stack = stack.filter( _._2.after(time) )
        }
    }
}
