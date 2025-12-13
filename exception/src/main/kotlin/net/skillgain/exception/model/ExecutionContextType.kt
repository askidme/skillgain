package net.skillgain.exception.model

enum class ExecutionContextType(val code: String) {
    WEB("web"),
    SYSTEM("system");

    companion object {
        fun from(hasHttpRequest: Boolean): ExecutionContextType =
            if (hasHttpRequest) WEB else SYSTEM
    }
}