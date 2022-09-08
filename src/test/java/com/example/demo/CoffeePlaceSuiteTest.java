package com.example.demo;

import com.example.demo.unitTest.CartUnitTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({CartUnitTest.class})
public class CoffeePlaceSuiteTest {
}