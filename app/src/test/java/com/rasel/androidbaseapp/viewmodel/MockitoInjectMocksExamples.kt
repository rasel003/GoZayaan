package com.rasel.androidbaseapp.viewmodel

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

interface Service {
    fun send(msg: String?): Boolean
}

open class SMSService : Service {
    override fun send(msg: String?): Boolean {
        println("Sending SMS")
        return true
    }
}

open class EmailService : Service {
    override fun send(msg: String?): Boolean {
        println("Sending email")
        return true
    }
}


open class AppServices(private val emailService: EmailService, private val smsService: SMSService) {
    fun sendSMS(msg: String?): Boolean {
        return smsService.send(msg)
    }

    fun sendEmail(msg: String?): Boolean {
        return emailService.send(msg)
    }
}


class MockitoInjectMocksExamples {
    @Mock
    var emailService: EmailService? = null

    @Mock
    var smsService: SMSService? = null

    @InjectMocks
    var appServicesConstructorInjectionMock: AppServices? = null
    @Before
    fun init_mocks() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun test_constructor_injection_mock() {
        Mockito.`when`(appServicesConstructorInjectionMock!!.sendEmail("Email")).thenReturn(true)
        Mockito.`when`(appServicesConstructorInjectionMock!!.sendSMS(ArgumentMatchers.anyString()))
            .thenReturn(true)
        Assert.assertTrue(appServicesConstructorInjectionMock!!.sendEmail("Email"))
        Assert.assertFalse(appServicesConstructorInjectionMock!!.sendEmail("Unstubbed Email"))
        Assert.assertTrue(appServicesConstructorInjectionMock!!.sendSMS("SMS"))
    }
}