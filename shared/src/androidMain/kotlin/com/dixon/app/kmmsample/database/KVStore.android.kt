@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.dixon.app.kmmsample.database

import android.content.Context
import com.liftric.kvault.KVault
import java.lang.ref.WeakReference

actual object KVStore {

    var contextRef = WeakReference<Context?>(null)

    private var kVault: KVault? = null

    private val ready: Boolean
        get() = kVault != null

    @JvmStatic
    actual fun database(): KVault? {
        if (!ready) {
            val context = contextRef.get() ?: return null
            kVault = KVault(context, "kmmSampleKVStore")
        }
        return kVault
    }
}



