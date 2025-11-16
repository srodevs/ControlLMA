package com.controllma.domain.usecases

import com.controllma.data.network.response.UserResponse
import com.controllma.data.repository.UserRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class GetUserUseCaseTest {
    private lateinit var userRepositoryImpl: UserRepositoryImpl
    private lateinit var getUserUseCase: GetUserUseCase

    @Before
    fun setUp() {
        userRepositoryImpl = mockk()
        getUserUseCase = GetUserUseCase(userRepositoryImpl)
    }

    @Test
    fun `when repository returns user then use case returns user`() = runTest {
        // given
        val fakeUser = UserResponse(
            email = "correo@example.com",
            uuid = "1",
            typeUser = "Rodrigo",
        )

        coEvery { userRepositoryImpl.getUserInf() } returns fakeUser

        // when
        val result = getUserUseCase()

        // Assert
        assertEquals(fakeUser, result)
        coVerify(exactly = 1) { userRepositoryImpl.getUserInf() }
    }

    @Test
    fun `when repository returns null then use case returns null`() = runTest {
        // given
        coEvery { userRepositoryImpl.getUserInf() } returns null

        // when
        val result = getUserUseCase()

        // Assert
        assertEquals(null, result)
        coVerify(exactly = 1) { userRepositoryImpl.getUserInf() }
    }

    @Test(expected = Exception::class)
    fun `when repository throws exception then use case throws exception`() = runTest {
        // given
        coEvery { userRepositoryImpl.getUserInf() } throws Exception("DB error")

        // when
        getUserUseCase()

        // Assert
        coVerify(exactly = 1) { userRepositoryImpl.getUserInf() }
    }

}