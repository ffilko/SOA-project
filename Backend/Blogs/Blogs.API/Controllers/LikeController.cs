using Blogs.Core.DTO;
using Blogs.Core.Interfaces;
using Blogs.Core.Model;
using Microsoft.AspNetCore.Mvc;

namespace Blogs.API.Controllers
{
    
    [ApiController]
    [Route("api/likes")]
    public class LikeControler : ControllerBase
    {
        private readonly ILikeService _likeService;

        public LikeControler(ILikeService likeService)
        {
            _likeService = likeService;
        }

        [HttpPost]
        public ActionResult<CreateLikeDTO> Create([FromBody] CreateLikeDTO dto)
        {
            var like = new Like
            {
                UserId = dto.UserId,
                BlogId = dto.BlogId
            };

            var created = _likeService.Create(like);

            var response = new LikeResponseDTO
            {
                Id = created.Id,
                UserId = created.UserId,
                BlogId = created.BlogId,
            };

            return Ok(response);
        }


        [HttpGet("{blogId}")]
        public ActionResult<int> LikeSumByBlogId(Guid blogId)
        {
            var sum = _likeService.GetSumByBlogId(blogId);

            return Ok(sum);
        }

        [HttpDelete("{id}")]
        public IActionResult DeleteLike(Guid id)
        {
            var deleted = _likeService.Delete(id);

            if (!deleted)
                return NotFound();

            return Ok();
        }
    }
    
}
