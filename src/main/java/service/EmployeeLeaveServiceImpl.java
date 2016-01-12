package service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import dao.EmployeeDao;
import dao.EmployeeLeaveDao;
import dao.LeavesettingDao;
import entity.EmployeeLeave;
import exceptions.EmployeeLeaveNotFoundException;
import exceptions.EmployeeNotFoundException;
import exceptions.LeavesettingNotFoundException;
import resources.specification.EmployeeLeaveSpecification;
import transfer.EmployeeLeaveTransfer;
import transfer.EmployeeLeaveTransfer.Employee;
import transfer.EmployeeLeaveTransfer.Leavesetting;

public class EmployeeLeaveServiceImpl implements EmployeeLeaveService{
	
	private EmployeeLeaveDao employeeLeaveDao;
	private EmployeeDao employeeDao;
	private LeavesettingDao leavesettingDao;
	
	public EmployeeLeaveServiceImpl(EmployeeLeaveDao employeeLeaveDao, EmployeeDao employeeDao, LeavesettingDao leavesettingDao) {
		this.employeeLeaveDao = employeeLeaveDao;
		this.employeeDao = employeeDao;
		this.leavesettingDao = leavesettingDao;
	}

	@Override
	public EmployeeLeaveTransfer retrieve(long id) {
		EmployeeLeave employeeLeave = employeeLeaveDao.findOne(id);
		if (employeeLeave == null) {
			throw new EmployeeLeaveNotFoundException(id);
		}
		return toEmployeeLeaveTransfer(employeeLeave);
	}

	@Override
	public void delete(long id) {
		try {
			employeeLeaveDao.delete(id);
		} catch (Exception e) {
			throw new EmployeeLeaveNotFoundException(id);
		}
	}

	@Override
	public EmployeeLeaveTransfer save(EmployeeLeaveTransfer employeeLeave) {
		employeeLeave.setId(null);
		EmployeeLeave newEntry = new EmployeeLeave();
		setUpEmployeeLeave(employeeLeave, newEntry);
		return toEmployeeLeaveTransfer(employeeLeaveDao.save(newEntry));
	}

	@Override
	public Page<EmployeeLeaveTransfer> findAll(EmployeeLeaveSpecification spec, Pageable pageable) {
		List<EmployeeLeaveTransfer> transfers = new ArrayList<EmployeeLeaveTransfer>();
		Page<EmployeeLeave> employeeLeaves = employeeLeaveDao.findAll(spec, pageable);
		for (EmployeeLeave employeeLeave : employeeLeaves) {
			transfers.add(toEmployeeLeaveTransfer(employeeLeave));
		}
		Page<EmployeeLeaveTransfer> rets = new PageImpl<EmployeeLeaveTransfer>(transfers, pageable, employeeLeaves.getTotalElements());
		return rets;
	}

	@Override
	public EmployeeLeaveTransfer update(long id, EmployeeLeaveTransfer updated) {
		EmployeeLeave employeeLeave = employeeLeaveDao.findOne(id);
		if (employeeLeave == null) {
			throw new EmployeeLeaveNotFoundException(id);
		}
		setUpEmployeeLeave(updated, employeeLeave);
		return toEmployeeLeaveTransfer(employeeLeaveDao.save(employeeLeave));
	}
	
	private void setUpEmployeeLeave(EmployeeLeaveTransfer transfer, EmployeeLeave newEntry) {
		if (transfer.isEmployeeSet()) {
			if (transfer.getEmployee().isIdSet()) {
				entity.Employee employee = employeeDao.findOne(transfer.getEmployee().getId());
				if (employee == null) {
					throw new EmployeeNotFoundException(transfer.getEmployee().getId());
				}
				System.out.println("************ id : " + employee.getId());
				newEntry.setEmployee(employee);
			}
		}
		if (transfer.isLeavesettingSet()) {
			if (transfer.getLeavesetting().isIdSet()) {
				entity.Leavesetting leavesetting = leavesettingDao.findOne(transfer.getLeavesetting().getId());
				if (leavesetting == null) {
					throw new LeavesettingNotFoundException(transfer.getLeavesetting().getId());
				}
				System.out.println("************ id : " + leavesetting.getId());
				newEntry.setLeavesetting(leavesetting);
			}
		}
		if (transfer.isUsedDaysSet()) {
			newEntry.setUsedDays(transfer.getUsedDays());
		}
	}
	
	private EmployeeLeaveTransfer toEmployeeLeaveTransfer(EmployeeLeave employeeLeave) {
		EmployeeLeaveTransfer ret = new EmployeeLeaveTransfer();
		ret.setId(employeeLeave.getId());
		ret.setUsedDays(employeeLeave.getUsedDays());
		
		entity.Employee entity = employeeLeave.getEmployee();
		Employee employee = new EmployeeLeaveTransfer.Employee();
		employee.setId(entity.getId());
		employee.setName(entity.getName());
//		employee.setDateofjoined(entity.getDateofjoined());
		ret.setEmployee(employee);
		
		entity.Leavesetting leave = employeeLeave.getLeavesetting();
		Leavesetting leavesetting = new EmployeeLeaveTransfer.Leavesetting();
		leavesetting.setId(leave.getId());
		leavesetting.setName(leave.getName());
		leavesetting.setDays(leave.getDays());
		ret.setLeavesetting(leavesetting);
		return ret;
	}

}