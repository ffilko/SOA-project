using Blogs.Core.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Blogs.Core.Interfaces
{
    public interface ICommentRepository
    {
        Comment Create(Comment comment);
        List<Comment> GetByBlogId(Guid blogId);
    }
}
