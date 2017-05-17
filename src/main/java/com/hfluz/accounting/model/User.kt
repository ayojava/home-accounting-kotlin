package com.hfluz.accounting.model

import javax.persistence.Basic
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

/**
 * Created by hfluz on 03/05/17.
 */
@Entity
data class User private constructor(
        @Id
        @Column(name = "id")
        var id: Int = 0,
        @Basic
        @Column(name = "username")
        var username: String? = null,
        @Basic
        @Column(name = "password")
        var password: String? = null
) {
    constructor() : this(0, null, null)
}
