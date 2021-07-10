package com.a4nt0n64r.valuteconverter.models

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import java.io.Serializable

//БОДЖЕ, почему не JSON

@Root(strict = false, name = "ValCurs")
data class ValCurs @JvmOverloads constructor(

    @field:Attribute(name = "Date")
    @param:Attribute(name = "Date")
    val date: String,

    @field:Attribute(name = "name")
    @param:Attribute(name = "name")
    val name: String,


    @field:ElementList(name = "Valute", inline = true, required = false)
    @param:ElementList(name = "Valute", inline = true, required = false)
    val valute: List<Valute>
)

@Root(strict = false, name = "Valute")
data class Valute @JvmOverloads constructor(

    @field:Attribute(name = "ID", required = false)
    @param:Attribute(name = "ID", required = false)
    val id: String,

    @field:Element(name = "NumCode", required = false)
    @param:Element(name = "NumCode", required = false)
    val numCode: String,

    @field:Element(name = "CharCode", required = false)
    @param:Element(name = "CharCode", required = false)
    val charCode: String,

    @field:Element(name = "Nominal", required = false)
    @param:Element(name = "Nominal", required = false)
    val nominal: String,

    @field:Element(name = "Name", required = false)
    @param:Element(name = "Name", required = false)
    val name: String,

    @field:Element(name = "Value", required = false)
    @param:Element(name = "Value", required = false)
    val value: String,
)

fun Valute.toValuteUI(): ValuteUI {
    return ValuteUI(
        numCode,
        charCode,
        nominal.replace(",", ".").toDouble(),
        name,
        value.replace(",", ".").toDouble())
}


data class ValuteUI(
    val numCode: String,
    val charCode: String,
    val nominal: Double,
    val name: String,
    val value: Double,
    var isSelected: Boolean = false
) : Serializable
