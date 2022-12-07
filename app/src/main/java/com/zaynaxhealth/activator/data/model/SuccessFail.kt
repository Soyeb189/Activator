package com.zaynaxhealth.activator.data.model

import java.io.Serializable

data class SuccessFail (
    val title: String,
    val subTitle: String,
    val buttonMessage: String,
    val from: String?,
):Serializable