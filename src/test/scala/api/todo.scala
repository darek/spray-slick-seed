package api

import domain.Todo
import domain.Item
import helper._
import service.TodoFormats
import spec.TestSpec
import spray.http.StatusCodes._

/**
 * TodoApiSpec test class, checks if API calls return expected values
 */
class TodoApiSpec extends TestSpec with TodoFormats {

  "TodoApi" should {

    "Return Todo list when adding list" in {
      val todoList = Todo(None, TodoHelper.randomString(10))
      Post("/todo", todoList) ~> routes ~> check {
        val todo = responseAs[Todo]

        status must be(Created)
        todo.name must beEqualTo(todoList.name)
        todo.id must not be (None)
      }
    }

    "Return Item when adding item to List" in {
      val todoList = TodoHelper.addTodo(Todo(None, TodoHelper.randomString(10)))
      val item = Item(None, None, TodoHelper.randomString(10))
      Post(s"/todo/${todoList.id.get}", item) ~> routes ~> check {
        val item = responseAs[Item]
        status must be(Created)
        item.label must beEqualTo(item.label)
        item.list must beEqualTo(todoList.id)
        item.id must not be (None)
      }
    }

    "Return Items list" in {
      val todoList = TodoHelper.createRandomList(5)
      Get(s"/todo/${todoList.id.get}") ~> routes ~> check {
        status must be(OK)
        val itemsList = responseAs[List[Item]]
        itemsList must have size (5)
      }
    }

    "Return NotFound when getting unknown list" in {
      Get(s"/todo/9999") ~> sealRoute(routes) ~> check {
        status must be(NotFound)
        responseAs[String] must contain("List does not exists")
      }
    }

    "Return BadRequest with Operation not supported message when trying to delete item" in {
      Delete("/todo/1/1") ~> sealRoute(routes) ~> check {
        status must be(NotImplemented)
        responseAs[String] must contain("Operation 'delete' is not yet supported.")
      }
    }

  }

}