package com.example.aS8131475assignment2.util

/** Turns an API field name into a readable label, e.g. "currentPrice" -> "Current Price". */
fun String.toDisplayLabel(): String =
    replace(Regex("([a-z0-9])([A-Z])"), "\$1 \$2")
        .replace('_', ' ')
        .replaceFirstChar { it.uppercase() }
