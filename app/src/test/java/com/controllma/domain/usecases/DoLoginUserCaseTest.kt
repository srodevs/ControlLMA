package com.controllma.domain.usecases

import com.controllma.core.TypeLoginResponse
import com.controllma.data.repository.UserRepositoryImpl
import com.controllma.domain.model.LoginResultModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class DoLoginUserCaseTest {
    private lateinit var userRepositoryImpl: UserRepositoryImpl
    private lateinit var doLoginUserCase: DoLoginUserCase

    @Before
    fun setUp() {
        userRepositoryImpl = mockk()
        doLoginUserCase = DoLoginUserCase(userRepositoryImpl)
    }

    @Test
    fun `when repository returns success login then use case returns success`() = runTest {
        // given
        val email = "example@mail.com"
        val pass = "12345"

        val expectedResult = LoginResultModel(
            loginStatus = TypeLoginResponse.Incorrect,
            msg = ""
        )

        coEvery { userRepositoryImpl.login(email, pass) } returns expectedResult

        // when
        val result = doLoginUserCase(email, pass)

        // Assert
        assertEquals(expectedResult, result)
        coVerify(exactly = 1) { userRepositoryImpl.login(email, pass) }
    }

    @Test
    fun `when repository returns failed login then use case returns failure`() = runTest {
        // given
        val email = "example@mail.com"
        val pass = "pass"

        val expectedResult = LoginResultModel(
            loginStatus = TypeLoginResponse.Incorrect,
            msg = "Invalid credentials",
        )

        coEvery { userRepositoryImpl.login(email, pass) } returns expectedResult

        // when
        val result = doLoginUserCase(email, pass)

        // Assert
        assertEquals(expectedResult, result)
        coVerify(exactly = 1) { userRepositoryImpl.login(email, pass) }
    }

    @Test(expected = Exception::class)
    fun `when repository throws exception then use case throws exception`() = runTest {
        // given
        val email = "test@mail.com"
        val pass = "12345"

        coEvery { userRepositoryImpl.login(email, pass) } throws Exception("Network error")

        // when
        doLoginUserCase(email, pass)

        // Assert (handled by expected)
        coVerify(exactly = 1) { userRepositoryImpl.login(email, pass) }
    }

}