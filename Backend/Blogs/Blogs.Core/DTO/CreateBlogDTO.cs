namespace Blogs.Core.DTO
{
    public class CreateBlogDTO
    {
        public Guid UserId { get; set; }
        public string Title { get; set; }
        public string Description { get; set; }
        public List<string> Images { get; set; }
    }
}