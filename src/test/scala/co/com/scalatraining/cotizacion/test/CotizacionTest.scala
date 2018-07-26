package co.com.scalatraining.cotizacion.test

import co.com.scalatraining.cotizacion.Cotizacion
import org.scalatest.FunSuite

class CotizacionTest extends FunSuite {

  test("Limpiar el historial"){
    val entrada: List[Cotizar] =  List(
      new Cotizar(),
      new CotizacionTest("2018/07", "S4N", 10, 1000000),
      new CotizacionTest("2018/08", "S4N", 10, 2000000),
      new CotizacionTest("2018/09", "S4N", 10, 4000000),
      new CotizacionTest("2018/10", "S4K", 0, 1000000),
      new CotizacionTest("2018/07", "S4K", 10, 0))
  }

  val v: Cotizar = new Cotizar()

}
