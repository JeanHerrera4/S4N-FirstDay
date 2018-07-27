package co.com.scalatraining.cotizacion.test

import co.com.scalatraining.cotizacion.{Cotizacion, Cotizar, Periodo}
import org.scalatest.FunSuite

class CotizacionTest extends FunSuite {

  test("Prueba limpiar Historial"){

    val v1 =  new Cotizar("2018/07", "S4N", 10, 1000000)
    val v2 =  new Cotizar("2018/07", "S4N", 30, 2000000)
    val v3 =  new Cotizar("2018/08", "PSL", 15, 1000000)
    val v4 =  new Cotizar("2018/08", "S4N", 10, 1000000)
    val v5 =  new Cotizar("2018/08", "S4N", 5, 500000)
    val v6 =  new Cotizar("2018/09", "GLOBANT", 10, 1000000)
    val v7 =  new Cotizar("2018/09", "S4N", 10, 1000000)
    val v8 =  new Cotizar("2018/10", "S4N", 10, 1000000)
    val v9 =  new Cotizar("2018/10", "S4N", 20, 1000000)
    val v10 = new Cotizar("2018/11", "S4N", 30, 6000000)

    val listaCotizaciones = List(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10)

    val cotizacion = new Cotizacion

    assert(cotizacion.limpiarHistoriaLaboral(listaCotizaciones) == List(Periodo("2018/07",3000000), Periodo("2018/08",5000000), Periodo("2018/09",6000000), Periodo("2018/10",3000000), Periodo("2018/11",6000000)))
  }

}
