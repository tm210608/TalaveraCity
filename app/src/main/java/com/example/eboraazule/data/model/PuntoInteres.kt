package com.example.eboraazule.data.model

import com.google.android.gms.maps.model.LatLng

enum class PuntoInteres(
    val id: String,
    val titulo: String, 
    val descripcion: String, 
    val coords: LatLng, 
    val narrador: String,
    val radioMetros: Double = 100.0
) {
    PuenteViejo(
        "poi_puente",
        "Puente Viejo",
        "Este puente romano-medieval sobre el Tajo es el testigo más antiguo de la ciudad, uniendo las dos orillas y el camino hacia la cerámica.",
        LatLng(39.9575, -4.8315),
        "Anselmo, el Pescador"
    ),
    CeramicaAzul(
        "poi_museo",
        "Museo Ruiz de Luna",
        "Sede del patrimonio cerámico más importante de la ciudad. Antiguo convento de los Agustinos Recoletos.",
        LatLng(39.9588, -4.8335),
        "Juan Ruiz de Luna"
    ),
    Basilica(
        "poi_basilica",
        "Basílica de Nuestra Señora del Prado",
        "Conocida como la 'Sixtina de la cerámica' por sus impresionantes azulejos de los siglos XVI al XX.",
        LatLng(39.9632, -4.8256),
        "Fray Gabriel de Talavera"
    ),
    TorreAlbarrana(
        "poi_torres",
        "Torres Albarranas",
        "Majestuosas torres de defensa del siglo XIII. Son un símbolo de la resistencia histórica de Talavera.",
        LatLng(39.9579, -4.8341),
        "Capitán de la Guardia"
    ),
    AlfarCarmen(
        "poi_alfar",
        "El Alfar del Carmen",
        "Antiguo alfar convertido en centro cultural. Representa la evolución industrial de nuestra cerámica.",
        LatLng(39.9595, -4.8290),
        "Maestra Alfarera"
    ),
    PlazaPan(
        "poi_plaza",
        "Plaza del Pan",
        "El centro neurálgico de la ciudad antigua, rodeada de edificios históricos y decorada con azulejería típica.",
        LatLng(39.9583, -4.8322),
        "El Sereno"
    )
}
