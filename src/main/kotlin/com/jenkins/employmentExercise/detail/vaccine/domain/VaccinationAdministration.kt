package com.jenkins.employmentExercise.detail.vaccine.domain

data class VaccinationAdministration(
    val patientId: Int = 0,
    val vaccineCode: String = "",
    val vaccineAdministrationDate: String = ""
)
