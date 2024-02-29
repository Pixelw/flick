package tech.pixelw.flick.core.ui

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

object LifecycleLogger : DefaultLifecycleObserver {

    private const val TAG = "LifecycleLogger"

    override fun onCreate(owner: LifecycleOwner) {
        Log.d(TAG, "onCreate() called with: owner = $owner")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        Log.d(TAG, "onDestroy() called with: owner = $owner")
    }

    override fun onPause(owner: LifecycleOwner) {
        Log.d(TAG, "onPause() called with: owner = $owner")
    }

    override fun onResume(owner: LifecycleOwner) {
        Log.d(TAG, "onResume() called with: owner = $owner")
    }

    override fun onStart(owner: LifecycleOwner) {
        Log.d(TAG, "onStart() called with: owner = $owner")
    }

    override fun onStop(owner: LifecycleOwner) {
        Log.d(TAG, "onStop() called with: owner = $owner")
    }
}