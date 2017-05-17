package com.hfluz.accounting.controller

import com.hfluz.accounting.dao.TransacaoDAO
import com.hfluz.accounting.model.Transacao
import com.hfluz.accounting.model.enumeration.CategoriaTransacao
import com.hfluz.accounting.model.enumeration.TipoPagamento
import com.hfluz.accounting.model.enumeration.TipoTransacao
import org.omnifaces.util.Messages
import org.primefaces.context.RequestContext

import javax.faces.view.ViewScoped
import javax.inject.Inject
import javax.inject.Named
import javax.validation.constraints.NotNull
import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDate

/**
 * Created by hfluz on 03/05/17.
 */
@Named
@ViewScoped
class TransacaoController(val now: LocalDate = LocalDate.now(),
                          @NotNull
                          var mes: Int = now.monthValue,
                          @NotNull
                          var ano: Int = now.year,
                          var transacao: Transacao = Transacao()

) : Serializable {

    var transacoes: List<Transacao>? = null
        private set

    /**
     * Utilizada se o tipo de pagamento for cartão de crédito.
     */
    var quantidadeParcelas = 1

    @Inject
    private lateinit var transacaoDAO: TransacaoDAO

    fun init() {
        carregarTransacoes()
    }

    fun resetarTransacao() {
        transacao = Transacao()
    }

    fun carregarTransacoes() {
        transacoes = transacaoDAO.buscar(mes, ano)
    }

    fun salvar() {
        if (TipoTransacao.RECEITA === transacao.tipoTransacao) {
            transacao.categoria = CategoriaTransacao.NENHUMA
            transacao.tipoPagamento = TipoPagamento.NENHUM
        }
        if (transacao.id == 0L && TipoPagamento.CARTAO_CREDITO == transacao.tipoPagamento
                && quantidadeParcelas > 1) {
            salvarTransacoesParcelamento()
        } else {
            transacaoDAO.salvar(transacao)
        }
        carregarTransacoes()
        RequestContext.getCurrentInstance().execute("PF('transacaoDlg').hide()")
        Messages.addGlobalInfo("Transação " + transacao.descricao + " salva com sucesso.")
    }

    private fun salvarTransacoesParcelamento() {
        var idPrimeiraParcela: Long? = null
        for (parcela in 1..quantidadeParcelas) {
            if (idPrimeiraParcela == null) {
                val transacaoSalva = transacaoDAO.salvar(transacao)
                idPrimeiraParcela = transacaoSalva.id
                transacaoSalva.idPrimeiraParcela = idPrimeiraParcela
                transacaoSalva.parcela = 1.toShort()
                transacaoDAO.salvar(transacaoSalva)
            } else {
                transacao.parcela = parcela as? Short
                transacao.idPrimeiraParcela = idPrimeiraParcela
                transacaoDAO.salvar(transacao)
            }
            transacao.date = transacao.date?.plusMonths(1)
        }
        quantidadeParcelas = 1
        resetarTransacao()
    }

    fun excluir(transacao: Transacao) {
        transacaoDAO.excluir(transacao)
        carregarTransacoes()
    }

    val valorTotalReceitas: BigDecimal
        get() = transacoes?.filter{ TipoTransacao.RECEITA == it.tipoTransacao }
                ?.map{ it.valor }
                ?.reduce{ a, b -> a?.add(b) } ?: BigDecimal.ZERO

    val valorTotalDespesas: BigDecimal
        get() = transacoes?.filter{ TipoTransacao.DESPESA == it.tipoTransacao }
                ?.map{ it.valor }
                ?.reduce{ a, b -> a?.add(b) } ?: BigDecimal.ZERO
}
