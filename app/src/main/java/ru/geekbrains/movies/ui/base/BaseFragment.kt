package ru.geekbrains.movies.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import ru.geekbrains.movies.domain.AppState
import ru.geekbrains.movies.domain.IAppState
import ru.geekbrains.movies.ui.ToolbarListener
import java.lang.reflect.ParameterizedType


abstract class BaseFragment<VB : ViewBinding>(
    @LayoutRes contentLayoutId: Int
) : Fragment(contentLayoutId) {
    private var binding: VB? = null
    protected val viewBinding: VB get() = binding!!

    /**
     * Инициализация слушателей
     */
    abstract fun initListeners()

    /**
     * Инициализация подписок
     */
    abstract fun initObservers()
    protected fun renderData(result: IAppState) {
        when (result) {
            is AppState.Error -> {
                showLoading(false)
                showError(result.error)
            }
            is AppState.Loading -> showLoading(true)
            is AppState.Success<*> -> renderSuccess(result)
        }
    }

    abstract fun renderSuccess(result: AppState.Success<*>)
    abstract fun showLoading(isShow: Boolean)
    abstract fun showError(throwable: Throwable)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ((javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<VB>)
            .getMethod(
                "inflate",
                LayoutInflater::class.java,
                ViewGroup::class.java,
                Boolean::class.java
            ).also {
                binding = it.invoke(null, layoutInflater, container, false) as VB
            }

        initObservers()
        initListeners()
        return viewBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    protected fun showToolBar(visible: Boolean) {
        (requireActivity() as ToolbarListener).showToolBar(visible)
    }
}