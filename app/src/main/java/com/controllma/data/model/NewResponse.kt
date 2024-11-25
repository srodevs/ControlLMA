package com.controllma.data.model

data class NewResponse(
    var newId: String?,
    var title: String?,
    var publisher: String?,
    var description: String?,
    var timestamp: Long?,
    var urlImage: String?
) {
    constructor() : this(null, null, null, null, null, null)
}

data class NewModel(
    var newId: String?,
    var title: String?,
    var description: String?,
    var publisher: String?,
    var timestamp: Long?,
    var urlImage: String?
)