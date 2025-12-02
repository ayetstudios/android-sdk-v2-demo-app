package com.example.androidsdkv2demoapp

import com.ayet.sdk.AyetSdk

/**
 * Ayet SDK Configuration
 *
 * Modify these values to match your Ayet account settings.
 */
object AyetConfig {

    // SDK version
    const val SDK_VERSION = "1.0.0"

    // Your Ayet placement ID
    const val PLACEMENT_ID = 21209

    // Ad slot names (as configured in your Ayet dashboard)
    const val ADSLOT_OFFERWALL = "SdkV2Offerwall"
    const val ADSLOT_SURVEYWALL = "SdkV2Surveywall"
    const val ADSLOT_FEED = "SdkV2OfferwallApi"

    // User targeting defaults
    val DEFAULT_GENDER = AyetSdk.Gender.MALE
    const val DEFAULT_AGE = 27
    const val DEFAULT_CUSTOM_1 = "demo_app_example_custom"
}
