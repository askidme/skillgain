package net.skillgain.common.i18n

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.context.support.ResourceBundleMessageSource
import java.util.*

class MessageResolverTest {

    private val messageSource = ResourceBundleMessageSource().apply {
        setBasename("messages")
        setDefaultEncoding("UTF-8")
        setUseCodeAsDefaultMessage(true)
    }

    private val messageResolver = MessageResolver(messageSource)

    @Test
    fun `should resolve simple message`() {
        val result = messageResolver.getMessage("simple.message")
        assertThat(result).isEqualTo("Just a plain message.")
    }

    @Test
    fun `should resolve message with argument`() {
        val result = messageResolver.getMessage("greeting", arrayOf("Idris"))
        assertThat(result).isEqualTo("Hello, Idris!")
    }

    @Test
    fun `should return default message when key not found`() {
        val result = messageResolver.getMessage("nonexistent.key", defaultMessage = "Fallback")
        assertThat(result).isEqualTo("Fallback")
    }

    @Test
    fun `should fallback to code when key not found and no default given`() {
        val result = messageResolver.getMessage("missing.key")
        assertThat(result).isEqualTo("missing.key")
    }

    @Test
    fun `should resolve with explicit locale`() {
        val result = messageResolver.getMessage("simple.message", locale = Locale.US)
        assertThat(result).isEqualTo("Just a plain message.")
    }
}