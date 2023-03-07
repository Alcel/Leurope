package com.example.leurope

import com.google.gson.annotations.SerializedName

data class RouteResponse(
    @SerializedName("features")val feau:List<Feature>
)
data class Feature(
    @SerializedName("geometry")val geo:Geometry
)
data class Geometry(
    @SerializedName("coordinates")val coordenadas:List<List<Double>>
)
