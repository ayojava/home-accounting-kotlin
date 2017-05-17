package com.hfluz.accounting.model

import com.hfluz.accounting.model.enumeration.CategoriaTransacao
import com.hfluz.accounting.model.enumeration.TipoPagamento
import com.hfluz.accounting.model.enumeration.TipoTransacao
import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * Created by hfluz on 03/05/17.
 */
@Entity
@Table(name = "transaction")
data class Transacao private constructor(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        var id: Long,
        @NotNull
        @Column(name = "tipo_transacao")
        @Enumerated(EnumType.STRING)
        var tipoTransacao: TipoTransacao? = null,
        @Column(name = "tipo_pagamento")
        @Enumerated(EnumType.STRING)
        var tipoPagamento: TipoPagamento? = null,
        @Column(name = "categoria")
        @Enumerated(EnumType.STRING)
        var categoria: CategoriaTransacao? = null,
        @Basic
        @Column(name = "date")
        var date: LocalDate? = null,
        @Basic
        @Column(name = "value", precision = 10, scale = 2)
        var valor: BigDecimal? = null,
        @Basic
        @Column(name = "location")
        var estabelecimento: String? = null,
        @Basic
        @Column(name = "description")
        var descricao: String? = null,
        @Column(name = "installment")
        var parcela: Short? = null,
        @Basic
        @Column(name = "creation_date")
        @Temporal(TemporalType.TIMESTAMP)
        var creationDate: Date? = null,
        @Column(name = "id_first_installment")
        var idPrimeiraParcela: Long? = null
) : Serializable {

    constructor() : this(0, null, null, CategoriaTransacao.NENHUMA, LocalDate.now(), null, null, null, null, null, null)

    @PrePersist
    fun setarCreationDate() {
        creationDate = Date()
    }
}
