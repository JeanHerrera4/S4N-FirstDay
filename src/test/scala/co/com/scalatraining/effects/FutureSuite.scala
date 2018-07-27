package co.com.scalatraining.effects

import java.util.Random
import java.util.concurrent.Executors

import org.scalatest.FunSuite

import scala.collection.immutable.{IndexedSeq, Seq}
import scala.language.postfixOps
import scala.util.{Failure, Success}
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class FutureSuite extends FunSuite {

  test("Un futuro se puede crear") {

    val hiloPpal = Thread.currentThread().getName

    var hiloFuture = ""

    println(s"Test 1 - El hilo ppal es ${hiloPpal}")

    val saludo: Future[String] = Future {
      hiloFuture = Thread.currentThread().getName
      println(s"Test 1 - El hilo del future es ${hiloFuture}")

      Thread.sleep(500)
      "Hola"
    }
    val resultado: String = Await.result(saludo, 10 seconds)
    assert(resultado == "Hola")
    assert(hiloPpal != hiloFuture)
  }

  test("map en Future") {


    val t1 = Thread.currentThread().getName
    println(s"Test 2 - El hilo del ppal es ${t1}")


    val saludo = Future {
      val t2 = Thread.currentThread().getName
      println(s"Test 2 - El hilo del future es ${t2}")

      Thread.sleep(500)
      "Hola"
    }

    Thread.sleep(5000)

    val saludo2 = Future{
      println(s"Test 2 - Hilo normal ${Thread.currentThread().getName}")
    }

    val saludoCompleto = saludo.map(mensaje => {
      val t3 = Thread.currentThread().getName
      println(s"Test 2 - El hilo del map es ${t3}")

      mensaje + " muchachos"
    })


    val resultado = Await.result(saludo, 10 seconds)
    assert(resultado == "Hola")
  }

  test("Se debe poder encadenar Future con for-comp") {
    val f1 = Future {
      Thread.sleep(200)
      1
    }

    val f2 = Future {
      Thread.sleep(200)
      2
    }

    val f3: Future[Int] = for {
      res1 <- f1
      res2 <- f2
    } yield res1 + res2

    val res = Await.result(f3, 10 seconds)

    assert(res == 3)

  }

  test("Se debe poder encadenar Future con for-comp x2") {
    val f1 = Future {
      Thread.sleep(200)
      1
    }

    val f2 = Future {
      Thread.sleep(200)
      2/0
    }

    val f3: Future[Int] = for {
      res1 <- f1
      res2 <- f2.recover{case e: Exception => 2}
    } yield res1 + res2

    val res = Await.result(f3, 10 seconds)

    println(s"Test failure: $res")
    assert(res == 3)

  }

  test("Se debe poder manejar el error de un Future de forma imperativa") {
    val divisionCero = Future {
      Thread.sleep(100)
      10 / 0
    }
    var error = false

    /*val r: Unit = divisionCero.onFailure {
      case e: Exception => error = true
    }*/

    val k: Unit = divisionCero.recover{case e: Exception => error = true}

    Thread.sleep(1000)

    //assert(error == true)
    assert(error == true)
  }

  test("Se debe poder manejar el exito de un Future de forma imperativa") {

    val division = Future {
      5/0
    }

    var r = 0

    /*val f: Unit = division.onComplete {
      case Success(res) => r = res
      case Failure(e) => r = 1
    }*/

    val k: Future[Int] = division.map((x) => x + 1).recover{case e: Exception => 1}

    Thread.sleep(150)

    //val res = Await.result(division, 10 seconds)
    val re = Await.result(k, 10 second)

    assert(re == 1)
  }

  test("Se debe poder manejar el error de un Future de forma funcional sincronicamente") {

    var threadName1 = ""
    var threadName2 = ""

    val divisionPorCero = Future {
      threadName1 = Thread.currentThread().getName
      Thread.sleep(100)
      10 / 0
    }.recover {
      case e: ArithmeticException => {
        threadName2 = Thread.currentThread().getName
        "No es posible dividir por cero"
      }
    }

    val res = Await.result(divisionPorCero, 10 seconds)

    assert(threadName1 == threadName2)
    assert(res == "No es posible dividir por cero")

  }

  test("Se debe poder manejar el error de un Future de forma funcional asincronamente") {

    var threadName1 = ""
    var threadName2 = ""

    implicit val ecParaPrimerHilo = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(1))

    val f1 = Future {
      threadName1 = Thread.currentThread().getName
      2/0
    }(ecParaPrimerHilo)
    .recoverWith {
      case e: ArithmeticException => {

        implicit val ecParaRecuperacion = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(1))

        Future{
          threadName2 = Thread.currentThread().getName
          1
        }(ecParaRecuperacion)
      }
    }

    val res = Await.result(f1, 10 seconds)

    println(s"Test en recoverWith thread del fallo: $threadName1")
    println(s"Test en recoverWith thread de recuperacion: $threadName2")

    assert(threadName1 != threadName2)
    assert(res==1)
  }

  test("Los future **iniciados** fuera de un for-comp deben iniciar al mismo tiempo") {

    val timeForf1 = 100
    val timeForf2 = 200
    val timeForf3 = 100

    val additionalTime = 50D

    val estimatedElapsed = (Math.max(Math.max(timeForf1, timeForf2), timeForf3) + additionalTime)/1000

    val f1 = Future {
      Thread.sleep(timeForf1)
      1
    }
    val f2 = Future {
      Thread.sleep(timeForf2)
      2
    }
    val f3 = Future {
      Thread.sleep(timeForf3)
      3
    }

    val t1: Long = System.nanoTime()

    val resultado = for {
      a <- f1
      b <- f2
      c <- f3
    } yield (a+b+c)

    val res = Await.result(resultado, 10 seconds)
    val elapsed = (System.nanoTime() - t1) / 1.0E09

    println(s"Future **iniciados** fuera del for-comp estimado: $estimatedElapsed real: $elapsed")
    assert(elapsed <= estimatedElapsed)
    assert(res == 6)

  }

  test("Los future **definidos** fuera de un for-comp deben iniciar secuencialmente") {

    val timeForf1 = 100
    val timeForf2 = 300
    val timeForf3 = 500

    val estimatedElapsed:Double = (timeForf1 + timeForf2 + timeForf3)/1000

    def f1 = Future {
      Thread.sleep(timeForf1)
      1
    }
    def f2 = Future {
      Thread.sleep(timeForf2)
      2
    }
    def f3 = Future {
      Thread.sleep(timeForf3)
      3
    }

    val t1 = System.nanoTime()

    val resultado = for {
      a <- f1
      b <- f2
      c <- f3
    } yield (a+b+c)

    val res = Await.result(resultado, 10 seconds)
    val elapsed = (System.nanoTime() - t1) / 1.0E09

    println(s"Future **definidos** fuera del for-comp estimado: $estimatedElapsed real: $elapsed")

    assert(elapsed >= estimatedElapsed)
    assert(res == 6)

  }

  test("Los future declarados dentro de un for-comp deben iniciar secuencialmente") {

    val t1 = System.nanoTime()

    val timeForf1 = 100
    val timeForf2 = 100
    val timeForf3 = 100

    val estimatedElapsed = (timeForf1 + timeForf2 + timeForf3)/1000

    val resultado = for {
      a <- Future {
        Thread.sleep(timeForf1)
        1
      }
      b <- Future {
        Thread.sleep(timeForf2)
        2
      }
      c <- Future {
        Thread.sleep(timeForf3)
        3
      }
    } yield (a+b+c)

    val res = Await.result(resultado, 10 seconds)
    val elapsed = (System.nanoTime() - t1) / 1.0E09

    assert(elapsed >= estimatedElapsed)
    assert(res == 6)
  }

  test("Future.sequence"){

    val listOfFutures: List[Future[Int]] = Range(1, 11).map(Future.successful(_)).toList

    val resSequence: Future[List[Int]] = Future.sequence {
      listOfFutures
    }

    val resFuture = resSequence.map(l => l.sum/l.size)

    val res = Await.result(resFuture, 10 seconds)

    assert(res ==  Range(1,11).sum/Range(1,11).size)

  }

  test("Future.traverse"){
    def foo(i:List[Int]):Future[Int]=Future.successful(i.sum/i.size)
    val resFuture = Future.traverse(Range(1,11).map(Future.successful(_))){
      x => x
    }.map(l => l.sum/l.size)

    val res = Await.result(resFuture, 10 seconds)

    assert(res ==  Range(1,11).sum/Range(1,11).size)

  }

  test("Ejercicio Servicio de Clima") {


      object clima {

        def obtenerClima(): Future[String] = {
          implicit val ecParaClima = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(5))
          val a: Future[String] = Future {
            Thread.sleep(500)
            s"El clima está en 20 grado Farenheint ${Thread.currentThread().getName}"
          }(ecParaClima)
          a
        }
      }

      object guardar {

        implicit val ecParaGuardar = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(1))
        def guardar(s: String): Future[String] = {
          val a: Future[String] = Future {
            Thread.sleep(500)
            s"Se guarda el clima ${Thread.currentThread().getName}"
          }(ecParaGuardar)
          a
        }
      }

    /*object Cliente{
      def ejecutar():Future[Boolean] = {
        clima.obtenerClima().flatMap(x => guardar.guardar(x))
      }
    }*/

      Range(1, 20).map {
        x => x
          //val t = clima.obtenerClima()
          //val r = guardar.guardar(clima.obtenerClima())
         // val f = Cliente.ejecutar()
          //val q = Await.result(t, 10 seconds)
          //val p = Await.result(f, 10 seconds)
      }
    }

  test("Ejercicio repositorio Git"){

    case class Repositorio(dueno: String, nombre:String, lineas: Int, lenguaje: String)
    case class User(nombre: String, repositorio: String)
    case class UserRepos(lenguaje: String, repeticiones: Int)
    case class userComplete()

    val usersRepositories = List(User("Jean","Repo1"), User("Pedro","Repo2"), User("Jean","Repo3"), User("Pedro","Repo4"))
    val repositories = List(Repositorio("Jean","Repo1", 1000, "Java"), Repositorio("Pedro", "Repo2", 1000, "Scala"),
                            Repositorio("Pedro", "Repo3", 500, "Python"), Repositorio("Jean", "Repo4", 2000, "Java"))

    object Consulta{

      def consultaRepositoriosUser(listaDeUsuarios: List[User], name: String): Future[List[String]] =
      Future{
         Thread.sleep(200)
         listaDeUsuarios
           .filter(x => x.nombre.equalsIgnoreCase(name)).map(x => x.repositorio)
      }

      def lenguajeRepositorios(listaDeRepositorios: List[Repositorio]): Future[Map[String, Int]] =
      Future{
        Thread.sleep(200)
        listaDeRepositorios
          .groupBy(x => x.lenguaje).mapValues(x => x.length).map(x => x._1 -> x._2)
      }

      def userRepositories(listaDeRepositorios: List[Repositorio], name: String): Future[List[String]] =
        Future{
          Thread.sleep(200)
          listaDeRepositorios
            .filter(x => x.dueno.equalsIgnoreCase(name)).map(x => x.lenguaje)
        }
    }

    val a = Consulta.consultaRepositoriosUser(usersRepositories, "Pedro")
    println(a)
    val b = Consulta.lenguajeRepositorios(repositories)
    println(b)
    val c = Consulta.userRepositories(repositories, "Jean")
    println(c)

    val res = Await.result(a, 10 seconds)
    val res2 = Await.result(b, 10 seconds)
    val res3 = Await.result(c, 10 seconds)

    println(res)
    println(res2)
    println(res3)
  }



}