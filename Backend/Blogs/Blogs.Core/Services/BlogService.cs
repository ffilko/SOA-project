using Blogs.Core.Interfaces;
using Blogs.Core.Model;
using System.Text.Json;

namespace Blogs.Core.Services
{
    public class BlogService : IBlogService
    {
        private readonly IBlogRepository _blogRepository;
        private readonly ICommentRepository _commentRepository;
        private readonly HttpClient _httpClient;

        public BlogService(IBlogRepository blogRepository, ICommentRepository commentRepository, HttpClient httpClient)
        {
            _blogRepository = blogRepository;
            _commentRepository = commentRepository;
            _httpClient = httpClient;
        }

        public Blog Create(Blog blog) => _blogRepository.Create(blog);

        public Blog GetById(Guid id) => _blogRepository.GetById(id);

        public List<Blog> GetAll() => _blogRepository.GetAll();

        public async Task<List<Blog>> GetFeed(Guid userId)
        {
            var response = await _httpClient.GetAsync(
                $"http://followers-service:8082/api/follow/{userId}/following");

            if (!response.IsSuccessStatusCode)
                return new List<Blog>();

            var json = await response.Content.ReadAsStringAsync();
            var followingIds = JsonSerializer.Deserialize<List<string>>(json);

            var blogs = _blogRepository.GetAll();

            return blogs
                .Where(b => followingIds.Contains(b.UserId.ToString()))
                .ToList();
        }

        public void SoftDeleteByUser(Guid userId)
        {
            var blogs = _blogRepository.GetAll()
                .Where(b => b.UserId == userId)
                .ToList();

            foreach (var blog in blogs)
            {
                blog.IsDeleted = true;
                _blogRepository.Update(blog);

                var comments = _commentRepository.GetByBlogId(blog.Id);
                foreach (var comment in comments)
                {
                    comment.IsDeleted = true;
                    _commentRepository.Update(comment);
                }
            }
        }

        public void RestoreByUser(Guid userId)
        {
            var blogs = _blogRepository.GetAllIncludingDeleted()
                .Where(b => b.UserId == userId && b.IsDeleted)
                .ToList();

            foreach (var blog in blogs)
            {
                blog.IsDeleted = false;
                _blogRepository.Update(blog);

                var comments = _commentRepository.GetByBlogIdIncludingDeleted(blog.Id);
                foreach (var comment in comments)
                {
                    comment.IsDeleted = false;
                    _commentRepository.Update(comment);
                }
            }
        }
    }
    
}