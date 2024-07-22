package org.example.api

import java.util.*

fun createBasicAuthTokenForHeader(username: String, password: String) =
    StringBuilder("Basic ").append(Base64.getEncoder().encodeToString("$username:$password".toByteArray()))
        .toString()