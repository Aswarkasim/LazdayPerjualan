package com.scrollupstudio.penjualan.data.database

import android.content.Context
import android.content.SharedPreferences
import hu.autsoft.krate.Krate
import hu.autsoft.krate.booleanPref
import hu.autsoft.krate.stringPref

class PrefsManager (context: Context): Krate{

    private val PREFS_ON_BOARDING: String = "prefs_on_boarding"
    private val PREFS_IS_LOGIN: String = "prefs_is_login"
    private val PREFS_USERNAME: String = "prefs_username"
    private val PREFS_PASSWORD: String = "prefs_password"
    private val PREFS_NAMA_PEGAWAI: String = "prefs_nama_pegawai"
    private val PREFS_JK: String = "prefs_jk"
    private val PREFS_ALAMAT: String = "prefs_alamat"
    private val PREFS_IS_AKTIF: String = "prefs_is_aktif"


    override val sharedPreferences: SharedPreferences

    init {
        sharedPreferences = context.applicationContext.getSharedPreferences(
            "lazday_pos_prefs123456", Context.MODE_PRIVATE
        )
    }

    var prefsIsLogin by booleanPref(PREFS_IS_LOGIN, false)
    var prefsUsename by stringPref(PREFS_USERNAME,"")
    var prefsPassword by stringPref(PREFS_PASSWORD, "")
    var prefsNamaPegawai by  stringPref(PREFS_NAMA_PEGAWAI,"")
    var prefsJk by stringPref(PREFS_JK, "")
    var prefsAlamat by stringPref(PREFS_ALAMAT, "")
    var prefsIsAktif by stringPref(PREFS_IS_AKTIF, "")

    fun logout(){
        sharedPreferences.edit().clear().apply()
    }
}