package post.interfaces.repositories;


import post.entity.postEntity;

public interface IPostRepository {
	public void createPost(postEntity post);
	public postEntity updateContent(int postId, String newContent, int authorId);
	public int delete(int postId);
	public postEntity findById(int postId);
}
