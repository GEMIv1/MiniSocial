package user.interfaces.repositories;

import post.entity.likeEntity;
import post.entity.postEntity;
import user.entity.userEntity;

public interface ILikeRepository {
	public void save(likeEntity like);
	public likeEntity findByUserAndPost(userEntity user, postEntity post);
	public void delete(likeEntity existingLike);
	public long countLikesByPost(postEntity post);
}
