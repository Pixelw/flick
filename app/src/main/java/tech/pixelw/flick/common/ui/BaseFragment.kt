package tech.pixelw.flick.common.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment<B : ViewDataBinding>(@LayoutRes open val layoutId: Int) : Fragment() {

    protected lateinit var binding: B

    open fun usingBinding() = false

    /**
     * 填充view, 子类实现initView建议在 onViewCreated 进行
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (usingBinding()) {
            binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
            require(this::binding.isInitialized) { "DataBindingUtil.inflate returns a null binding object" }
            return binding.root
        }
        return inflater.inflate(layoutId, container, false)
    }

}