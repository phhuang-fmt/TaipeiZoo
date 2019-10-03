package app.phhuang.training.model

import com.google.gson.annotations.SerializedName

data class ResultXXX(
    val F_AlsoKnown: String?,
    val F_Brief: String?,
    val F_CID: Any,
    val F_Code: Any,
    val F_Family: String,
    val F_Feature: String?,
    @SerializedName("F_Function＆Application")
    val F_Function_Application: String?,
    val F_Genus: String,
    val F_Geo: String,
    val F_Keywords: String,
    val F_Location: String,
    val F_Name_Ch: String?,
    val F_Name_En: String,
    val F_Name_Latin: String?,
    val F_Pic01_ALT: Any,
    val F_Pic01_URL: String?,
    val F_Pic02_ALT: Any,
    val F_Pic02_URL: String,
    val F_Pic03_ALT: Any,
    val F_Pic03_URL: String,
    val F_Pic04_ALT: Any,
    val F_Pic04_URL: String,
    val F_Summary: String,
    val F_Update: String?,
    val F_Vedio_URL: String,
    val F_Voice01_ALT: Any,
    val F_Voice01_URL: String,
    val F_Voice02_ALT: Any,
    val F_Voice02_URL: String,
    val F_Voice03_ALT: Any,
    val F_Voice03_URL: String,
    val F_pdf01_ALT: Any,
    val F_pdf01_URL: String,
    val F_pdf02_ALT: Any,
    val F_pdf02_URL: String,
    val _id: Int
)