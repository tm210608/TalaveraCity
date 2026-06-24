package com.talaveracity.app.domain.usecase

import com.talaveracity.app.domain.model.CulturalEvent
import com.talaveracity.app.domain.repository.CulturalRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class GetEventsUseCaseTest {

    private val repository: CulturalRepository = mockk()
    private val getEventsUseCase = GetEventsUseCase(repository)

    @Test
    fun `cuando el repositorio retorna exito, el caso de uso retorna la lista de eventos`() = runTest {
        // Given
        val mockEvents = listOf(
            CulturalEvent("1", "Evento 1", "Hoy", "Plaza", "url", "Desc")
        )
        coEvery { repository.getEvents() } returns Result.success(mockEvents)

        // When
        val result = getEventsUseCase()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(mockEvents, result.getOrNull())
    }

    @Test
    fun `cuando el repositorio falla, el caso de uso retorna el error`() = runTest {
        // Given
        val exception = Exception("Network Error")
        coEvery { repository.getEvents() } returns Result.failure(exception)

        // When
        val result = getEventsUseCase()

        // Then
        assertTrue(result.isFailure)
        assertEquals("Network Error", result.exceptionOrNull()?.message)
    }
}




