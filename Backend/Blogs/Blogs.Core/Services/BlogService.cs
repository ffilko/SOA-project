using Blogs.Core.Interfaces;
using Blogs.Core.Model;

namespace Blogs.Core.Services
{
    public class BlogService : IBlogService
    {
        private readonly IBlogRepository _blogRepository;

        public BlogService(IBlogRepository blogRepository)
        {
            _blogRepository = blogRepository;
        }

        public Blog Create(Blog blog)
        {
            return _blogRepository.Create(blog);
        }

        public Blog GetById(Guid id)
        {
            return _blogRepository.GetById(id);
        }

        public List<Blog> GetAll()
        {
            return _blogRepository.GetAll();
        }
    }
}