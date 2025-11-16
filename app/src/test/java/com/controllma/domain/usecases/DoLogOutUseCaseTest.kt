package com.controllma.domain.usecases

import com.controllma.data.repository.UserRepositoryImpl
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test


class DoLogOutUseCaseTest {
    private lateinit var userRepositoryImpl: UserRepositoryImpl
    private lateinit var doLogOutUseCase: DoLogOutUseCase

    @Before
    fun setUp() {
        userRepositoryImpl = mockk()
        doLogOutUseCase = DoLogOutUseCase(userRepositoryImpl)
    }

    @Test
    fun `when use case invoked then repository logout is called once`() {
        // given
        every { userRepositoryImpl.logOut() } just Runs

        // when
        doLogOutUseCase()

        // Assert
        verify(exactly = 1) { userRepositoryImpl.logOut() }
    }

    @Test(expected = Exception::class)
    fun `when repository throws exception then use case throws exception`() {
        // given
        every { userRepositoryImpl.logOut() } throws Exception("Logout error")

        // when
        doLogOutUseCase()

        // Assert
        verify(exactly = 1) { userRepositoryImpl.logOut() }
    }
}
