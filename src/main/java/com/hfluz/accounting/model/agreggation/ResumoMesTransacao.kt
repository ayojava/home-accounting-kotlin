package com.hfluz.accounting.model.agreggation

import java.math.BigDecimal

/**
 * Created by hfluz on 08/05/17.
 */
class ResumoMesTransacao(var ano: Int?, var mes: Int?, var totalReceitas: BigDecimal?, var totalDespesas: BigDecimal?) {

    val saldo: BigDecimal?
        get() = totalReceitas?.subtract(totalDespesas)
}
