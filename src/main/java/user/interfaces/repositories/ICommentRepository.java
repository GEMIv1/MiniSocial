package user.interfaces.repositories;

import post.entity.commentEntity;
import post.entity.postEntity;

public interface ICommentRepository {
	public void save(commentEntity like);
	public void delete(commentEntity existingLike);
	public long countCommentByPost(postEntity post);
}
