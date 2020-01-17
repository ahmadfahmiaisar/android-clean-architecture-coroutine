package io.fajarca.home.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import io.fajarca.home.domain.MarvelCharacter
import io.fajarca.home.domain.usecase.GetCharactersUseCase
import io.fajarca.home.util.TestCoroutineRule
import io.fajarca.home.util.TestUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    // Run tasks synchronously
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var viewModel: HomeViewModel
    

    @Mock
    private lateinit var observer : Observer<HomeViewModel.CharacterState<List<MarvelCharacter>>>


    @Mock
    private lateinit var useCase: GetCharactersUseCase

    @Mock
    lateinit var lifeCycleOwner: LifecycleOwner
    lateinit var lifeCycle: LifecycleRegistry

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        viewModel = HomeViewModel(useCase)

        lifeCycle = LifecycleRegistry(lifeCycleOwner)
        `when` (lifeCycleOwner.lifecycle).thenReturn(lifeCycle)
        lifeCycle.handleLifecycleEvent(Lifecycle.Event.ON_START)

        viewModel.characters.observe(lifeCycleOwner, observer)


    }

    @Test
    fun `when get all all character is success, observer should receive success result`() = testCoroutineRule.runBlockingTest  {
        //Given
        val marvelCharacters = mutableListOf<MarvelCharacter>()
        marvelCharacters.add(MarvelCharacter(1, "Marvel", "image-url"))

        `when`(useCase.execute()).thenReturn(marvelCharacters)

        //When
        viewModel.getAllCharacters()

        //Then
        verify(observer).onChanged(HomeViewModel.CharacterState.Loading)
        verify(observer).onChanged(HomeViewModel.CharacterState.Success(marvelCharacters))

    }

    @Test
    fun `when get all all character is empty, observer should receive empty result`() = testCoroutineRule.runBlockingTest  {
        //Given
        val marvelCharacters = emptyList<MarvelCharacter>()

        `when`(useCase.execute()).thenReturn(marvelCharacters)

        //When
        viewModel.getAllCharacters()

        //Then
        verify(observer).onChanged(HomeViewModel.CharacterState.Loading)
        verify(observer).onChanged(HomeViewModel.CharacterState.Empty)

    }

}