package com.mazekine.depool.scout

import com.google.gson.annotations.SerializedName

data class DepoolListResponse(
    @SerializedName("data")
    val dt: DataResponse,
)

data class DataResponse(
    val accounts: List<DepoolIdWrapper>,
)

data class DepoolIdWrapper(
    val id: String,
)
