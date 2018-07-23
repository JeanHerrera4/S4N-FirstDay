package co.com.scalatraining.tuples

import org.scalatest.FunSuite

class StackSuite  extends FunSuite {

  test("Una tupla se debe poder crear"){
    val tupla = (1, 2,"3", List(1, 2, 3))
    assert(tupla._2 == 2)
    assert(tupla._4.tail.head == 2)
  }

  test("Una tupla se debe poder crear bien crack"){
    val tupla2 = (List(1, 2, 3),
      List(4, 5, 6),
      List(7, 8, 9),
      List(10, 11, 12),
      List(13, 14, 15))

    val tuplaFinal = (tupla2._1.head, tupla2._2.head, tupla2._3.head, tupla2._4.head, tupla2._5.head)
    assert((1,4,7,10,13) == tuplaFinal)

  }

  }
