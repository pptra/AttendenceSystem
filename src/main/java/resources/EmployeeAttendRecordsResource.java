package resources;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import resources.specification.AttendRecordSpecification;
import resources.specification.SimplePageRequest;
import service.AttendRecordService;
import transfer.AttendRecordTransfer;
import transfer.AttendRecordTransfer.Employee;
import exceptions.AttendRecordNotFoundException;

@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmployeeAttendRecordsResource {

	@Autowired
	private AttendRecordService attendRecordService;

	// @Autowired
	// private EmployeeService employeeService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@PreAuthorize("isAuthenticated() and #id == principal.id or hasAuthority('admin')")
	public Page<AttendRecordTransfer> findAll(@PathParam("id") long id,
			@BeanParam AttendRecordSpecification spec,
			@BeanParam SimplePageRequest pageRequest) {
		spec.setApplicantId(id);
		return attendRecordService.findAll(spec, pageRequest);
	}

	@GET
	@Path(value = "{recordid}")
	@Produces(MediaType.APPLICATION_JSON)
	@PreAuthorize("isAuthenticated() and #id == principal.id or hasAuthority('admin')")
	public AttendRecordTransfer getAttendRecord(@PathParam("id") long id,
			@PathParam("recordid") long recordId) {
		AttendRecordSpecification spec = new AttendRecordSpecification();
		spec.setApplicantId(id);
		spec.setId(recordId);
		AttendRecordTransfer ret = attendRecordService.retrieve(spec);
		if (ret == null) {
			throw new AttendRecordNotFoundException(recordId);
		}
		return ret;
	}

	@DELETE
	@Path(value = "{recordid}")
	@Produces(MediaType.APPLICATION_JSON)
	@PreAuthorize("isAuthenticated() and #id == principal.id or hasAuthority('admin')")
	public Response deleteAttendRecord(@PathParam("id") long id,
			@PathParam("recordid") long recordId) {
		AttendRecordSpecification spec = new AttendRecordSpecification();
		spec.setApplicantId(id);
		spec.setId(recordId);
		attendRecordService.delete(spec);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON)
				.build();
	}

	@PUT
	@Path(value = "{recordid}")
	@Produces(MediaType.APPLICATION_JSON)
	@PreAuthorize("isAuthenticated() and #id == principal.id or hasAuthority('admin')")
	public AttendRecordTransfer updateAttendRecord(@PathParam("id") long id,
			@PathParam("recordid") long recordId,
			AttendRecordTransfer attendRecord) {
		AttendRecordSpecification spec = new AttendRecordSpecification();
		spec.setApplicantId(id);
		spec.setId(recordId);
		return attendRecordService.update(spec, attendRecord);
	}

	// **Method to save or create
	@POST
	@PreAuthorize("isAuthenticated() and #id == principal.id or hasAuthority('admin')")
	public AttendRecordTransfer saveAttendRecord(@PathParam("id") long id,
			@Context SecurityContext sc, AttendRecordTransfer attendRecord) {
		// EmployeeTransfer employee = employeeService.findByUsername(sc
		// .getUserPrincipal().getName());
		Employee e = new Employee();
		e.setId(id);
		attendRecord.setApplicant(e);
		return attendRecordService.save(attendRecord);
	}
}
