package com.controllma.domain.usecases

import com.controllma.data.network.response.RollCall
import com.controllma.data.repository.RollCallImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class RegisterRollCallUseCaseTest {
        private lateinit var rollCallImpl: RollCallImpl
        private lateinit var registerRollCallUseCase: RegisterRollCallUseCase

        @Before
        fun setUp() {
            rollCallImpl = mockk()
            registerRollCallUseCase = RegisterRollCallUseCase(rollCallImpl)
        }

        @Test
        fun `when repository returns true then use case returns true`() = runTest {
            // given
            val rollCall = RollCall(
                date = "2025-11-15",
                registerId = "1",
                timestamp = null,
                uuId = "present"
            )

            coEvery { rollCallImpl.registerRollCall(rollCall) } returns true

            // when
            val result = registerRollCallUseCase(rollCall)

            // Assert
            assertEquals(true, result)
            coVerify(exactly = 1) { rollCallImpl.registerRollCall(rollCall) }
        }

        @Test
        fun `when repository returns false then use case returns false`() = runTest {
            // given
            val rollCall = RollCall(
                date = "2025-11-15",
                registerId = "1",
                timestamp = null,
                uuId = "present"
            )

            coEvery { rollCallImpl.registerRollCall(rollCall) } returns false

            // when
            val result = registerRollCallUseCase(rollCall)

            // Assert
            assertEquals(false, result)
            coVerify(exactly = 1) { rollCallImpl.registerRollCall(rollCall) }
        }

        @Test(expected = Exception::class)
        fun `when repository throws exception then use case throws exception`() = runTest {
            // given
            val rollCall = RollCall(
                date = "2025-11-15",
                registerId = "1",
                timestamp = null,
                uuId = "present"
            )

            coEvery { rollCallImpl.registerRollCall(rollCall) } throws Exception("DB error")

            // when
            registerRollCallUseCase(rollCall)

            // Assert
            coVerify(exactly = 1) { rollCallImpl.registerRollCall(rollCall) }
        }

}