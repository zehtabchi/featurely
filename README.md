# featurely
Featurely: A KSP Feature Flag Library for Android

Featurely is a lightweight and easy-to-use library that leverages Kotlin Symbol Processing (KSP) to streamline feature flag management in your Android applications. By using annotations, the library automatically generates a centralized registry, eliminating manual setup and reducing the potential for errors.
✨ Features

    Automated Registry Generation: Use the @FeatureFlag annotation on a class or object, and Featurely automatically creates a FeatureFlagRegistry with a corresponding enum for each flag.

    Type-Safe Access: Access your feature flags using a generated, type-safe enum, preventing typos and ensuring all flags are correctly referenced.

    Centralized Management: All flags are managed in a single, convenient registry, making it simple to check, enable, or disable features at runtime.

    Default Values: Easily define a default value for each feature flag directly in the annotation.

🚀 Getting Started
Prerequisites

To use Featurely, you must have KSP enabled in your project.
Installation

Add the following dependencies to your module-level build.gradle.kts file:
    
    // build.gradle.kts (Module: app)
    plugins {
    id("com.google.devtools.ksp")
    }

    dependencies {
    // Add the Featurely library and its KSP processor
    implementation("com.mindfulpayments.featurely:featurely-lib:1.0.0") // Replace with your version
    ksp("com.mindfulpayments.featurely:featurely-processor:1.0.0") // Replace with your version
    }

Usage

    Define Your Feature Flags: Simply create a class or object and annotate it with @FeatureFlag.

    import com.mindfulpayments.featurely.annotations.FeatureFlag

    @FeatureFlag(name = "NewUserProfile", default = true)
    object NewUserProfile

    @FeatureFlag(name = "DarkMode", default = false)
    object DarkMode

    Access the Flags: The library will generate a FeatureFlagRegistry with a FeatureFlags enum. You can now access and modify your flags in a type-safe way.

    import com.mindfulpayments.featurely.FeatureFlagRegistry
    import com.mindfulpayments.featurely.FeatureFlags

    // Check a flag's status
    if (FeatureFlagRegistry.getFlag(FeatureFlags.NewUserProfile)) {
        // Show the new user profile UI
    } else {
        // Show the old UI
    }

    // Toggle a flag at runtime
    FeatureFlagRegistry.setFlag(FeatureFlags.DarkMode, true)

📚 Example Project

A simple example Android project demonstrating the usage of this library can be found in the repository's sample directory.
🤝 Contributing

Contributions are welcome! If you find a bug or have a feature request, please open an issue.
📝 License

This project is licensed under the MIT License - see the LICENSE file for details.
