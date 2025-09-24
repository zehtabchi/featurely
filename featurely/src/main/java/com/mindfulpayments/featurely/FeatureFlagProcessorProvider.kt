package com.mindfulpayments.featurely

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class FeatureFlagProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        environment.logger.info("Featurely KSP: FeatureFlagProcessorProvider is being loaded!")
        return FeatureFlagProcessor(environment)
    }
}