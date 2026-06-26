package com.talaveracity.app.domain.usecase

import com.talaveracity.app.domain.model.CulturalEvent
import com.talaveracity.app.domain.repository.CulturalRepository
import javax.inject.Inject

class GetAnnouncementsUseCase @Inject constructor(
    private val repository: CulturalRepository
) {
    suspend operator fun invoke(): Result<List<CulturalEvent>> {
        return repository.getMunicipalAnnouncements()
    }
}
