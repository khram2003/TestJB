package com.example.testtaskjb.converters

import java.time.Year
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter(autoApply = true)
class YearAttributeConverter: AttributeConverter<Year, Short> {
    override fun convertToDatabaseColumn(year: Year?): Short? = year?.value?.toShort()

    override fun convertToEntityAttribute(number: Short?): Year? {
        if (number != null) return Year.of(number.toInt())
        return null
    }
}
