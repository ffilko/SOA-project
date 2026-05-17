using Blogs.Core.Interfaces;
using Blogs.Core.Model;
using System.Text.Json;

namespace Blogs.Core.Services
{
    public class BlogService : IBlogService
    {
        private readonly IBlogRepository _blogRepository;
        private readonly HttpClient _httpClient;

        public BlogService(IBlogRepository blogRepository, HttpClient httpClient)
        {
            _blogRepository = blogRepository;
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
    }
}