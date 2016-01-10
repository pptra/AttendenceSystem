package dao;

import java.util.Date;

import org.springframework.security.crypto.password.PasswordEncoder;

import transfer.AttendRecordTransfer;
import entity.AttendRecord;
import entity.AttendRecordType;
import entity.Department;
import entity.Employee;
import entity.EmployeeRole;
import entity.Gender;
import entity.Role;

public class DataBaseInitializer {

	private EmployeeDao employeeDao;
	private RoleDao roleDao;
	private DepartmentDao depDao;
	private AttendRecordTypeDao recordTypeDao;
	private AttendRecordDao recordDao;
	private EmployeeRoleDao employeeRoleDao;
	private LeavesettingDao leavesettingDao;
	private PasswordEncoder passwordEncoder;

	public DataBaseInitializer(EmployeeDao employeeDao, RoleDao roleDao,
			DepartmentDao depDao, AttendRecordTypeDao recordTypeDao,
			EmployeeRoleDao employeeRoleDao, LeavesettingDao leavesettingDao,
			AttendRecordDao recordDao, PasswordEncoder passwordEncoder) {
		super();
		this.employeeDao = employeeDao;
		this.roleDao = roleDao;
		this.depDao = depDao;
		this.recordTypeDao = recordTypeDao;
		this.employeeRoleDao = employeeRoleDao;
		this.passwordEncoder = passwordEncoder;
		this.leavesettingDao = leavesettingDao;
		this.recordDao = recordDao;
	}

	public void initDataBase() {
		AttendRecordType annual = new AttendRecordType();
		annual.setName("annual");
		recordTypeDao.save(annual);

		AttendRecordType sick = new AttendRecordType();
		sick.setName("sick");
		recordTypeDao.save(sick);

		AttendRecordType personal = new AttendRecordType();
		personal.setName("personal");
		recordTypeDao.save(personal);

		AttendRecordType official = new AttendRecordType();
		official.setName("official");
		recordTypeDao.save(official);

		AttendRecordType other = new AttendRecordType();
		other.setName("other");
		recordTypeDao.save(other);

		Department dep = new Department();
		dep.setManager_id(1l);
		dep.setResponseto(1l);
		dep.setName("sale");
		dep = depDao.save(dep);
		
		Employee demo = new Employee();
		demo.setDateofjoined(new Date());
		demo.setEmail("demo@gmail.com");
		demo.setName("demo");
		demo.setPassword(this.passwordEncoder.encode("demo"));
		demo.setUsername("demo");
		Role role = new Role();
		role.setName("admin");
		Role user = new Role();
		user.setName("user");
		demo.setGender(Gender.male.name());
		role = roleDao.save(role);
		user = roleDao.save(user);
		demo.setDepartment(dep);
		demo = this.employeeDao.save(demo);

		Employee admin = new Employee();
		admin.setDateofjoined(new Date());
		admin.setEmail("pohsun@gmail.com");
		admin.setName("pohsun, Huang");
		admin.setPassword(this.passwordEncoder.encode("2ggudoou"));
		admin.setUsername("pohsun");
		admin.setGender(Gender.male.name());
		admin.setDepartment(dep);
		admin.setEmployee(demo);
		this.employeeDao.save(admin);

		EmployeeRole adminEmployee = new EmployeeRole();
		adminEmployee.setEmployee(admin);
		adminEmployee.setRole(role);
		employeeRoleDao.save(adminEmployee);
		
		EmployeeRole demoEmployee = new EmployeeRole();
		demoEmployee.setEmployee(demo);
		demoEmployee.setRole(role);
		employeeRoleDao.save(demoEmployee);

		AttendRecord record = new AttendRecord();
		record.setBookDate(new Date());
		record.setDuration(1D);
		record.setEmployee(admin);
		record.setEndDate(new Date());
		record.setReason("reason");
		record.setStartDate(new Date());
		record.setType(annual);
		record.setStatus(AttendRecordTransfer.Status.permit.name());
		recordDao.save(record);

		AttendRecord record2 = new AttendRecord();
		record2.setBookDate(new Date());
		record2.setDuration(1D);
		record2.setEmployee(admin);
		record2.setEndDate(new Date());
		record2.setReason("reason");
		record2.setStartDate(new Date());
		record2.setType(sick);
		record2.setStatus(AttendRecordTransfer.Status.permit.name());
		recordDao.save(record2);

		AttendRecord record3 = new AttendRecord();
		record3.setBookDate(new Date());
		record3.setDuration(1D);
		record3.setEmployee(admin);
		record3.setEndDate(new Date());
		record3.setReason("reason");
		record3.setStartDate(new Date());
		record3.setType(personal);
		record3.setStatus(AttendRecordTransfer.Status.permit.name());
		recordDao.save(record3);

	}

}
