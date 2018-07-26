package co.com.scalatraining.cotizacion

import co.com.scalatraining.cotizacion.test.CotizacionTest
import org.scalatest.FunSuite

class Cotizacion extends FunSuite{

  case class Cotizar(periodo:String, aportante:String, dias: Int, ibc: Int)
  case class Periodo(periodo: String, salario: Int)

    def limpiarHistoriaLaboral(listaCotizaciones: List[Cotizar]):Map[String, Int] = {
      listaCotizaciones
      .filter(x => x.dias != 0 && x.ibc != 0)
        .map(x => Cotizar(x.periodo, x.aportante, x.dias, x.ibc * 30 / x.dias))
        .distinct
        .groupBy(x => (x.periodo, x.aportante))
        .map(x => x._1 -> x._2.foldLeft(0) { (acc, item) =>
        if(acc < item.ibc){
          item.ibc
        }else{
          acc
        }
      })
      .groupBy(x => x._1._1)
      .map(x => x._1 -> x._2
      .foldLeft(0){(acc, item) => acc + item._2})
    }

  test("Prueba limpiar Historial"){

    //val Cotizacion: Cotizacion = new Cotizar("2018/07", "S4N", 10, 1000000)
  }

}
