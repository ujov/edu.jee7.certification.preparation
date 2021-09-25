package edu.jee7.certification.preparation.jsp;

import java.util.Arrays;
import java.util.List;

public class Familiy {

	private final List<Person> members;

	private final Person[] memberArr;

	public Familiy() {
		members = Arrays.asList(new Person("Schmaehli", 42), new Person("Behri", 38), new Person("Bauchi", 5));
		memberArr = members.toArray(new Person[members.size()]);
	}

	public List<Person> getMembers() {
		return members;
	}

	public Person[] getMemberArr() {
		return memberArr;
	}

	@Override
	public String toString() {
		return "Familiy [members=" + members + ", memberArr=" + Arrays.toString(memberArr) + "]";
	}
}
