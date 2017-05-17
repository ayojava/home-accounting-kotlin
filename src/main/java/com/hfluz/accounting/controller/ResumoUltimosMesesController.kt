package com.hfluz.accounting.controller

import com.hfluz.accounting.dao.TransacaoDAO
import com.hfluz.accounting.model.agreggation.ResumoMesTransacao
import com.hfluz.accounting.model.enumeration.TipoTransacao
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*
import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by hfluz on 08/05/17.
 */
@Named
@RequestScoped
class ResumoUltimosMesesController(var resumoMesTransacoes: MutableList<ResumoMesTransacao> = ArrayList<ResumoMesTransacao>()) {
    @Inject
    private lateinit var transacaoDAO: TransacaoDAO

    fun init() {
        var localDate = LocalDate.now()
        val anoAtual = localDate.year
        val mesAtual = localDate.monthValue
        localDate = localDate.minusMonths(6)
        while (localDate.year < anoAtual || (localDate.monthValue <= mesAtual && localDate.year == anoAtual)) {
            val transacoes = transacaoDAO.buscar(localDate.monthValue, localDate.year)
            resumoMesTransacoes.add(
                    ResumoMesTransacao(localDate.year, localDate.monthValue,
                            transacoes.filter { TipoTransacao.RECEITA == it.tipoTransacao }.map { it.valor }.fold(BigDecimal.ZERO) { a, b -> a?.add(b) },
                            transacoes.filter { TipoTransacao.DESPESA == it.tipoTransacao }.map { it.valor }.fold(BigDecimal.ZERO) { a, b -> a?.add(b) })
            )
            localDate = localDate.plusMonths(1)
        }

    }
}
