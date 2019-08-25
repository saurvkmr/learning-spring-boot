package com.kumar.learningspringboot.vo

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Chapter(@Id val id: String, val name: String)