package resources.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.QueryParam;

import org.springframework.data.jpa.domain.Specification;

import com.google.common.base.Strings;

import entity.Employee;

public class EmployeeSpecification implements Specification<Employee> {

	@QueryParam("departmentName")
	private String departmentName;
	@QueryParam("name")
	private String name;
	@QueryParam("username")
	private String username;
	@QueryParam("gender")
	private String gender;
	@QueryParam("employeeName")
	private String employeeName;

	@Override
	public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query,
			CriteriaBuilder cb) {
		List<Predicate> predicates = new ArrayList<Predicate>();
		if (!Strings.isNullOrEmpty(username)) {
			predicates.add(cb.like(root.<String> get("username"), "%"
					+ username + "%"));
		}
		if (!Strings.isNullOrEmpty(gender)) {
			predicates.add(cb.like(root.<String> get("gender"), "%" + gender
					+ "%"));
		}
		if (!Strings.isNullOrEmpty(name)) {
			predicates
					.add(cb.like(root.<String> get("name"), "%" + name + "%"));
		}
		if (!Strings.isNullOrEmpty(departmentName)) {
			predicates.add(cb.like(root.get("department").<String> get("name"),
					"%" + departmentName + "%"));
		}
		if (!Strings.isNullOrEmpty(employeeName)) {
			predicates.add(cb.like(root.get("employee").<String> get("name"),
					"%" + employeeName + "%"));
		}
		if (predicates.isEmpty()) {
			return null;
		}

		return cb.and(predicates.toArray(new Predicate[0]));
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

}
