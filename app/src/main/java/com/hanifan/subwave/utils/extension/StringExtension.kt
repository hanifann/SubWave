package com.hanifan.subwave.utils.extension

fun String?.ensureError(): String {
    return this ?: "An unknown error occurred, please try again later"
}