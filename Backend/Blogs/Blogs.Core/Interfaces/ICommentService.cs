using Blogs.Core.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Blogs.Core.Interfaces
{
    public interface ICommentService
    {
        Comment Create(Comment comment);
        Comment Update(Comment comment);
        Comment GetById(Guid id);
        Task<Comment> AddComment(Guid blogId, Guid userId, string content, Guid blogAuthorId);
        List<Comment> GetByBlogId(Guid blogId);
        List<Comment> GetAll();
        Task<bool> CanComment(Guid userId, Guid blogAuthorId);
    }
}
