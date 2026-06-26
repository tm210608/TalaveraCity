package com.talaveracity.app.domain.model

import com.google.android.gms.maps.model.LatLng

enum class PoiCategory {
    MONUMENT,
    MUSEUM,
    CHURCH,
    POTTERY,
    HISTORY
}

enum class PuntoInteres(
    val id: String,
    val titulo: String, 
    val descripcion: String, 
    val coords: LatLng, 
    val category: PoiCategory,
    val imageUrl: String = "",
    val narrador: String = "Guía Talavera",
    val radioMetros: Double = 100.0
) {
    PuenteViejo(
        "poi_puente",
        "Puente Viejo (Romano)",
        "El testigo más antiguo sobre el Tajo, con origen romano y reconstrucciones medievales.",
        LatLng(39.9575, -4.8315),
        PoiCategory.MONUMENT,
        "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?auto=format&fit=crop&q=80&w=800"
    ),
    MuseoRuizLuna(
        "poi_museo_luna",
        "Museo Ruiz de Luna",
        "Antiguo convento que alberga la mayor colección de cerámica de Talavera del mundo.",
        LatLng(39.9588, -4.8335),
        PoiCategory.MUSEUM,
        "https://images.unsplash.com/photo-1590650516494-0c8e4a4dd67e?auto=format&fit=crop&q=80&w=800"
    ),
    BasilicaPrado(
        "poi_basilica",
        "Basílica de Ntra. Sra. del Prado",
        "La 'Sixtina de la Cerámica'. Sus paredes narran la historia de la ciudad en azulejos.",
        LatLng(39.9632, -4.8256),
        PoiCategory.CHURCH,
        "https://images.unsplash.com/photo-1543783230-050414a6003b?auto=format&fit=crop&q=80&w=800"
    ),
    TorreAlbarrana(
        "poi_torres",
        "Torres Albarranas",
        "Parte del sistema defensivo amurallado del siglo XIII, únicas en la península.",
        LatLng(39.9579, -4.8341),
        PoiCategory.MONUMENT
    ),
    AlfarCarmen(
        "poi_alfar",
        "El Alfar del Carmen",
        "Centro cultural en un antiguo alfar, preservando el proceso artesanal.",
        LatLng(39.9595, -4.8290),
        PoiCategory.POTTERY
    ),
    PlazaPan(
        "poi_plaza",
        "Plaza del Pan",
        "Corazón del casco antiguo, rodeada de la Colegiata y el Ayuntamiento.",
        LatLng(39.9583, -4.8322),
        PoiCategory.HISTORY
    ),
    ColegiataSantaMaria(
        "poi_colegiata",
        "Colegiata de Santa María la Mayor",
        "Templo de estilo gótico-mudéjar con un impresionante rosetón de cerámica.",
        LatLng(39.9581, -4.8326),
        PoiCategory.CHURCH
    ),
    MurallasTalavera(
        "poi_murallas",
        "Murallas de la Calle Carnicerías",
        "El recinto amurallado mejor conservado, con sus imponentes torres y paseos.",
        LatLng(39.9585, -4.8350),
        PoiCategory.MONUMENT
    ),
    TeatroPalenque(
        "poi_teatro",
        "Teatro Palenque",
        "Construido sobre los restos de la antigua iglesia de San Ginés.",
        LatLng(39.9589, -4.8318),
        PoiCategory.HISTORY
    ),
    PuenteMetal(
        "poi_puente_hierro",
        "Puente de Hierro (Reina Sofía)",
        "Hito de la ingeniería de principios del siglo XX sobre el río Tajo.",
        LatLng(39.9545, -4.8365),
        PoiCategory.MONUMENT
    )
}
