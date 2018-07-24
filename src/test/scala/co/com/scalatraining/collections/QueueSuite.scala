package co.com.scalatraining.collections

import java.util

import org.scalatest.FunSuite

import scala.collection.immutable.Queue

class QueueSuite extends FunSuite {

  test("Crear cola vacía"){
    var cola1:Queue[Int] = Queue()
    val cola2:Queue[Int] = Queue.empty
    assert(cola1 == cola2)
  }

  test("Adicción de un elemento a la cola"){
    val cola1 = Queue.empty[Int]
    val cola2 = cola1.enqueue(5)
    assert(cola1 == Queue.empty[Int])
    assert(cola2 == Queue(5))
  }

  test("A una cola se le debe poder eliminar elementos con drop") {
    val cola = Queue(1, 2, 3, 4)
    val dropped = cola.drop(2)

    assertResult(Queue(3, 4)) {
      dropped
    }
  }

  test("A una cola se le pueden descartar elementos en dirección determinada (right)") {
    val cola = Queue(1, 2, 3, 4)
    assertResult(Queue(1, 2)) {
      cola.dropRight(2)
    }
  }

  test("Qué pasa si hacemos top a una cola y cola vacía()") {
    val cola = Queue(2,4,6)
    assertResult(2){
      cola.head
    }
    val cola2 = Queue()
    assertThrows[NoSuchElementException] {
      cola2.head
    }
  }

  test("Se debe poder obtener todos los elementos de una cola sin el primer elemento") {
    val cola = Queue(1, 2, 3, 4)
    assertResult(Queue(2, 3, 4)) {
      cola.tail
    }
  }

  test("Que pasa si hacemos tail a un Queue()") {
    val cola = Queue()
    assertThrows[NoSuchElementException] {
      val res =  cola.tail
    }
  }

  test("head en una cola") {
    val cola = Queue(1, 2, 3, 4)
    assertResult(1) {
      cola.head
    }
  }

  test("Borrar elementos con dequeue"){
    val cola = Queue[Int](1,2,3,4)
    assertResult(1,2,3){
      cola.dequeue
    }
  }


}
