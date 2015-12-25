package resources;

import java.util.Collection;

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
import org.springframework.stereotype.Component;

import service.DepartmentService;
import transfer.DepartmentTransfer;
import entity.Department;

@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/department")
public class DepartmentsResource {

	@Autowired
	private DepartmentService departmentService;

	@GET
	@Path(value = "{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public DepartmentTransfer getDepartment(@PathParam("id") long id) {
		return departmentService.retrieve(id);
	}

	// ** Method to delete
	@DELETE
	@Path(value = "{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteDepartment(@PathParam("id") long id) {
		departmentService.delete(id);
		return Response.status(Status.OK)
				.entity("department has been successfully deleted")
				.type(MediaType.APPLICATION_JSON).build();
	}

	// **Method to update
	@PUT
	@Path(value = "{id}")
	public DepartmentTransfer updateDepartment(@PathParam("id") long id,
			Department department) {
		return departmentService.update(id, department);
	}

	// **Method to save or create
	@POST
	public DepartmentTransfer saveDepartment(Department department) {
		return departmentService.save(department);
	}

	// ** Method to find All the departments in the list

	@GET
	public Collection<DepartmentTransfer> findallDepartment() {
		return departmentService.findAll();
	}

}