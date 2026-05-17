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
        Task<Comment> AddComment(Guid blogId, Guid userId, string content, Guid blogAuthorId);
        List<Comment> GetByBlogId(Guid blogId);
        Task<bool> CanComment(Guid userId, Guid blogAuthorId);
    }
}
