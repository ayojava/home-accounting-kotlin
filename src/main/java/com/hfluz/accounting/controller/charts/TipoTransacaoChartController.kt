package com.hfluz.accounting.controller.charts

import com.hfluz.accounting.dao.TransacaoDAO
import com.hfluz.accounting.model.enumeration.TipoTransacao
import org.primefaces.model.chart.AxisType
import org.primefaces.model.chart.BarChartModel
import org.primefaces.model.chart.ChartSeries
import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by hfluz on 04/05/17.
 */
@Named
@RequestScoped
class TipoTransacaoChartController {
    @Inject
    private lateinit var transacaoDAO: TransacaoDAO
    var barChartModel: BarChartModel = BarChartModel()

    fun init() {
        barChartModel.isAnimate = true
        barChartModel.title = "Receitas e Despesas nos últimos 6 meses"
        barChartModel.axes[AxisType.Y]?.label = "Valor (R$)"

        barChartModel.legendPosition = "w"


        val chartTransacoes = transacaoDAO.buscarTransacoesPorAnoMesETipoTransacao()
        for (ct in TipoTransacao.values().asList().asReversed()) {
            val totalMesAnoTipoTransacao = chartTransacoes.filter { ct == it.tipoTransacao }
            if (totalMesAnoTipoTransacao.isNotEmpty()) {
                val series = ChartSeries()
                series.label = ct.name
                for (obj in totalMesAnoTipoTransacao) {
                    series.set(obj.ano.toString() + "-" + (if (obj.mes ?: 0 < 10 ) "0" else "") + obj.mes, obj.valorTotal)
                }
                barChartModel.addSeries(series)
            }
        }
    }
}
