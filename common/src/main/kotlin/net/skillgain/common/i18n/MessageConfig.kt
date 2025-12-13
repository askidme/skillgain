package net.skillgain.common.i18n

import net.skillgain.common.i18n.MessageBundles.BASE_NAMES
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean

@Configuration
class MessageConfig {
    @Bean
    fun messageSource(): MessageSource {
        return ResourceBundleMessageSource().apply {
            setBasenames(*BASE_NAMES)
            setDefaultEncoding("UTF-8")
            setUseCodeAsDefaultMessage(true)
        }
    }

    @Bean
    fun validator(messageSource: MessageSource): LocalValidatorFactoryBean {
        val validator = LocalValidatorFactoryBean()
        validator.setValidationMessageSource(messageSource)
        return validator
    }
}