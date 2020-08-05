package com.forleven.backenddevelopertest.service.helper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.collections.impl.block.factory.HashingStrategies;
import org.eclipse.collections.impl.utility.ListIterate;

import com.forleven.backenddevelopertest.domain.Phone;
import com.forleven.backenddevelopertest.domain.Student;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class StudentServiceHelper {
	private StudentServiceHelper() {}

	public static Student setStudentPhonesToDistinct(Student student) {
		return student.withPhones(ImmutableList.copyOf(assocAndRetrievePhonesAsList(student, getDistinctPhones(student.getPhones()))));
	}
	
	public static List<Phone> getStudentPhonesToBeStored(Student student, Student storedStudent) {
		Set<Phone> phoneSet = assocAndRetrievePhonesAsSet(student, getDistinctPhones(student.getPhones()));
		Set<Phone> storedPhoneSet = assocAndRetrievePhonesAsSet(student, storedStudent.getPhones());
		
		Set<Phone> alreadyStoredPhoneSet = Sets.intersection(storedPhoneSet, phoneSet);
		Set<Phone> newPhoneSet = Sets.difference(phoneSet, alreadyStoredPhoneSet);
		
		return Lists.newArrayList(Sets.union(newPhoneSet, alreadyStoredPhoneSet));
	}

	public static List<Phone> getDistinctPhones(List<Phone> phones) {
		return ListIterate.distinct(phones, HashingStrategies.fromFunctions(Phone::getPhoneNumber, Phone::getPhoneType));
	}
	
	public static Stream<Phone> assocPhonesToStudent(Student student, List<Phone> phones) {
		return phones.stream().map(p -> p.withStudent(student));
	}
	
	public static List<Phone> assocAndRetrievePhonesAsList(Student student, List<Phone> phones) {
		return assocPhonesToStudent(student, phones).collect(Collectors.toList());
	}
	
	public static Set<Phone> assocAndRetrievePhonesAsSet(Student student, List<Phone> phones) {
		return assocPhonesToStudent(student, phones).collect(Collectors.toSet());
	}
}
