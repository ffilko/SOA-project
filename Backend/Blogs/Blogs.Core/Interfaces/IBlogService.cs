using Blogs.Core.Model;

namespace Blogs.Core.Interfaces
{
    public interface IBlogService
    {
        Blog Create(Blog blog);
        Blog GetById(Guid id);
        List<Blog> GetAll();
        Task<List<Blog>> GetFeed(Guid userId);
        void SoftDeleteByUser(Guid userId);
        void RestoreByUser(Guid userId);
    }
}