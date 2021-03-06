package com.hfluz.accounting.controller.charts

import com.hfluz.accounting.dao.TransacaoDAO
import com.hfluz.accounting.model.enumeration.CategoriaTransacao
import org.primefaces.model.chart.AxisType
import org.primefaces.model.chart.ChartSeries
import org.primefaces.model.chart.DateAxis
import org.primefaces.model.chart.LineChartModel
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*
import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by hfluz on 04/05/17.
 */
@Named
@RequestScoped
class CategoriaTransacaoChartController(val categoriaCharts: MutableList<LineChartModel> = ArrayList<LineChartModel>()) {
    @Inject
    private lateinit var transacaoDAO: TransacaoDAO

    fun init() {
        val cores = createStack()

        val despesasPorAnoMesECategoria = transacaoDAO.buscarDespesasPorAnoMesECategoria()
        for (ct in CategoriaTransacao.values().filter { CategoriaTransacao.NENHUMA != it }) {
            val chart = createChart(ct)
            chart.seriesColors = cores.pop()
            val totalMesAnoCategoria = despesasPorAnoMesECategoria.filter { ct == it.categoria }
            val menorAno = totalMesAnoCategoria.minBy { it.ano ?: 0 }?.ano ?: LocalDate.now().year
            val menorMes = totalMesAnoCategoria.minBy { it.mes ?: 0 }?.mes ?: LocalDate.now().monthValue
            if (!totalMesAnoCategoria.isEmpty()) {
                val series = ChartSeries()
                series.label = ct.name
                var menorData = LocalDate.of(menorAno,menorMes,1)
                val dataLimite = LocalDate.now().plusMonths(1).withDayOfMonth(2)
                while(menorData.isBefore(dataLimite)){
                    val listaFiltrada = totalMesAnoCategoria.filter { it.ano == menorData.year && it.mes == menorData.monthValue }
                    val valorTotal = if(listaFiltrada.count() == 0) BigDecimal.ZERO else listaFiltrada.map { it.valorTotal }.first()
//                for (obj in totalMesAnoCategoria) {
                    series.set(menorData.year.toString() + "-" + menorData.monthValue + "-01", valorTotal)
                    menorData = menorData.plusMonths(1)
                }
                chart.addSeries(series)
                categoriaCharts.add(chart)
            }
        }
    }

    private fun createStack(): Stack<String> {
        val cores = Stack<String>()
        cores.push("8CBF36")
        cores.push("C33C54")
        cores.push("2D936C")
        cores.push("F7CE5B")
        cores.push("391463")
        cores.push("E53D00")
        cores.push("FFE900")
        cores.push("21A0A0")
        cores.push("474056")
        return cores
    }

    private fun createChart(categoriaTransacao: CategoriaTransacao): LineChartModel {
        val lcm = LineChartModel()
        lcm.isAnimate = true
        lcm.title = "Despesas da categoria $categoriaTransacao nos últimos 6 meses"
        val axis = DateAxis("Mês/Ano")
        axis.tickInterval = "4 weeks"
        axis.tickAngle = -50
        axis.tickFormat = "%b, %y"
        lcm.axes.put(AxisType.X, axis)
        lcm.axes[AxisType.Y]?.label = "Valor das despesas (R$)"
//        lcm.isShowPointLabels = false
//        lcm.legendPosition = "w"
        return lcm
    }
}
