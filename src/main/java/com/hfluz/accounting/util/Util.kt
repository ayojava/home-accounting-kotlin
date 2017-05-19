package com.hfluz.accounting.util

import com.hfluz.accounting.model.Transacao
import com.hfluz.accounting.model.enumeration.TipoTransacao
import java.math.BigDecimal
import java.time.LocalDate

/**
 * Created by hfluz on 18/05/17.
 */
fun somarTransacoes(transacoes: List<Transacao>?, tipoTransacao: TipoTransacao) =
        transacoes?.filter { tipoTransacao == it.tipoTransacao }
                ?.map { it.valor }
                ?.fold(BigDecimal.ZERO) { a, b -> a.add(b) } ?: BigDecimal.ZERO

fun LocalDate.getMonthAndYear(): Pair<Int, Int> = Pair(monthValue, year)
