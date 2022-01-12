package com.jenkins.employmentExercise.detail.vaccine.domain

import org.springframework.format.annotation.DateTimeFormat

data class VaccinationStatus(
    val patientId: Int = 0,
    val vaccineRegimen: String = "",
    @DateTimeFormat(
        iso = DateTimeFormat.ISO.DATE,
        pattern = "YYYY-MM-DD"
    ) val vaccinatedFullyDate: String? = null
)
