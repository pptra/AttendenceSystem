package resources.version1.admin;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import resources.specification.DepartmentSpecification;
import resources.specification.SimplePageRequest;
import service.DepartmentService;
import transfer.DepartmentTransfer;

@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/departments")
public class DepartmentsResource {

	@Autowired
	private DepartmentService departmentService;

	@GET
	@Path(value = "{id}")
	public DepartmentTransfer getDepartment(@PathParam("id") long id) {
		return departmentService.retrieve(id);
	}

	// ** Method to delete
	@DELETE
	@Path(value = "{id}")
	public Response deleteDepartment(@PathParam("id") long id) {
		departmentService.delete(id);
		return Response.status(Status.NO_CONTENT).type(MediaType.APPLICATION_JSON)
				.build();
	}

	// **Method to update
	@PUT
	@Path(value = "{id}")
	public DepartmentTransfer updateDepartment(@PathParam("id") long id,
			DepartmentTransfer department) {
		return departmentService.update(id, department);
	}

	// **Method to save or create
	@POST
	public DepartmentTransfer saveDepartment(DepartmentTransfer department) {
		return departmentService.save(department);
	}

	// ** Method to find All the departments in the list

	@GET
	public Page<DepartmentTransfer> findallDepartment(
			@BeanParam SimplePageRequest pageRequest,
			@BeanParam DepartmentSpecification spec) {
		return departmentService.findAll(spec, pageRequest);
	}
}
