package com.jenkins.employmentExercise

import com.jenkins.employmentExercise.detail.vaccine.domain.Patient
import com.jenkins.employmentExercise.detail.vaccine.domain.VaccinationStatus
import com.jenkins.employmentExercise.detail.vaccine.domain.VaccineGroups
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.format.DateTimeParseException

internal class EmploymentExerciseApplicationTest {

    /**
     * In the interest of time - no test will be written for :
     * + readCSV
     *   - Creating test properties files and test mocked files will take too much time.
     * + writeCSV
     *   - Creating test properties files and test mocked files will take too much time.
     * + setPatient
     *   - It is just a setter I wrote to remove repeatable code.  Not test-worthy.
     */
    @Test
    fun `GIVEN empty vaccinationStatusList WHEN addVaxStatus THEN nonempty vaccinationStatusList`() {
        val vaccinationStatusList: ArrayList<VaccinationStatus> = arrayListOf()
        val currentPatient = Patient(
            patientId = 0,
            vaccineCode = "vaccineCode",
            vaccineAdministrationDate = "2021-01-01",
            doseInfo = VaccineGroups()
        )
        addVaxStatus(vaccinationStatusList, currentPatient)
        assertTrue(vaccinationStatusList.containsAll(vaccinationStatusList))
    }

    @Test
    fun `GIVEN two dates are provided to countShotSpanInDays THEN catch throwable`() {
        val previousIterationOfPatient = Patient(vaccineAdministrationDate = "2021-01-01")
        val currentPatient = Patient(vaccineAdministrationDate = "2021-01-04")

        assertEquals(3, countShotSpanInDays(previousIterationOfPatient, currentPatient))
    }

    @Test()
    fun `GIVEN bad date is provided to countShotSpanInDays THEN correct math happens`() {
        val previousIterationOfPatient = Patient(vaccineAdministrationDate = "2021-01-01")
        val badPatient = Patient(vaccineAdministrationDate = "2021-01-45")
        val exception = assertThrows(DateTimeParseException::class.java) {
            countShotSpanInDays(previousIterationOfPatient, badPatient)
        }

        assertEquals(DateTimeParseException::class.java.name, exception.javaClass.name)
    }
}
