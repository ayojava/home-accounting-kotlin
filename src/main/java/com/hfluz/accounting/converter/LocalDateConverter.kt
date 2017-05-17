package com.hfluz.accounting.converter

import javax.faces.component.PartialStateHolder
import javax.faces.component.UIComponent
import javax.faces.context.FacesContext
import javax.faces.convert.Converter
import javax.faces.convert.FacesConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**

 * @author hfluz
 */
@FacesConverter(forClass = LocalDate::class)
class LocalDateConverter : Converter, PartialStateHolder {

    val pattern = "dd/MM/yy"
    private val formatter = DateTimeFormatter.ofPattern(pattern)
    private var transientFlag = true
    private var initialState: Boolean = false

    override fun getAsObject(pFacesCtx: FacesContext, pComponent: UIComponent, value: String?): Any? {
        println("localDateConverter getAsObject")
        if (value == null || value.isEmpty()) {
            return null
        }
        println("value " + value)
        return LocalDate.parse(value, formatter)
    }

    override fun getAsString(pFacesCtx: FacesContext, pComponent: UIComponent, value: Any?): String {
        println("localDateConverter getAsString")
        if (value == null) {
            return ""
        }

        if (value is LocalDate) {
            val retorno = formatter.format((value as LocalDate?)!!)
            println("retorno " + retorno)
            return retorno
        }

        throw IllegalArgumentException("Expecting a LocalDateTime instance but received " + value.javaClass.name)
    }

    override fun saveState(context: FacesContext): Any {
        return ""
    }

    override fun restoreState(context: FacesContext, state: Any) {}

    override fun isTransient(): Boolean {
        return transientFlag
    }

    override fun setTransient(transientFlag: Boolean) {
        this.transientFlag = transientFlag
    }

    override fun markInitialState() {
        initialState = true
    }

    override fun initialStateMarked(): Boolean {
        return initialState
    }

    override fun clearInitialState() {
        initialState = false
    }
}
