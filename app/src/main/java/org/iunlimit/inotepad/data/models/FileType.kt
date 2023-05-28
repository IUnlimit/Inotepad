package org.iunlimit.inotepad.data.models

import org.iunlimit.inotepad.R
import java.util.Locale

enum class FileType(
    val value: String,
    val color: Int,
    val clazz: Short
) {

    MD(".md", R.color.md, MD_CLAZZ),

    TXT(".txt", R.color.white, TXT_CLAZZ),
    JSON(".json", R.color.json, CODE_CLAZZ),
    XML(".xml", R.color.xml, CODE_CLAZZ),
    HTML(".html", R.color.html, CODE_CLAZZ),
    JS(".js", R.color.js, CODE_CLAZZ),
    JAVA(".java", R.color.java, CODE_CLAZZ),
    C(".c", R.color.c, CODE_CLAZZ),
    H(".h", R.color.h, CODE_CLAZZ),
    GO(".go", R.color.go, CODE_CLAZZ),
    PY(".py", R.color.py, CODE_CLAZZ),

    PNG(".png", R.color.png, IMAGE_CLAZZ),
    JPG(".jpg", R.color.jpg, IMAGE_CLAZZ),
    JPEG(".jpeg", R.color.jpg, IMAGE_CLAZZ),
    GIF(".gif", R.color.gif, IMAGE_CLAZZ),

    DOC(".doc", R.color.doc, OFFICE_CLAZZ),
    DOCX(".docx", R.color.doc, OFFICE_CLAZZ),
    XLS(".xls", R.color.xls, OFFICE_CLAZZ),
    XLSX(".xlsx", R.color.xls, OFFICE_CLAZZ),
    PPT(".ppt", R.color.ppt, OFFICE_CLAZZ),
    PDF(".pdf", R.color.pdf, OFFICE_CLAZZ),

    ZIP(".zip", R.color.zip, COMPRESS_CLAZZ),
    RAR(".rar", R.color.rar, COMPRESS_CLAZZ),

    UNKNOWN(".blob", R.color.darkGray, BLOB_CLAZZ);

    /**
     * 类型是否可直接文本化
     * */
    fun isEditable(): Boolean {
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
const val TXT_CLAZZ: Short = 1
const val MD_CLAZZ: Short = 2
const val IMAGE_CLAZZ: Short = 3
const val OFFICE_CLAZZ: Short = 4
const val COMPRESS_CLAZZ: Short = 5
const val BLOB_CLAZZ: Short = 6