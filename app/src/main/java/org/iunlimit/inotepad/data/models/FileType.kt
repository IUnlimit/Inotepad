package org.iunlimit.inotepad.data.models

import org.iunlimit.inotepad.R

enum class FileType(
    val value: String,
    val color: Int
) {

    TXT(".txt", R.color.white),
    MD(".md", R.color.red),

    JSON(".json", R.color.red),
    XML(".xml", R.color.red),
    HTML(".html", R.color.red),

    JS(".js", R.color.red),
    JAVA(".java", R.color.red),
    C(".c", R.color.red),
    H(".h", R.color.red),
    GO(".go", R.color.red),
    PY(".py", R.color.red);

    companion object {

        /**
         * @throws NoSuchElementException
         * */
        fun parse(s: String): FileType {
            return values().first { type ->
                type.value == s
            }
        }

    }

}