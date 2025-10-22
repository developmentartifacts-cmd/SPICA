package com.gibson.spica.utils

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.FirebaseAnalytics.Event
import com.google.firebase.analytics.FirebaseAnalytics.Param

/**
 * Handles Firebase Analytics event logging and screen tracking.
 * Requires a Context from an Activity or Application.
 */
object AnalyticsHelper {

    private var firebaseAnalytics: FirebaseAnalytics? = null

    /**
     * Initialize Analytics. Must be called once (e.g., in MainActivity.onCreate()).
     */
    fun init(context: Context) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(context)
    }

    /**
     * Log a custom event.
     */
    fun logEvent(eventName: String, params: Map<String, String> = emptyMap()) {
        val analytics = firebaseAnalytics ?: return
        val bundle = android.os.Bundle()
        params.forEach { (key, value) -> bundle.putString(key, value) }
        analytics.logEvent(eventName, bundle)
    }

    /**
     * Track a screen view.
     */
    fun logScreenView(screenName: String) {
        val analytics = firebaseAnalytics ?: return
        val bundle = android.os.Bundle().apply {
            putString(Param.SCREEN_NAME, screenName)
            putString(Param.SCREEN_CLASS, screenName)
        }
        analytics.logEvent(Event.SCREEN_VIEW, bundle)
    }
}
