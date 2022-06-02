package com.ekotyoo.racana.data.datasource.remote.response

import com.google.gson.annotations.SerializedName

data class DestinationResponse(

	@field:SerializedName("data")
	val data: DestinationData,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

data class DestinationData(

	@field:SerializedName("brief")
	val brief: String,

	@field:SerializedName("imageUrl")
	val imageUrl: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("location")
	val location: String,

	@field:SerializedName("lon")
	val lon: Double,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("expense")
	val expense: Int,

	@field:SerializedName("lat")
	val lat: Double
)
