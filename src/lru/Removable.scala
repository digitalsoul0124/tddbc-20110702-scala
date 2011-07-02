package lru
import java.util.Date

trait Removable {
  def remove(time: Date)
}