package tech.pixelw.flick.core.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import tech.pixelw.flick.R
import tech.pixelw.flick.core.misc.LogUtil

abstract class BaseActivity<B : ViewDataBinding> : AppCompatActivity() {

    protected lateinit var binding: B

    protected val edge2EdgeBuilder: ImmersiveUtilX.Edge2EdgeBuilder by lazy {
        ImmersiveUtilX.Edge2EdgeBuilder(this, window)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (usingBinding()) {
            binding = DataBindingUtil.setContentView(this, layoutID)
            require(this::binding.isInitialized) { "DataBindingUtil.setContentView returns a null binding object" }
        } else {
            setContentView(layoutID)
        }
        if (mainFragment != null && savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(R.id.fragment_container_view, mainFragment!!)
            }
        }
        LogUtil.d(TAG, "BaseActivity onCreate finish")
        onCreate()
    }

    fun makeToast(string: String?) {
        runOnUiThread { Toast.makeText(this, string, Toast.LENGTH_LONG).show() }
    }

    protected open val layoutID: Int = R.layout.activity_fragment_container
    protected open fun onCreate() {}

    /**
     * 是否使用binding
     *
     * @return 是否使用binding
     */
    open fun usingBinding() = false

    protected open val mainFragment: Fragment? = null

    companion object {
        private const val TAG = "BaseActivity"
    }
}