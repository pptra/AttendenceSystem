package service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import resources.specification.LeavesettingSpecification;
import service.LeavesettingService;
import transfer.LeavesettingTransfer;
import transfer.LeavesettingTransfer.Type;
import dao.AttendRecordTypeDao;
import dao.LeavesettingDao;
import entity.AttendRecordType;
import entity.Leavesetting;
import exceptions.AttendRecordTypeNotFoundException;
import exceptions.LeavesettingNotFoundException;
import exceptions.RemovingIntegrityViolationException;

public class LeavesettingServiceImpl implements LeavesettingService {

	private LeavesettingDao leavesettingDao;
	private AttendRecordTypeDao attendRecordTypeDao;

	public LeavesettingServiceImpl(LeavesettingDao leavesettingDao,
			AttendRecordTypeDao attendRecordTypeDao) {
		this.leavesettingDao = leavesettingDao;
		this.attendRecordTypeDao = attendRecordTypeDao;
	}

	@Override
	public LeavesettingTransfer retrieve(long id) {
		Leavesetting leavesetting = leavesettingDao.findOne(id);
		if (leavesetting == null) {
			throw new LeavesettingNotFoundException(id);
		}
		return toLeavesettingTransfer(leavesetting);
	}

	@Override
	public void delete(long id) {
		try {
			leavesettingDao.delete(id);
		} catch (org.springframework.dao.DataIntegrityViolationException e) {
			throw new RemovingIntegrityViolationException(Leavesetting.class);
		} catch (org.springframework.dao.EmptyResultDataAccessException e) {
			throw new LeavesettingNotFoundException(id);
		}
	}

	@Override
	public LeavesettingTransfer save(LeavesettingTransfer leavesetting) {
		leavesetting.setId(null);
		Leavesetting newEntry = new Leavesetting();
		setUpLeavesetting(leavesetting, newEntry);
		return toLeavesettingTransfer(leavesettingDao.save(newEntry));
	}

	@Override
	public LeavesettingTransfer update(long id, LeavesettingTransfer updated) {
		Leavesetting leavesetting = leavesettingDao.findOne(id);
		if (leavesetting == null) {
			throw new LeavesettingNotFoundException(id);
		}
		setUpLeavesetting(updated, leavesetting);
		return toLeavesettingTransfer(leavesettingDao.save(leavesetting));
	}

	@Override
	public Page<LeavesettingTransfer> findAll(LeavesettingSpecification spec,
			Pageable pageable) {
		List<LeavesettingTransfer> transfers = new ArrayList<LeavesettingTransfer>();
		Page<Leavesetting> leavesettings = leavesettingDao.findAll(spec,
				pageable);
		for (Leavesetting leavesetting : leavesettings) {
			transfers.add(toLeavesettingTransfer(leavesetting));
		}
		Page<LeavesettingTransfer> rets = new PageImpl<LeavesettingTransfer>(
				transfers, pageable, leavesettings.getTotalElements());
		return rets;
	}

	@Override
	public LeavesettingTransfer findByTypeIdAndYear(long typeId, long year) {
		Leavesetting leavesetting = leavesettingDao.findByTypeIdAndYear(typeId,
				year);
		return toLeavesettingTransfer(leavesetting);
	}

	private void setUpLeavesetting(LeavesettingTransfer transfer,
			Leavesetting newEntry) {
		if (transfer.isTypeSet()) {
			if (transfer.getType().isIdSet()) {
				entity.AttendRecordType type = attendRecordTypeDao
						.findOne(transfer.getType().getId());
				if (type == null) {
					throw new AttendRecordTypeNotFoundException(transfer
							.getType().getId());
				}
				newEntry.setType(type);
			}
		}

		if (transfer.isYearSet()) {
			newEntry.setYear(transfer.getYear());
		}
		if (transfer.isDaysSet()) {
			newEntry.setDays(transfer.getDays());
		}

		newEntry.setName(transfer.getName());
	}

	private LeavesettingTransfer toLeavesettingTransfer(
			Leavesetting leavesetting) {
		LeavesettingTransfer ret = new LeavesettingTransfer();
		ret.setId(leavesetting.getId());
		ret.setYear(leavesetting.getYear());
		ret.setDays(leavesetting.getDays());
		ret.setName(leavesetting.getName());

		AttendRecordType attendRecordType = leavesetting.getType();
		Type type = new LeavesettingTransfer.Type();
		type.setId(attendRecordType.getId());
		type.setName(attendRecordType.getName());
		ret.setType(type);

		return ret;
	}
}