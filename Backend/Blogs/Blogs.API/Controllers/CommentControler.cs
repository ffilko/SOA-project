using Blogs.Core.DTO;
using Blogs.Core.Interfaces;
using Blogs.Core.Model;
using Blogs.Infrastructure.Database;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace Comments.API.Controllers
{
    [ApiController]
    [Route("api/comments")]
    public class CommentControler:ControllerBase
    {
        private readonly ICommentService _commentService;

        public CommentControler(ICommentService commentService)
        {
            _commentService = commentService;
        }

        [HttpPost]
        public ActionResult<CommentResponseDTO> Create([FromBody] CreateCommentDTO dto)
        {
            var comment = new Comment
            {
                UserId = dto.UserId,
                BlogId = dto.BlogId,
                Content = dto.Text
            };

            var created = _commentService.Create(comment);

            var response = new CommentResponseDTO
            {
                Id = created.Id,
                UserId = created.UserId,
                BlogId = created.BlogId,
                Content = created.Content,
                CreatedAt = created.CreatedAt,
                LastChange = created.LastChange,
            };

            return Ok(response);
        }

        [HttpGet("{id}")]
        public ActionResult<CommentResponseDTO> GetById(Guid id)
        {
            var Comment = _commentService.GetById(id);
            if (Comment == null)
                return NotFound();

            var response = new CommentResponseDTO
            {
                Id = Comment.Id,
                UserId = Comment.UserId,
                BlogId = Comment.BlogId,
                Content = Comment.Content,
                CreatedAt = Comment.CreatedAt,
                LastChange = Comment.LastChange
            };

            return Ok(response);
        }

        [HttpGet("blog/{blogId}")]
        public ActionResult<List<CommentResponseDTO>> GetByBlogId(Guid blogId)
        {
            var comments = _commentService.GetByBlogId(blogId);

            var response = comments.Select(comment => new CommentResponseDTO
            {
                Id = comment.Id,
                UserId = comment.UserId,
                BlogId = comment.BlogId,
                Content = comment.Content,
                CreatedAt = comment.CreatedAt,
                LastChange = comment.LastChange
            }).ToList();


            return Ok(comments);
        }

        [HttpGet]
        public ActionResult<List<CommentResponseDTO>> GetAll()
        {
            var Comments = _commentService.GetAll();

            var response = Comments.Select(Comment => new CommentResponseDTO
            {
                Id = Comment.Id,
                UserId = Comment.UserId,
                BlogId = Comment.BlogId,
                Content = Comment.Content,
                CreatedAt = Comment.CreatedAt,
                LastChange = Comment.LastChange
            }).ToList();

            return Ok(response);
        }

        [HttpPut("{id}")]
        public IActionResult Update(Guid id, UpdateCommentDTO dto)
        {
            var comment = _commentService.GetById(id);

            if (comment == null)
                return NotFound();

            comment.Content = dto.Text;

            _commentService.Update(comment);

            return Ok(comment);
        }
    }
}
