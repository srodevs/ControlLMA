package com.controllma.ui.views

import com.controllma.core.StorageUser
import com.controllma.data.network.response.NewResponse
import com.controllma.data.network.response.UserResponse
import com.controllma.domain.usecases.CreateNewUseCase
import com.controllma.domain.usecases.DoLogOutUseCase
import com.controllma.domain.usecases.DoLoginUserCase
import com.controllma.domain.usecases.GetAllNewsUseCase
import com.controllma.domain.usecases.GetAllRollCallUseCase
import com.controllma.domain.usecases.GetUserUseCase
import com.controllma.domain.usecases.RegisterRollCallUseCase
import com.controllma.rules.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private lateinit var storageUser: StorageUser
    private lateinit var loginUseCase: DoLoginUserCase
    private lateinit var getUserUseCase: GetUserUseCase
    private lateinit var logoutUseCase: DoLogOutUseCase
    private lateinit var getAllNewsUseCase: GetAllNewsUseCase
    private lateinit var createNewUseCase: CreateNewUseCase
    private lateinit var registerRollCallUseCase: RegisterRollCallUseCase
    private lateinit var getAllRollCallUseCase: GetAllRollCallUseCase
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        storageUser = mockk()
        loginUseCase = mockk()
        getUserUseCase = mockk()
        logoutUseCase = mockk()
        getAllNewsUseCase = mockk()
        createNewUseCase = mockk()
        registerRollCallUseCase = mockk()
        getAllRollCallUseCase = mockk()

        viewModel = MainViewModel(
            storageUser,
            loginUseCase,
            getUserUseCase,
            logoutUseCase,
            getAllNewsUseCase,
            createNewUseCase,
            registerRollCallUseCase,
            getAllRollCallUseCase
        )
    }


    @Test
    fun `login button enabled only when email valid and pass length  8`() = runTest {
        viewModel.onLoginChange("test@gmail.com", "123456789")

        assert(viewModel.btnEnable.value)
    }

    @Test
    fun `getUserInf calls use case and disables loading afterwards`() = runTest {
        val mockUser = UserResponse("id123", "email@test.com", "")

        coEvery { getUserUseCase() } returns mockUser

        var callback: UserResponse? = null

        viewModel.getUserInf { callback = it }

        assertEquals(mockUser, callback)
        assertFalse(viewModel.loading.value)
    }

    @Test
    fun `getAllNews collects flow and updates state`() = runTest {
        val list = listOf(NewResponse("1", "Titulo", null, null, null, null))
        coEvery { getAllNewsUseCase() } returns flowOf(list)

        viewModel.getAllNews()

        assertEquals(list, viewModel.newsList.value)
    }

    @Test
    fun `onPublish calls CreateNewUseCase and returns result`() = runTest {
        coEvery { createNewUseCase(any()) } returns true

        viewModel.onNewsChange("Titulo X", "Desc X")

        var responseValue = false
        viewModel.onPublish("uuid123") { responseValue = it }

        assertTrue(responseValue)
    }

    @Test
    fun `createRollCall returns true when register succeeds`() = runTest {
        coEvery { registerRollCallUseCase(any()) } returns true

        var result = false
        viewModel.createRollCall("uuid123") { result = it }

        assertTrue(result)
        assertFalse(viewModel.loading.value)
    }


    @Test
    fun `logout calls logoutUseCase and callback returns true`() = runTest {
        coEvery { logoutUseCase() } returns Unit

        var value = false
        viewModel.logOut { value = it }

        assertTrue(value)
    }

    @Test
    fun `getUserType returns flow from storage`() = runTest {
        val flow = flowOf("admin")
        every { storageUser.getUserType() } returns flow

        val result = viewModel.getUserType().first()

        assertEquals("admin", result)
    }

}