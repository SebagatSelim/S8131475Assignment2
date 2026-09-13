package com.example.aS8131475assignment2.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.aS8131475assignment2.data.LoginResponse
import com.example.aS8131475assignment2.network.ApiService
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
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var apiService: ApiService
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        apiService = mock()
        viewModel = LoginViewModel(apiService)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Idle`() {
        assertEquals(LoginState.Idle, viewModel.loginState.value)
    }

    @Test
    fun `login with blank username sets Error state`() {
        viewModel.login("", "SomeName")
        assertTrue(viewModel.loginState.value is LoginState.Error)
    }

    @Test
    fun `login with blank password sets Error state`() {
        viewModel.login("12345678", "")
        assertTrue(viewModel.loginState.value is LoginState.Error)
    }

    @Test
    fun `successful login sets Success state with keypass`() = runTest {
        whenever(apiService.login(any())).thenReturn(LoginResponse("investments"))

        viewModel.login("12345678", "John")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.loginState.value
        assertTrue(state is LoginState.Success)
        assertEquals("investments", (state as LoginState.Success).keypass)
    }

    @Test
    fun `failed login sets Error state`() = runTest {
        whenever(apiService.login(any())).thenThrow(RuntimeException("Invalid credentials"))

        viewModel.login("12345678", "WrongName")
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.loginState.value is LoginState.Error)
    }
}