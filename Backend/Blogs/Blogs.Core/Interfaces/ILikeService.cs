using Blogs.Core.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Blogs.Core.Interfaces
{
    public interface ILikeService
    {
        Like Create(Like like);
        int GetSumByBlogId(Guid blogId);
        bool Delete(Guid id);
        List<Like> GetAll();
    }
}
