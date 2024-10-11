@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.dixon.app.kmmsample.database

import com.liftric.kvault.KVault

actual object KVStore {

    private var kVault: KVault? = null

    actual fun database(): KVault? = KVault()
}
