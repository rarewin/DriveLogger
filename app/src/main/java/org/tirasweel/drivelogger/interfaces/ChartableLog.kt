package org.tirasweel.drivelogger.interfaces

interface ChartableLog {
    val logDate: Long
    val logMilliMileage: Long
    val logTotalMilliMileage: Long?
    val logFuelEfficiency: Double?
}
