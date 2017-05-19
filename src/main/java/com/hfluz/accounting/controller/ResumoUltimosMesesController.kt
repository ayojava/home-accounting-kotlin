package com.hfluz.accounting.controller

import com.hfluz.accounting.dao.TransacaoDAO
import com.hfluz.accounting.model.agreggation.ResumoMesTransacao
import com.hfluz.accounting.model.enumeration.TipoTransacao
import com.hfluz.accounting.util.somarTransacoes
import com.hfluz.accounting.util.getMonthAndYear
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
        val (mesAtual,anoAtual) = localDate.getMonthAndYear()
        localDate = localDate.minusMonths(6)
        while (localDate.year < anoAtual || (localDate.monthValue <= mesAtual && localDate.year == anoAtual)) {
            val transacoes = transacaoDAO.buscar(localDate.monthValue, localDate.year)
            resumoMesTransacoes.add(
                    ResumoMesTransacao(localDate.year, localDate.monthValue,
                            somarTransacoes(transacoes,TipoTransacao.RECEITA),
                            somarTransacoes(transacoes,TipoTransacao.DESPESA))
            )
            localDate = localDate.plusMonths(1)
        }

    }
}
