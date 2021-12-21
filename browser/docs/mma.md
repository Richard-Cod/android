 MMA (Mobile Marketing Automation)
================================

Mozilla wants to engage with users more. MMA is the project for this purpose. When a user performs a certain UI action (or set of UI actions), she will see a prompt and have a chance to interact with it. For example, if a user uses Firefox 10 times a week, but Firefox is not her default browser, we'll prompt the user the next time when she launches our app, and guide her to set us as default browser.

Mozilla is using a third party framework called "Leanplum" in order to achieve this. Leanplum is a San Francisco company, founded in 2012. We put their SDK in our codebase via Carthage.

The SDK is documented at https://www.leanplum.com/docs/android

There are four major components in Leanplum SDK.
1. Events : Events are fired when users perform certain actions.
2. User Attributes: User Attributes are set on a per-user basis, and inform us about an aspect of the user.
3. Deep Links: Actions that users can perform to interact with the Message.
4. Messages:  User Interaction points that we want to engage with users that help them use Firefox better.

An Event or a series of Events plus some User Attributes may trigger a Message, and when the user acts on the Message, a Deep Link may be processed.


Data collection
=====================================================


Who will have Leanplum enabled?
======================================================

Everyone with Telemetry enabled.


Where does data sent to the Leanplum backend go?
==============================================

The Leanplum SDK is hard-coded to send data to the endpoint https://www.leanplum.com.  The endpoint is
defined by ``com.leanplum.internal.Constants.API_HOST_NAME`` at https://github.com/Leanplum/Leanplum-Android-SDK/blob/master/AndroidSDKCore/src/main/java/com/leanplum/internal/Constants.java#L32

The user is identified by Leanplum using a random UUID generated by us when Leanplum is initialized for the first time. See: https://github.com/mozilla-mobile/fenix/blob/master/app/src/main/java/org/mozilla/fenix/components/metrics/LeanplumMetricsService.kt
This unique identifier is only used by Leanplum and can't be tracked back to any Firefox users.


What data is collected and sent to the Leanplum backend?
======================================================

The Leanplum SDK collects and sends the following information at various times while the SDK is in use.

Sent every time when an event is triggered:
~~~~~~~~~~~~~~~
  "action" -> "track"                   // track: an event is tracked.
  "event" -> "Launch"                   // Used when an event is triggered. e.g. E_Saved_Bookmark.
  "info" -> ""                          // Used when an event is triggered. Basic context associated with the event.
  "value" -> 0.0                        // Used when an event is triggered. Value of that event.
  "messageId" -> 5111602214338560       // Used when an event is triggered. The ID of the message.
~~~~~~~~~~~~~~~

Sent when the app starts:
~~~~~~~~~~~~~~~
  "action" -> "start"                   // start: Leanplum SDK starts. heartbeat
  "userAttributes" -> "{                // A set of key-value pairs used to describe the user.
    "Focus Installed" -> true           // If Focus for Android is installed.
    "Klar Installed" -> true            // If Klar for Android is installed.
    "Fennec Installed" -> true          // If Fennec for Android is installed.
  }"
  "appId" -> "app_6Ao...."              // Leanplum App ID.
  "clientKey" -> "dev_srwDUNZR...."     // Leanplum client access key.
  "systemName" -> "Android"                 // Fixed String in SDK.
  "locale" -> "zh_TW"                   // System Locale.
  "timezone" -> "Asia/Taipei"           // System Timezone.
  "versionName" -> "55.0a1"             // Fennec version.
  "systemVersion" -> "10.3.1"            // System version.
  "deviceModel" -> "Galaxy"             // System device model.
  "timezoneOffsetSeconds" -> "28800"    // User timezone offset with PST.
  "deviceName" -> "sdaswani-31710"       // System device name.
  "region" -> "(detect)"                // Not used. We strip location so this is will be the default stub value in Leanplum SDK.
  "city" -> "(detect)"                  // Same as above.
  "country" -> "(detect)"               // Same as above.
  "location" -> "(detect)"              // Same as above.
  "newsfeedMessages" -> " size = 0"     // Not used. New Leanplum Inbox message(Leanplum feature) count.
  "includeDefaults" -> "false"          // Not used. Always false.
 ~~~~~~~~~~~~~~~

Sent every time a session is renewed or has a state change:
~~~~~~~~~~~~~~~
  "action" -> "heartbeat"               // heartbeat: every 15 minutes when app is in the foreground
                                        // pauseSession: when app goes to background
                                        // resumeSession: when app goes to foreground
~~~~~~~~~~~~~~~
                         
                        
Sent for every Message:
~~~~~~~~~~~~~~~
  "userId" -> "b13b3c239d01aa7c"        // Set by Fennec, we use random uuid so users are anonymous to Leanplum.
  "deviceId" -> "b13b3c239d01aa7c"      // Same as above.
  "sdkVersion" -> "2.2.2-SNAPSHOT"      // Leanplum SDK version.
  "devMode" -> "true"                   // If the SDK is in developer mode. For official builds, it's false.
  "time" -> "1.497595093902E9"          // System time in second.
  "token" -> "nksZ5pa0R5iegC7wj...."    // Token come from Leanplum backend.
~~~~~~~~~~~~~~~

Notes on what data is collected
===============================
User Identifier
---------------
Since Device ID is a random UUID, Leanplum can't map the device to any know Client ID in Fennec nor Advertising ID.

User Attributes
---------------

<table>
  <tr>
    <th>Key</th>
    <th>Description</th>
    <th>Data Review</th>
  </tr>
  <tr>
    <td>`default_browser`</td>
    <td>A string containing the name of the default browser if property of Mozilla or an empty string</td>
    <td><a href="https://github.com/mozilla-mobile/fenix/pull/3459#issuecomment-502252010">#3459</a></td>
  </tr>
  <tr>
    <td>`focus_installed`</td>
    <td>A boolean indicating that Firefox Focus is installed</td>
    <td><a href="https://github.com/mozilla-mobile/fenix/pull/3459#issuecomment-502252010">#3459</a></td>
  </tr>
  <tr>
    <td>`klar_installed`</td>
    <td>A boolean indicating that Firefox Klar is installed</td>
    <td><a href="https://github.com/mozilla-mobile/fenix/pull/3459#issuecomment-502252010">#3459</a></td>
  </tr>
  <tr>
    <td>`fennec_installed`</td>
    <td>A boolean indicating that Fennec is installed</td>
    <td><a href="https://github.com/mozilla-mobile/fenix/pull/3459#issuecomment-502252010">#3459</a></td>
  </tr> 
  <tr>
    <td>`fxa_signed_in`</td>
    <td>A boolean indicating that the user is signed in to FxA</td>
    <td><a href="https://github.com/mozilla-mobile/fenix/pull/4568#issuecomment-519159545">#4568</a></td>
  </tr>
  <tr>
    <td>`fxa_has_synced_items`</td>
    <td>A boolean indicating that the user has opted to sync at least one category of items with FxA</td>
    <td><a href="https://github.com/mozilla-mobile/fenix/pull/4568#issuecomment-519159545">#4568</a></td>
  </tr>
  <tr>
    <td>`search_widget_installed`</td>
    <td>A boolean indicating that the user has at least one search widget placed on the home screen</td>
    <td><a href="https://github.com/mozilla-mobile/fenix/pull/4694#issuecomment-520591275">#4694</a></td>
  </tr>
  <tr>
    <td>`tracking_protection_enabled`</td>
    <td>A boolean indicating that the user has enabled tracking protection</td>
    <td><a href="https://github.com/mozilla-mobile/fenix/pull/11965#issuecomment-649731798">#11965</a></td>
  </tr>
  <tr>
    <td>`tracking_protection_setting`</td>
    <td>A string indicating the level at which the user has set tracking protection. Possible values are `none`, `standard`, `strict` and `custom`</td>
    <td><a href="https://github.com/mozilla-mobile/fenix/pull/11965#issuecomment-649731798">#11965</a></td>
  </tr>
  <tr>
    <td>`fenix`</td>
    <td>A boolean indicating that this is a Fenix installation</td>
    <td><a href="https://github.com/mozilla-mobile/fenix/pull/8208">#8208</a></td>
  </tr>
  <tr>
      <td>`installed_addons`</td>
      <td>A boolean indicating that there are addons installed</td>
      <td><a href="https://github.com/mozilla-mobile/fenix/pull/13233">#13233</a></td>
  </tr>
  <tr>
      <td>`enabled_addons`</td>
      <td>A boolean indicating that there are addons enabled</td>
      <td><a href="https://github.com/mozilla-mobile/fenix/pull/13233">#13233</a></td>
  </tr>
</table>

Events
-------
Most of the Leanplum events can be mapped to a single combination of Telemetry event (Event+Method+Extra).
Some events are not collected in Mozilla Telemetry. This will be addressed separately in each campaign review.
There are three elements that are used for each event. They are: event name, value(default: 0.0), and info(default: "").
Default value for event value is 0.0. Default value for event info is empty string.

Here is the list of current Events sent, which can be found here in the code base: https://github.com/mozilla-mobile/fenix/blob/master/app/src/main/java/org/mozilla/fenix/components/metrics/LeanplumMetricsService.kt

<table>
  <tr>
    <th>Event</th>
    <th>Description</th>
    <th>Data Review</th>
  </tr>
  <tr>
    <td>`E_Opened_App_FirstRun`</td>
    <td>The first launch after install</td>
    <td><a href="https://github.com/mozilla-mobile/fenix/pull/3459#issuecomment-502252010">#3459</a></td>
  </tr>
  <tr>
    <td>`E_Opened_App`</td>
    <td>Whenever the App is launched.</td>
    <td><a href="https://github.com/mozilla-mobile/fenix/pull/3459#issuecomment-502252010">#3459</a></td>
  </tr>
  <tr>
    <td>`E_Interact_With_Search_URL_Area`</td>
    <td>The user interacts with search url area.</td>
    <td><a href="https://github.com/mozilla-mobile/fenix/pull/3459#issuecomment-502252010">#3459</a></td>
  </tr>
  <tr>
    <td>`E_Opened_Bookmark`</td>
    <td>The user opened a bookmark</td>
    <td><a href="https://github.com/mozilla-mobile/fenix/pull/3632#issuecomment-505135753">#3632</a></td>
  </tr>
  <tr>
    <td>`E_Add_Bookmark`</td>
    <td>The user added a bookmark</td>
    <td><a href="https://github.com/mozilla-mobile/fenix/pull/3632#issuecomment-505135753">#3632</a></td>
  </tr> 
  <tr>
    <td>`E_Remove_Bookmark`</td>
    <td>The user removed a bookmark</td>
    <td><a href="https://github.com/mozilla-mobile/fenix/pull/3632#issuecomment-505135753">#3632</a></td>
  </tr> 
  <tr>
    <td>`E_Collection_Created`</td>
    <td>The user created a new collection</td>
    <td><a href="https://github.com/mozilla-mobile/fenix/pull/4626#issuecomment-519691332">#4626</a></td>
  </tr> 
  <tr>
    <td>`E_Collection_Tab_Opened`</td>
    <td>The user opened a tab from a previously created collection</td>
    <td><a href="https://github.com/mozilla-mobile/fenix/pull/4626#issuecomment-519691332">#4626</a></td>
  </tr>
  <tr>
    <td>`E_FxA_New_Signup`</td>
    <td>The user completed the signup process to new FxA account</td>
    <td><a href="https://github.com/mozilla-mobile/fenix/pull/4626#issuecomment-519691332">#4626</a></td>
  </tr>
  <tr>
    <td>`E_Sign_In_FxA`</td>
    <td>The user successfully signed in to FxA</td>
    <td><a href="https://github.com/mozilla-mobile/fenix/pull/4626#issuecomment-519691332">#4626</a></td>
  </tr>
  <tr>
    <td>`E_Sign_In_FxA_Fennec_to_Fenix`</td>
    <td>The user successfully signed in to FxA using previously signed in Fennec account</td>
    <td><a href="https://github.com/mozilla-mobile/fenix/pull/4626#issuecomment-519691332">#4626</a></td>
  </tr>
  <tr>
    <td>`E_Sign_Out_FxA`</td>
    <td>The user successfully signed out of FxA</td>
    <td><a href="https://github.com/mozilla-mobile/fenix/pull/4626#issuecomment-519691332">#4626</a></td>
  </tr>
  <tr>
    <td>`E_Cleared_Private_Data`</td>
    <td>The user cleared one or many types of private data</td>
    <td><a href="https://github.com/mozilla-mobile/fenix/pull/4626#issuecomment-519691332">#4626</a></td>
  </tr>
  <tr>
    <td>`E_Dismissed_Onboarding`</td>
    <td>The user finished onboarding. Could be triggered by pressing "start browsing," opening settings, or invoking a search.</td>
    <td><a href="https://github.com/mozilla-mobile/fenix/pull/3459#issuecomment-502191109">#3459</a></td>
  </tr>
  <tr>
    <td>`E_Fennec_To_Fenix_Migrated`</td>
    <td>The user has just migrated from Fennec to Fenix.</td>
    <td><a href="https://github.com/mozilla-mobile/fenix/pull/8208#issuecomment-584040440">#8208</a></td>
  </tr>
  <tr>
    <td>`E_Addon_Installed`</td>
    <td>The user has installed an addon from the addon management page.</td>
    <td><a href="https://github.com/mozilla-mobile/fenix/pull/12136#issuecomment-651922547">#12136</a></td>
  </tr>
  <tr>
    <td>`E_Search_Widget_Added`</td>
    <td>The user has installed the search widget to their homescreen.</td>
    <td><a href="https://github.com/mozilla-mobile/fenix/pull/13003">#13003</a></td>
  </tr>
  <tr>
    <td>`E_Changed_Default_To_Fenix`</td>
    <td>The user has changed their default browser to Fenix while Fenix was in the background and then resumed the app.</td>
    <td><a href="https://github.com/mozilla-mobile/fenix/pull/13003">#13003</a></td>
  </tr>
  <tr>
    <td>`E_Changed_ETP`</td>
    <td>The user has changed their enhanched tracking protection setting.</td>
    <td><a href="https://github.com/mozilla-mobile/fenix/pull/13003">#13003</a></td>
  </tr>
</table>

Deep links
-------
Deep links are hooks utilized by marketing to direct users to certain portions of the application through a link. They can also be invoked by other applications or even users
directly to access specific screens quickly.

Here is the list of current deep links available, which can be found here in the code base: https://github.com/mozilla-mobile/fenix/blob/master/app/src/main/AndroidManifest.xml

<table>
  <tr>
    <th>Deep link</th>
    <th>Description</th>
  </tr>
  <tr>
    <td>`fenix://home`</td>
    <td>Opens to the Fenix home screen</td>
  </tr>
  <tr>
    <td>`fenix://urls_bookmarks`</td>
    <td>Opens to the list of the user's bookmarks at its root</td>
  </tr>
  <tr>
    <td>`fenix://urls_history`</td>
    <td>Opens to the list of pages the user has visited</td>
  </tr>
  <tr>
    <td>`fenix://home_collections`</td>
    <td>Opens to the list of collections the user has saved. It is implemented as `fenix://home`</td>
  </tr>
  <tr>
    <td>`fenix://settings`</td>
    <td>Opens to the top level settings screen</td>
  </tr>
  <tr>
    <td>`fenix://turn_on_sync`</td>
    <td>Opens to the turn on sync screen. **Only valid if the user is not signed in to FxA**</td>
  </tr>
  <tr>
    <td>`fenix://settings_search_engine`</td>
    <td>Opens to the search engine settings screen</td>
  </tr>
  <tr>
    <td>`fenix://settings_accessibility`</td>
    <td>Opens to the accessibility settings screen</td>
  </tr>
  <tr>
    <td>`fenix://settings_delete_browsing_data`</td>
    <td>Opens to the delete browsing data settings screen</td>
  </tr>
  <tr>
    <td>`fenix://settings_addon_manager`</td>
    <td>Opens to the settings page to install and manage addons</td>
  </tr>
  <tr>
    <td>`fenix://settings_logins`</td>
    <td>Opens to the Logins and passwords settings page configure how logins are treated. This is *not* the list of actual logins</td>
  </tr>
  <tr>
    <td>`fenix://settings_tracking_protection`</td>
    <td>Opens to the Enhanced Tracking Protection settings page</td>
  </tr>
  <tr>
    <td>`fenix://settings_privacy`</td>
    <td>Opens to the settings page which contains the privacy settings. Currently, this is the same as `fenix://settings`</td>
  </tr>
  <tr>
    <td>`fenix://enable_private_browsing`</td>
    <td>Opens to the Fenix home screen and enables private browsing</td>
  </tr>
  <tr>
    <td>`fenix://open?url={DESIRED_URL}`</td>
    <td>Creates a new tab, opens to the browser screen and loads the {DESIRED_URL}</td>
  </tr>
  <tr>
    <td>`fenix://make_default_browser`</td>
    <td>Opens to the Android default apps settings screen. **Only works on Android API >=24**</td>
  </tr>
  <tr>
    <td>`fenix://settings_notifications`</td>
    <td>Opens to the Android notification settings screen for Fenix. **Only works on Android API >=24**</td>
  </tr>
  <tr>
    <td>`fenix://install_search_widget`</td>
    <td>Adds the search widget to the users launcher homescreen. **Only works on Android API >=26**</td>
  </tr>
</table>

Messages
-----------
Messages are in-app prompts to the user from Leanplum. The user interaction of that prompt will be sent to the Leanplum backend (such as "Accept" or "Show") to track overall engagement with the Message. The Message is downloaded from Leanplum when the Leanplum SDK is initialized at App start, assuming the fulfillment criteria for the Message is met. As mentioned before, the fulfillment criteria is a set of required Events and User Attributes. The fulfillment criteria are set in the Leanplum backend.

We currently don't have any messages planned for Firefox Preview