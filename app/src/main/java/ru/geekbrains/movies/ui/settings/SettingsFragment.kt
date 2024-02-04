package ru.geekbrains.movies.ui.settings


import ru.geekbrains.movies.R
import ru.geekbrains.movies.databinding.FragmentSettingsBinding
import ru.geekbrains.movies.domain.AppState
import ru.geekbrains.movies.domain.IAppState
import ru.geekbrains.movies.ui.base.BaseFragment
import ru.geekbrains.movies.utils.showSnakeBar
import org.koin.androidx.viewmodel.ext.android.viewModel



class SettingsFragment : BaseFragment<FragmentSettingsBinding>(R.layout.fragment_settings) {
    val viewModel: SettingsViewModel by viewModel()

    override fun initListeners() {}
    override fun initObservers() {}
    override fun renderSuccess(result: AppState.Success<*>) {}
    override fun showLoading(isShow: Boolean) {}
    override fun showError(throwable: Throwable) {
        viewBinding.root.showSnakeBar(throwable.localizedMessage)
    }
}