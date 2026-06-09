package com.example.a211684_rabiatul_drnazatulaini_project2.data.network

//describe the DATA that we RECEIVE from OpenChargeMap
data class StationResponse(
    val AddressInfo: AddressInfo,
    val Connections: List<Connection>
)

data class AddressInfo(
    val Title: String,
    val Town:String?, //can be null (empty/no value)
    val Distance: Double?
)

data class Connection(
    val PowerKW: Double?,
    val ConnectionType: ConnectionType
)

data class ConnectionType(
    val Title:String
)