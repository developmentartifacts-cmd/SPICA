package com.gibson.spica.utils

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.FirebaseApp

/**
 * Simple analytics helper to log Firebase events across SPICA.
 * Keeps event tracking consistent and centralized.
 */
object AnalyticsHelper {

    private val analytics: FirebaseAnalytics by lazy {
        FirebaseAnalytics.getInstance(FirebaseApp.getInstance())
    }

    /**
     * Log a named event with optional parameters.
     */
    fun logEvent(eventName: String, params: Map<String, Any>? = null) {
        val bundle = android.os.Bundle()
        params?.forEach { (key, value) ->
            when (value) {
                is String -> bundle.putString(key, value)
                is Int -> bundle.putInt(key, value)
                is Long -> bundle.putLong(key, value)
                is Double -> bundle.putDouble(key, value)
                is Boolean -> bundle.putBoolean(key, value)
            }
        }
        analytics.logEvent(eventName, bundle)
    }

    /**
     * Set the current screen name for analytics tracking.
     */
    fun setCurrentScreen(screenName: String) {
        // Note: This must be called with an Activity context — handled at screen level if needed
        // FirebaseAnalytics.getInstance(context).setCurrentScreen(activity, screenName, null)
        // We'll keep a placeholder call until integrated with specific activities
    }

    /**
     * Log a user property (e.g., user_type = "premium").
     */
    fun setUserProperty(key: String, value: String) {
        analytics.setUserProperty(key, value)
    }
}
