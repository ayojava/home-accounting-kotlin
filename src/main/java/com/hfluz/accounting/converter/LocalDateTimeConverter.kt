package com.hfluz.accounting.converter

import javax.faces.component.PartialStateHolder
import javax.faces.component.UIComponent
import javax.faces.context.FacesContext
import javax.faces.convert.Converter
import javax.faces.convert.FacesConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**

 * @author hfluz
 */
@FacesConverter(forClass = LocalDateTime::class)
class LocalDateTimeConverter : Converter, PartialStateHolder {

    /**
     * `p:calendar` must to use the same pattern as the converter, so we provide it here.
     * @return
     */
    val pattern = "dd/MM/yyyy HH:mm"
    private val formatter = DateTimeFormatter.ofPattern(pattern)
    private var transientFlag = true
    private var initialState: Boolean = false

    override fun getAsObject(pFacesCtx: FacesContext, pComponent: UIComponent, value: String?): Any? {
        if (value == null || value.isEmpty()) {
            return null
        }
        return LocalDateTime.parse(value, formatter)
    }

    override fun getAsString(pFacesCtx: FacesContext, pComponent: UIComponent, value: Any?): String {
        if (value == null) {
            return ""
        }

        if (value is LocalDateTime) {
            return formatter.format((value as LocalDateTime?)!!)
        }

        throw IllegalArgumentException("Expecting a LocalDateTime instance but received " + value.javaClass.name)
    }

    // PartialStateHolder implementation

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