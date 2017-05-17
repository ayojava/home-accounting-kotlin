package com.hfluz.accounting.controller.charts

import com.hfluz.accounting.dao.TransacaoDAO
import org.primefaces.model.chart.AxisType
import org.primefaces.model.chart.BarChartModel
import org.primefaces.model.chart.ChartSeries
import java.math.BigDecimal
import java.math.RoundingMode
import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by hfluz on 04/05/17.
 */
@Named
@RequestScoped
class DistribuicaoDespesasEmDiasController(var barChartModel: BarChartModel = BarChartModel()) {
    @Inject
    private lateinit var transacaoDAO: TransacaoDAO

    fun init() {
        val despesasPorDia = transacaoDAO.buscarDespesasPorDiaDentroDoMes()
        barChartModel.isAnimate = true
        barChartModel.title = "Receitas e Despesas nos últimos 6 meses"
        barChartModel.axes[AxisType.Y]?.label = "Valor (R$)"


        val series = ChartSeries()
        for (ct in despesasPorDia) {
            series.set(ct.dia, ct.valorTotal?.divide(BigDecimal.valueOf(3), RoundingMode.HALF_UP))
        }
        barChartModel.addSeries(series)
    }
}
