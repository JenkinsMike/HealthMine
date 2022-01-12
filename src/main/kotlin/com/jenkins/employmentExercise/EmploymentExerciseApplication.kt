package com.jenkins.employmentExercise

import com.jenkins.employmentExercise.detail.vaccine.domain.*
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVPrinter
import org.slf4j.LoggerFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import java.io.IOException
import java.io.Reader
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDate.parse
import kotlin.jvm.Throws


@SpringBootApplication
class EmploymentExerciseApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            /**
             * Run as a Spring Application.
             *  - Push Play
             *  - Read files
             *  - Create output log to file
             *  [- Read SQL Insert Statements]
             *  [- Load local MariaDB]
             *  [- Run Query]
             *  [- Create Query output]
             *
             *  Bracketed items are future state possibilities or could
             *  be done if I wrote a docker container, but that would
             *  make execution of the code on other machines harder.
             *
             *  Flyway migration also didn't work too well, but I could try again
             *  now that I built the database tables in MySQL.
             *
             *  I am also aware that I have a bit more SpringBoot plugins than necessary.
             *  They were all thoughts and trials.  I thought I'd leave them in to show
             *  all the different thoughts I had.
             */
            SpringApplication.run(EmploymentExerciseApplication::class.java, *args)

            val logger = LoggerFactory.getLogger(this::class.java)

            /**
             * allowed hard-coded filepath, but not in an ENUM or Sealed Class
             */
            val vaccineGroupsPath = "vaccine_groups.csv"
            val vaccinationAdministrationPath = "vaccination_administration.csv"
            val vaccinationLocalOutputPath = "vaccinatedPatients.csv"

            /**
             * read in the files into lists of a Data Class
             */
            val vaccineGroupsList: ArrayList<VaccineGroups> =
                readCSV(vaccineGroupsPath) as ArrayList<VaccineGroups>
//            vaccineGroupsList.forEachIndexed { index, item ->
//                logger.info("for input index $index the data is $item")
//            }
            val vaccinationAdministrationList: ArrayList<VaccinationAdministration> =
                readCSV(vaccinationAdministrationPath) as ArrayList<VaccinationAdministration>
//            vaccinationAdministrationList.forEachIndexed { index, item ->
//                logger.info("for input index $index the data is $item")
//            }

            val vaccinationStatusList: ArrayList<VaccinationStatus> = arrayListOf()
            var currentPatient = Patient()
            vaccinationAdministrationList.forEach { vaxAdministration ->
                /**
                 * get a Patient's info.
                 */
                if (currentPatient.patientId != vaxAdministration.patientId) {
                    currentPatient = setPatientFirst(vaxAdministration, vaccineGroupsList)
                    /**
                     * J&J rule execution
                     */
                    if (vaccineSubstring(currentPatient).equals(RegimenEnum.JANSSEN.vaccineColloquial, true)) {
                        addVaxStatus(vaccinationStatusList, currentPatient)
                    }
                } else {
                    if (currentPatient.doseInfo?.vaccineGroupName?.contains("First", true) == true) {
                        val previousIterationOfPatient = currentPatient
                        currentPatient = setPatientSecond(vaxAdministration, vaccineGroupsList)
                        /**
                         * start the Pfizer and Moderna Rules
                         */
                        when {
                            vaccineSubstring(currentPatient).equals("${RegimenEnum.PFIZER.vaccineColloquial} Second", true) -> {
                                if (countShotSpanInDays(
                                        previousIterationOfPatient,
                                        currentPatient
                                    ) >= RegimenEnum.PFIZER.minShotDayDelta
                                ) {
                                    addVaxStatus(vaccinationStatusList, currentPatient)
                                }
                            }
                            vaccineSubstring(currentPatient).equals("${RegimenEnum.MODERNA.vaccineColloquial} Second", true) -> {
                                if (countShotSpanInDays(
                                        previousIterationOfPatient,
                                        currentPatient
                                    ) >= RegimenEnum.MODERNA.minShotDayDelta
                                ) {
                                    addVaxStatus(vaccinationStatusList, currentPatient)
                                }
                            }
                        }
                    }
                }
            }
            /**
             * Write the CSV
             */
            writeCSV(vaccinationLocalOutputPath, vaccinationStatusList)
//            vaccinationStatusList.forEach { println(it) }
        }
    }
}

fun vaccineSubstring(currentPatient: Patient): String? =
    currentPatient.doseInfo?.vaccineGroupName?.substringBeforeLast(" ")

fun countShotSpanInDays(
    previousIterationOfPatient: Patient,
    currentPatient: Patient
): Int =
    parse(previousIterationOfPatient.vaccineAdministrationDate)
        .datesUntil(
            parse(currentPatient.vaccineAdministrationDate)
        ).count().toInt()

fun setPatientFirst(
    vaxAdministration: VaccinationAdministration,
    vaccineGroupsList: ArrayList<VaccineGroups>
): Patient =
    Patient(
        patientId = vaxAdministration.patientId,
        vaccineCode = vaxAdministration.vaccineCode,
        vaccineAdministrationDate = vaxAdministration.vaccineAdministrationDate,
        doseInfo = vaccineGroupsList.find { vaccineGroups ->
            vaccineGroups.vaccineCode == vaxAdministration.vaccineCode
        } ?: VaccineGroups().copy(vaccineGroupName = "", vaccineCode = "")
    )

fun setPatientSecond(
    vaxAdministration: VaccinationAdministration,
    vaccineGroupsList: ArrayList<VaccineGroups>
): Patient =
    Patient(
        patientId = vaxAdministration.patientId,
        vaccineCode = vaxAdministration.vaccineCode,
        vaccineAdministrationDate = vaxAdministration.vaccineAdministrationDate,
        doseInfo = vaccineGroupsList.find { vaccineGroups ->
            vaccineGroups.vaccineCode == vaxAdministration.vaccineCode
                    && vaccineGroups.vaccineGroupName.contains("Second", true)
        } ?: VaccineGroups().copy(vaccineGroupName = "", vaccineCode = "")
    )

fun addVaxStatus(
    vaccinationStatusList: ArrayList<VaccinationStatus>,
    patient: Patient
) {
    vaccinationStatusList.add(
        VaccinationStatus(
            patientId = patient.patientId,
            vaccineRegimen = patient.doseInfo!!.vaccineGroupName.substringBefore(" "),
            vaccinatedFullyDate = patient.vaccineAdministrationDate
        )
    )
}

@Throws(FileSystemException::class, IOException::class)
fun readCSV(path: String): List<Unit> {
    val logger = LoggerFactory.getLogger(EmploymentExerciseApplication::class.java)

    val reader = try {
        Files.newBufferedReader(Paths.get(path))
    } catch (e: FileSystemException) {
        logger.error("There is no file called $path in this repo.  Aborting.")
        e.printStackTrace()
    } catch (e: IOException) {
        logger.error("Input filename $path is invalid.  Aborting.")
        e.printStackTrace()
    }

    val validVaccineGroupsFilename = "vaccine_groups"
    val validVaccinationAdministrationFilename = "vaccination_administration"
    val csvParser = CSVParser(
        reader as Reader?,
        CSVFormat.DEFAULT
            .withFirstRecordAsHeader()
            .withTrim()
    )
    /**
     * no longer putting file on C:/.  no need for both operations
     *     when (path.substringAfterLast("\\").substringBefore(".")) {
     */
    when (path.substringBefore(".")) {
        validVaccineGroupsFilename -> {
            val vaccineGroupsList: ArrayList<VaccineGroups> = arrayListOf()
            for (csvRecord in csvParser) {
                val vaccineGroupsItem = VaccineGroups().copy(
                    vaccineGroupName = csvRecord.get("vaccine_group_name"),
                    vaccineCode = csvRecord.get("vaccine_code")
                )
                vaccineGroupsList.add(vaccineGroupsItem)
            }
            return vaccineGroupsList as List<Unit>
        }
        validVaccinationAdministrationFilename -> {
            val vaccinationAdministrationList: ArrayList<VaccinationAdministration> = arrayListOf()
            for (csvRecord in csvParser) {
                val vaccinationAdministrationItem = VaccinationAdministration().copy(
                    patientId = csvRecord.get("patient_id").toInt(),
                    vaccineCode = csvRecord.get("vaccine_code"),
                    vaccineAdministrationDate = csvRecord.get("administration_date")
                )
                vaccinationAdministrationList.add(vaccinationAdministrationItem)
            }
            return vaccinationAdministrationList as List<Unit>
        }
        else ->
            throw Exception(
                "Invalid Input filename.  Aborting.\n" +
                        "Check to make sure your two filenames match the following :\n" +
                        "$validVaccineGroupsFilename \n" +
                        "$validVaccinationAdministrationFilename"
            )
    }
}

private fun writeCSV(
    path: String,
    vaccinationStatusList: ArrayList<VaccinationStatus>
) {
    val writer = Files.newBufferedWriter(Paths.get(path))
    val csvPrinter = CSVPrinter(
        writer,
        CSVFormat.DEFAULT.withHeader(
            "patientId",
            "vaccineRegimen",
            "vaccineAdministrationCompletionDate"
        ).withTrim()
    )

    vaccinationStatusList.forEach {
        csvPrinter.printRecord(
            it.patientId,
            it.vaccineRegimen,
            it.vaccinatedFullyDate
        )
    }

    csvPrinter.flush()
    csvPrinter.close()
}
