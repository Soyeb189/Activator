package com.zaynaxhealth.activator.utilities

import java.util.*

class CamelCaseTitle {

    companion object {

        fun formatTitle(title: String): String {

            var title = title.substring(0, 1).uppercase(Locale.getDefault()) + title.substring(1)
                .lowercase(Locale.getDefault())

            var separator = "_"
            var replaceWith = " "
            var st = title
            if (st.contains(separator)) {

                var arr_word = st.split(separator)

                var finalStr = ""

                for (i in 0 until arr_word.size) {

                    var post = ""
                    if (i != arr_word.size - 1) {
                        post = replaceWith
                    }

                    finalStr = finalStr + arr_word[i].substring(0, 1)
                        .uppercase(Locale.getDefault()) + arr_word[i].substring(1)
                        .lowercase(Locale.getDefault()) + post
                }
                return finalStr
            }
            return title
        }
    }
}