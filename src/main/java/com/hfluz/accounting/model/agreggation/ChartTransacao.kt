package com.hfluz.accounting.model.agreggation

import com.hfluz.accounting.model.enumeration.CategoriaTransacao
import com.hfluz.accounting.model.enumeration.TipoTransacao

import java.math.BigDecimal

/**
 * Created by hfluz on 04/05/17.
 */
class ChartTransacao {
    var ano: Int? = null
    var mes: Int? = null
    var dia: Int? = null
    var categoria: CategoriaTransacao? = null
    var tipoTransacao: TipoTransacao? = null
    var valorTotal: BigDecimal? = null
}
