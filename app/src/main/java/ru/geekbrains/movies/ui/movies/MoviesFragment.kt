package ru.geekbrains.movies.ui.movies

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.NavHostFragment
import ru.geekbrains.movies.R
import ru.geekbrains.movies.databinding.FragmentMoviesBinding
import ru.geekbrains.movies.domain.AppState
import ru.geekbrains.movies.domain.models.MoviesResponse
import ru.geekbrains.movies.ui.base.BaseFragment
import ru.geekbrains.movies.ui.detail.DetailFragment.Companion.KEY_MOVIE_ID
import ru.geekbrains.movies.ui.movies.adapter.MovieAdapter
import ru.geekbrains.movies.utils.showSnakeBar
import org.koin.androidx.viewmodel.ext.android.viewModel


class MoviesFragment() : BaseFragment<FragmentMoviesBinding>(R.layout.fragment_movies),
    MovieAdapter.Delegate {

    private val viewModel: MoviesViewModel by viewModel()
    private val adapter by lazy { MovieAdapter(this) }

    override fun initListeners() {
        viewBinding.searchEditText.setOnEditorActionListener { view, actionId, _ ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH) {
                if (view.text.isNotEmpty()) {
                    viewModel.setCurrentPage(ONE_VALUE, ONE_VALUE)
                    viewModel.searchMovies(viewBinding.searchEditText.text.toString())
                    hideKeyboardForTextView()
                    true
                } else {
                    hideKeyboardForTextView()
                    false
                }
            } else {
                false
            }
        }

        viewBinding.search.setEndIconOnClickListener {
            viewModel.setCurrentPage(ONE_VALUE, ONE_VALUE)
            viewModel.getMoviesTopRated(true)
            viewBinding.searchEditText.setText("")
            hideKeyboardForTextView()
        }

        viewBinding.searchEditText.doAfterTextChanged {
            it?.let {
                if (it.length > TWO_VALUE) {
                    viewModel.searchMovies(viewBinding.searchEditText.text.toString())
                } else {
                    viewModel.getMoviesTopRated(true)
                }
            }
        }
    }

    private fun hideKeyboardForTextView() {
        val view = requireActivity().currentFocus
        view?.let {
            val inputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                        InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, INPUT_METHOD_MANAGER_FLAGS)
        }
        (view as? TextView)?.clearFocus()
    }

    override fun initObservers() {
        viewModel.getMoviesLiveData()
            .observe(viewLifecycleOwner) { res -> renderData(result = res) }
    }

    override fun renderSuccess(result: AppState.Success<*>) {
        showLoading(false)
        when (val moveResponse = result.data) {
            is MoviesResponse -> {
                renderMovies(moveResponse)
            }
        }
    }

    private fun renderMovies(moveResponse: MoviesResponse) {
        adapter.setItems(moveResponse.movies)
        if (adapter.itemCount > ZERO_VALUE)
            viewModel.setCurrentPage(moveResponse.page, moveResponse.totalPages)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerSetting()
    }

    private fun initRecyclerSetting() {
        viewBinding.recyclerView.also { recycler ->
            recycler.adapter = adapter
            recycler.setHasFixedSize(true)
        }
    }

    override fun onResume() {
        super.onResume()
        showToolBar(false)
        if (viewBinding.searchEditText.text.toString().isEmpty()) {
            viewModel.getMoviesTopRated(true)
        } else {
            viewModel.setCurrentPage(ONE_VALUE, ONE_VALUE)
        }
    }

    override fun onPause() {
        super.onPause()
        showToolBar(true)
    }

    override fun onItemClick(movie: MoviesResponse.Movie) {
        NavHostFragment.findNavController(this).navigate(R.id.nav_detail, bundleOf().apply {
            putInt(KEY_MOVIE_ID, movie.id)
        })
    }

    override fun getMoreMovies() {
        if (viewBinding.searchEditText.text.toString().isEmpty()) {
            viewModel.getMoviesTopRated(true)
        }
    }

    override fun showLoading(isShow: Boolean) {
        viewBinding.progress.isVisible = isShow
    }

    override fun showError(throwable: Throwable) {
        viewBinding.root.showSnakeBar(throwable.localizedMessage)
    }

    companion object {
        private const val ZERO_VALUE = 0
        private const val ONE_VALUE = 1
        private const val TWO_VALUE = 2
        private const val INPUT_METHOD_MANAGER_FLAGS = 0
    }
}