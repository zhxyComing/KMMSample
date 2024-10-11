@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.dixon.app.kmmsample.database

import com.liftric.kvault.KVault

expect object KVStore {

    fun database(): KVault?
}



