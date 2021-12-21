/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.fenix.settings

import android.content.Context
import android.content.Intent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import mozilla.components.support.ktx.android.content.appVersionName
import mozilla.components.support.ktx.android.content.getColorFromAttr
import org.mozilla.fenix.BuildConfig
import org.mozilla.fenix.Config
import org.mozilla.fenix.IntentReceiverActivity
import org.mozilla.fenix.R
import org.mozilla.fenix.settings.account.AuthIntentReceiverActivity
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.Locale

object SupportUtils {
    const val RATE_APP_URL = "market://details?id=" + BuildConfig.APPLICATION_ID
    const val POCKET_TRENDING_URL = "https://getpocket.com/fenix-top-articles"
    const val WIKIPEDIA_URL = "https://www.wikipedia.org/"
    const val FENIX_PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
    const val FIREFOX_BETA_PLAY_STORE_URL = "market://details?id=org.mozilla.firefox_beta"
    const val FIREFOX_NIGHTLY_PLAY_STORE_URL = "market://details?id=org.mozilla.fenix"
    const val GOOGLE_URL = "https://www.google.com/"

    enum class SumoTopic(internal val topicStr: String) {
        FENIX_MOVING("sync-delist"),
        HELP("faq-android"),
        PRIVATE_BROWSING_MYTHS("common-myths-about-private-browsing"),
        YOUR_RIGHTS("your-rights"),
        TRACKING_PROTECTION("tracking-protection-firefox-preview"),
        WHATS_NEW("whats-new-firefox-preview"),
        SEND_TABS("send-tab-preview"),
        SET_AS_DEFAULT_BROWSER("set-firefox-preview-default"),
        SEARCH_SUGGESTION("how-search-firefox-preview"),
        CUSTOM_SEARCH_ENGINES("custom-search-engines"),
        UPGRADE_FAQ("firefox-preview-upgrade-faqs"),
        SYNC_SETUP("how-set-firefox-sync-firefox-preview")
    }

    enum class MozillaPage(internal val path: String) {
        PRIVATE_NOTICE("about-ghostery/privacy-statements/"),
        MANIFESTO("about/manifesto/")
    }

    /**
     * Gets a support page URL for the corresponding topic.
     */
    @Suppress("UNUSED_PARAMETER")
    fun getSumoURLForTopic(
        context: Context,
        topic: SumoTopic,
        locale: Locale = Locale.getDefault()
    ): String {
        /* Ghostery Begin: Ghostery support url +/
        val escapedTopic = getEncodedTopicUTF8(topic.topicStr)
        // Remove the whitespace so a search is not triggered:
        val appVersion = context.appVersionName?.replace(" ", "")
        val osTarget = "Android"
        val langTag = getLanguageTag(locale)
        return "https://support.mozilla.org/1/mobile/$appVersion/$osTarget/$langTag/$escapedTopic"
        */
        return when (topic) {
            SumoTopic.HELP -> "https://8dgdupnn2g3.typeform.com/to/OM1DwbZg"
            // "What's new" is used also in the "About Ghostery" preferences entry
            SumoTopic.WHATS_NEW -> "https://www.mayyan.org/about"
            else -> "This topic is unsupported"
        }
        /* Ghostery End */
    }


    /**
     * Gets a support page URL for the corresponding topic.
     * Used when the app version and os are not part of the URL.
     */
    fun getGenericSumoURLForTopic(topic: SumoTopic, locale: Locale = Locale.getDefault()): String {
        val escapedTopic = getEncodedTopicUTF8(topic.topicStr)
        val langTag = getLanguageTag(locale)
        return "https://support.mozilla.org/$langTag/kb/$escapedTopic"
    }

    fun getFirefoxAccountSumoUrl(): String {
        return "https://support.mozilla.org/kb/access-mozilla-services-firefox-account"
    }

    /* Ghostery Begin: Privacy Policy points to Ghostery's website */
    @Suppress("UNUSED_PARAMETER")
    fun getMozillaPageUrl(page: MozillaPage, locale: Locale = Locale.getDefault()): String {
        val path = page.path
        return "https://www.ghostery.com/$path"
    }
    /* Ghostery End */

    fun getWhatsNewUrl(context: Context) = if (Config.channel.isFennec) {
        getGenericSumoURLForTopic(SumoTopic.UPGRADE_FAQ)
    } else {
        getSumoURLForTopic(context, SumoTopic.WHATS_NEW)
    }

    fun createCustomTabIntent(context: Context, url: String): Intent = CustomTabsIntent.Builder()
        .setInstantAppsEnabled(false)
        .setToolbarColor(context.getColorFromAttr(R.attr.foundation))
        .build()
        .intent
        .setData(url.toUri())
        .setClassName(context, IntentReceiverActivity::class.java.name)
        .setPackage(context.packageName)

    fun createAuthCustomTabIntent(context: Context, url: String): Intent =
        createCustomTabIntent(context, url).setClassName(context, AuthIntentReceiverActivity::class.java.name)

    private fun getEncodedTopicUTF8(topic: String): String {
        try {
            return URLEncoder.encode(topic, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            throw IllegalStateException("utf-8 should always be available", e)
        }
    }

    private fun getLanguageTag(locale: Locale): String {
        val language = locale.language
        val country = locale.country // Can be an empty string.
        return if (country.isEmpty()) language else "$language-$country"
    }
}
