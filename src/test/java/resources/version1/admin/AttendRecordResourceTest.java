package resources.version1.admin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.util.Calendar;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import resources.ObjectMapperContextResolver;
import resources.ResourceTest;
import transfer.AttendRecordTransfer;
import transfer.EmployeeLeaveTransfer;
import assertion.AssertUtils;
import entity.PageModel;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AttendRecordResourceTest extends ResourceTest {

	@Test
	public void testSaveAttendRecord() throws ParseException {
		Calendar startDateC = Calendar.getInstance();
		startDateC.set(2015, 0, 7, 10, 0);
		Calendar endDateC = Calendar.getInstance();
		endDateC.set(2015, 0, 7, 18, 0);
		AttendRecordTransfer record = new AttendRecordTransfer();
		AttendRecordTransfer.Type type = new AttendRecordTransfer.Type();
		type.setId(1L);
		record.setType(type);
		record.setStartDate(startDateC.getTime());
		record.setEndDate(endDateC.getTime());
		record.setReason("reason");

		Response response = target("records").register(JacksonFeature.class)
				.register(ObjectMapperContextResolver.class).request()
				.header("user", "demo").post(Entity.json(record));
		assertEquals(Status.OK.getStatusCode(), response.getStatus());
		AttendRecordTransfer transfer = response
				.readEntity(AttendRecordTransfer.class);
		assertNotNull(transfer.getId());
		assertEquals(record.getType().getId(), transfer.getType().getId());
		assertEquals(record.getStartDate(), transfer.getStartDate());
		assertEquals(record.getEndDate(), transfer.getEndDate());
		assertEquals(record.getReason(), transfer.getReason());
		assertEquals(1l, transfer.getApplicant().getId().longValue());
		assertEquals(AttendRecordTransfer.Status.permit, transfer.getStatus());
	}

	@Test
	public void testSaveAttendRecordWithEndDateOverlayCrossYear()
			throws ParseException {
		Calendar startDateC = Calendar.getInstance();
		startDateC.set(2015, 4, 4, 10, 0);
		Calendar endDateC = Calendar.getInstance();
		endDateC.set(2015, 4, 5, 18, 0);
		AttendRecordTransfer record = new AttendRecordTransfer();
		AttendRecordTransfer.Type type = new AttendRecordTransfer.Type();
		type.setId(1L);
		record.setType(type);
		record.setStartDate(startDateC.getTime());
		record.setEndDate(endDateC.getTime());
		record.setReason("reason");

		Response response = target("records").register(JacksonFeature.class)
				.register(ObjectMapperContextResolver.class).request()
				.header("user", "demo").post(Entity.json(record));
		assertEquals(Status.OK.getStatusCode(), response.getStatus());
		AttendRecordTransfer transfer = response
				.readEntity(AttendRecordTransfer.class);
		assertNotNull(transfer.getId());
		assertEquals(record.getType().getId(), transfer.getType().getId());
		assertEquals(record.getStartDate(), transfer.getStartDate());
		assertEquals(record.getEndDate(), transfer.getEndDate());
		assertEquals(record.getReason(), transfer.getReason());
		assertEquals(1l, transfer.getApplicant().getId().longValue());
		assertEquals(AttendRecordTransfer.Status.permit, transfer.getStatus());

		response = target("employeeleaves").register(JacksonFeature.class)
				.register(ObjectMapperContextResolver.class).request()
				.header("user", "demo").get();
		PageModel<EmployeeLeaveTransfer> rets = response
				.readEntity(new GenericType<PageModel<EmployeeLeaveTransfer>>() {
				});

		for (EmployeeLeaveTransfer elt : rets.getContent()) {
			if (elt.getLeavesetting().getYear().longValue() == 1l) {
				assertEquals(5d, elt.getUsedDays().doubleValue(), 0.01);
			}

			if (elt.getLeavesetting().getYear().longValue() == 2l) {
				assertEquals(6d, elt.getUsedDays().doubleValue(), 0.01);
			}
		}
	}

	@Test
	public void testSaveAttendRecordWithStartDateOverlayCrossYear()
			throws ParseException {
		Calendar startDateC = Calendar.getInstance();
		startDateC.set(2015, 4, 5, 10, 0);
		Calendar endDateC = Calendar.getInstance();
		endDateC.set(2015, 4, 7, 18, 0);
		AttendRecordTransfer record = new AttendRecordTransfer();
		AttendRecordTransfer.Type type = new AttendRecordTransfer.Type();
		type.setId(1L);
		record.setType(type);
		record.setStartDate(startDateC.getTime());
		record.setEndDate(endDateC.getTime());
		record.setReason("reason");

		Response response = target("records").register(JacksonFeature.class)
				.register(ObjectMapperContextResolver.class).request()
				.header("user", "demo").post(Entity.json(record));
		assertEquals(Status.OK.getStatusCode(), response.getStatus());
		AttendRecordTransfer transfer = response
				.readEntity(AttendRecordTransfer.class);
		assertNotNull(transfer.getId());
		assertEquals(record.getType().getId(), transfer.getType().getId());
		assertEquals(record.getStartDate(), transfer.getStartDate());
		assertEquals(record.getEndDate(), transfer.getEndDate());
		assertEquals(record.getReason(), transfer.getReason());
		assertEquals(1l, transfer.getApplicant().getId().longValue());
		assertEquals(AttendRecordTransfer.Status.permit, transfer.getStatus());

		response = target("employeeleaves").register(JacksonFeature.class)
				.register(ObjectMapperContextResolver.class).request()
				.header("user", "demo").get();
		PageModel<EmployeeLeaveTransfer> rets = response
				.readEntity(new GenericType<PageModel<EmployeeLeaveTransfer>>() {
				});

		for (EmployeeLeaveTransfer elt : rets.getContent()) {
			if (elt.getLeavesetting().getYear().longValue() == 1l) {
				assertEquals(6d, elt.getUsedDays().doubleValue(), 0.01);
			}

			if (elt.getLeavesetting().getYear().longValue() == 2l) {
				assertEquals(9d, elt.getUsedDays().doubleValue(), 0.01);
			}
		}
	}

	@Test
	public void testSaveAttendRecordWithCrossYear() throws ParseException {
		Calendar startDateC = Calendar.getInstance();
		startDateC.set(2015, 4, 4, 10, 0);
		Calendar endDateC = Calendar.getInstance();
		endDateC.set(2015, 4, 7, 18, 0);
		AttendRecordTransfer record = new AttendRecordTransfer();
		AttendRecordTransfer.Type type = new AttendRecordTransfer.Type();
		type.setId(1L);
		record.setType(type);
		record.setStartDate(startDateC.getTime());
		record.setEndDate(endDateC.getTime());
		record.setReason("reason");

		Response response = target("records").register(JacksonFeature.class)
				.register(ObjectMapperContextResolver.class).request()
				.header("user", "demo").post(Entity.json(record));
		assertEquals(Status.OK.getStatusCode(), response.getStatus());
		AttendRecordTransfer transfer = response
				.readEntity(AttendRecordTransfer.class);
		assertNotNull(transfer.getId());
		assertEquals(record.getType().getId(), transfer.getType().getId());
		assertEquals(record.getStartDate(), transfer.getStartDate());
		assertEquals(record.getEndDate(), transfer.getEndDate());
		assertEquals(record.getReason(), transfer.getReason());
		assertEquals(1l, transfer.getApplicant().getId().longValue());
		assertEquals(AttendRecordTransfer.Status.permit, transfer.getStatus());

		response = target("employeeleaves").register(JacksonFeature.class)
				.register(ObjectMapperContextResolver.class).request()
				.header("user", "demo").get();
		PageModel<EmployeeLeaveTransfer> rets = response
				.readEntity(new GenericType<PageModel<EmployeeLeaveTransfer>>() {
				});

		for (EmployeeLeaveTransfer elt : rets.getContent()) {
			if (elt.getLeavesetting().getYear().longValue() == 1l) {
				assertEquals(4d, elt.getUsedDays().doubleValue(), 0.01);
			}

			if (elt.getLeavesetting().getYear().longValue() == 2l) {
				assertEquals(5d, elt.getUsedDays().doubleValue(), 0.01);
			}
		}
	}

	@Test
	public void testSaveAttendRecordWithIncludeMarkupDay() {
		Calendar startDateC = Calendar.getInstance();
		startDateC.set(2015, 0, 6, 10, 0);
		Calendar endDateC = Calendar.getInstance();
		endDateC.set(2015, 0, 6, 18, 0);
		AttendRecordTransfer record = new AttendRecordTransfer();
		AttendRecordTransfer.Type type = new AttendRecordTransfer.Type();
		type.setId(1L);
		record.setType(type);
		record.setStartDate(startDateC.getTime());
		record.setEndDate(endDateC.getTime());
		record.setReason("reason");

		Response response = target("records").register(JacksonFeature.class)
				.register(ObjectMapperContextResolver.class).request()
				.header("user", "demo").post(Entity.json(record));
		assertEquals(Status.OK.getStatusCode(), response.getStatus());
		AttendRecordTransfer transfer = response
				.readEntity(AttendRecordTransfer.class);
		assertNotNull(transfer.getId());
		assertEquals(record.getType().getId(), transfer.getType().getId());
		assertEquals(record.getStartDate(), transfer.getStartDate());
		assertEquals(record.getEndDate(), transfer.getEndDate());
		assertEquals(record.getReason(), transfer.getReason());
		assertEquals(1l, transfer.getApplicant().getId().longValue());
		assertEquals(AttendRecordTransfer.Status.permit, transfer.getStatus());
	}

	@Test
	public void testSaveAttendRecordWithIncludeHoliday() {
		Calendar startDateC = Calendar.getInstance();
		startDateC.set(2015, 0, 4, 10, 0);
		Calendar endDateC = Calendar.getInstance();
		endDateC.set(2015, 0, 4, 18, 0);
		AttendRecordTransfer record = new AttendRecordTransfer();
		AttendRecordTransfer.Type type = new AttendRecordTransfer.Type();
		type.setId(1L);
		record.setType(type);
		record.setStartDate(startDateC.getTime());
		record.setEndDate(endDateC.getTime());
		record.setReason("reason");

		Response response = target("records").register(JacksonFeature.class)
				.register(ObjectMapperContextResolver.class).request()
				.header("user", "demo").post(Entity.json(record));
		AssertUtils.assertBadRequest(response);
	}

	@Test
	public void testSaveAttendRecordWithNoEnoughAvailableLeave() {
		Calendar startDateC = Calendar.getInstance();
		startDateC.set(2015, 1, 10, 10, 0);
		Calendar endDateC = Calendar.getInstance();
		endDateC.set(2015, 1, 31, 18, 0);
		AttendRecordTransfer record = new AttendRecordTransfer();
		AttendRecordTransfer.Type type = new AttendRecordTransfer.Type();
		type.setId(1L);
		record.setType(type);
		record.setStartDate(startDateC.getTime());
		record.setEndDate(endDateC.getTime());
		record.setReason("reason");

		Response response = target("records").register(JacksonFeature.class)
				.register(ObjectMapperContextResolver.class).request()
				.header("user", "demo").post(Entity.json(record));
		AssertUtils.assertBadRequest(response);
	}

	@Test
	public void testUpdateAttendRecord() {
		Calendar startDateC = Calendar.getInstance();
		startDateC.set(2015, 1, 10, 10, 0);
		Calendar endDateC = Calendar.getInstance();
		endDateC.set(2015, 1, 10, 18, 0);
		AttendRecordTransfer record = new AttendRecordTransfer();
		AttendRecordTransfer.Type type = new AttendRecordTransfer.Type();
		type.setId(1L);
		record.setType(type);
		record.setStartDate(startDateC.getTime());
		record.setEndDate(endDateC.getTime());
		record.setReason("reason");

		Response response = target("records").path("1")
				.register(JacksonFeature.class)
				.register(ObjectMapperContextResolver.class).request()
				.header("user", "demo").put(Entity.json(record));
		assertEquals(Status.OK.getStatusCode(), response.getStatus());
		AttendRecordTransfer transfer = response
				.readEntity(AttendRecordTransfer.class);
		assertEquals(1L, transfer.getId().longValue());
		assertEquals(record.getType().getId(), transfer.getType().getId());
		assertEquals(record.getStartDate(), transfer.getStartDate());
		assertEquals(record.getEndDate(), transfer.getEndDate());
		assertEquals(record.getReason(), transfer.getReason());
		assertEquals(1l, transfer.getApplicant().getId().longValue());
		assertEquals(AttendRecordTransfer.Status.pending, transfer.getStatus());
	}

	@Test
	public void test1UpdateAttendRecordWithNotFoundException() {
		Calendar startDateC = Calendar.getInstance();
		startDateC.set(2015, 1, 10, 10, 0);
		Calendar endDateC = Calendar.getInstance();
		endDateC.set(2015, 1, 10, 18, 0);
		AttendRecordTransfer record = new AttendRecordTransfer();
		AttendRecordTransfer.Employee applicant = new AttendRecordTransfer.Employee();
		applicant.setId(1L);
		record.setApplicant(applicant);
		AttendRecordTransfer.Type type = new AttendRecordTransfer.Type();
		type.setId(1L);
		record.setType(type);
		record.setStartDate(startDateC.getTime());
		record.setEndDate(endDateC.getTime());
		record.setReason("reason");

		Response response = target("records").path("3")
				.register(JacksonFeature.class).request()
				.header("user", "demo").put(Entity.json(record));
		AssertUtils.assertNotFound(response);
	}

	@Test
	public void testGetAttendRecord() {
		Response response = target("records").path("1")
				.register(JacksonFeature.class).request()
				.header("user", "demo").get();
		assertEquals(Status.OK.getStatusCode(), response.getStatus());
		AttendRecordTransfer transfer = response
				.readEntity(AttendRecordTransfer.class);
		assertEquals(1L, transfer.getId().longValue());
	}

	@Test
	public void test1GetAttendRecordWithNotFoundException() {
		Response response = target("records").path("4")
				.register(JacksonFeature.class).request()
				.header("user", "demo").get();
		assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
	}

	@Test
	public void testZDeleteAttendRecord() {
		Response response = target("records").path("1")
				.register(JacksonFeature.class).request()
				.header("user", "demo").delete();
		assertEquals(Status.NO_CONTENT.getStatusCode(), response.getStatus());
	}

	@Test
	public void test1DeleteAttendRecordWithNotFoundException() {
		Response response = target("records").path("4")
				.register(JacksonFeature.class).request()
				.header("user", "demo").delete();
		AssertUtils.assertNotFound(response);
	}

	@Test
	public void testFindAllRecords() {
		Response response = target("records").register(JacksonFeature.class)
				.request().header("user", "demo").get();
		assertEquals(Status.OK.getStatusCode(), response.getStatus());
		PageModel<AttendRecordTransfer> rets = response
				.readEntity(new GenericType<PageModel<AttendRecordTransfer>>() {
				});
		assertEquals(2, rets.getTotalElements());
	}

	@Override
	protected Class<?>[] getResource() {
		return new Class<?>[] { AttendRecordsResource.class,
				EmployeeLeavesResource.class };
	}

}
