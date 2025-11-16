package com.controllma.domain.usecases

import app.cash.turbine.test
import com.controllma.data.network.response.RollCall
import com.controllma.data.repository.RollCallImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class GetAllRollCallUseCaseTest {
    private lateinit var rollCallImpl: RollCallImpl
    private lateinit var getAllRollCallUseCase: GetAllRollCallUseCase

    @Before
    fun setUp() {
        rollCallImpl = mockk()
        getAllRollCallUseCase = GetAllRollCallUseCase(rollCallImpl)
    }

    @Test
    fun `when repository returns roll call list then use case returns same flow`() = runTest {
        // given
        val uuid = "1234-5678"

        val fakeRollCallList = listOf(
            RollCall("1", null, "Juan", date = null),
            RollCall("2", null, "Pedro", date = "")
        )

        coEvery { rollCallImpl.getAllRollCAll(uuid) } returns flowOf(fakeRollCallList)

        // when
        val flow = getAllRollCallUseCase(uuid)

        // Assert
        flow.test {
            val emission = awaitItem()
            assertEquals(fakeRollCallList, emission)
            awaitComplete()
        }

        coVerify(exactly = 1) { rollCallImpl.getAllRollCAll(uuid) }
    }

    @Test(expected = Exception::class)
    fun `when repository throws exception then use case throws exception`() = runTest {
        // given
        val uuid = "1234-5678"
        coEvery { rollCallImpl.getAllRollCAll(uuid) } throws Exception("Database error")

        // when
        getAllRollCallUseCase(uuid)

        // Assert
        coVerify(exactly = 1) { rollCallImpl.getAllRollCAll(uuid) }
    }


}