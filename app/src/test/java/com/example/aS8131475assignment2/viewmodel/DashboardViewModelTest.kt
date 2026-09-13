package com.example.aS8131475assignment2.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.aS8131475assignment2.data.DashboardResponse
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
class DashboardViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var apiService: ApiService
    private lateinit var viewModel: DashboardViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        apiService = mock()
        viewModel = DashboardViewModel(apiService)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadDashboard sets Loading state immediately`() = runTest {
        whenever(apiService.getDashboard(any())).thenReturn(
            DashboardResponse(entities = emptyList(), entityTotal = 0)
        )

        viewModel.loadDashboard("investments")

        assertEquals(DashboardState.Loading, viewModel.dashboardState.value)
    }

    @Test
    fun `successful load sets Success state with entities`() = runTest {
        val fakeEntities = listOf(
            mapOf("name" to "Item A", "description" to "First item"),
            mapOf("name" to "Item B", "description" to "Second item")
        )
        whenever(apiService.getDashboard(any())).thenReturn(
            DashboardResponse(entities = fakeEntities, entityTotal = 2)
        )

        viewModel.loadDashboard("investments")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.dashboardState.value
        assertTrue(state is DashboardState.Success)
        assertEquals(2, (state as DashboardState.Success).entities.size)
    }

    @Test
    fun `failed load sets Error state`() = runTest {
        whenever(apiService.getDashboard(any())).thenThrow(RuntimeException("Network error"))

        viewModel.loadDashboard("investments")
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.dashboardState.value is DashboardState.Error)
    }
}