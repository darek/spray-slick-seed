package helper

import domain.{ Item, Todo }
import service.TodoFormats

/**
 * Test helper for Todo API
 * TodoFormats provides marshllers (check _Post)
 */
class TodoHelper extends SpecHelper("/todo") with TodoFormats {

  def addTodo(todo: Todo): Todo = _Post[Todo, Todo](prefix, todo)

  def addItem(item: Item, todoId: Int): Item = _Post[Item, Item](s"$prefix/${todoId}", item)

  def createRandomList(itemsCount: Int): Todo = {
    val todo = addTodo(Todo(None, randomString(10)))
    for {
      i <- 1 to itemsCount
    } yield {
      addItem(Item(None, None, randomString(7)), todo.id.get)
    }
    todo
  }

}

object TodoHelper extends TodoHelper