package org.iunlimit.inotepad.data.models

import org.iunlimit.inotepad.R
import java.util.*

enum class FileType(
    val value: String,
    val color: Int,
    private val clazz: Short
) {

    MD(".md", R.color.red, MD_CLAZZ),

    PNG(".png", R.color.red, IMAGE_CLAZZ),
    JPG(".jpg", R.color.red, IMAGE_CLAZZ),
    JPEG(".jpeg", R.color.red, IMAGE_CLAZZ),
    GIF(".gif", R.color.red, IMAGE_CLAZZ),

    DOC(".doc", R.color.red, OFFICE_CLAZZ),
    DOCX(".docx", R.color.red, OFFICE_CLAZZ),
    XLS(".xls", R.color.red, OFFICE_CLAZZ),
    XLSX(".xlsx", R.color.red, OFFICE_CLAZZ),
    PPT(".ppt", R.color.red, OFFICE_CLAZZ),
    PDF(".pdf", R.color.red, OFFICE_CLAZZ),

    TXT(".txt", R.color.white, CODE_CLAZZ),
    JSON(".json", R.color.red, CODE_CLAZZ),
    XML(".xml", R.color.red, CODE_CLAZZ),
    HTML(".html", R.color.red, CODE_CLAZZ),
    JS(".js", R.color.red, CODE_CLAZZ),
    JAVA(".java", R.color.red, CODE_CLAZZ),
    C(".c", R.color.red, CODE_CLAZZ),
    H(".h", R.color.red, CODE_CLAZZ),
    GO(".go", R.color.red, CODE_CLAZZ),
    PY(".py", R.color.red, CODE_CLAZZ),

    UNKNOWN(".blob", R.color.darkGray, BLOB_CLAZZ);

    /**
     * 类型是否可直接文本化
     * */
    fun isTextType(): Boolean {
        return clazz <= MD_CLAZZ
    }

    companion object {

        /**
         * @throws NoSuchElementException
         * */
        fun parse(s: String?): FileType {
            if (s == null) return UNKNOWN
            return values().firstOrNull { type ->
                type.value == s.toLowerCase(Locale.CHINA)
            } ?: UNKNOWN
        }

    }

}

const val CODE_CLAZZ: Short = 0
const val MD_CLAZZ: Short = 1
const val IMAGE_CLAZZ: Short = 2
const val OFFICE_CLAZZ: Short = 3
const val BLOB_CLAZZ: Short = 4