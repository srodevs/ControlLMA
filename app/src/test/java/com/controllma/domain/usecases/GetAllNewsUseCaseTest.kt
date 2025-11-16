package com.controllma.domain.usecases

import app.cash.turbine.test
import com.controllma.data.network.response.NewResponse
import com.controllma.data.repository.NewsRepositoryImpl
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class GetAllNewsUseCaseTest {
        private lateinit var newsRepositoryImpl: NewsRepositoryImpl
        private lateinit var getAllNewsUseCase: GetAllNewsUseCase

        @Before
        fun setUp() {
            newsRepositoryImpl = mockk()
            getAllNewsUseCase = GetAllNewsUseCase(newsRepositoryImpl)
        }

        @Test
        fun `when repository returns flow of news then use case returns same flow`() = runTest {
            // given
            val fakeNews = listOf(
                NewResponse("id1", "Titulo 1", "Contenido 1", "", null, ""),
                NewResponse("id2", "Titulo 2", "Contenido 2", "", null, "")
            )

            every { newsRepositoryImpl.getAllNews() } returns flowOf(fakeNews)

            // when
            val flow = getAllNewsUseCase()

            // Assert
            flow.test {
                val emission = awaitItem()
                assertEquals(fakeNews, emission)
                awaitComplete()

                verify(exactly = 1) { newsRepositoryImpl.getAllNews() }
            }
        }

        @Test(expected = Exception::class)
        fun `when repository throws exception then use case throws exception`() = runTest {
            // given
            every { newsRepositoryImpl.getAllNews() } throws Exception("Database error")

            // when
            getAllNewsUseCase()

            // Assert
            verify(exactly = 1) { newsRepositoryImpl.getAllNews() }
        }

}