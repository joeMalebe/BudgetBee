package za.co.app.budgetbee.extensionFunctions

import org.junit.Assert.assertEquals
import org.junit.Test
import za.co.app.budgetbee.utils.getDateStringByFormat
import java.util.*

class ExtentionsTest {

    @Test
    fun testCalendar_getDateStringByFormat() {
        val calendar = Calendar.getInstance()
        calendar.set(2001, 6, 4)
        val defaultFormat = calendar.getDateStringByFormat()
        val dayMonthYear = calendar.getDateStringByFormat("dd MMMM yyyy")
        assertEquals("Wed, 4 Jul 2001", defaultFormat)
        assertEquals("04 July 2001", dayMonthYear)
    }
}