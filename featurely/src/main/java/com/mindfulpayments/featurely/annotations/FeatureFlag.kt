package com.mindfulpayments.featurely.annotations

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class FeatureFlag(
    val name: String,
    val default: Boolean
)

