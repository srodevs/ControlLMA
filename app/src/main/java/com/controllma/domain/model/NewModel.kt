package com.controllma.domain.model


data class NewModel(
    var newId: String?,
    var title: String?,
    var description: String?,
    var publisher: String?,
    var timestamp: Long?,
    var urlImage: String?
)