package com.controllma.domain.usecases

import com.controllma.data.repository.NewsRepositoryImpl
import com.controllma.domain.model.NewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


class CreateNewUseCaseTest {
    private lateinit var newsRepositoryImpl: NewsRepositoryImpl
    private lateinit var createNewUseCase: CreateNewUseCase

    @Before
    fun setUp() {
        newsRepositoryImpl = mockk()
        createNewUseCase = CreateNewUseCase(newsRepositoryImpl)
    }

    @Test
    fun `when repository returns true then use case returns true`() {
        // given
        val newModel = NewModel("titulo", "contenido", null, null, null, null)
        every { newsRepositoryImpl.createNew(newModel) } returns true

        // when
        val result = createNewUseCase(newModel)

        // Assert
        assertTrue(result)
        verify(exactly = 1) { newsRepositoryImpl.createNew(newModel) }
    }

    @Test
    fun `when repository returns false then use case returns false`() {
        // given
        val newModel = NewModel("titulo", "contenido", null, null, null, null)
        every { newsRepositoryImpl.createNew(newModel) } returns false

        // when
        val result = createNewUseCase(newModel)

        // Assert
        assertFalse(result)
        verify(exactly = 1) { newsRepositoryImpl.createNew(newModel) }
    }

    @Test(expected = Exception::class)
    fun `when repository throws exception then use case throws exception`() {
        // given
        val newModel = NewModel("titulo", "contenido", null, null, null, null)
        every { newsRepositoryImpl.createNew(newModel) } throws Exception("Error inesperado")

        // when
        createNewUseCase(newModel)

        // Assert
        verify(exactly = 1) { newsRepositoryImpl.createNew(newModel) }
    }

}
