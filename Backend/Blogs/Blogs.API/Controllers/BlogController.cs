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

        public BlogController(IBlogService blogService)
        {
            _blogService = blogService;
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
    }
}