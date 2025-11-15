package com.controllma.data.network.response

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
