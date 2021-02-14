package com.example.test.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.test.api.ApiInterface
import com.example.test.model.Meta
import com.example.test.model.Pagination
import com.example.test.model.User
import com.example.test.model.UserData
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import okio.IOException
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.stubbing.OngoingStubbing

@ExperimentalCoroutinesApi
class UserListViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @InjectMocks
    private lateinit var viewModel: UserListViewModel

    @Mock
    private lateinit var api: ApiInterface

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }


    @Test
    fun `get users positive case`() {
        val userData = UserData(
            data = listOf(user1, user2, user3),
            code = 200,
            meta = meta
        )

        observe(viewModel.users)

        wheneverBlocking { api.getAllUsers() }.thenReturn(userData)

        viewModel.loadUsers()

        assert(viewModel.users.value == userData.data)
    }

    @Test
    fun `get users negative case`() {
        val userObserver = observe(viewModel.users)
        val errorObserver = observe(viewModel.error)

        val exception = IOException("404")
        wheneverBlocking { api.getAllUsers() }.thenThrow(exception)

        viewModel.loadUsers()

        assert(viewModel.error.value == exception)

        verifyZeroInteractions(userObserver)
        verify(errorObserver).onChanged(exception)
    }

    @Test
    fun `create new user`() {
        observe(viewModel.users)

        wheneverBlocking { api.createUser(user4) }.thenReturn(Unit)

        viewModel.createNewUser(user4.name, user4.email)

        assert(viewModel.users.value?.size == 1)
    }

    companion object {
        private val user1 = User(
            name = "user1",
            status = "Active",
            email = "useremail1@gmail.com",
            gender = "male",
            id = 1,
            created_at = "2021-02-14T03:50:03.422+05:30"
        )

        private val user2 = User(
            name = "user2",
            status = "Active",
            email = "useremail2@gmail.com",
            gender = "male",
            id = 2,
            created_at = "2021-02-14T03:50:03.422+05:30"
        )

        private val user3 = User(
            name = "user3",
            status = "Active",
            email = "useremail3@gmail.com",
            gender = "male",
            id = 3,
            created_at = "2021-02-14T03:50:03.422+05:30"
        )

        private val user4 = User(
            name = "user4",
            status = "Active",
            email = "useremail4@gmail.com",
            gender = "male",
            id = 4,
            created_at = "2021-02-14T03:50:03.422+05:30"
        )

        private val meta = Meta(pagination = Pagination(limit = 1, page = 1, pages = 1, total = 1))
    }
}


@ExperimentalCoroutinesApi
class MainCoroutineRule(
    private val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
) : TestWatcher() {

    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
}

fun <T> wheneverBlocking(methodCall: suspend () -> T): OngoingStubbing<T> {
    return runBlocking { Mockito.`when`(methodCall()) }
}

fun <T> observe(liveData: LiveData<T>) = mock<Observer<T>>().apply {
    liveData.observeForever(this)
}
