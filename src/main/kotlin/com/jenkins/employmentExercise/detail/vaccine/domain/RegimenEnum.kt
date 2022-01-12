package com.jenkins.employmentExercise.detail.vaccine.domain

enum class RegimenEnum(
    val vaccineColloquial: String,
    val minShotDayDelta: Int
) {
    PFIZER("Pfizer", 17),
    MODERNA("Moderna", 24),
    JANSSEN("Janssen", 0);
}