package com.example.a211684_rabiatul_drnazatulaini_lab5.data
//data source = contain real fixed data
object DataSource {
    val StationList = listOf(
        StationUiState(
            stationName = "Bangi Gateway Mall",
            power = "60 kW max",
            distance = "1.8 km",
            rating = "4.5",
            dc = "DC 2",
            ac = "AC 1"
        ),
        StationUiState(
            stationName = "Christine's Bakery Bangi Avenue Mall",
            power = "40 kW max",
            distance = "2.6 km",
            rating = "5.0",
            dc = "DC 1",
            ac = "0"
        ),
        StationUiState(
            stationName = "Tenera Hotel Bangi",
            power = "24 kW max",
            distance = "3.7 km",
            rating = "4.5",
            dc = "DC 1",
            ac = "AC 2"
        ),
        StationUiState(
            stationName = "Bangi Gold Resort",
            power = "200 kW max",
            distance = "4.3 km",
            rating = "4.6",
            dc = "DC 5",
            ac = "AC 6"
        ),
        StationUiState(
            stationName = "Lotus's Bandar Puteri Bangi",
            power = "50 kW max",
            distance = "4.5 km",
            rating = "4.8",
            dc = "DC 1",
            ac = "AC 1"
        ),
    )

    val ChargerList = listOf(
        ChargerUiState(
            chargerName = "ELECTRAX CHARGER 01",
            power = "200kW max",
            connectors = listOf(
                Connector(1, "CCS 2", true),
                Connector(2, "CCS 2", true),
                Connector(3, "CCS 2", false),
                Connector(4, "CCS 2", true)
            )
        ),
        ChargerUiState(
            chargerName = "ELECTRAX CHARGER 05",
            power = "60kW max",
            connectors = listOf(
                Connector(1, "CCS 2", true),
                Connector(2, "CCS 2", true),
            )
        ) ,

        ChargerUiState(
            chargerName = "ELECTRAX CHARGER 06",
            power = "22kW max",
            connectors = listOf(
                Connector(2, "Type 2", true),
            )
        ),
        ChargerUiState(
            chargerName = "ELECTRAX CHARGER 07",
            power = "22kW max",
            connectors = listOf(
                Connector(2, "Type 2", true),
            )
        ),
        ChargerUiState(
            chargerName = "ELECTRAX CHARGER 08",
            power = "22kW max",
            connectors = listOf(
                Connector(2, "Type 2", true),
            )
        ),
        ChargerUiState(
            chargerName = "ELECTRAX CHARGER 09",
            power = "22kW max",
            connectors = listOf(
                Connector(2, "Type 2", true),
            )
        )
    )

    val PaymentMethodList = listOf(
        PaymentMethod("TnG eWallet", PaymentType.EWALLET),
        PaymentMethod("Debit/Credit Card", PaymentType.CARD),
        PaymentMethod("Online Banking", PaymentType.FPX)
    )

    val FeeLists = listOf(
        Fee("Charging Fee", "MYR 1.60/kWh"),
        Fee("Idling Fee", "MYR 0.4/1 min"),
        Fee("Idling Grace Period", "15 mins")
    )
}
