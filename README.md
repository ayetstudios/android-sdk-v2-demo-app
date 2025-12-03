# Ayet Android SDK v2 Demo App

A demo application showcasing the integration of the [Ayet Android SDK v2](https://central.sonatype.com/artifact/io.ayet/android-sdk-v2).

<table>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/ccbf118c-dbbc-4c52-bcd9-fd507148154c" width="250"></td>
    <td><img src="https://github.com/user-attachments/assets/73985f81-268d-4b63-aab2-5a65b2807a35" width="250"></td>
  </tr>
</table>

## Requirements

- Android SDK 24+ (Android 7.0)
- JDK 11+

## Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/AyetStudios/android-sdk-v2-demo-app.git
   ```

2. Open the project in Android Studio

3. Sync Gradle and run on a device or emulator

## Configuration

All SDK configuration values are in [`AyetConfig.kt`](app/src/main/java/com/example/androidsdkv2demoapp/AyetConfig.kt):

```kotlin
object AyetConfig {
    const val PLACEMENT_ID = 21209

    const val ADSLOT_OFFERWALL = "SdkV2Offerwall"
    const val ADSLOT_SURVEYWALL = "SdkV2Surveywall"
    const val ADSLOT_FEED = "SdkV2OfferwallApi"

    val DEFAULT_GENDER = AyetSdk.Gender.MALE
    const val DEFAULT_AGE = 27
    const val DEFAULT_CUSTOM_1 = "demo_app_example_custom"
}
```
**Note**: The package name must be set to `com.example.androidsdkv2demoapp` in your placement settings to use the demo app:
<img width="1170" height="322" alt="image" src="https://github.com/user-attachments/assets/2310f9d2-7d41-4efa-b5f0-38dc134f24a3" />


## Features Demonstrated

- SDK initialization
- Show Offerwall
- Show Surveywall
- Show Reward Status
- Get Offers (API)
- External identifier management

## Documentation

For full SDK documentation, see: https://docs.ayetstudios.com/v/product-docs/offerwall/sdk-integrations-v2/android-sdk-v2

## License

MIT License - see [LICENSE](LICENSE) for details.
