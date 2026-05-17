using Blogs.Core.Interfaces;
using Blogs.Core.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Blogs.Core.Services
{
    public class CommentService : ICommentService
    {
        private readonly ICommentRepository _commentRepository;
        private readonly HttpClient _httpClient;

        public CommentService(ICommentRepository commentRepository, HttpClient httpClient)
        {
            _commentRepository = commentRepository;
            _httpClient = httpClient;
        }

        public async Task<Comment> AddComment(Guid blogId, Guid userId, string content, Guid blogAuthorId)
        {
            var response = await _httpClient.GetAsync(
                $"http://followers-service:8082/api/follow/check/{userId}/{blogAuthorId}");

            if (!response.IsSuccessStatusCode)
                throw new Exception("Follow service error");

            var canComment = bool.Parse(await response.Content.ReadAsStringAsync());

            if (!canComment)
                throw new Exception("Not allowed");

            var comment = new Comment
            {
                BlogId = blogId,
                UserId = userId,
                Content = content
            };

            return _commentRepository.Create(comment);
        }

        public List<Comment> GetByBlogId(Guid blogId)
        {
            return _commentRepository.GetByBlogId(blogId);
        }

        public async Task<bool> CanComment(Guid userId, Guid blogAuthorId)
        {
            var response = await _httpClient.GetAsync(
                $"http://followers-service:8082/api/follow/check/{userId}/{blogAuthorId}");

            if (!response.IsSuccessStatusCode)
                return false;

            return bool.Parse(await response.Content.ReadAsStringAsync());
        }
    }
}
