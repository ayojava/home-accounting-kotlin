package com.hfluz.accounting.dao

import com.hfluz.accounting.model.Transacao
import com.hfluz.accounting.model.agreggation.ChartTransacao
import com.hfluz.accounting.model.enumeration.TipoTransacao
import org.hibernate.Session
import org.hibernate.transform.Transformers
import java.time.LocalDate
import javax.ejb.Stateless
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

/**
 * Created by hfluz on 03/05/17.
 */
@Stateless
open class TransacaoDAO {
    @PersistenceContext(unitName = "home-accounting-pu")
    protected lateinit var entityManager: EntityManager

    open fun buscarPorId(id: Long): Transacao {
        return entityManager.createQuery("select t from Transacao t where t.id = :id")
                .setParameter("id", id)
                .resultList.stream().findAny().orElse(null) as Transacao
    }

    open fun buscar(ano: Int?, mes: Int?): List<Transacao> {
        return entityManager.createQuery(
                        "select t from Transacao t where year(t.date) = :ano and month(t.date) = :mes " + "order by t.tipoTransacao desc, t.date")
                .setParameter("mes", mes)
                .setParameter("ano", ano)
                .resultList as List<Transacao>
    }

    open fun buscar(ano: Int?): List<Transacao> {
        return entityManager.createQuery(
                "select t from Transacao t where year(t.date) = :ano " + "order by t.tipoTransacao desc, t.date")
                .setParameter("ano", ano)
                .resultList as List<Transacao>
    }

    open fun buscarDespesasPorAnoMesECategoria(): List<ChartTransacao> {
        val seisMesesAtras = LocalDate.now().withDayOfMonth(1).minusMonths(6)
        val proximoMes = LocalDate.now().plusMonths(1)
        return entityManager.unwrap(Session::class.java).createQuery(
                "select year(t.date) as ano, month(t.date) as mes,t.categoria as categoria,sum(t.valor) as valorTotal " +
                        "from Transacao t " +
                        "where year(t.date) <= year(:proximoMes) and month(t.date) <= month(:proximoMes) " +
                        "and t.date >= :seisMesesAtras " +
                        "group by year(t.date),month(t.date),t.categoria " +
                        "order by year(t.date),month(t.date),t.categoria")
                .setResultTransformer(Transformers.aliasToBean(ChartTransacao::class.java))
                .setParameter("seisMesesAtras", seisMesesAtras)
                .setParameter("proximoMes", proximoMes)
                .list() as List<ChartTransacao>
    }

    open fun buscarDespesasPorDiaDentroDoMes(): List<ChartTransacao> {
        val quatroMesesAtras = LocalDate.now().withDayOfMonth(1).minusMonths(5)
        return entityManager.unwrap(Session::class.java).createQuery(
                "select day(t.date) as dia,sum(t.valor) as valorTotal " +
                        "from Transacao t " +
                        "where (year(t.date) < year(current_date) or (year(t.date) = year(current_date) and month(t.date) < month(current_date))) " +
                        "and t.date >= :quatroMesesAtras and t.tipoTransacao = :tipoTransacao " +
                        "group by day(t.date) " +
                        "order by day(t.date)")
                .setResultTransformer(Transformers.aliasToBean(ChartTransacao::class.java))
                .setParameter("quatroMesesAtras", quatroMesesAtras)
                .setParameter("tipoTransacao", TipoTransacao.DESPESA)
                .list() as List<ChartTransacao>
    }

    open fun buscarTransacoesPorAnoMesETipoTransacao(): List<ChartTransacao> {
        val seisMesesAtras = LocalDate.now().withDayOfMonth(1).minusMonths(6)
        return entityManager.unwrap(Session::class.java).createQuery(
                "select year(t.date) as ano, month(t.date) as mes,t.tipoTransacao as tipoTransacao,sum(t.valor) as valorTotal " +
                        "from Transacao t " +
                        "where (year(t.date) < year(current_date) or (year(t.date) = year(current_date) and month(t.date) <= (month(current_date)+1))) " +
                        "and t.date >= :seisMesesAtras " +
                        "group by year(t.date),month(t.date),t.tipoTransacao " +
                        "order by year(t.date),month(t.date),t.tipoTransacao")
                .setResultTransformer(Transformers.aliasToBean(ChartTransacao::class.java))
                .setParameter("seisMesesAtras", seisMesesAtras)
                .list() as List<ChartTransacao>
    }

    open fun salvar(transacao: Transacao): Transacao {
        return entityManager.merge(transacao)
    }

    open fun excluir(transacao: Transacao) {
        val t = buscarPorId(transacao.id)
        entityManager.remove(t)
    }
}
