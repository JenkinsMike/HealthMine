package com.jenkins.employmentExercise.detail.vaccine.domain

data class Patient(
    val patientId: Int = 0,
    val vaccineCode: String = "",
    val vaccineAdministrationDate: String = "",
    val doseInfo: VaccineGroups? = null
)
