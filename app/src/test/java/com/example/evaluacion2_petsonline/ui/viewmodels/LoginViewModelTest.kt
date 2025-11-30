package com.example.evaluacion2_petsonline.ui.viewmodels

import android.app.Application
import com.example.evaluacion2_petsonline.data.repository.AuthRepository
import com.example.evaluacion2_petsonline.domain.model.LoginData
import com.example.evaluacion2_petsonline.domain.model.LoginResponse
import com.example.evaluacion2_petsonline.viewmodel.LoginViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel
    private lateinit var mockRepository: AuthRepository
    private val testDispatcher = StandardTestDispatcher()
    private val mockApplication = mockk<Application>(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockRepository = mockk()
        viewModel = LoginViewModel(mockApplication, mockRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `si el email es invalido, debe mostrar error`() {
        viewModel.onEmailChange("correomalo")
        viewModel.onPasswordChange("123456")
        viewModel.login()
        assertEquals("Correo electrónico inválido", viewModel.uiState.value.error)
    }

    @Test
    fun `si los campos estan vacios, debe mostrar error`() {
        viewModel.onEmailChange("")
        viewModel.onPasswordChange("")
        viewModel.login()
        assertEquals("Todos los campos son obligatorios", viewModel.uiState.value.error)
    }

    @Test
    fun `si el login es correcto, success debe ser true`() {
        runTest {
            viewModel.onEmailChange("test@correo.com")
            viewModel.onPasswordChange("123456")

            val fakeData = LoginData(
                user = mockk(relaxed = true),
                accessToken = "fake_token"
            )
            val fakeResponse = LoginResponse(true, "Exito", fakeData)

            coEvery { mockRepository.login(any(), any()) } returns Result.success(fakeResponse)

            viewModel.login()

            testDispatcher.scheduler.advanceUntilIdle()

            assertTrue(viewModel.uiState.value.success)
            assertEquals(null, viewModel.uiState.value.error)
        }
    }

    @Test
    fun `si el login falla, debe guardar el mensaje de error`() {
        runTest {
            viewModel.onEmailChange("admin@sistema.com")
            viewModel.onPasswordChange("ClaveMala")

            val errorMsg = "Credenciales incorrectas"
            coEvery { mockRepository.login(any(), any()) } returns Result.failure(Exception(errorMsg))

            viewModel.login()
            testDispatcher.scheduler.advanceUntilIdle()

            assertTrue(!viewModel.uiState.value.success)
            assertEquals(errorMsg, viewModel.uiState.value.error)
            assertTrue(!viewModel.uiState.value.isLoading)
        }
    }

    @Test
    fun `resetSuccess debe volver success a false`() {
        runTest {
            val fakeData = LoginData(mockk(relaxed=true), "token")
            val fakeResponse = LoginResponse(true, "Exito", fakeData)
            coEvery { mockRepository.login(any(), any()) } returns Result.success(fakeResponse)

            viewModel.onEmailChange("a@b.com")
            viewModel.onPasswordChange("123456")
            viewModel.login()
            testDispatcher.scheduler.advanceUntilIdle()

            assertTrue(viewModel.uiState.value.success)

            viewModel.resetSuccess()

            assertTrue(!viewModel.uiState.value.success)
        }
    }

    @Test
    fun `el estado inicial debe estar vacio y sin errores`() {
        val state = viewModel.uiState.value

        assertEquals("", state.email)
        assertEquals("", state.password)
        assertEquals(null, state.error)
        assertTrue(!state.isLoading)
        assertTrue(!state.success)
    }
}