package com.example.testtaskjb.converters

import com.example.testtaskjb.Author
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.IOException
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter(autoApply = true)
class JpaConverterJson : AttributeConverter<Author?, String?> {
    override fun convertToDatabaseColumn(metaData: Author?): String? {
        return try {
            objectMapper.writeValueAsString(metaData)
        } catch (e: JsonProcessingException) {
            throw IllegalStateException("Conversion of author failed", e)
        }
    }

    override fun convertToEntityAttribute(dbData: String?): Author? {
        return try {
            objectMapper.readValue(dbData, Author::class.java)
        } catch (e: IOException) {
            throw IllegalStateException("Conversion of author failed", e)
        }
    }

    companion object {
        private val objectMapper = ObjectMapper()
    }

}
