using Blogs.Core.DTO;
using Blogs.Core.Interfaces;
using Blogs.Core.Model;
using Microsoft.AspNetCore.Mvc;

namespace Blogs.API.Controllers
{
    [ApiController]
    [Route("api/blogs")]
    public class BlogController : ControllerBase
    {
        private readonly IBlogService _blogService;
        private readonly ICommentService _commentService;

        public BlogController(IBlogService blogService, ICommentService commentService)
        {
            _blogService = blogService;
            _commentService = commentService;
        }

        [HttpPost]
        public ActionResult<BlogResponseDTO> Create([FromBody] CreateBlogDTO dto)
        {
            var blog = new Blog
            {
                UserId = dto.UserId,
                Title = dto.Title,
                Description = dto.Description,
                Images = dto.Images ?? new List<string>()
            };

            var created = _blogService.Create(blog);

            var response = new BlogResponseDTO
            {
                Id = created.Id,
                UserId = created.UserId,
                Title = created.Title,
                Description = created.Description,
                CreatedAt = created.CreatedAt,
                Images = created.Images
            };

            return Ok(response);
        }

        [HttpGet("{id}")]
        public ActionResult<BlogResponseDTO> GetById(Guid id)
        {
            var blog = _blogService.GetById(id);
            if (blog == null)
                return NotFound();

            var response = new BlogResponseDTO
            {
                Id = blog.Id,
                UserId = blog.UserId,
                Title = blog.Title,
                Description = blog.Description,
                CreatedAt = blog.CreatedAt,
                Images = blog.Images
            };

            return Ok(response);
        }

        [HttpGet]
        public ActionResult<List<BlogResponseDTO>> GetAll()
        {
            var blogs = _blogService.GetAll();

            var response = blogs.Select(blog => new BlogResponseDTO
            {
                Id = blog.Id,
                UserId = blog.UserId,
                Title = blog.Title,
                Description = blog.Description,
                CreatedAt = blog.CreatedAt,
                Images = blog.Images
            }).ToList();

            return Ok(response);
        }

        [HttpGet("feed/{userId}")]
        public async Task<ActionResult<List<BlogResponseDTO>>> GetFeed(Guid userId)
        {
            var blogs = await _blogService.GetFeed(userId);

            var response = blogs.Select(blog => new BlogResponseDTO
            {
                Id = blog.Id,
                UserId = blog.UserId,
                Title = blog.Title,
                Description = blog.Description,
                CreatedAt = blog.CreatedAt,
                Images = blog.Images
            }).ToList();

            return Ok(response);
        }

        [HttpPost("{blogId}/comment")]
        public async Task<IActionResult> Comment(Guid blogId, [FromBody] CommentDTO dto)
        {
            var blog = _blogService.GetById(blogId);
            if (blog == null)
                return NotFound();

            bool canComment = await _commentService.CanComment(dto.UserId, blog.UserId);

            if (!canComment)
                return Forbid("You can only comment blogs of users you follow.");

            return Ok();
        }

        [HttpPost("{blogId}/comments")]
        public async Task<IActionResult> AddComment(Guid blogId, [FromBody] CreateCommentDTO dto)
        {
            var blog = _blogService.GetById(blogId);
            if (blog == null)
                return NotFound();
            Console.WriteLine($"AddComment: userId={dto.UserId}, blogAuthorId={blog.UserId}, content={dto.Content}");
            var comment = await _commentService.AddComment(
                blogId,
                dto.UserId,
                dto.Content,
                blog.UserId
            );

            return Ok(comment);
        }

        [HttpGet("{blogId}/comments")]
        public IActionResult GetComments(Guid blogId)
        {
            return Ok(_commentService.GetByBlogId(blogId));
        }
    }
}