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

    protected var binding: B? = null

    open fun usingBinding() = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (usingBinding()) {
            binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
            return binding?.root
        }
        return inflater.inflate(layoutId, container, false)
    }

}