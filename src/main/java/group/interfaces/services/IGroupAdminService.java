package group.interfaces.services;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import group.entity.groupEntity;
import user.entity.userEntity;

public interface IGroupAdminService {
	public Response createGrp(groupEntity grp,HttpServletRequest req);
	public Response editDescription(int grpId, String newDescription,HttpServletRequest req);
	public Response editGrpStatus(int grpId, boolean status,HttpServletRequest req);
	public Response deleteGrp(int grpId, HttpServletRequest req);
	public Response deleteMemeberInGrp(int grpId, int usrId,HttpServletRequest req);
	public Response acceptMemeberInGrp(int grpId, int usrId,HttpServletRequest req);
	public Response getGrpStatus(int grpId);
	public Response rejectMemeberInGrp(int grpId, int usrId, HttpServletRequest req);

}
