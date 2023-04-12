package com.idis.core.business.commands.test;

import com.idis.core.domain.test.TestObj;
import com.nimblej.core.IRequest;

import java.util.Date;

public record CreateTestObjCommand(String name, int age, Date dateOfBirth) implements IRequest<TestObj> { }
