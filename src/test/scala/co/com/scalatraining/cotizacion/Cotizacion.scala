package co.com.scalatraining.cotizacion

import org.scalatest.FunSuite

case class Cotizar(periodo:String, aportante:String, dias: Int, ibc: Int)
case class Periodo(periodo: String, salario: Int)

class Cotizacion extends FunSuite{

    def limpiarHistoriaLaboral(listaCotizaciones: List[Cotizar]): List[Periodo] = {
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
        .map( x => Periodo(x._1, x._2)).toList.sortBy(x => x.periodo)
    }
}
