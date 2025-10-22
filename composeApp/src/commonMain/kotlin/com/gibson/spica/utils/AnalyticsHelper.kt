package com.gibson.spica.utils

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase

/**
 * Simple analytics helper to log Firebase events across SPICA.
 * Keeps event tracking consistent and centralized.
 */
object AnalyticsHelper {

    private val analytics: FirebaseAnalytics by lazy {
        FirebaseAnalytics.getInstance(com.google.firebase.ktx.Firebase.app)
    }

    /**
     * Log a named event with optional parameters.
     */
    fun logEvent(eventName: String, params: Map<String, Any>? = null) {
        Firebase.analytics.logEvent(eventName) {
            params?.forEach { (key, value) ->
                when (value) {
                    is String -> param(key, value)
                    is Int -> param(key, value.toLong())
                    is Long -> param(key, value)
                    is Double -> param(key, value)
                    is Boolean -> param(key, if (value) 1L else 0L)
                }
            }
        }
    }

    /**
     * Set the current screen name for analytics tracking.
     */
    fun setCurrentScreen(screenName: String) {
        analytics.setCurrentScreen(
            /* activity = */ null,
            /* screenName = */ screenName,
            /* screenClassOverride = */ null
        )
    }

    /**
     * Log a user property (e.g., user_type = "premium").
     */
    fun setUserProperty(key: String, value: String) {
        analytics.setUserProperty(key, value)
    }
}
