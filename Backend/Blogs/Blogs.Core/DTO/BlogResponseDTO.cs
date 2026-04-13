namespace Blogs.Core.DTO
{
    public class BlogResponseDTO
    {
        public Guid Id { get; set; }
        public Guid UserId { get; set; }
        public string Title { get; set; }
        public string Description { get; set; }
        public DateTime CreatedAt { get; set; }
        public List<string> Images { get; set; }
    }
}