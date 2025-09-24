package com.example.piapplication

import com.mindfulpayments.featurely.annotations.FeatureFlag

@FeatureFlag(name = "FeatureA", default = false)
object FeatureA

@FeatureFlag(name = "FeatureB", default = false)
object FeatureB