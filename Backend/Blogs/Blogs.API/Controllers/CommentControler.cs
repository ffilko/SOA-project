using Blogs.Core.DTO;
using Blogs.Core.Interfaces;
using Blogs.Core.Model;
using Microsoft.AspNetCore.Mvc;

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
                Text = dto.Text
            };

            var created = _commentService.Create(comment);

            var response = new CommentResponseDTO
            {
                Id = created.Id,
                UserId = created.UserId,
                BlogId = created.BlogId,
                Text = created.Text,
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
                Text = Comment.Text,
                CreatedAt = Comment.CreatedAt,
                LastChange = Comment.LastChange
            };

            return Ok(response);
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
                Text = Comment.Text,
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

            comment.Text = dto.Text;

            _commentService.Update(comment);

            return Ok(comment);
        }
    }
}
